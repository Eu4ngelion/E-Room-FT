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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
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

import jakarta.annotation.PostConstruct;

@Route("user/detail-ruangan")
@org.springframework.stereotype.Component
@Scope("prototype")
public class UserDetailRuanganView extends HorizontalLayout implements HasUrlParameter<String> {

    private String ruanganId;
    private RoomData roomData;
    private List<ScheduleData> schedules = new ArrayList<>();
    private Div scheduleList;
    private SidebarComponent sidebar;
    private VerticalLayout mainContent;

    @Value("${api.base.url:}")
    private String apiBaseUrl;

    public UserDetailRuanganView() {
        String role = (String) UI.getCurrent().getSession().getAttribute("role");
        if (role == null || (!role.equalsIgnoreCase("mahasiswa") && !role.equalsIgnoreCase("dosen"))) {
            Notification.show("Anda tidak memiliki akses ke halaman ini.", 3000, Notification.Position.BOTTOM_END);
            UI.getCurrent().access(() -> {
                UI.getCurrent().navigate("");
            });
            return;
        }

        setSizeFull();
        setPadding(false);
        setSpacing(false);

        sidebar = new SidebarComponent();

        mainContent = new VerticalLayout();
        mainContent.setSizeFull();
        mainContent.setPadding(false);
        mainContent.getStyle().set("background-color", "#FEE6D5");
        mainContent.getStyle().set("overflow", "auto");

        add(sidebar, mainContent);
    }

    @PostConstruct
    private void init() {
        System.out.println("Injected apiBaseUrl: " + apiBaseUrl);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
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

        System.out.println("Extracted ruanganId: " + ruanganId);

        fetchRoomData();
        if (roomData != null) {
            mainContent.add(createContent());
        } else {
            Notification.show("Data ruangan tidak ditemukan.", 3000, Notification.Position.MIDDLE);
            UI.getCurrent().access(() -> {
                UI.getCurrent().navigate("user/ruangan");
            });
        }
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
        roomDetailsLayout.setAlignItems(FlexComponent.Alignment.START);
        roomDetailsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        roomDetailsLayout.getStyle()
                .set("background-color", "transparent")
                .set("border-radius", "8px");

        Span roomTitle = new Span("Detail Ruang " + toTitleCase(roomData.getTipe()) + " " + roomData.getName());
        roomTitle.getStyle()
                .set("font-size", "2rem")
                .set("font-weight", "bold")
                .set("color", "black");
        roomDetailsLayout.add(roomTitle);

        Div imageDiv = new Div();
        imageDiv.getStyle()
                .set("width", "100%")
                .set("height", "500px")
                .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("color", "white")
                .set("font-size", "2rem")
                .set("border-radius", "8px")
                .set("overflow", "hidden");
        if (roomData.getImage() != null && !roomData.getImage().isEmpty()) {
            Image roomImage = new Image("/Uploads/" + roomData.getImage(), roomData.getImage());
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

        // VerticalLayout roomInfo = new VerticalLayout();
        // roomInfo.setWidthFull();
        // roomInfo.setSpacing(false);
        // roomInfo.getStyle()
        //         .set("background-color", "transparent")
        //         .set("padding", "0")
        //         .set("margin", "0");
        // roomInfo.add(createDetailItem("Gedung", roomData.getGedung()));
        // roomInfo.add(createDetailItem("Tipe", toTitleCase(roomData.getTipe())));
        // roomInfo.add(createDetailItem("Lokasi", roomData.getLocation()));
        // roomInfo.add(createDetailItem("Fasilitas", roomData.getFacilities()));
        // roomInfo.add(createDetailItem("Kapasitas", roomData.getCapacity()));
        // roomInfo.add(createDetailItem("Kode", roomData.getName()));
        // roomDetailsLayout.add(roomInfo);

        // New Room Info
        HorizontalLayout roomInfo = new HorizontalLayout();
        roomInfo.setWidthFull();
        roomInfo.setSpacing(true);
        roomInfo.getStyle()
                .set("background-color", "white")
                .set("padding", "0")
                .set("margin", "0")
                .set("border-radius", "8px")
                .set("box-shadow", "var(--lumo-box-shadow-xs)");
        roomInfo.setAlignItems(FlexComponent.Alignment.START);
        roomInfo.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        VerticalLayout leftInfo = new VerticalLayout();
        leftInfo.setWidth("50%");
        leftInfo.setPadding(true);
        leftInfo.setSpacing(true);
        leftInfo.getStyle()
                .set("background-color", "transparent")
                .set("padding", "1.5rem");
        leftInfo.add(createDetailItem("Nama Gedung", roomData.getGedung()));
        leftInfo.add(createDetailItem("Tipe Ruangan", toTitleCase(roomData.getTipe())));
        leftInfo.add(createDetailItem("Lokasi Ruangan", roomData.getLocation()));

        VerticalLayout rightInfo = new VerticalLayout();
        rightInfo.setWidth("50%");
        rightInfo.setPadding(true);
        rightInfo.setSpacing(true);
        rightInfo.getStyle()
                .set("background-color", "transparent")
                .set("padding", "1.5rem");
        rightInfo.add(createDetailItem("Fasilitas", roomData.getFacilities()));
        rightInfo.add(createDetailItem("Kapasitas", roomData.getCapacity()));
        rightInfo.add(createDetailItem("Kode Ruangan", roomData.getName()));
        roomInfo.add(leftInfo, rightInfo);
        roomDetailsLayout.add(roomInfo);




        Button pinjamBtn = new Button("Pinjam");
        pinjamBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        pinjamBtn.getStyle()
                .set("background-color", "#FF6B35")
                .set("color", "white")
                .set("border", "none")
                .set("padding", "0.5rem 2rem")
                .set("border-radius", "4px")
                .set("font-size", "1rem")
                .set("width", "20%");
        pinjamBtn.addClickListener(e -> {
            UI.getCurrent().access(() -> {
                UI.getCurrent().getSession().setAttribute("pengajuan_room_id", roomData.getRuanganId());
                UI.getCurrent().getSession().setAttribute("pengajuan_room_name", roomData.getName());
                UI.getCurrent().navigate("user/pengajuan");
            });
        });
        roomDetailsLayout.add(pinjamBtn);

        VerticalLayout scheduleLayout = new VerticalLayout();
        scheduleLayout.setWidth("40%");
        scheduleLayout.setPadding(true);
        scheduleLayout.setSpacing(true);
        scheduleLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        scheduleLayout.getStyle()
                .set("background-color", "white")
                .set("border-radius", "8px")
                .set("box-shadow", "var(--lumo-box-shadow-xs)")
                .set("padding", "1.5rem")
                .set("margin-top", "2rem");

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
        scheduleLayout.add(scheduleHeader);

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

        scheduleList.getStyle()
                .set("width", "100%")
                .set("max-height", "400px")
                .set("overflow-y", "auto")
                .set("padding", "0.5rem")
                .set("border-radius", "8px")
                .set("background-color", "#f9f9f9");
        fetchScheduleData(LocalDate.now());
        updateScheduleList();
        scheduleLayout.add(scheduleList);
        content.add(roomDetailsLayout, scheduleLayout);
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
        valueSpan.getStyle()
                .set("font-weight", "600")
                .set("color", "var(--lumo-secondary-text-color)");

        item.add(labelSpan, valueSpan);
        return item;
    }

    private void fetchRoomData() {
        try {
            if (apiBaseUrl == null || apiBaseUrl.trim().isEmpty()) {
                Notification.show("Error: API base URL is not configured!", 3000, Notification.Position.MIDDLE);
                throw new IllegalStateException("API base URL is not configured in application.properties");
            }
            String normalizedBaseUrl = apiBaseUrl.trim();
            if (!normalizedBaseUrl.startsWith("http://") && !normalizedBaseUrl.startsWith("https://")) {
                Notification.show("Error: API base URL missing scheme (http:// or https://)!", 3000, Notification.Position.MIDDLE);
                throw new IllegalArgumentException("API base URL missing scheme: " + normalizedBaseUrl);
            }
            if (normalizedBaseUrl.endsWith("/")) {
                normalizedBaseUrl = normalizedBaseUrl.substring(0, normalizedBaseUrl.length() - 1);
            }

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(normalizedBaseUrl + "/api/v1/ruangan/" + ruanganId))
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
        } catch (IllegalStateException | IllegalArgumentException e) {
            Notification.show("Error: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private void fetchScheduleData(LocalDate date) {
        try {
            if (apiBaseUrl == null || apiBaseUrl.trim().isEmpty()) {
                Notification.show("Error: API base URL is not configured!", 3000, Notification.Position.MIDDLE);
                throw new IllegalStateException("API base URL is not configured in application.properties");
            }
            String normalizedBaseUrl = apiBaseUrl.trim();
            if (!normalizedBaseUrl.startsWith("http://") && !normalizedBaseUrl.startsWith("https://")) {
                Notification.show("Error: API base URL missing scheme (http:// or https://)!", 3000, Notification.Position.MIDDLE);
                throw new IllegalArgumentException("API base URL missing scheme: " + normalizedBaseUrl);
            }
            if (normalizedBaseUrl.endsWith("/")) {
                normalizedBaseUrl = normalizedBaseUrl.substring(0, normalizedBaseUrl.length() - 1);
            }

            String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(normalizedBaseUrl + "/api/v1/ruangan/" + ruanganId + "/jadwal?tanggal=" + formattedDate))
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
        } catch (IllegalStateException | IllegalArgumentException e) {
            Notification.show("Error: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private void parseRoomData(String jsonData) {
        try {
            String dataStr = extractJsonObject(jsonData, "data");
            if (dataStr == null) {
                Notification.show("No room data found.", 3000, Notification.Position.MIDDLE);
                return;
            }

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
        Div scheduleListHeader = new Div();
        scheduleListHeader.getStyle()
                .set("display", "flex")
                .set("justify-content", "space-between")
                .set("padding", "0.5rem")
                .set("font-weight", "bold")
                .set("border-bottom", "1px solid var(--lumo-contrast-10pct)");
        scheduleListHeader.add(new Span("Jam"), new Span("Status"));
        scheduleList.add(scheduleListHeader);

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