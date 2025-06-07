package com.eroomft;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Route("admin/verifikasi")
@PageTitle("Verifikasi Peminjaman")
public class AdminVerifikasiView extends AppLayout implements BeforeEnterObserver {

    private VerticalLayout contentLayout;
    private List<Peminjaman> peminjamanList = new ArrayList<>();

    public AdminVerifikasiView() {
        createDrawer();
        setContent(createContent());
        fetchPeminjamanData();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String role = (String) UI.getCurrent().getSession().getAttribute("role");
        if (role == null || !role.equalsIgnoreCase("admin")) {
            Notification.show("Anda tidak memiliki akses ke halaman ini.", 3000, Notification.Position.MIDDLE);
            UI.getCurrent().access(() -> UI.getCurrent().navigate(""));
        }
    }

    // Sidebar

    private void createDrawer() {
        String currentPage = "admin/verifikasi";

        Image logo = new Image("/frontend/RooQue.png", "Logo RooQue");
        logo.setWidth("50px");

        Span title = new Span("RooQue Admin");
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


        Button dashboardBtn = createStyledButton(VaadinIcon.DASHBOARD, "Dasbor", currentPage.equals("dashboard"), "admin/dashboard");
        Button manajemenRuanganBtn = createStyledButton(VaadinIcon.BUILDING, "Manajemen Ruangan", currentPage.equals("admin/manajemen"), "admin/manajemen");

        Span peminjamanHeader = new Span("PEMINJAMAN RUANGAN");
        peminjamanHeader.getStyle()
            .set("margin", "1rem 0 0.5rem 1rem")
            .set("font-size", "0.8rem")
            .set("font-weight", "bold")
            .set("color", "black");

        Button verifikasiBtn = createStyledButton(VaadinIcon.CHECK_SQUARE, "Verifikasi Peminjaman", currentPage.equals("verifikasi"), "admin/verifikasi");
        Button riwayatBtn = createStyledButton(VaadinIcon.CLOCK, "Riwayat Peminjaman", currentPage.equals("riwayat"), "admin/riwayat");
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

    // button hover
    private Button createStyledButton(VaadinIcon icon, String text, boolean isActive, String targetPage) {
        Button btn = new Button(text, new Icon(icon));

        btn.getStyle()
            .set("margin-inline", "1rem") // Horizontal margin
            .set("padding", "0.5rem") // Optional: Add padding for better spacing
            .set("gap", "0.5rem"); // Space between icon and text

        // Set initial styles immediately
        if (isActive) {
            btn.getStyle()
                .set("background-color", "#FF6B35")
                .set("width", "calc(100% - 2rem)") // Full width minus margins
                .set("color", "white");
        } else {
            btn.getStyle()
                .set("color", "black")
                .set("background-color", "transparent");
        }

        // Hover effects
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
            };
        });

        // Click listener
        btn.addClickListener(event -> {
            // Logika untuk navigasi ke halaman yang sesuai
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
        // .set("border", "none")
            // vertical margin
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
            // Logika untuk keluar dari aplikasi
            Notification.show("Anda telah keluar dari aplikasi.", 3000, Notification.Position.BOTTOM_END)
                .setPosition(Notification.Position.BOTTOM_END);
            UI.getCurrent().getSession().close(); // Hapus session
            UI.getCurrent().navigate(""); 
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

        H1 title = new H1("Verifikasi Peminjaman");
        title.getStyle().set("margin", "2rem 0");
        contentLayout.add(title);

        return contentLayout;
    }

    private void fetchPeminjamanData() {
        UI ui = UI.getCurrent();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create("http://localhost:8081/api/v1/peminjaman"))
                .GET()
                .header("Accept", "*/*")
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                parsePeminjamanData(response.body());
                ui.access(() -> updateContent());
            } else {
                ui.access(() -> Notification.show("Gagal mengambil data peminjaman: " + response.statusCode(),
                    3000, Notification.Position.MIDDLE));
            }
        } catch (Exception e) {
            ui.access(() -> Notification.show("Error mengambil data peminjaman: " + e.getMessage(),
                3000, Notification.Position.MIDDLE));
        }
    }

    private void parsePeminjamanData(String jsonResponse) {
        try {
            JSONObject json = new JSONObject(jsonResponse);
            JSONArray data = json.getJSONArray("data");

            peminjamanList.clear();
            for (int i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                peminjamanList.add(new Peminjaman(
                    item.getInt("idPeminjaman"),
                    item.getString("namaAkun"),
                    item.getString("akunId"),
                    item.getString("namaRuangan"),
                    item.getString("tanggalPeminjaman"),
                    item.getString("waktuMulai") + "–" + item.getString("waktuSelesai"),
                    item.getString("status")
                ));
            }
        } catch (Exception e) {
            UI.getCurrent().access(() -> Notification.show("Error parsing data peminjaman: " + e.getMessage(),
                3000, Notification.Position.MIDDLE));
        }
    }

    private void updateContent() {
        contentLayout.removeAll();
        H1 title = new H1("Verifikasi Peminjaman");
        title.getStyle().set("margin", "2rem 0");
        contentLayout.add(title);

        if (peminjamanList.isEmpty()) {
            Label noData = new Label("Tidak ada peminjaman yang menunggu verifikasi.");
            noData.getStyle().set("font-size", "16px").set("color", "#666");
            contentLayout.add(noData);
        } else {
            for (Peminjaman p : peminjamanList) {
                if (p.getStatus().equalsIgnoreCase("MENUNGGU")) {
                    contentLayout.add(createPeminjamanCard(p));
                }
            }
        }
    }

    private HorizontalLayout createPeminjamanCard(Peminjaman peminjaman) {
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

        Label nama = new Label(peminjaman.getNamaAkun() + " - " + peminjaman.getAkunId());
        nama.getStyle().set("font-weight", "600").set("font-size", "16px");

        HorizontalLayout ruang = new HorizontalLayout(new Icon(VaadinIcon.BUILDING), new Label(peminjaman.getNamaRuangan()));
        ruang.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout tanggal = new HorizontalLayout(new Icon(VaadinIcon.CALENDAR), new Label(peminjaman.getTanggalPeminjaman()));
        tanggal.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout jam = new HorizontalLayout(new Icon(VaadinIcon.CLOCK), new Label(peminjaman.getWaktu()));
        jam.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout detail = new HorizontalLayout(ruang, tanggal, jam);
        detail.setSpacing(true);
        detail.setAlignItems(FlexComponent.Alignment.CENTER);

        info.add(nama, detail);

        HorizontalLayout tombol = new HorizontalLayout();
        tombol.setSpacing(true);

        Button status = new Button(peminjaman.getStatus());
        status.getStyle()
            .set("background-color", "#fff933")
            .set("color", "#000")
            .set("font-weight", "600");

        Button detailBtn = new Button("Detail Peminjaman");
        detailBtn.getStyle()
            .set("background-color", "#f97316")
            .set("color", "#fff")
            .set("font-weight", "600");

        Dialog detailDialog = createStyledDialog(peminjaman);
        detailBtn.addClickListener(e -> {
            fetchDetailPeminjaman(peminjaman.getIdPeminjaman(), detailDialog, peminjaman);
            detailDialog.open();
        });

        tombol.add(status, detailBtn);
        card.add(info, tombol);

        return card;
    }

    private void fetchDetailPeminjaman(int idPeminjaman, Dialog dialog, Peminjaman peminjaman) {
        UI ui = UI.getCurrent();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create("http://localhost:8081/api/v1/peminjaman/detail/" + idPeminjaman))
                .GET()
                .header("Accept", "*/*")
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ui.access(() -> parseDetailPeminjaman(response.body(), dialog, peminjaman));
            } else {
                ui.access(() -> Notification.show("Gagal mengambil detail peminjaman: " + response.statusCode(),
                    3000, Notification.Position.MIDDLE));
            }
        } catch (Exception e) {
            ui.access(() -> Notification.show("Error mengambil detail peminjaman: " + e.getMessage(),
                3000, Notification.Position.MIDDLE));
        }
    }

    private void parseDetailPeminjaman(String jsonResponse, Dialog dialog, Peminjaman peminjaman) {
        try {
            JSONObject json = new JSONObject(jsonResponse);
            JSONObject data = json.getJSONObject("data");

            PeminjamanDetail detail = new PeminjamanDetail(
                data.getInt("peminjamanId"),
                data.optString("namaPeminjam", "Tidak tersedia"),
                data.optString("emailPeminjam", "Tidak tersedia"),
                data.optString("keperluan", "Tidak tersedia"),
                data.optString("namaRuangan", "Tidak tersedia"),
                data.optString("tanggalPeminjaman", "Tidak tersedia"),
                data.optString("waktuMulai", "") + "–" + data.optString("waktuSelesai", ""),
                data.optInt("kapasitas", 0)
            );

            UI.getCurrent().access(() -> updateDialogContent(dialog, detail, peminjaman.getIdPeminjaman()));
        } catch (Exception e) {
            UI.getCurrent().access(() -> Notification.show("Error parsing detail peminjaman: " + e.getMessage(),
                3000, Notification.Position.MIDDLE));
        }
    }

    private void updateDialogContent(Dialog dialog, PeminjamanDetail detail, int idPeminjaman) {
        dialog.removeAll();
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(true);
        dialogLayout.setSpacing(false);
        dialogLayout.setWidthFull();

        Label header = new Label("Detail Peminjaman Ruangan");
        header.getStyle()
            .set("font-size", "24px")
            .set("font-weight", "800")
            .set("margin-bottom", "20px");

        dialogLayout.add(header);
        dialogLayout.add(createLabeledField("Nama Peminjam", detail.getNamaPeminjam()));
        dialogLayout.add(createLabeledField("Email", detail.getEmailPeminjam()));
        dialogLayout.add(createLabeledField("Keperluan", detail.getKeperluan()));
        dialogLayout.add(createLabeledField("Ruangan", detail.getNamaRuangan()));

        HorizontalLayout tanggalJam = new HorizontalLayout();
        tanggalJam.setWidthFull();
        tanggalJam.setSpacing(true);
        tanggalJam.add(
            createLabeledField("Tanggal", detail.getTanggalPeminjaman()),
            createLabeledField("Jam", detail.getWaktu())
        );
        dialogLayout.add(tanggalJam);
        dialogLayout.add(createLabeledField("Kapasitas", String.valueOf(detail.getKapasitas())));

        HorizontalLayout actions = new HorizontalLayout();
        actions.setSpacing(true);
        actions.setWidthFull();
        actions.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        Button tolak = new Button("Tolak", e -> {
            updatePeminjamanStatus(idPeminjaman, false);
            dialog.close();
        });
        tolak.getStyle()
            .set("background-color", "#ef4444")
            .set("color", "white")
            .set("font-weight", "600")
            .set("border-radius", "8px")
            .set("width", "120px");

        Button izinkan = new Button("Izinkan", e -> {
            updatePeminjamanStatus(idPeminjaman, true);
            dialog.close();
        });
        izinkan.getStyle()
            .set("background-color", "#22c55e")
            .set("color", "white")
            .set("font-weight", "600")
            .set("border-radius", "8px")
            .set("width", "120px");

        actions.add(tolak, izinkan);
        dialogLayout.add(actions);

        dialog.add(dialogLayout);
    }

    private Dialog createStyledDialog(Peminjaman peminjaman) {
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(true);
        dialogLayout.setSpacing(false);
        dialogLayout.setWidthFull();

        Label header = new Label("Detail Peminjaman Ruangan");
        header.getStyle()
            .set("font-size", "24px")
            .set("font-weight", "800")
            .set("margin-bottom", "20px");

        dialogLayout.add(header);
        dialogLayout.add(createLabeledField("Nama Peminjam", peminjaman.getNamaAkun()));
        dialogLayout.add(createLabeledField("Email", "Memuat..."));
        dialogLayout.add(createLabeledField("Keperluan", "Memuat..."));
        dialogLayout.add(createLabeledField("Ruangan", peminjaman.getNamaRuangan()));

        HorizontalLayout tanggalJam = new HorizontalLayout();
        tanggalJam.setWidthFull();
        tanggalJam.setSpacing(true);
        tanggalJam.add(
            createLabeledField("Tanggal", peminjaman.getTanggalPeminjaman()),
            createLabeledField("Jam", peminjaman.getWaktu())
        );
        dialogLayout.add(tanggalJam);
        dialogLayout.add(createLabeledField("Kapasitas", "Memuat..."));

        dialog.add(dialogLayout);
        return dialog;
    }

    private VerticalLayout createLabeledField(String labelText, String value) {
        Label label = new Label(labelText);
        label.getStyle().set("font-weight", "700").set("margin-bottom", "4px");

        TextField field = new TextField();
        field.setValue(value);
        field.setReadOnly(true);
        field.setWidthFull();
        field.getStyle()
            .set("background-color", "#f3f4f6")
            .set("border-radius", "8px");

        VerticalLayout layout = new VerticalLayout(label, field);
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.setWidthFull();

        return layout;
    }

    private void updatePeminjamanStatus(int idPeminjaman, boolean isSetuju) {
        UI ui = UI.getCurrent();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create("http://localhost:8081/api/v1/peminjaman/" + idPeminjaman + "?isSetuju=" + isSetuju))
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .header("Accept", "*/*")
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("PATCH Response: " + response.body() + ", Status: " + response.statusCode());

            if (response.statusCode() == 200) {
                ui.access(() -> {
                    Notification.show("Peminjaman " + (isSetuju ? "disetujui" : "ditolak") + "!", 3000, Notification.Position.MIDDLE);
                    fetchPeminjamanData();
                });
            } else {
                ui.access(() -> Notification.show("Gagal memperbarui status peminjaman: " + response.statusCode(),
                    3000, Notification.Position.MIDDLE));
            }
        } catch (Exception e) {
            System.err.println("Status update error: " + e.getMessage());
            ui.access(() -> Notification.show("Error memperbarui status peminjaman: " + e.getMessage(),
                3000, Notification.Position.MIDDLE));
        }
    }

    public static class Peminjaman {
        private final int idPeminjaman;
        private final String namaAkun;
        private final String akunId;
        private final String namaRuangan;
        private final String tanggalPeminjaman;
        private final String waktu;
        private final String status;

        public Peminjaman(int idPeminjaman, String namaAkun, String akunId, String namaRuangan, String tanggalPeminjaman, String waktu, String status) {
            this.idPeminjaman = idPeminjaman;
            this.namaAkun = namaAkun;
            this.akunId = akunId;
            this.namaRuangan = namaRuangan;
            this.tanggalPeminjaman = tanggalPeminjaman;
            this.waktu = waktu;
            this.status = status;
        }

        public int getIdPeminjaman() { return idPeminjaman; }
        public String getNamaAkun() { return namaAkun; }
        public String getAkunId() { return akunId; }
        public String getNamaRuangan() { return namaRuangan; }
        public String getTanggalPeminjaman() { return tanggalPeminjaman; }
        public String getWaktu() { return waktu; }
        public String getStatus() { return status; }
    }

    public static class PeminjamanDetail {
        private final int idPeminjaman;
        private final String namaPeminjam;
        private final String emailPeminjam;
        private final String keperluan;
        private final String namaRuangan;
        private final String tanggalPeminjaman;
        private final String waktu;
        private final int kapasitas;

        public PeminjamanDetail(int idPeminjaman, String namaPeminjam, String emailPeminjam, String keperluan, String namaRuangan, String tanggalPeminjaman, String waktu, int kapasitas) {
            this.idPeminjaman = idPeminjaman;
            this.namaPeminjam = namaPeminjam;
            this.emailPeminjam = emailPeminjam;
            this.keperluan = keperluan;
            this.namaRuangan = namaRuangan;
            this.tanggalPeminjaman = tanggalPeminjaman;
            this.waktu = waktu;
            this.kapasitas = kapasitas;
        }

        public int getIdPeminjaman() { return idPeminjaman; }
        public String getNamaPeminjam() { return namaPeminjam; }
        public String getEmailPeminjam() { return emailPeminjam; }
        public String getKeperluan() { return keperluan; }
        public String getNamaRuangan() { return namaRuangan; }
        public String getTanggalPeminjaman() { return tanggalPeminjaman; }
        public String getWaktu() { return waktu; }
        public int getKapasitas() { return kapasitas; }
    }
}