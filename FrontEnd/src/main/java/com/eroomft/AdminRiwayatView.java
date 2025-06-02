package com.eroomft;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("admin/riwayat")
@PageTitle("Riwayat Peminjaman")
public class AdminRiwayatView extends AppLayout implements BeforeEnterObserver {

    private VerticalLayout contentLayout;
    private List<RiwayatPeminjaman> riwayatList = new ArrayList<>();

    public AdminRiwayatView() {
        createDrawer();
        setContent(createContent());
        fetchRiwayatData();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String role = (String) UI.getCurrent().getSession().getAttribute("role");
        if (role == null || !role.equalsIgnoreCase("admin")) {
            Notification.show("Anda tidak memiliki akses ke halaman ini.", 3000, Notification.Position.MIDDLE);
            UI.getCurrent().access(() -> UI.getCurrent().navigate(""));
        }
    }

    private void createDrawer() {
        String currentPage = "admin/riwayat";

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
        Button manajemenBtn = createStyledButton(VaadinIcon.BUILDING, "Manajemen Ruangan", currentPage.equals("admin/manajemen"), "admin/manajemen");

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
            manajemenBtn,
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
                UI.getCurrent().access(() -> UI.getCurrent().navigate(targetPage));
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
            UI.getCurrent().getSession().setAttribute("role", null);
            UI.getCurrent().access(() -> UI.getCurrent().navigate(""));
        });
        return button;
    }

    private Component createContent() {
        contentLayout = new VerticalLayout();
        contentLayout.setPadding(true);
        contentLayout.setSpacing(true);
        contentLayout.setWidthFull();
        contentLayout.setHeightFull();
        contentLayout.getStyle()
            .set("min-height", "400px")
            .set("background-color", "#FEE6D5");
        contentLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        contentLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        contentLayout.getStyle().set("padding", "2rem");

        H1 title = new H1("Riwayat Peminjaman");
        title.getStyle().set("margin", "2rem 0");
        contentLayout.add(title);

        return contentLayout;
    }

    private void fetchRiwayatData() {
        UI ui = UI.getCurrent();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create("http://localhost:8081/api/v1/peminjaman/riwayat"))
                .GET()
                .header("Accept", "*/*")
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                parseRiwayatData(response.body());
                ui.access(() -> updateContent());
            } else {
                ui.access(() -> Notification.show("Gagal mengambil data riwayat: " + response.statusCode(),
                    3000, Notification.Position.MIDDLE));
            }
        } catch (Exception e) {
            ui.access(() -> Notification.show("Error mengambil data riwayat: " + e.getMessage(),
                3000, Notification.Position.MIDDLE));
        }
    }

    private void parseRiwayatData(String jsonResponse) {
        try {
            JSONObject json = new JSONObject(jsonResponse);
            JSONArray data = json.getJSONArray("data");

            riwayatList.clear();
            for (int i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                if (!item.getBoolean("deleted")) {
                    riwayatList.add(new RiwayatPeminjaman(
                        item.getInt("logPeminjamanId"),
                        item.getString("akunId"),
                        item.getString("namaPeminjam"),
                        item.getString("tipeRuangan"),
                        item.getString("gedung"),
                        item.getString("namaRuangan"),
                        item.getString("keperluan"),
                        item.getString("tanggalPeminjaman"),
                        item.getString("waktuMulai").substring(0, 5) + "–" + item.getString("waktuSelesai").substring(0, 5),
                        item.getString("status")
                    ));
                }
            }
        } catch (Exception e) {
            UI.getCurrent().access(() -> Notification.show("Error parsing data riwayat: " + e.getMessage(),
                3000, Notification.Position.MIDDLE));
        }
    }

    private void updateContent() {
        contentLayout.removeAll();
        H1 title = new H1("Riwayat Peminjaman");
        title.getStyle().set("margin", "2rem 0");
        contentLayout.add(title);

        if (riwayatList.isEmpty()) {
            Span noData = new Span("Tidak ada riwayat peminjaman.");
            noData.getStyle().set("font-size", "16px").set("color", "#666");
            contentLayout.add(noData);
        } else {
            for (RiwayatPeminjaman r : riwayatList) {
                contentLayout.add(createCard(r));
            }
        }
    }

    private Component createCard(RiwayatPeminjaman riwayat) {
        HorizontalLayout card = new HorizontalLayout();
        card.setWidth("90%");
        card.setPadding(true);
        card.setSpacing(true);
        card.getStyle()
            .set("background", "#fff")
            .set("border-radius", "10px")
            .set("box-shadow", "0 2px 8px rgba(0,0,0,0.1)")
            .set("padding", "1.5rem")
            .set("display", "flex")
            .set("justify-content", "space-between")
            .set("align-items", "center");

        VerticalLayout info = new VerticalLayout();
        info.setPadding(false);
        info.setSpacing(true);

        Span nama = new Span(riwayat.getNamaPeminjam() + " - " + riwayat.getAkunId());
        nama.getStyle().set("font-weight", "600").set("font-size", "16px");

        HorizontalLayout ruang = new HorizontalLayout(new Icon(VaadinIcon.BUILDING), new Span(riwayat.getGedung() + ", " + riwayat.getNamaRuangan()));
        ruang.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout tanggal = new HorizontalLayout(new Icon(VaadinIcon.CALENDAR), new Span(riwayat.getTanggalPeminjaman()));
        tanggal.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout jam = new HorizontalLayout(new Icon(VaadinIcon.CLOCK), new Span(riwayat.getWaktu()));
        jam.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout detail = new HorizontalLayout(ruang, tanggal, jam);
        detail.setSpacing(true);
        detail.setAlignItems(FlexComponent.Alignment.CENTER);

        info.add(nama, detail);

        HorizontalLayout tombol = new HorizontalLayout();
        tombol.setSpacing(true);

        Button status = new Button(riwayat.getStatus());
        switch (riwayat.getStatus().toLowerCase()) {
            case "ditolak":
            status.getStyle()
                .set("background-color", "red")
                .set("color", "#fff");
            break;
            case "dibatalkan":
            status.getStyle()
                .set("background-color", "orange")
                .set("color", "#fff");
            break;
            case "selesai":
            status.getStyle()
                .set("background-color", "gray")
                .set("color", "#fff");
            break;
            default:
            status.getStyle()
                .set("background-color", "#fff933")
                .set("color", "#000");
            break;
        }
        status.getStyle().set("font-weight", "600");
        status.getStyle().set("border-radius", "5px").set("padding", "0.5rem 1rem");
        // set the width 
        status.setWidth("150px");
        status.setEnabled(false);

        tombol.add(status);
        card.add(info, tombol);

        return card;
    }

    public static class RiwayatPeminjaman {
        private final int logPeminjamanId;
        private final String akunId;
        private final String namaPeminjam;
        private final String tipeRuangan;
        private final String gedung;
        private final String namaRuangan;
        private final String keperluan;
        private final String tanggalPeminjaman;
        private final String waktu;
        private final String status;

        public RiwayatPeminjaman(int logPeminjamanId, String akunId, String namaPeminjam, String tipeRuangan,
                                 String gedung, String namaRuangan, String keperluan, String tanggalPeminjaman,
                                 String waktu, String status) {
            this.logPeminjamanId = logPeminjamanId;
            this.akunId = akunId;
            this.namaPeminjam = namaPeminjam;
            this.tipeRuangan = tipeRuangan;
            this.gedung = gedung;
            this.namaRuangan = namaRuangan;
            this.keperluan = keperluan;
            this.tanggalPeminjaman = tanggalPeminjaman;
            this.waktu = waktu;
            this.status = status;
        }

        public int getLogPeminjamanId() { return logPeminjamanId; }
        public String getAkunId() { return akunId; }
        public String getNamaPeminjam() { return namaPeminjam; }
        public String getTipeRuangan() { return tipeRuangan; }
        public String getGedung() { return gedung; }
        public String getNamaRuangan() { return namaRuangan; }
        public String getKeperluan() { return keperluan; }
        public String getTanggalPeminjaman() { return tanggalPeminjaman; }
        public String getWaktu() { return waktu; }
        public String getStatus() { return status; }
    }
}