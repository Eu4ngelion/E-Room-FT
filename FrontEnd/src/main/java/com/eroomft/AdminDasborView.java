package com.eroomft;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.applayout.AppLayout;

@Route("/admin/dashboard")
public class AdminDasborView extends AppLayout {
    // Variabel global
    private int countRuangan = 0;
    private int countPeminjamanHariIni = 0;
    private int countMenungguPersetujuan = 0;
    private HorizontalLayout statCards;
    private VerticalLayout peminjamanRows;
    private List<Peminjaman> peminjamanList = new ArrayList<>();

    public AdminDasborView() {
        String role = (String) UI.getCurrent().getSession().getAttribute("role");
        if (role == null || !role.equalsIgnoreCase("admin")) {
            Notification.show("Anda tidak memiliki akses ke halaman ini.", 3000, Notification.Position.BOTTOM_END);
            UI.getCurrent().access(() -> {
                UI.getCurrent().navigate("");
            });
        }

        createDrawer();
        setContent(createContent());
        fetchDashboardData();
        fetchPeminjamanData();
    }

    // sidebar
    private void createDrawer() {
        String currentPage = "admin/dashboard";

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

        Button dashboardBtn = createStyledButton(VaadinIcon.DASHBOARD, "Dasbor", currentPage.equals("admin/dashboard"), "admin/dashboard");
        Button manajemenRuanganBtn = createStyledButton(VaadinIcon.BUILDING, "Manajemen Ruangan", currentPage.equals("admin/manajemen"), "admin/manajemen");

        Span peminjamanHeader = new Span("PEMINJAMAN RUANGAN");
        peminjamanHeader.getStyle()
            .set("margin", "1rem 0 0.5rem 1rem")
            .set("font-size", "0.8rem")
            .set("font-weight", "bold")
            .set("color", "black");

        Button verifikasiBtn = createStyledButton(VaadinIcon.CHECK_SQUARE, "Verifikasi Peminjaman", currentPage.equals("admin/verifikasi"), "admin/verifikasi");
        Button riwayatBtn = createStyledButton(VaadinIcon.CLOCK, "Riwayat Peminjaman", currentPage.equals("admin/riwayat"), "admin/riwayat");
        Button keluar = createExitButton(VaadinIcon.SIGN_OUT, "Keluar");

        navigation.add(
            utamaHeader,
            dashboardBtn,
            manajemenRuanganBtn,
            peminjamanHeader,
            verifikasiBtn,
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
            Notification.show("Anda telah keluar dari aplikasi.", 3000, Notification.Position.MIDDLE);
            UI.getCurrent().getSession().setAttribute("role", null);
            UI.getCurrent().navigate("");
        });
        return button;
    }

    private VerticalLayout createContent() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.getStyle()
            .set("padding-left", "2rem")
            .set("padding-right", "2rem")
            .set("background-color", "#FEE6D5");
        content.setSpacing(true);

        statCards = createStatCards();
        peminjamanRows = createRows();
        content.add(createHeaderBar(), statCards, createTableHeader(), peminjamanRows);

        return content;
    }

    private Component createHeaderBar() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setPadding(true);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.getStyle()
            .set("background-color", "white")
            .set("border-radius", "20px")
            .set("box-shadow", "0 2px 6px rgba(0,0,0,0.1)");

        Span title = new Span("Dasbor E-ROOM");
        title.getStyle()
            .set("font-size", "24px")
            .set("font-weight", "bold")
            .set("color", "#FF6600");

        Span inisial = new Span("AD");
        inisial.getStyle()
            .set("background-color", "#FF6600")
            .set("color", "white")
            .set("border-radius", "50%")
            .set("width", "50px")
            .set("height", "50px")
            .set("display", "flex")
            .set("align-items", "center")
            .set("justify-content", "center")
            .set("font-weight", "bold")
            .set("font-size", "18px");

        Div circle = new Div(inisial);
        circle.getStyle().set("display", "flex");

        String namaAdmin = (String) UI.getCurrent().getSession().getAttribute("nama");
        if (namaAdmin == null) {
            Notification.show("Silakan login kembali.", 3000, Notification.Position.MIDDLE);
            UI.getCurrent().navigate("");
            return header;
        }

        Span nama = new Span(namaAdmin);
        nama.getStyle()
            .set("font-weight", "bold")
            .set("color", "#FF6600")
            .set("font-size", "14px");

        String emailAdmin = (String) UI.getCurrent().getSession().getAttribute("email");
        if (emailAdmin == null) {
            Notification.show("Silakan login kembali.", 3000, Notification.Position.MIDDLE);
            UI.getCurrent().navigate("");
            return header;
        }
        Span email = new Span(emailAdmin);
        email.getStyle()
            .set("font-size", "13px")
            .set("color", "black");

        VerticalLayout userInfo = new VerticalLayout(nama, email);
        userInfo.setPadding(false);
        userInfo.setSpacing(false);
        userInfo.setAlignItems(FlexComponent.Alignment.START);

        HorizontalLayout profile = new HorizontalLayout(circle, userInfo);
        profile.setAlignItems(FlexComponent.Alignment.CENTER);
        profile.setSpacing(true);

        header.add(title, profile);
        return header;
    }

    private HorizontalLayout createStatCards() {
        HorizontalLayout stats = new HorizontalLayout();
        stats.setWidthFull();
        stats.setJustifyContentMode(FlexComponent.JustifyContentMode.EVENLY);
        stats.add(
            createStatCard("Total Ruangan", String.valueOf(countRuangan)),
            createStatCard("Peminjaman Hari Ini", String.valueOf(countPeminjamanHariIni)),
            createStatCard("Menunggu Persetujuan", String.valueOf(countMenungguPersetujuan))
        );
        return stats;
    }

    private Component createTableHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setPadding(true);
        header.getStyle().set("background", "#FFA769").set("border-radius", "10px");

        header.add(
            createHeaderCell("No", "5%"),
            createHeaderCell("Ruangan", "20%"),
            createHeaderCell("Jam", "20%"),
            createHeaderCell("Tanggal", "25%"),
            createHeaderCell("Status", "20%")
        );
        return header;
    }

    private Component createHeaderCell(String text, String width) {
        Span span = new Span(text);
        span.getStyle().set("font-weight", "bold");
        Div wrapper = new Div(span);
        wrapper.setWidth(width);
        return wrapper;
    }

    private VerticalLayout createRows() {
        VerticalLayout rows = new VerticalLayout();
        rows.setWidthFull();
        rows.setPadding(false);
        rows.setSpacing(true);

        for (Peminjaman p : peminjamanList) {
            HorizontalLayout row = new HorizontalLayout();
            row.setWidthFull();
            row.setPadding(true);
            row.getStyle().set("background", "white").set("border-radius", "10px").set("box-shadow", "0 2px 4px #ccc");

            row.add(
                createRowCell(String.valueOf(p.getNo()), "5%"),
                createRowCell(p.getRuangan(), "20%", true),
                createRowCell(p.getJam(), "20%", true),
                createRowCell(p.getTanggal(), "25%", true),
                createStatusCell(p.getStatus(), "20%")
            );

            rows.add(row);
        }

        return rows;
    }

    private Component createRowCell(String text, String width) {
        return createRowCell(text, width, false);
    }

    private Component createRowCell(String text, String width, boolean bold) {
        Span span = new Span(text);
        if (bold) span.getStyle().set("font-weight", "bold");
        Div wrapper = new Div(span);
        wrapper.setWidth(width);
        return wrapper;
    }

    private Component createStatusCell(String statusText, String width) {
        Span status = new Span(statusText);
        status.getStyle()
            .set("color", "black")
            .set("border-radius", "20px")
            .set("padding", "4px 12px")
            .set("font-weight", "bold");

        if (statusText.equalsIgnoreCase("diizinkan")) {
            status.getStyle().set("background", "#34C759");
        } else {
            status.getStyle().set("background", "#F7B733");
        }

        Div wrapper = new Div(status);
        wrapper.setWidth(width);
        return wrapper;
    }

    private VerticalLayout createStatCard(String title, String value) {
        VerticalLayout card = new VerticalLayout();
        card.setPadding(true);
        card.setSpacing(false);
        card.setAlignItems(FlexComponent.Alignment.CENTER);
        card.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        card.getStyle()
            .set("background", "white")
            .set("border-radius", "10px")
            .set("box-shadow", "4px 6px 8px #ccc")
            .set("width", "450px")
            .set("height", "150px");

        Span titleSpan = new Span(title);
        titleSpan.getStyle().set("font-size", "14px");

        H2 valueText = new H2(value);
        valueText.getStyle().set("margin", "0");
        valueText.addClassName("stat-value");

        card.add(titleSpan, valueText);
        return card;
    }

    public static class Peminjaman {
        int no;
        String ruangan, jam, tanggal, status;
        public Peminjaman(int no, String ruangan, String jam, String tanggal, String status) {
            this.no = no;
            this.ruangan = ruangan;
            this.jam = jam;
            this.tanggal = tanggal;
            this.status = status;
        }
        public int getNo() { return no; }
        public String getRuangan() { return ruangan; }
        public String getJam() { return jam; }
        public String getTanggal() { return tanggal; }
        public String getStatus() { return status; }
    }

    private void fetchPeminjamanData() {
        UI ui = UI.getCurrent();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create("http://localhost:8081/api/v1/peminjaman"))
                .GET()
                .header("Accept", "application/json")
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                parsePeminjamanData(response.body());
                ui.access(() -> updatePeminjamanRows());
            } else {
                ui.access(() -> Notification.show("Failed to fetch peminjaman data: " + response.statusCode(),
                    3000, Notification.Position.MIDDLE));
            }
        } catch (IOException | InterruptedException e) {
            ui.access(() -> Notification.show("Error connecting to server: " + e.getMessage(),
                3000, Notification.Position.MIDDLE));
        }
    }

    private void fetchDashboardData() {
        UI ui = UI.getCurrent();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create("http://localhost:8081/api/v1/dashboard"))
                .GET()
                .header("Accept", "application/json")
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                parseDashboardData(response.body());
                ui.access(() -> updateStatCards());
            } else {
                ui.access(() -> Notification.show("Failed to fetch dashboard data: " + response.statusCode(),
                    3000, Notification.Position.MIDDLE));
            }
        } catch (IOException | InterruptedException e) {
            ui.access(() -> Notification.show("Error connecting to server: " + e.getMessage(),
                3000, Notification.Position.MIDDLE));
        }
    }

    private void parsePeminjamanData(String jsonResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);
            JsonNode data = root.path("data");

            peminjamanList.clear();
            for (JsonNode peminjamanNode : data) {
                int idPeminjaman = peminjamanNode.path("idPeminjaman").asInt();
                String namaRuangan = peminjamanNode.path("namaRuangan").asText();
                String tanggalPeminjaman = peminjamanNode.path("tanggalPeminjaman").asText();
                String waktuMulai = peminjamanNode.path("waktuMulai").asText();
                String waktuSelesai = peminjamanNode.path("waktuSelesai").asText();
                String status = peminjamanNode.path("status").asText();

                peminjamanList.add(new Peminjaman(
                    idPeminjaman,
                    namaRuangan,
                    waktuMulai + "–" + waktuSelesai,
                    tanggalPeminjaman,
                    status
                ));
            }
        } catch (IOException e) {
            UI.getCurrent().access(() -> Notification.show("Error parsing peminjaman data: " + e.getMessage(),
                3000, Notification.Position.MIDDLE));
        }
    }

    private void parseDashboardData(String jsonResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);
            JsonNode data = root.path("data");

            countRuangan = data.path("jumlahRuangan").asInt(0);
            countPeminjamanHariIni = data.path("jumlahPeminjamanHariIni").asInt(0);
            countMenungguPersetujuan = data.path("jumlahPeminjamanMenunggu").asInt(0);
        } catch (IOException e) {
            UI.getCurrent().access(() -> Notification.show("Error parsing dashboard data: " + e.getMessage(),
                3000, Notification.Position.MIDDLE));
        }
    }

    private void updatePeminjamanRows() {
        peminjamanRows.removeAll();
        for (Peminjaman p : peminjamanList) {
            HorizontalLayout row = new HorizontalLayout();
            row.setWidthFull();
            row.setPadding(true);
            row.getStyle().set("background", "white").set("border-radius", "10px").set("box-shadow", "0 2px 4px #ccc");

            row.add(
                createRowCell(String.valueOf(p.getNo()), "5%"),
                createRowCell(p.getRuangan(), "20%", true),
                createRowCell(p.getJam(), "20%", true),
                createRowCell(p.getTanggal(), "25%", true),
                createStatusCell(p.getStatus(), "20%")
            );

            peminjamanRows.add(row);
        }
    }

    private void updateStatCards() {
        statCards.removeAll();
        statCards.add(
            createStatCard("Total Ruangan", String.valueOf(countRuangan)),
            createStatCard("Peminjaman Hari Ini", String.valueOf(countPeminjamanHariIni)),
            createStatCard("Menunggu Persetujuan", String.valueOf(countMenungguPersetujuan))
        );
    }
}