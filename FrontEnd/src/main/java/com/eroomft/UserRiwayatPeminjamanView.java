package com.eroomft;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Route("user/riwayat")
public class UserRiwayatPeminjamanView extends AppLayout {

    public UserRiwayatPeminjamanView() {
        String role = (String) UI.getCurrent().getSession().getAttribute("role");
        if (role == null || (!role.equalsIgnoreCase("mahasiswa") && !role.equalsIgnoreCase("dosen"))) {
            Notification.show("Anda tidak memiliki akses ke halaman ini.", 3000, Notification.Position.BOTTOM_END);
            UI.getCurrent().access(() -> UI.getCurrent().navigate(""));
            return;
        }
        createDrawer();
        setContent(createContent());
    }

    private void createDrawer() {
        String currentPage = "user/riwayat";

        Image logo = new Image("https://fahutan.unmul.ac.id/laboratorium/assets/images/LOGO%20UNMUL.png", "Logo");
        logo.setWidth("50px");

        Span title = new Span("E-Room Teknik");
        title.getStyle()
                .set("font-weight", "bold")
                .set("font-size", "1.2rem");

        HorizontalLayout logoSection = new HorizontalLayout(logo, title);
        logoSection.setAlignItems(FlexComponent.Alignment.CENTER);
        logoSection.setWidthFull();
        logoSection.setSpacing(true);
        logoSection.getStyle()
                .set("padding", "1rem")
                .set("border-bottom", "1px solid #e0e0e0");

        VerticalLayout navigation = new VerticalLayout();
        navigation.setPadding(false);
        navigation.setSpacing(false);

        Span utamaHeader = new Span("UTAMA");
        utamaHeader.getStyle()
                .set("margin", "1rem 0 0.5rem 1rem")
                .set("font-size", "0.8rem")
                .set("font-weight", "bold")
                .set("color", "black");

        Button dashboardBtn = createStyledButton(VaadinIcon.DASHBOARD, "Beranda", currentPage.equals("user/beranda"), "user/beranda");
        Button manajemenRuanganBtn = createStyledButton(VaadinIcon.BUILDING, "Daftar Ruangan", currentPage.equals("user/ruangan"), "user/ruangan");

        Span peminjamanHeader = new Span("PEMINJAMAN RUANGAN");
        peminjamanHeader.getStyle()
                .set("margin", "1rem 0 0.5rem 1rem")
                .set("font-size", "0.8rem")
                .set("font-weight", "bold")
                .set("color", "black");

        Button verifikasiBtn = createStyledButton(VaadinIcon.CHECK_SQUARE, "Ajukan Peminjaman", currentPage.equals("user/pengajuan"), "user/pengajuan");
        Button daftarPeminjamanBtn = createStyledButton(VaadinIcon.EYE, "Daftar Peminjaman", currentPage.equals("user/daftar-peminjaman"), "user/daftar-peminjaman");
        Button riwayatBtn = createStyledButton(VaadinIcon.CLOCK, "Riwayat Peminjaman", currentPage.equals("user/riwayat"), "user/riwayat");
        Button keluar = createExitButton(VaadinIcon.SIGN_OUT, "Keluar");

        navigation.add(
                utamaHeader,
                dashboardBtn,
                manajemenRuanganBtn,
                peminjamanHeader,
                verifikasiBtn,
                daftarPeminjamanBtn,
                riwayatBtn,
                keluar
        );

        addToDrawer(logoSection, navigation);
    }

    private Button createStyledButton(VaadinIcon icon, String text, boolean isActive, String targetPage) {
        Button btn = new Button(text, new Icon(icon));
        btn.getStyle()
                .set("margin-inline", "1rem")
                .set("padding", "0.5rem")
                .set("gap", "0.5rem");

        if (isActive) {
            btn.getStyle()
                    .set("background-color", "#FF6B35")
                    .set("width", "calc(100% - 2rem)")
                    .set("color", "white");
        } else {
            btn.getStyle()
                    .set("color", "black")
                    .set("background-color", "transparent");
        }

        btn.getElement().addEventListener("mouseenter", e -> {
            btn.getStyle()
                    .set("background-color", "#FB9A59")
                    .set("width", "calc(100% - 2rem)")
                    .set("color", "white");
        });

        btn.getElement().addEventListener("mouseleave", e -> {
            if (!isActive) {
                btn.getStyle()
                        .set("background-color", "transparent")
                        .remove("width")
                        .set("color", "black");
            } else {
                btn.getStyle()
                        .set("background-color", "#FF6B35")
                        .set("color", "white");
            }
        });

        btn.addClickListener(event -> {
            if (!isActive) {
                UI.getCurrent().navigate(targetPage);
            }
        });
        return btn;
    }

    private Button createExitButton(VaadinIcon icon, String text) {
        Button button = new Button(text, new Icon(icon));
        button.addClassNames(
                LumoUtility.JustifyContent.START,
                LumoUtility.AlignItems.START,
                LumoUtility.Width.FULL
        );
        button.getStyle()
                .set("margin-top", "100px")
                .set("border-radius", "5px")
                .set("margin-inline", "1rem")
                .set("width", "calc(100% - 2rem)")
                .set("background", "#FF6666")
                .set("color", "white")
                .set("padding", "0.75rem 1rem")
                .set("justify-content", "flex-start")
                .set("text-align", "left")
                .set("display", "flex")
                .set("align-items", "center");

        button.addClickListener(event -> {
            Notification.show("Anda telah keluar dari aplikasi.", 3000, Notification.Position.BOTTOM_END);
            UI.getCurrent().getSession().close();
            UI.getCurrent().navigate("");
        });
        return button;
    }

    private Component createContent() {
        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);
        content.setSpacing(false);
        content.setSizeFull();
        
        // Align from top and centered
        content.setAlignItems(FlexComponent.Alignment.CENTER); // Horizontal center
        content.setJustifyContentMode(FlexComponent.JustifyContentMode.START); // Top alignment
        content.getStyle().set("padding", "2rem 2rem 0 2rem"); // Padding from top

        // Title
        Span title = new Span("Riwayat Peminjaman Saya");
        title.getStyle()
                .set("font-size", "1.5rem")
                .set("font-weight", "bold")
                .set("margin-bottom", "1rem");
        content.add(title);

        // Fetch data from API
        List<RiwayatPeminjaman> riwayatList = fetchRiwayatPeminjamanData();

        // Table header
        HorizontalLayout header = new HorizontalLayout();
        header.setWidth("80%"); // Match the width of the grid
        header.getStyle()
            .set("margin", "0 auto")
            .set("padding", "0.5rem 1rem")
            .set("background-color", "#FF6B35")
            .set("color", "white")
            .set("border-radius", "8px 8px 0 0")
            .set("display", "grid")
            .set("grid-template-columns", "7.5% 12% 16% 12% 16% 17% 10%")
            .set("gap", "0.5rem")
            .set("font-weight", "bold")
            .set("font-size", "0.9rem");
        header.add(new Span("No"), new Span("Gedung"), new Span("Ruangan"), new Span("Jam Mulai"),
            new Span("Jam Selesai"), new Span("Tanggal"), new Span("Status")); 
        content.add(header);

        // Table
        Grid<RiwayatPeminjaman> grid = new Grid<>(RiwayatPeminjaman.class, false);
        grid.setWidth("80%");
        grid.getStyle()
                .set("margin", "0 auto")
                .set("border", "1px solid #e0e0e0")
                .set("border-radius", "0 0 8px 8px")
                .set("overflow", "hidden");

        // Remove Grid header
        grid.getElement().getThemeList().add("no-header");

        // Columns
        grid.addColumn(riwayat -> riwayatList.indexOf(riwayat) + 1)
            .setWidth("5%");

        grid.addColumn(RiwayatPeminjaman::getGedung)
            .setWidth("10%");

        grid.addColumn(RiwayatPeminjaman::getNamaRuangan)
            .setWidth("15%");

        grid.addColumn(riwayat -> riwayat.getWaktuMulai().substring(0, 5)) // Extract HH:MM
            .setWidth("10%");

        grid.addColumn(riwayat -> riwayat.getWaktuSelesai().substring(0, 5)) // Extract HH:MM
            .setWidth("10%");

        grid.addColumn(RiwayatPeminjaman::getTanggalPeminjaman)
            .setWidth("15%");

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

            if ("DITOLAK".equals(riwayat.getStatus())) {
                statusSpan.getStyle().set("background-color", "#DC3545");
            } else if ("DIBATALKAN".equals(riwayat.getStatus())) {
                statusSpan.getStyle().set("background-color", "#FFA500");
            } else if ("SELESAI".equals(riwayat.getStatus())) {
                statusSpan.getStyle().set("background-color", "#28A745"); 
            }
            return statusSpan;
        }).setWidth("15%");

        // Set data
        grid.setItems(riwayatList);

        // Display message if table is empty
        if (riwayatList.isEmpty()) {
            Span emptyMessage = new Span("Belum ada riwayat peminjaman yang terdaftar.");
            emptyMessage.getStyle()
                    .set("color", "#666")
                    .set("font-style", "italic")
                    .set("margin-top", "1rem");
            content.add(emptyMessage);
        } else {
            content.add(grid);
        }

        content.setSizeFull();
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        content.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        content.getStyle()
                .set("background-color", "#f9f9f9");
        return content;
    }

    private List<RiwayatPeminjaman> fetchRiwayatPeminjamanData() {
        List<RiwayatPeminjaman> riwayatList = new ArrayList<>();
        String akunId = (String) UI.getCurrent().getSession().getAttribute("akunId");

        if (akunId == null) {
            Notification.show("Akun ID tidak ditemukan. Silakan login ulang.", 3000, Notification.Position.BOTTOM_END);
            return riwayatList;
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/api/v1/peminjaman/riwayat?akunId=" + akunId))
                .header("accept", "*/*")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
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
                }
            } else {
                Notification.show("Gagal mengambil data riwayat peminjaman: " + response.body(), 3000, Notification.Position.BOTTOM_END);
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error connecting to server: " + e.getMessage(), 3000, Notification.Position.BOTTOM_END);
        }

        return riwayatList;
    }

    // RiwayatPeminjaman class to hold API data
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

        // Getters and setters
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