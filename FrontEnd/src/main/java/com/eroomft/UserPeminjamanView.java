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

@Route("user/daftar-peminjaman")
public class UserPeminjamanView extends AppLayout {

    public UserPeminjamanView() {
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
        String currentPage = "user/daftar-peminjaman";

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
        
        // Top-center alignment: CENTER horizontally, START vertically
        content.setAlignItems(FlexComponent.Alignment.CENTER); // Horizontal center
        content.setJustifyContentMode(FlexComponent.JustifyContentMode.START); // Start from top
        
        // Add some padding from top
        content.getStyle().set("padding", "2rem 2rem 0 2rem");

        // Title
        Span title = new Span("Daftar Peminjaman Saya");
        title.getStyle()
                .set("font-size", "1.5rem")
                .set("font-weight", "bold")
                .set("margin-bottom", "1rem");
        content.add(title);

        // Fetch data from API
        List<Peminjaman> peminjamanList = fetchPeminjamanData();

        // Make a separate component for the table header and color it orange
        HorizontalLayout header = new HorizontalLayout();
        header.setWidth("80%");
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

        content.add(header);

        // Table
        Grid<Peminjaman> grid = new Grid<>(Peminjaman.class, false);
        grid.setWidth("80%");
        grid.getStyle()
            .set("border", "1px solid #e0e0e0")
            .set("border-radius", "8px")
            .set("overflow", "hidden")
            .set("margin", "0 auto"); // Center the grid horizontally


        // Columns
        grid.addColumn(peminjaman -> peminjamanList.indexOf(peminjaman) + 1)
        // set header color orange
            .setWidth("5%");

        grid.addColumn(peminjaman -> peminjaman.getNamaRuangan().substring(0, 1))
            .setWidth("10%");

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
            if ("MENUNGGU".equals(peminjaman.getStatus())) {
                statusSpan.getStyle().set("background-color", "#FFC107"); // Yellow
            } else if ("DIIZINKAN".equals(peminjaman.getStatus())) {
                statusSpan.getStyle().set("background-color", "#28A745"); // Green
            }
            return statusSpan;
        }).setWidth("15%");

        grid.addComponentColumn(peminjaman -> {
            Button batalkanBtn = new Button("Batalkan");
            batalkanBtn.getStyle()
                    .set("background-color", "#DC3545") // Red
                    .set("color", "white")
                    .set("border", "none")
                    .set("border-radius", "4px")
                    .set("padding", "0.3rem 0.8rem");
            batalkanBtn.addClickListener(e -> {
                // Styled confirmation text
                Div confirmText = new Div();
                confirmText.setText("Apakah Kamu Yakin Ingin\nMenghapus Ruangan ini?");
                confirmText.getStyle()
                    .set("font-weight", "bold")
                    .set("font-size", "18px")
                    .set("text-align", "center")
                    .set("background-color", "transparent");

                // Create notification first so it can be closed from button listeners
                Notification notification = Notification.show("", 5000, Notification.Position.MIDDLE);

                Button confirmButton = new Button("Ya", event -> {
                    cancelPeminjaman(peminjaman.getIdPeminjaman());
                    notification.close(); // Close the notification
                    Notification.show("Peminjaman sedang dibatalkan...", 3000, Notification.Position.BOTTOM_CENTER);
                });
                confirmButton.getStyle()
                    .set("background-color", "#28A745") // Green
                    .set("color", "white")
                    .set("border", "none")
                    .set("border-radius", "4px")
                    .set("padding", "0.3rem 1.2rem")
                    .set("font-weight", "bold");

                Button cancelButton = new Button("Tidak", event -> {
                    notification.close(); // Close the notification
                    Notification.show("Pembatalan dibatalkan.", 3000, Notification.Position.BOTTOM_CENTER);
                });
                cancelButton.getStyle()
                    .set("background-color", "#DC3545") // Red
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
            });
            return batalkanBtn;
        }).setWidth("15%");

        // Set data and show grid only if not empty
        if (!peminjamanList.isEmpty()) {
            grid.setItems(peminjamanList);
            content.add(grid);
        } else {
            Span emptyMessage = new Span("Belum ada peminjaman yang terdaftar.");
            emptyMessage.getStyle()
                    .set("color", "#666")
                    .set("font-style", "italic")
                    .set("margin-top", "1rem");
            content.add(emptyMessage);
        }

        return content;
    }

    private List<Peminjaman> fetchPeminjamanData() {
        List<Peminjaman> peminjamanList = new ArrayList<>();
        String akunId = (String) UI.getCurrent().getSession().getAttribute("akunId");

        if (akunId == null) {
            Notification.show("Akun ID tidak ditemukan. Silakan login ulang.", 3000, Notification.Position.BOTTOM_END);
            return peminjamanList;
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/api/v1/peminjaman?akunId=" + akunId))
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
                }
            } else {
                Notification.show("Gagal mengambil data peminjaman: " + response.body(), 3000, Notification.Position.BOTTOM_END);
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error connecting to server: " + e.getMessage(), 3000, Notification.Position.BOTTOM_END);
        }

        return peminjamanList;
    }

    private void cancelPeminjaman(int idPeminjaman) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/api/v1/peminjaman/" + idPeminjaman))
                .header("accept", "*/*")
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                Notification.show("Peminjaman berhasil dibatalkan.", 3000, Notification.Position.BOTTOM_END);
                // Refresh the page to reflect the updated data
                UI.getCurrent().getPage().reload();
            } else {
                Notification.show("Gagal membatalkan peminjaman: " + response.body(), 3000, Notification.Position.BOTTOM_END);
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error connecting to server: " + e.getMessage(), 3000, Notification.Position.BOTTOM_END);
        }
    }

    // Peminjaman class to hold API data
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

        // Getters and setters
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
