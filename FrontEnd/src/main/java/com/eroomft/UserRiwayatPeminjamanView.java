package com.eroomft;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;

@Route("user/riwayat")
@org.springframework.stereotype.Component
@Scope("prototype")
public class UserRiwayatPeminjamanView extends HorizontalLayout {

    private SidebarComponent sidebar;
    private VerticalLayout mainContent;

    private String apiBaseUrl;

    public UserRiwayatPeminjamanView(@Value("${api.base.url:http://localhost:8081}") String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl != null ? apiBaseUrl : "http://localhost:8081";
        String role = (String) UI.getCurrent().getSession().getAttribute("role");
        if (role == null || (!role.equalsIgnoreCase("mahasiswa") && !role.equalsIgnoreCase("dosen"))) {
            Notification.show("Anda tidak memiliki akses ke halaman ini.", 3000, Notification.Position.MIDDLE);
            UI.getCurrent().access(() -> UI.getCurrent().navigate(""));
            return;
        }

        setSizeFull();
        setPadding(false);
        setSpacing(false);

        sidebar = new SidebarComponent();

        mainContent = new VerticalLayout();
        mainContent.setSizeFull();
        mainContent.setPadding(false);
        mainContent.getStyle()
                .set("background-color", "#FEE6D5")
                .set("overflow", "auto");

        mainContent.add(createContent());
        add(sidebar, mainContent);
    }

    @PostConstruct
    private void init() {
        System.out.println("Injected apiBaseUrl: " + apiBaseUrl);
        if (apiBaseUrl == null || apiBaseUrl.trim().isEmpty()) {
            System.err.println("WARNING: apiBaseUrl is not configured. Using default: http://localhost:8081");
            apiBaseUrl = "http://localhost:8081";
        }
    }

    private Component createContent() {
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);
        content.setSizeFull();
        content.getStyle()
                .set("background-color", "#FEE6D5");
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        content.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        Span title = new Span("Riwayat Peminjaman Saya");
        title.getStyle()
                .set("font-size", "1.5rem")
                .set("font-weight", "bold")
                .set("margin-bottom", "1rem");
        content.add(title);

        List<RiwayatPeminjaman> riwayatList = fetchRiwayatPeminjamanData();

        VerticalLayout tableContainer = new VerticalLayout();
        tableContainer.setWidth("80%");
        tableContainer.setHeight("90%");
        tableContainer.setPadding(false);
        tableContainer.setSpacing(false);
        tableContainer.getStyle()
                .set("background-color", "transparent");

        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.getStyle()
                .set("padding", "0.5rem 1rem")
                .set("background-color", "#FF6B35")
                .set("color", "white")
                .set("border-radius", "8px")
                .set("display", "grid")
                .set("grid-template-columns", "7.5% 15% 17.5% 12.5% 12.5% 20% 15%")
                .set("gap", "0.5rem")
                .set("font-weight", "bold")
                .set("font-size", "0.9rem");

        header.add(new Span("No"), new Span("Gedung"), new Span("Ruangan"), new Span("Jam Mulai"),
                new Span("Jam Selesai"), new Span("Tanggal"), new Span("Status"));

        tableContainer.add(header);

        Grid<RiwayatPeminjaman> grid = new Grid<>(RiwayatPeminjaman.class, false);
        grid.setWidthFull();
        grid.getStyle()
                .set("border", "1px solid #e0e0e0")
                .set("border-radius", "8px")
                .set("overflow", "hidden");

        grid.getElement().getThemeList().add("no-header");

        grid.addColumn(riwayat -> riwayatList.indexOf(riwayat) + 1)
                .setWidth("7.5%");

        grid.addColumn(riwayat -> {
            String gedung = riwayat.getGedung();
            return gedung != null && !gedung.isEmpty()
                    ? gedung.substring(0, 1).toUpperCase() + gedung.substring(1).toLowerCase()
                    : "";
        }).setWidth("15%");

        grid.addColumn(RiwayatPeminjaman::getNamaRuangan)
                .setWidth("17.5%");

        grid.addColumn(riwayat -> riwayat.getWaktuMulai() != null ? riwayat.getWaktuMulai().substring(0, 5) : "")
                .setWidth("12.5%");

        grid.addColumn(riwayat -> riwayat.getWaktuSelesai() != null ? riwayat.getWaktuSelesai().substring(0, 5) : "")
                .setWidth("12.5%");

        grid.addColumn(RiwayatPeminjaman::getTanggalPeminjaman)
                .setWidth("20%");

        grid.addComponentColumn(riwayat -> {
            Span statusSpan = new Span(riwayat.getStatus());
            statusSpan.getStyle()
                    .set("padding", "0.2rem 0.5rem")
                    .set("border-radius", "4px")
                    .set("color", "white")
                    .set("text-align", "center")
                    .set("display", "inline-block")
                    .set("min-width", "6rem")
                    .set("width", "6rem");
            if ("DITOLAK".equalsIgnoreCase(riwayat.getStatus())) {
                statusSpan.getStyle().set("background-color", "#DC3545");
            } else if ("DIBATALKAN".equalsIgnoreCase(riwayat.getStatus())) {
                statusSpan.getStyle().set("background-color", "#FFA500");
            } else if ("SELESAI".equalsIgnoreCase(riwayat.getStatus())) {
                statusSpan.getStyle().set("background-color", "#28A745");
            }
            return statusSpan;
        }).setWidth("15%");

        if (!riwayatList.isEmpty()) {
            grid.setItems(riwayatList);
            tableContainer.add(grid);
        } else {
            Span emptyMessage = new Span("Belum ada riwayat peminjaman yang terdaftar.");
            emptyMessage.getStyle()
                    .set("color", "#666")
                    .set("font-style", "italic")
                    .set("margin-top", "1rem");
            tableContainer.add(emptyMessage);
        }

        content.add(tableContainer);
        return content;
    }

    private List<RiwayatPeminjaman> fetchRiwayatPeminjamanData() {
        List<RiwayatPeminjaman> riwayatList = new ArrayList<>();
        String akunId = (String) UI.getCurrent().getSession().getAttribute("akunId");

        if (akunId == null) {
            Notification.show("Akun ID tidak ditemukan. Silakan login ulang.", 3000, Notification.Position.MIDDLE);
            return riwayatList;
        }

        try {
            if (apiBaseUrl == null) {
                System.err.println("ERROR: apiBaseUrl is null. Using default: http://localhost:8081");
                apiBaseUrl = "http://localhost:8081";
            }
            String normalizedBaseUrl = apiBaseUrl.trim();
            if (!normalizedBaseUrl.startsWith("http://") && !normalizedBaseUrl.startsWith("https://")) {
                Notification.show("Error: API base URL missing scheme (http:// or https://)!", 3000, Notification.Position.MIDDLE);
                throw new IllegalArgumentException("API base URL missing scheme: " + normalizedBaseUrl);
            }
            if (normalizedBaseUrl.endsWith("/")) {
                normalizedBaseUrl = normalizedBaseUrl.substring(0, normalizedBaseUrl.length() - 1);
            }

            System.out.println("Fetching riwayat peminjaman data for akunId: " + akunId + " from URL: " + normalizedBaseUrl + "/api/v1/peminjaman/riwayat?akunId=" + akunId);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(normalizedBaseUrl + "/api/v1/peminjaman/riwayat?akunId=" + akunId))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("API Response Status: " + response.statusCode() + ", Body: " + response.body());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.body());
                JsonNode data = root.path("data");

                if (data.isArray()) {
                    for (JsonNode node : data) {
                        RiwayatPeminjaman riwayat = new RiwayatPeminjaman();
                        riwayat.setLogPeminjamanId(node.path("logPeminjamanId").asInt());
                        riwayat.setAkunId(node.path("akunId").asText());
                        riwayat.setNamaPeminjam(node.path("namaPeminjam").asText());
                        riwayat.setTipeRuangan(node.path("tipeRuangan").asText());
                        riwayat.setGedung(node.path("gedung").asText());
                        riwayat.setNamaRuangan(node.path("namaRuangan").asText());
                        riwayat.setKeperluan(node.path("keperluan").asText());
                        riwayat.setTanggalPeminjaman(node.path("tanggalPeminjaman").asText());
                        riwayat.setWaktuMulai(node.path("waktuMulai").asText());
                        riwayat.setWaktuSelesai(node.path("waktuSelesai").asText());
                        riwayat.setStatus(node.path("status").asText());
                        riwayat.setDeleted(node.path("deleted").asBoolean());
                        riwayatList.add(riwayat);
                    }
                    System.out.println("Fetched " + riwayatList.size() + " riwayat peminjaman records.");
                } else {
                    System.out.println("No array found in 'data' field of response.");
                }
            } else {
                Notification.show("Gagal mengambil data riwayat peminjaman: HTTP " + response.statusCode() + " - " + response.body(), 3000, Notification.Position.MIDDLE);
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error connecting to server: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            System.err.println("Exception during fetchRiwayatPeminjamanData: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            Notification.show("Error: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            System.err.println("IllegalArgumentException: " + e.getMessage());
        }

        return riwayatList;
    }

    public static class RiwayatPeminjaman {
        private int logPeminjamanId;
        private String akunId;
        private String namaPeminjam;
        private String tipeRuangan;
        private String gedung;
        private String namaRuangan;
        private String keperluan;
        private String tanggalPeminjaman;
        private String waktuMulai;
        private String waktuSelesai;
        private String status;
        private boolean deleted;

        public int getLogPeminjamanId() { return logPeminjamanId; }
        public void setLogPeminjamanId(int logPeminjamanId) { this.logPeminjamanId = logPeminjamanId; }
        public String getAkunId() { return akunId; }
        public void setAkunId(String akunId) { this.akunId = akunId; }
        public String getNamaPeminjam() { return namaPeminjam; }
        public void setNamaPeminjam(String namaPeminjam) { this.namaPeminjam = namaPeminjam; }
        public String getTipeRuangan() { return tipeRuangan; }
        public void setTipeRuangan(String tipeRuangan) { this.tipeRuangan = tipeRuangan; }
        public String getGedung() { return gedung; }
        public void setGedung(String gedung) { this.gedung = gedung; }
        public String getNamaRuangan() { return namaRuangan; }
        public void setNamaRuangan(String namaRuangan) { this.namaRuangan = namaRuangan; }
        public String getKeperluan() { return keperluan; }
        public void setKeperluan(String keperluan) { this.keperluan = keperluan; }
        public String getTanggalPeminjaman() { return tanggalPeminjaman; }
        public void setTanggalPeminjaman(String tanggalPeminjaman) { this.tanggalPeminjaman = tanggalPeminjaman; }
        public String getWaktuMulai() { return waktuMulai; }
        public void setWaktuMulai(String waktuMulai) { this.waktuMulai = waktuMulai; }
        public String getWaktuSelesai() { return waktuSelesai; }
        public void setWaktuSelesai(String waktuSelesai) { this.waktuSelesai = waktuSelesai; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public boolean isDeleted() { return deleted; }
        public void setDeleted(boolean deleted) { this.deleted = deleted; }
    }
}