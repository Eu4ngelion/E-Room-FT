package com.eroomft;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("user/detail-ruangan")
public class UserDetailRuanganView extends AppLayout implements HasUrlParameter<String> {

    private String ruanganId;
    private RoomData roomData;
    private List<ScheduleData> schedules = new ArrayList<>();
    private Div scheduleList;

    public UserDetailRuanganView() {
        String role = (String) UI.getCurrent().getSession().getAttribute("role");
        if (role == null || (!role.equalsIgnoreCase("mahasiswa") && !role.equalsIgnoreCase("dosen"))) {
            Notification.show("Anda tidak memiliki akses ke halaman ini.", 3000, Notification.Position.BOTTOM_END);
            UI.getCurrent().access(() -> {
                UI.getCurrent().navigate("");
            });
            return;
        }
        createDrawer();
    }

@Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        // Extract ruanganId from query parameters using BeforeEvent
        ruanganId = event.getLocation().getQueryParameters().getParameters()
                .getOrDefault("ruanganId", List.of())
                .stream()
                .findFirst()
                .orElse(null);

        if (ruanganId == null || ruanganId.isEmpty()) {
            Notification.show("Ruangan ID tidak ditemukan.", 3000, Notification.Position.MIDDLE);
            UI.getCurrent().access(() -> {
                UI.getCurrent().navigate("user/ruangan");
            });
            return;
        }

        // Debug log to verify ruanganId
        System.out.println("Extracted ruanganId: " + ruanganId);

        fetchRoomData();
        setContent(createContent());
    }

    private void createDrawer() {
        String currentPage = "user/ruangan";

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
            UI.getCurrent().navigate(targetPage);
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
        HorizontalLayout content = new HorizontalLayout();
        content.setPadding(true);
        content.setSpacing(true);
        content.setWidthFull();
        content.setHeightFull();
        content.getStyle()
                .set("background-color", "#FEE6D5");



        VerticalLayout roomDetailsLayout = new VerticalLayout();
        roomDetailsLayout.setWidth("60%");
        // roomDetailsLayout.setPadding(true);
        // roomDetailsLayout.setSpacing(true);
        roomDetailsLayout.setAlignItems(FlexComponent.Alignment.START);
        roomDetailsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
    
        roomDetailsLayout.getStyle()
                .set("background-color", "transparent")
                .set("border-radius", "8px");

        if (roomData == null) {
            Notification.show("Data ruangan tidak ditemukan.", 3000, Notification.Position.MIDDLE);
            UI.getCurrent().access(() -> {
                UI.getCurrent().navigate("user/ruangan");
            });
        }

        // room title
        Span roomTitle = new Span("Detail Ruang " + toTitleCase(roomData.getTipe()) + " " + roomData.getName());
        roomTitle.getStyle()
                .set("font-size", "2rem")
                .set("font-weight", "bold")
                .set("color", "black");
        roomDetailsLayout.add(roomTitle);

        // room image
        Div imageDiv = new Div();
        imageDiv.getStyle()
                .set("width", "100%")
                .set("height", "300px")
                .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("color", "white")
                .set("font-size", "2rem")
                .set("border-radius", "8px")
                .set("overflow", "hidden");
        if (roomData.getImage() != null && !roomData.getImage().isEmpty()) {
            Image roomImage = new Image("/uploads/" + roomData.getImage(), roomData.getImage());
            roomImage.getStyle()
                    .set("width", "100%")
                    .set("height", "100%")
                    .set("object-fit", "cover")
                    .set("border-radius", "8px");
            imageDiv.add(roomImage);
        } else {
            imageDiv.add(new Icon(VaadinIcon.BUILDING));
        }
        roomDetailsLayout.add(imageDiv);

        // detail ruangan
        VerticalLayout roomInfo = new VerticalLayout();
        roomInfo.setWidthFull();
        roomInfo.setSpacing(false);
        roomInfo.getStyle()
                .set("background-color", "transparent")
                .set("padding", "0")
                .set("margin", "0");
        roomInfo.add(createDetailItem("Gedung", roomData.getGedung()));
        roomInfo.add(createDetailItem("Tipe", toTitleCase(roomData.getTipe())));
        roomInfo.add(createDetailItem("Lokasi", roomData.getLocation()));
        roomInfo.add(createDetailItem("Fasilitas", roomData.getFacilities()));
        roomInfo.add(createDetailItem("Kapasitas", roomData.getCapacity()));
        roomInfo.add(createDetailItem("Kode", roomData.getName()));
        roomDetailsLayout.add(roomInfo);

        // simpan button
        Button pinjamBtn = new Button("Pinjam");
        pinjamBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        pinjamBtn.getStyle()
                .set("background-color", "#FF6B35")
                .set("color", "white")
                .set("border", "none")
                .set("padding", "0.5rem 2rem")
                .set("border-radius", "4px")
                .set("font-size", "1rem")
                .set("width", "20%")
                // .set("margin-top", "1rem")
                ;
        pinjamBtn.addClickListener(e -> {
            String targetUrl = "user/pengajuan?namaRuangan=" + roomData.getName();
            UI.getCurrent().access(() -> {
                // Store room data in session for the pengajuan page
                UI.getCurrent().getSession().setAttribute("pengajuan_room_id", roomData.getRuanganId());
                UI.getCurrent().getSession().setAttribute("pengajuan_room_name", "Ruang " + toTitleCase(roomData.getTipe()) + " " + roomData.getName());
                UI.getCurrent().navigate(targetUrl);
            });
        });
        roomDetailsLayout.add(pinjamBtn);
        
        // Schedule Section
        // Right : Vertical Layout for Schedule (40% width)
        // > Title (Jadwal Ruangan) & Date Picker
        // > Schedule List (displaying time slots and status)

        VerticalLayout ScheduleLayout = new VerticalLayout();
        ScheduleLayout.setWidth("40%");
        ScheduleLayout.setPadding(true);
        ScheduleLayout.setSpacing(true);
        ScheduleLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        ScheduleLayout.getStyle()
                .set("background-color", "white")
                .set("border-radius", "8px")
                .set("box-shadow", "var(--lumo-box-shadow-xs)")
                .set("padding", "1.5rem")
                .set("margin-top", "2rem")
                ;

        // Schedule Ttile + Date Picker
        HorizontalLayout scheduleHeader = new HorizontalLayout();
        scheduleHeader.setWidthFull();
        scheduleHeader.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        scheduleHeader.setAlignItems(FlexComponent.Alignment.CENTER);
        H3 scheduleTitle = new H3("Jadwal Ruangan");
        scheduleTitle.getStyle()
                .set("margin", "0 0 1rem 0")
                .set("color", "#FF6B35");
        DatePicker datePicker = new DatePicker("Pilih Tanggal");
        datePicker.setValue(LocalDate.now());
        datePicker.getStyle()
        .set("border", "2px solid #FF6B35")
        .set("border-radius", "8px");
        datePicker.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                fetchScheduleData(event.getValue());
                updateScheduleList();
            }
        });
        scheduleHeader.add(scheduleTitle, datePicker);
        ScheduleLayout.add(scheduleHeader);

        // Schedule List header (jam, status)
        scheduleList = new Div();
        Div scheduleListHeader = new Div();
        scheduleListHeader.getStyle()
                .set("display", "flex")
                .set("justify-content", "space-between")
                .set("padding", "0.5rem")
                .set("font-weight", "bold")
                .set("border-bottom", "1px solid var(--lumo-contrast-10pct)");
        scheduleListHeader.add(new Span("Jam"), new Span("Status"));
        scheduleList.add(scheduleListHeader);


        // Schedule List
        scheduleList.getStyle()
                .set("width", "100%")
                .set("max-height", "400px")
                .set("overflow-y", "auto")
                .set("padding", "0.5rem")
                .set("border-radius", "8px")
                .set("background-color", "#f9f9f9");
        fetchScheduleData(LocalDate.now()); // Inisialisasi dengan tanggal hari ini
        updateScheduleList();
        ScheduleLayout.add(scheduleList);
        content.add(roomDetailsLayout, ScheduleLayout);
        return content;
    }

    private Div createDetailItem(String label, String value) {
        Div item = new Div();
        item.getStyle()
                .set("margin-bottom", "0.5rem")
                .set("font-size", "1rem");

        Span labelSpan = new Span(label + ": ");
        labelSpan.getStyle()
                .set("font-weight", "600")
                .set("color", "black");

        Span valueSpan = new Span(value);
        valueSpan.getStyle().set("color", "black");
        valueSpan.getStyle()
                .set("font-weight", "600")
                .set("color", "var(--lumo-secondary-text-color)");  

        item.add(labelSpan, valueSpan);
        return item;
    }

    private void fetchRoomData() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/api/v1/ruangan/" + ruanganId))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                parseRoomData(response.body());
            } else {
                Notification.show("Failed to fetch room data: " + response.statusCode(), 3000, Notification.Position.MIDDLE);
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error connecting to server: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private void fetchScheduleData(LocalDate date) {
        try {
            String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/api/v1/ruangan/" + ruanganId + "/jadwal?tanggal=" + formattedDate))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                parseScheduleData(response.body());
            } else {
                Notification.show("Failed to fetch schedule data: " + response.statusCode(), 3000, Notification.Position.MIDDLE);
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error connecting to server: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private void parseRoomData(String jsonData) {
        try {
            String dataStr = extractJsonObject(jsonData, "data");
            if (dataStr == null) {
                Notification.show("No room data found.", 3000, Notification.Position.MIDDLE);
                return;
            }

            String ruanganId = extractJsonValue(dataStr, "ruanganId");
            String tipe = extractJsonValue(dataStr, "tipe");
            String nama = extractJsonValue(dataStr, "nama");
            String kapasitas = extractJsonValue(dataStr, "kapasitas");
            String fasilitas = extractJsonValue(dataStr, "fasilitas");
            String gedung = extractJsonValue(dataStr, "gedung");
            String lokasi = extractJsonValue(dataStr, "lokasi");
            String pathGambar = extractJsonValue(dataStr, "pathGambar");

            if (ruanganId != null && tipe != null && nama != null && kapasitas != null && fasilitas != null && gedung != null && lokasi != null) {
                roomData = new RoomData(ruanganId, tipe, nama, kapasitas, fasilitas, gedung, lokasi, pathGambar);
            }
        } catch (Exception e) {
            Notification.show("Error parsing room data: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private void parseScheduleData(String jsonData) {
        try {
            String dataStr = extractJsonArray(jsonData, "data");
            if (dataStr == null || dataStr.equals("[]")) {
                schedules.clear();
                return;
            }

            String[] scheduleObjects = dataStr.trim().split("\\},\\s*\\{");
            schedules.clear();
            for (String scheduleObj : scheduleObjects) {
                scheduleObj = scheduleObj.trim();
                if (!scheduleObj.startsWith("{")) {
                    scheduleObj = "{" + scheduleObj;
                }
                if (!scheduleObj.endsWith("}")) {
                    scheduleObj = scheduleObj + "}";
                }

                String jamMulai = extractJsonValue(scheduleObj, "jamMulai");
                String jamSelesai = extractJsonValue(scheduleObj, "jamSelesai");
                String status = extractJsonValue(scheduleObj, "status");

                if (jamMulai != null && jamSelesai != null && status != null) {
                    schedules.add(new ScheduleData(jamMulai, jamSelesai, status));
                }
            }
        } catch (Exception e) {
            Notification.show("Error parsing schedule data: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private void updateScheduleList() {
        scheduleList.removeAll();
        if (schedules.isEmpty()) {
            Span noSchedule = new Span("Tidak ada jadwal pada tanggal ini.");
            noSchedule.getStyle().set("color", "var(--lumo-secondary-text-color)");
            scheduleList.add(noSchedule);
            return;
        }

        for (ScheduleData schedule : schedules) {
            Div scheduleItem = new Div();
            scheduleItem.getStyle()
                    .set("padding", "0.5rem")
                    .set("border-bottom", "1px solid var(--lumo-contrast-10pct)")
                    .set("display", "flex")
                    .set("justify-content", "space-between");

            Span timeSpan = new Span(schedule.getJamMulai() + " - " + schedule.getJamSelesai());
            Span statusSpan;
            if (schedule.getStatus().equals("DIIZINKAN")) {
                statusSpan = new Span("DIPINJAM");
            } else {
                statusSpan = new Span(schedule.getStatus());
            }
            statusSpan.getStyle()
                    .set("color", "#F1556A")
                    .set("font-weight", "bold");

            scheduleItem.add(timeSpan, statusSpan);
            scheduleList.add(scheduleItem);
        }
    }

    private String extractJsonObject(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":";
            int keyIndex = json.indexOf(searchKey);
            if (keyIndex == -1) return null;

            int valueStart = keyIndex + searchKey.length();
            while (valueStart < json.length() && Character.isWhitespace(json.charAt(valueStart))) {
                valueStart++;
            }

            if (json.charAt(valueStart) == '{') {
                int braceCount = 1;
                int valueEnd = valueStart + 1;
                while (braceCount > 0 && valueEnd < json.length()) {
                    if (json.charAt(valueEnd) == '{') braceCount++;
                    if (json.charAt(valueEnd) == '}') braceCount--;
                    valueEnd++;
                }
                if (braceCount == 0) {
                    return json.substring(valueStart, valueEnd);
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private String extractJsonArray(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":";
            int keyIndex = json.indexOf(searchKey);
            if (keyIndex == -1) return null;

            int valueStart = keyIndex + searchKey.length();
            while (valueStart < json.length() && Character.isWhitespace(json.charAt(valueStart))) {
                valueStart++;
            }

            if (json.charAt(valueStart) == '[') {
                int bracketCount = 1;
                int valueEnd = valueStart + 1;
                while (bracketCount > 0 && valueEnd < json.length()) {
                    if (json.charAt(valueEnd) == '[') bracketCount++;
                    if (json.charAt(valueEnd) == ']') bracketCount--;
                    valueEnd++;
                }
                if (bracketCount == 0) {
                    return json.substring(valueStart + 1, valueEnd - 1);
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private String extractJsonValue(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":";
            int keyIndex = json.indexOf(searchKey);
            if (keyIndex == -1) return null;

            int valueStart = keyIndex + searchKey.length();
            while (valueStart < json.length() && Character.isWhitespace(json.charAt(valueStart))) {
                valueStart++;
            }

            if (valueStart < json.length() && json.charAt(valueStart) == '"') {
                valueStart++;
                int valueEnd = json.indexOf('"', valueStart);
                if (valueEnd != -1) {
                    return json.substring(valueStart, valueEnd);
                }
            } else {
                int valueEnd = valueStart;
                while (valueEnd < json.length() &&
                        json.charAt(valueEnd) != ',' &&
                        json.charAt(valueEnd) != '}' &&
                        json.charAt(valueEnd) != ']') {
                    valueEnd++;
                }
                return json.substring(valueStart, valueEnd).trim();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private String toTitleCase(String s) {
        return (s == null || s.isEmpty()) ? s : s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    private static class RoomData {
        private final String ruanganId;
        private final String tipe;
        private final String name;
        private final String capacity;
        private final String facilities;
        private final String gedung;
        private final String location;
        private final String image;

        public RoomData(String ruanganId, String tipe, String name, String capacity, String facilities, String gedung, String location, String image) {
            this.ruanganId = ruanganId;
            this.tipe = tipe;
            this.name = name;
            this.capacity = capacity;
            this.facilities = facilities;
            this.gedung = gedung;
            this.location = location;
            this.image = image;
        }

        public String getRuanganId() { return ruanganId; }
        public String getTipe() { return tipe; }
        public String getName() { return name; }
        public String getCapacity() { return capacity; }
        public String getFacilities() { return facilities; }
        public String getGedung() { return gedung; }
        public String getLocation() { return location; }
        public String getImage() { return image; }
    }

    private static class ScheduleData {
        private final String jamMulai;
        private final String jamSelesai;
        private final String status;

        public ScheduleData(String jamMulai, String jamSelesai, String status) {
            this.jamMulai = jamMulai;
            this.jamSelesai = jamSelesai;
            this.status = status;
        }

        public String getJamMulai() { return jamMulai; }
        public String getJamSelesai() { return jamSelesai; }
        public String getStatus() { return status; }
    }
}