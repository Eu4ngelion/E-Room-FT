package com.eroomft;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("user/pengajuan")
public class UserPengajuanView extends AppLayout {

    public UserPengajuanView() {
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
        String currentPage = "user/pengajuan";

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
            Notification.show("Anda telah keluar dari aplikasi.", 3000, Notification.Position.BOTTOM_END)
                    .setPosition(Notification.Position.BOTTOM_END);
            UI.getCurrent().getSession().close();
            UI.getCurrent().navigate("");
        });
        return button;
    }

    private Component createContent() {
        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);
        content.setSpacing(false);
        content.setHeightFull();

        // Title
        Span title = new Span("Ajukan Peminjaman Ruangan");
        title.getStyle()
                .set("font-size", "1.5rem")
                .set("font-weight", "bold")
                .set("margin-bottom", "1rem");
        content.add(title);

        // Form layout
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.setPadding(true);
        formLayout.setWidth("70%");
        formLayout.getStyle()
                .set("border", "1px solid #e0e0e0")
                .set("border-radius", "5px")
                .set("padding", "1rem");

        // Nama Peminjam
        String namaPeminjam = (String) UI.getCurrent().getSession().getAttribute("nama");
        Div namaPeminjamDiv = new Div();
        namaPeminjamDiv.setText("Nama Peminjam:");
        namaPeminjamDiv.getStyle()
                .set("font-weight", "bold");
        TextField namaPeminjamField = new TextField();
        namaPeminjamField.setValue(namaPeminjam != null ? namaPeminjam : "");
        namaPeminjamField.setReadOnly(true);
        namaPeminjamField.setWidthFull();
        namaPeminjamField.getStyle()
                .set("margin-bottom", "0.5rem");
        formLayout.add(namaPeminjamDiv, namaPeminjamField);

        // Keperluan
        Div keperluanDiv = new Div();
        keperluanDiv.setText("Keperluan:");
        keperluanDiv.getStyle()
                .set("font-weight", "bold");
        TextField keperluanField = new TextField();
        keperluanField.setPlaceholder("Masukkan keperluan peminjaman");
        keperluanField.setWidthFull();
        keperluanField.getStyle()
                .set("margin-bottom", "0.5rem");
        formLayout.add(keperluanDiv, keperluanField);

        // Tanggal Peminjaman
        Div tanggalDiv = new Div();
        tanggalDiv.setText("Tanggal Peminjaman:");
        tanggalDiv.getStyle()
                .set("font-weight", "bold");
        DatePicker tanggalPicker = new DatePicker();
        tanggalPicker.setPlaceholder("Pilih tanggal peminjaman");
        tanggalPicker.setWidthFull();
        tanggalPicker.getStyle()
                .set("margin-bottom", "0.5rem");
        tanggalPicker.setValue(LocalDate.now());
        tanggalPicker.setMin(LocalDate.now());
        formLayout.add(tanggalDiv, tanggalPicker);

        // Jam Mulai dan Jam Selesai
        HorizontalLayout jamLayout = new HorizontalLayout();
        jamLayout.setWidthFull();
        jamLayout.setSpacing(true);
        jamLayout.setAlignItems(FlexComponent.Alignment.BASELINE);

        // Jam Mulai Layout
        VerticalLayout jamMulaiLayout = new VerticalLayout();
        jamMulaiLayout.setSpacing(false);
        jamMulaiLayout.setPadding(false);

        Div jamMulaiLabel = new Div();
        jamMulaiLabel.setText("Jam Mulai:");
        jamMulaiLabel.getStyle()
                .set("font-weight", "bold");

        TextField jamMulaiField = new TextField();
        jamMulaiField.setPlaceholder("HH:MM");
        jamMulaiField.setWidthFull();
        jamMulaiField.getStyle()
                .set("margin-bottom", "1rem");
        jamMulaiField.setValue("08:00");
        jamMulaiLayout.add(jamMulaiLabel, jamMulaiField);

        // Jam Selesai Layout
        VerticalLayout jamSelesaiLayout = new VerticalLayout();
        jamSelesaiLayout.setSpacing(false);
        jamSelesaiLayout.setPadding(false);

        Div jamSelesaiLabel = new Div();
        jamSelesaiLabel.setText("Jam Selesai:");
        jamSelesaiLabel.getStyle()
                .set("font-weight", "bold");

        TextField jamSelesaiField = new TextField();
        jamSelesaiField.setPlaceholder("HH:MM");
        jamSelesaiField.setWidthFull();
        jamSelesaiField.getStyle()
                .set("margin-bottom", "1rem");
        jamSelesaiField.setValue("10:00");
        jamSelesaiLayout.add(jamSelesaiLabel, jamSelesaiField);

        jamLayout.add(jamMulaiLayout, jamSelesaiLayout);
        formLayout.add(jamLayout);

        // Nama Ruangan
        Div ruanganDiv = new Div();
        ruanganDiv.setText("Nama Ruangan:");
        ruanganDiv.getStyle()
                .set("font-weight", "bold");
        TextField ruanganField = new TextField();
        ruanganField.setPlaceholder("Masukkan nama ruangan (misal: A101)");
        ruanganField.setWidthFull();
        ruanganField.getStyle()
                .set("margin-bottom", "0.5rem");
        String namaRuangan = (String) UI.getCurrent().getSession().getAttribute("pengajuan_room_name");
        if (namaRuangan != null && !namaRuangan.isEmpty()) {
            ruanganField.setValue(namaRuangan);
        }
        formLayout.add(ruanganDiv, ruanganField);

        // Submit button
        Button submitButton = new Button("Ajukan");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submitButton.getStyle()
                .set("margin-top", "1rem")
                .set("align-self", "flex-end")
                .set("background-color", "#FF6B35")
                .set("color", "white")
                .set("border", "none")
                .set("padding", "0.5rem 2rem")
                .set("border-radius", "4px")
                .set("font-size", "1rem");

        submitButton.addClickListener(event -> {
            String keperluan = keperluanField.getValue();
            LocalDate tanggal = tanggalPicker.getValue();
            String jamMulai = jamMulaiField.getValue();
            String jamSelesai = jamSelesaiField.getValue();
            String namaRuanganValue = ruanganField.getValue();
            String akunId = (String) UI.getCurrent().getSession().getAttribute("akunId");

            if (tanggal == null || jamMulai.isEmpty() || jamSelesai.isEmpty() || namaRuanganValue.isEmpty() || akunId == null) {
                Notification.show("Semua field harus diisi.", 3000, Notification.Position.BOTTOM_END);
                // debug
                Notification.show("Debug Info: " +
                        "keperluan='" + keperluan + "', " +
                        "tanggal='" + tanggal + "', " +
                        "jamMulai='" + jamMulai + "', " +
                        "jamSelesai='" + jamSelesai + "', " +
                        "namaRuanganValue='" + namaRuanganValue + "', " +
                        "akunId='" + akunId + "'", 5000, Notification.Position.BOTTOM_END);

                return;
            }

            // Validate date
            if (tanggal.isBefore(LocalDate.now())) {
                Notification.show("Tanggal peminjaman hanya boleh hari ini atau setelahnya.", 3000, Notification.Position.BOTTOM_END);
                return;
            }

            // Validate time format
            if (!jamMulai.matches("\\d{2}:\\d{2}") || !jamSelesai.matches("\\d{2}:\\d{2}")) {
                Notification.show("Format jam harus HH:MM.", 3000, Notification.Position.BOTTOM_END);
                return;
            }

            // Validate time against current time if same day
            if (tanggal.equals(LocalDate.now())) {
                ZoneId witaZone = ZoneId.of("Asia/Makassar");
                LocalDateTime nowWITA = LocalDateTime.now(witaZone);
                int currentHour = nowWITA.getHour();
                int currentMinute = nowWITA.getMinute();
                String[] jamMulaiParts = jamMulai.split(":");
                if (Integer.parseInt(jamMulaiParts[0]) < currentHour ||
                    (Integer.parseInt(jamMulaiParts[0]) == currentHour && Integer.parseInt(jamMulaiParts[1]) <= currentMinute)) {
                    Notification.show("Jam mulai harus lebih besar dari jam sekarang.", 3000, Notification.Position.BOTTOM_END);
                    return;
                }
            }

            // Validate start time is before end time
            try {
                LocalTime mulai = LocalTime.parse(jamMulai);
                LocalTime selesai = LocalTime.parse(jamSelesai);
                if (!mulai.isBefore(selesai)) {
                    Notification.show("Jam mulai harus lebih kecil dari jam selesai.", 3000, Notification.Position.BOTTOM_END);
                    return;
                }
            } catch (Exception e) {
                Notification.show("Format jam tidak valid.", 3000, Notification.Position.BOTTOM_END);
                return;
            }

            // Prepare JSON payload
            String jsonPayload = String.format(
                "{\"akunId\": \"%s\", \"namaRuangan\": \"%s\", \"keperluan\": \"%s\", \"tanggalPeminjaman\": \"%s\", \"waktuMulai\": \"%s\", \"waktuSelesai\": \"%s\"}",
                akunId, namaRuanganValue, keperluan, tanggal.format(DateTimeFormatter.ISO_LOCAL_DATE), jamMulai, jamSelesai
            );

            // Send API request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/api/v1/peminjaman"))
                    .header("Content-Type", "application/json")
                    .header("accept", "*/*")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    Notification.show("Peminjaman berhasil diajukan!", 3000, Notification.Position.BOTTOM_END);
                    UI.getCurrent().navigate("user/daftar-peminjaman");
                } else {
                    Notification.show("Gagal mengajukan peminjaman: " + response.body(), 3000, Notification.Position.BOTTOM_END);
                }
            } catch (IOException | InterruptedException e) {
                Notification.show("Error connecting to server: " + e.getMessage(), 3000, Notification.Position.BOTTOM_END);
            }
        });

        formLayout.add(submitButton);
        formLayout.setSpacing(false);
        content.add(formLayout);
        content.setSizeFull();
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        content.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        content.getStyle()
                .set("padding", "2rem")
                .set("background-color", "#f9f9f9");
        return content;
    }
}