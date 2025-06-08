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
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;

@Route("user/daftar-peminjaman")
@org.springframework.stereotype.Component
@Scope("prototype")
public class UserPeminjamanView extends HorizontalLayout {

    private SidebarComponent sidebar;
    private VerticalLayout mainContent;

    private String apiBaseUrl;

    public UserPeminjamanView(@Value("${api.base.url:http://localhost:8081}") String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl != null ? apiBaseUrl : "http://localhost:8081";
        String role = (String) UI.getCurrent().getSession().getAttribute("role");
        if (role == null || (!role.equalsIgnoreCase("mahasiswa") && !role.equalsIgnoreCase("dosen"))) {
            Notification.show("Anda tidak memiliki akses ke halaman ini.", 3000, Notification.Position.MIDDLE);
            UI.getCurrent().access(() -> UI.getCurrent().navigate(""));
            return;
        }

        setSizeFull();
        setPadding(false);

        sidebar = new SidebarComponent();

        mainContent = new VerticalLayout();
        mainContent.setSizeFull();
        mainContent.setPadding(false);
        mainContent.getStyle()
                .set("background-color", "#FEE6D5")
                .set("overflow", "auto");

        mainContent.add(createContent());
        add(sidebar, mainContent);
        getElement().getStyle().set("background-color", "#FEE6D5");
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

        Span title = new Span("Daftar Peminjaman");
        title.getStyle()
                .set("font-size", "1.5rem")
                .set("font-weight", "bold")
                .set("margin-bottom", "1rem");
        content.add(title);

        List<Peminjaman> peminjamanList = fetchPeminjamanData();

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
                .set("grid-template-columns", "5% 10% 15% 10% 10% 15% 15% 15%")
                .set("gap", "0.5rem")
                .set("font-weight", "bold")
                .set("font-size", "0.9rem");

        header.add(new Span("No"), new Span("Gedung"), new Span("Ruangan"), new Span("Jam Mulai"),
                new Span("Jam Selesai"), new Span("Tanggal"), new Span("Status"), new Span("Aksi"));

        tableContainer.add(header);

        Grid<Peminjaman> grid = new Grid<>(Peminjaman.class, false);
        grid.setWidthFull();
        grid.getStyle()
                .set("border", "1px solid #e0e0e0")
                .set("border-radius", "8px")
                .set("overflow", "hidden");

        grid.addColumn(peminjaman -> peminjamanList.indexOf(peminjaman) + 1)
                .setWidth("5%");

        grid.addColumn(peminjaman -> {
            String tipeRuangan = peminjaman.getTipeRuangan();
            return tipeRuangan != null && !tipeRuangan.isEmpty()
                    ? tipeRuangan.substring(0, 1).toUpperCase() + tipeRuangan.substring(1).toLowerCase()
                    : "";
        }).setWidth("10%");

        grid.addColumn(Peminjaman::getNamaRuangan)
                .setWidth("15%");

        grid.addColumn(Peminjaman::getWaktuMulai)
                .setWidth("10%");

        grid.addColumn(Peminjaman::getWaktuSelesai)
                .setWidth("10%");

        grid.addColumn(Peminjaman::getTanggalPeminjaman)
                .setWidth("15%");

        grid.addComponentColumn(peminjaman -> {
            Span statusSpan = new Span(peminjaman.getStatus());
            statusSpan.getStyle()
                    .set("padding", "0.2rem 0.5rem")
                    .set("border-radius", "4px")
                    .set("color", "white")
                    .set("text-align", "center")
                    .set("display", "inline-block")
                    .set("min-width", "6rem")
                    .set("width", "6rem");
            if ("MENUNGGU".equalsIgnoreCase(peminjaman.getStatus())) {
                statusSpan.getStyle().set("background-color", "#FFC107");
            } else if ("DIIZINKAN".equalsIgnoreCase(peminjaman.getStatus())) {
                statusSpan.getStyle().set("background-color", "#28A745");
            } else if ("DITOLAK".equalsIgnoreCase(peminjaman.getStatus())) {
                statusSpan.getStyle().set("background-color", "#DC3545");
            }
            return statusSpan;
        }).setWidth("15%");

        grid.addComponentColumn(peminjaman -> {
            Button batalkanBtn = new Button("Batalkan");
            batalkanBtn.getStyle()
                    .set("background-color", "#DC3545")
                    .set("color", "white")
                    .set("border", "none")
                    .set("border-radius", "4px")
                    .set("padding", "0.3rem 0.8rem");

            batalkanBtn.addClickListener(e -> {
                Div confirmText = new Div();
                confirmText.setText("Apakah Anda Yakin Ingin\nMenghapus Peminjaman ini?");
                confirmText.getStyle()
                    .set("font-weight", "bold")
                    .set("font-size", "18px")
                    .set("text-align", "center");

                // Fix: Create notification correctly
                Notification notification = new Notification();
                notification.setDuration(5000);
                notification.setPosition(Notification.Position.MIDDLE);

                Button confirmButton = new Button("Ya", event -> {
                    cancelPeminjaman(peminjaman.getIdPeminjaman());
                    notification.close();
                    Notification.show("Peminjaman sedang dibatalkan...", 3000, Notification.Position.MIDDLE);
                });
                confirmButton.getStyle()
                    .set("background-color", "#28A745")
                    .set("color", "white")
                    .set("border", "none")
                    .set("border-radius", "4px")
                    .set("padding", "0.3rem 1.2rem")
                    .set("font-weight", "bold");

                Button cancelButton = new Button("Tidak", event -> {
                    notification.close();
                    Notification.show("Peminjaman tidak dibatalkan.", 3000, Notification.Position.MIDDLE);
                });
                cancelButton.getStyle()
                    .set("background-color", "#DC3545")
                    .set("color", "white")
                    .set("border", "none")
                    .set("border-radius", "4px")
                    .set("padding", "0.3rem 1.2rem")
                    .set("font-weight", "bold");

                HorizontalLayout confirmationLayout = new HorizontalLayout(confirmButton, cancelButton);
                confirmationLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
                confirmationLayout.setWidthFull();

                VerticalLayout dialogLayout = new VerticalLayout(confirmText, confirmationLayout);
                dialogLayout.setAlignItems(FlexComponent.Alignment.CENTER);
                dialogLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

                notification.add(dialogLayout);
                notification.open();
            });
            return batalkanBtn;
        }).setWidth("15%");

        if (!peminjamanList.isEmpty()) {
            grid.setItems(peminjamanList);
            tableContainer.add(grid);
        } else {
            Span emptyMessage = new Span("Belum ada peminjaman yang terdaftar.");
            emptyMessage.getStyle()
                    .set("color", "#666")
                    .set("font-style", "italic")
                    .set("margin-top", "1rem");
            tableContainer.add(emptyMessage);
        }

        content.add(tableContainer);
        return content;
    }

    private List<Peminjaman> fetchPeminjamanData() {
        List<Peminjaman> peminjamanList = new ArrayList<>();
        String akunId = (String) UI.getCurrent().getSession().getAttribute("akunId");

        if (akunId == null) {
            Notification.show("Akun ID tidak ditemukan. Silakan login ulang.", 3000, Notification.Position.MIDDLE);
            return peminjamanList;
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

            System.out.println("Fetching peminjaman data for akunId: " + akunId + " from URL: " + normalizedBaseUrl + "/api/v1/peminjaman?akunId=" + akunId);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(normalizedBaseUrl + "/api/v1/peminjaman?akunId=" + akunId))
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
                        Peminjaman peminjaman = new Peminjaman();
                        peminjaman.setIdPeminjaman(node.path("idPeminjaman").asInt());
                        peminjaman.setNamaAkun(node.path("namaAkun").asText());
                        peminjaman.setAkunId(node.path("akunId").asText());
                        peminjaman.setTipeRuangan(node.path("tipeRuangan").asText());
                        peminjaman.setNamaRuangan(node.path("namaRuangan").asText());
                        peminjaman.setTanggalPeminjaman(node.path("tanggalPeminjaman").asText());
                        peminjaman.setWaktuMulai(node.path("waktuMulai").asText());
                        peminjaman.setWaktuSelesai(node.path("waktuSelesai").asText());
                        peminjaman.setStatus(node.path("status").asText());
                        peminjamanList.add(peminjaman);
                    }
                    System.out.println("Fetched " + peminjamanList.size() + " peminjaman records.");
                } else {
                    System.out.println("No array found in 'data' field of response.");
                }
            } else {
                Notification.show("Gagal mengambil data peminjaman: HTTP " + response.statusCode() + " - " + response.body(), 3000, Notification.Position.MIDDLE);
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error connecting to server: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            System.err.println("Exception during fetchPeminjamanData: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            Notification.show("Error: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            System.err.println("IllegalArgumentException: " + e.getMessage());
        }

        return peminjamanList;
    }

    private void cancelPeminjaman(int idPeminjaman) {
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

            System.out.println("Canceling peminjaman with ID: " + idPeminjaman + " at URL: " + normalizedBaseUrl + "/api/v1/peminjaman/" + idPeminjaman);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(normalizedBaseUrl + "/api/v1/peminjaman/" + idPeminjaman))
                    .header("Accept", "application/json")
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Cancel API Response Status: " + response.statusCode() + ", Body: " + response.body());

            if (response.statusCode() == 200) {
                Notification.show("Peminjaman berhasil dibatalkan.", 2000, Notification.Position.MIDDLE);
                UI.getCurrent().getPage().reload();
            } else {
                Notification.show("Gagal membatalkan peminjaman: HTTP " + response.statusCode() + " - " + response.body(), 3000, Notification.Position.MIDDLE);
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error connecting to server: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            System.err.println("Exception during cancelPeminjaman: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            Notification.show("Error: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            System.err.println("IllegalArgumentException: " + e.getMessage());
        }
    }

    public static class Peminjaman {
        private int idPeminjaman;
        private String namaAkun;
        private String akunId;
        private String tipeRuangan;
        private String namaRuangan;
        private String tanggalPeminjaman;
        private String waktuMulai;
        private String waktuSelesai;
        private String status;

        public int getIdPeminjaman() { return idPeminjaman; }
        public void setIdPeminjaman(int idPeminjaman) { this.idPeminjaman = idPeminjaman; }
        public String getNamaAkun() { return namaAkun; }
        public void setNamaAkun(String namaAkun) { this.namaAkun = namaAkun; }
        public String getAkunId() { return akunId; }
        public void setAkunId(String akunId) { this.akunId = akunId; }
        public String getTipeRuangan() { return tipeRuangan; }
        public void setTipeRuangan(String tipeRuangan) { this.tipeRuangan = tipeRuangan; }
        public String getNamaRuangan() { return namaRuangan; }
        public void setNamaRuangan(String namaRuangan) { this.namaRuangan = namaRuangan; }
        public String getTanggalPeminjaman() { return tanggalPeminjaman; }
        public void setTanggalPeminjaman(String tanggalPeminjaman) { this.tanggalPeminjaman = tanggalPeminjaman; }
        public String getWaktuMulai() { return waktuMulai; }
        public void setWaktuMulai(String waktuMulai) { this.waktuMulai = waktuMulai; }
        public String getWaktuSelesai() { return waktuSelesai; }
        public void setWaktuSelesai(String waktuSelesai) { this.waktuSelesai = waktuSelesai; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}