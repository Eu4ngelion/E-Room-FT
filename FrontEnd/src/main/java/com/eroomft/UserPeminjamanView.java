package com.eroomft;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

@Route("user/daftar-peminjaman")
public class UserPeminjamanView extends HorizontalLayout {

    private SidebarComponent sidebar;
    private VerticalLayout mainContent;

    public UserPeminjamanView() {
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

    private Component createContent() {
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);
        content.setSizeFull();
        content.getStyle()
                .set("background-color", "#FEE6D5");
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        content.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        Span title = new Span("Daftar Peminjaman Saya");
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

            // Disable button if not cancelable
            boolean isCancelable = "MENUNGGU".equalsIgnoreCase(peminjaman.getStatus()) &&
                    isFutureBooking(peminjaman.getTanggalPeminjaman(), peminjaman.getWaktuSelesai());
            batalkanBtn.setEnabled(isCancelable);
            if (!isCancelable) {
                batalkanBtn.getStyle().set("opacity", "0.5");
            }

            batalkanBtn.addClickListener(e -> {
                Div confirmText = new Div();
                confirmText.setText("Apakah Kamu Yakin Ingin\nMenghapus Peminjaman ini?");
                confirmText.getStyle()
                        .set("font-weight", "bold")
                        .set("font-size", "18px")
                        .set("text-align", "center");

                Notification notification = Notification.show("", 5000, Notification.Position.MIDDLE);

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
                    Notification.show("Pembatalan dibatalkan.", 3000, Notification.Position.MIDDLE);
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

    private boolean isFutureBooking(String tanggal, String waktuSelesai) {
        try {
            LocalDate bookingDate = LocalDate.parse(tanggal, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalTime bookingEndTime = LocalTime.parse(waktuSelesai, DateTimeFormatter.ofPattern("HH:mm"));
            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now();

            if (bookingDate.isBefore(today)) {
                return false;
            } else if (bookingDate.isEqual(today)) {
                return bookingEndTime.isAfter(now);
            }
            return true;
        } catch (Exception e) {
            return false; // In case of parsing error, assume not cancelable
        }
    }

    private List<Peminjaman> fetchPeminjamanData() {
        List<Peminjaman> peminjamanList = new ArrayList<>();
        String akunId = (String) UI.getCurrent().getSession().getAttribute("akunId");

        if (akunId == null) {
            Notification.show("Akun ID tidak ditemukan. Silakan login ulang.", 3000, Notification.Position.MIDDLE);
            return peminjamanList;
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/api/v1/peminjaman?akunId=" + akunId))
                .header("accept", "application/json")
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
                Notification.show("Gagal mengambil data peminjaman: " + response.body(), 3000, Notification.Position.MIDDLE);
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error connecting to server: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }

        return peminjamanList;
    }

    private void cancelPeminjaman(int idPeminjaman) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/api/v1/peminjaman/" + idPeminjaman))
                .header("accept", "application/json")
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                Notification.show("Peminjaman berhasil dibatalkan.", 3000, Notification.Position.MIDDLE);
                UI.getCurrent().getPage().reload();
            } else {
                Notification.show("Gagal membatalkan peminjaman: " + response.body(), 3000, Notification.Position.MIDDLE);
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error connecting to server: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
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