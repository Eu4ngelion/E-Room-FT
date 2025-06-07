package com.eroomft;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
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
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import com.vaadin.flow.component.Component;

@Route("user/ruangan")
public class UserDaftarRuanganView extends HorizontalLayout {
    private List<RoomData> rooms = new ArrayList<>();
    private Div roomGrid;
    private Set<String> distinctGedung = new HashSet<>();
    private SidebarComponent sidebar;

    public UserDaftarRuanganView() {
        // Validasi Sesi Aktif
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
        
        VerticalLayout mainContent = new VerticalLayout();
        mainContent.setSizeFull();
        mainContent.setPadding(false);
        mainContent.getStyle().set("background-color", "#FEE6D5");
        mainContent.getStyle().set("overflow", "auto");

        // Ensure all preceding blocks are properly closed
        mainContent.add(createContent());

        add(sidebar, mainContent);
    }

    private Component createContent() {
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);
        content.setWidthFull();
        content.getStyle()
                .set("min-height", "100vh")
                .set("background-color", "#FEE6D5");

        Div headerBox = new Div();
        headerBox.getStyle()
                .set("background-color", "white")
                .set("padding", "1.5rem")
                .set("box-sizing", "border-box")
                .set("border-radius", "var(--lumo-border-radius-m)")
                .set("box-shadow", "var(--lumo-box-shadow-xs)")
                .set("width", "100%");

        H2 title = new H2("Daftar Ruangan");
        title.getStyle()
                .set("margin", "0 0 0.5rem 0")
                .set("color", "#FF6B35")
                .set("border-bottom", "3px solid #FF6B35")
                .set("padding-bottom", "0.5rem")
                .set("display", "inline-block");

        headerBox.add(title);

        fetchRoomData(null, null, null);
        fetchDistinctGedung();

        HorizontalLayout searchSection = createSearchSection();
        roomGrid = createRoomGrid();

        content.add(headerBox, searchSection, roomGrid);
        return content;
    }

    private HorizontalLayout createSearchSection() {
        HorizontalLayout searchBox = new HorizontalLayout();
        searchBox.setWidthFull();
        searchBox.getStyle()
                .set("background-color", "white")
                .set("padding", "1.5rem")
                .set("border-radius", "var(--lumo-border-radius-m)")
                .set("box-sizing", "border-box")
                .set("box-shadow", "var(--lumo-box-shadow-xs)")
                .set("margin-top", "0rem")
                .set("width", "100%");

        TextField searchField = new TextField();
        searchField.setPlaceholder("Cari Ruangan...");
        searchField.addClassNames("no-gray-background");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setWidth("300px");
        searchField.getStyle()
                .set("background-color", "white")
                .set("--lumo-contrast-5pct", "white")
                .set("--lumo-contrast-10pct", "white")
                .set("--lumo-primary-color", "#FF6B35")
                .set("border", "2px solid #FF6B35")
                .set("border-radius", "8px")
                .set("box-sizing", "border-box")
                .set("width", "100%");

        ComboBox<String> typeFilter = new ComboBox<>();
        typeFilter.setPlaceholder("Tipe");
        typeFilter.addClassNames("no-gray-background");
        typeFilter.setItems("Semua", "Kelas", "Laboratorium", "Seminar", "Rapat");
        typeFilter.getStyle()
                .set("background-color", "white")
                .set("--lumo-contrast-5pct", "white")
                .set("--lumo-contrast-10pct", "white")
                .set("--lumo-primary-color", "#FF6B35")
                .set("border", "2px solid #FF6B35")
                .set("border-radius", "8px");

        ComboBox<String> gedungFilter = new ComboBox<>();
        gedungFilter.setPlaceholder("Gedung");
        gedungFilter.addClassNames("no-gray-background");
        List<String> gedungList = new ArrayList<>(distinctGedung);
        gedungList.add(0, "Semua");
        gedungFilter.setItems(gedungList.toArray(String[]::new));
        gedungFilter.getStyle()
                .set("background-color", "white")
                .set("--lumo-contrast-5pct", "white")
                .set("--lumo-contrast-10pct", "white")
                .set("--lumo-primary-color", "#FF6B35")
                .set("border", "2px solid #FF6B35")
                .set("border-radius", "8px");

        Button searchBtn = new Button("Cari");
        searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchBtn.getStyle()
                .set("background-color", "#FF6B35")
                .set("border-color", "#FF6B35")
                .set("color", "white")
                .set("height", "40px")
                .set("border-radius", "8px");

        searchBtn.addClickListener(e -> {
            String searchText = searchField.getValue().trim();
            String selectedType = typeFilter.getValue();
            String selectedGedung = gedungFilter.getValue();

            roomGrid.removeAll();
            fetchRoomData(searchText, selectedType, selectedGedung);
            roomGrid.setVisible(true);

            Notification.show("Pencarian dilakukan: " + searchText + ", Tipe: " + selectedType + ", Gedung: " + selectedGedung, 3000, Notification.Position.MIDDLE);
        });

        HorizontalLayout searchLayout = new HorizontalLayout(
                searchField, typeFilter, gedungFilter, searchBtn
        );
        searchLayout.setAlignItems(FlexComponent.Alignment.END);
        searchLayout.setWidthFull();
        searchLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        searchLayout.expand(searchField);

        searchBox.add(searchLayout);

        HorizontalLayout wrapper = new HorizontalLayout(searchBox);
        wrapper.setWidthFull();
        wrapper.setPadding(false);
        wrapper.setMargin(false);
        wrapper.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        return wrapper;
    }

    private Div createRoomGrid() {
        Div grid = new Div();
        grid.getStyle()
                .set("display", "grid")
                .set("justify-content", "center")
                .set("grid-template-columns", "repeat(auto-fill, minmax(min(250px, 100vw / 4), 1fr))")
                .set("gap", "1.5rem")
                .set("margin-top", "0rem")
                .set("padding", "0.5rem")
                .set("box-sizing", "border-box")
                .set("margin-bottom", "1rem")
                .set("width", "100%");

        if (rooms == null || rooms.isEmpty()) {
            Notification.show("Tidak ada ruangan yang ditemukan. Memeriksa data: " + rooms.size() + " ruangan", 3000, Notification.Position.MIDDLE);
            grid.getElement().removeAllChildren();
            return grid;
        }

        grid.getElement().removeAllChildren();
        for (RoomData room : rooms) {
            if (room == null) {
                continue;
            }
            Div card = createRoomCard(room);
            grid.add(card);
        }
        return grid;
    }

    private Div createRoomCard(RoomData room) {
        Div card = new Div();
        card.getStyle()
                .set("border", "1px solid var(--lumo-contrast-20pct)")
                .set("border-radius", "var(--lumo-border-radius-m)")
                .set("padding", "0")
                .set("background", "white")
                .set("box-shadow", "var(--lumo-box-shadow-xs)")
                .set("overflow", "hidden")
                .set("cursor", "pointer");

        Div imageDiv = new Div();
        imageDiv.getStyle()
                .set("height", "150px")
                .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("color", "white")
                .set("font-size", "2rem")
                .set("border-radius", "8px 8px 0 0")
                .set("overflow", "hidden");

        if (room.getImage() != null && !room.getImage().isEmpty()) {
            Image localImage = new Image("/Uploads/" + room.getImage(), room.getImage());
            imageDiv.add(localImage);
            localImage.getStyle()
                    .set("width", "100%")
                    .set("height", "100%")
                    .set("object-fit", "cover")
                    .set("border-radius", "8px 8px 0 0");
        } else {
            imageDiv.add(new Icon(VaadinIcon.BUILDING));
        }

        card.add(imageDiv);

        Div content = new Div();
        content.getStyle().set("padding", "1rem");

        H3 roomName = new H3("Ruang " + toTitleCase(room.tipe) + " " + room.getName());
        roomName.getStyle()
                .set("margin", "0 0 0.5rem 0")
                .set("font-size", "1.1rem")
                .set("color", "var(--lumo-header-text-color)");

        Div details = new Div();
        details.add(createDetailItem("Kapasitas", room.capacity));
        details.add(createDetailItem("Fasilitas", room.facilities));
        details.add(createDetailItem("Lokasi", room.gedung + " " + room.location));

        Button pinjamBtn = new Button("Pinjam");
        pinjamBtn.getStyle()
                .set("background-color", "#FF6B35")
                .set("color", "white")
                .set("border", "none")
                .set("padding", "0.5rem 2rem")
                .set("border-radius", "4px")
                .set("font-size", "1rem")
                .set("width", "100%");

        pinjamBtn.addClickListener(e -> {
            UI.getCurrent().access(() -> {
                UI.getCurrent().navigate("user/pengajuan?ruanganId=" + room.getRuanganId());
            });
        });

        HorizontalLayout actionButton = new HorizontalLayout();
        actionButton.setWidthFull();
        actionButton.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        actionButton.getStyle().set("margin-top", "1rem");

        actionButton.add(pinjamBtn);

        content.add(roomName, details, actionButton);
        card.add(imageDiv, content);

        card.addClickListener(e -> {
            UI.getCurrent().access(() -> {
                Notification.show("Anda akan diarahkan ke detail ruangan.", 3000, Notification.Position.MIDDLE);
                UI.getCurrent().navigate("user/detail-ruangan?ruanganId=" + room.getRuanganId());
            });
        });

        return card;
    }

    private Div createDetailItem(String label, String value) {
        Div item = new Div();
        item.getStyle()
                .set("margin-bottom", "0.5rem")
                .set("font-size", "0.9rem");

        Span labelSpan = new Span(label + ": ");
        labelSpan.getStyle()
                .set("font-weight", "600")
                .set("color", "var(--lumo-secondary-text-color)");

        Span valueSpan = new Span(value);
        valueSpan.getStyle().set("color", "var(--lumo-body-text-color)");

        item.add(labelSpan, valueSpan);
        return item;
    }

    private void fetchRoomData(String searchQuery, String typeFilter, String gedungFilter) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(createUri(searchQuery, typeFilter, gedungFilter))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                parseRoomData(response.body());
                if (roomGrid != null) {
                    roomGrid.removeAll();
                    for (RoomData room : rooms) {
                        if (room != null) {
                            roomGrid.add(createRoomCard(room));
                        }
                    }
                }
            } else {
                Notification.show("Failed to fetch room data: " + response.statusCode() + " - " + response.body(), 3000, Notification.Position.MIDDLE);
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error connecting to server: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private void fetchDistinctGedung() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/api/v1/ruangan/gedung"))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                parseDistinctGedung(response.body());
            } else {
                Notification.show("Failed to fetch gedung data: " + response.statusCode(), 3000, Notification.Position.MIDDLE);
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error connecting to server: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private URI createUri(String searchQuery, String typeFilter, String gedungFilter) {
        String uri = "http://localhost:8081/api/v1/ruangan";
        boolean hasQuery = false;
        if (searchQuery != null && !searchQuery.isEmpty()) {
            uri += "?keyword=" + URLEncoder.encode(searchQuery, StandardCharsets.UTF_8);
            hasQuery = true;
        }
        if (typeFilter != null && !typeFilter.isEmpty() && !typeFilter.equalsIgnoreCase("Semua")) {
            String type = typeFilter.toUpperCase();
            if (type.equals("LABORATORIUM")) {
                type = "LAB";
            }
            if (hasQuery) {
                uri += "&tipe=" + URLEncoder.encode(type, StandardCharsets.UTF_8);
            } else {
                uri += "?tipe=" + URLEncoder.encode(type, StandardCharsets.UTF_8);
                hasQuery = true;
            }
        }
        if (gedungFilter != null && !gedungFilter.isEmpty() && !gedungFilter.equalsIgnoreCase("Semua")) {
            if (hasQuery) {
                uri += "&gedung=" + URLEncoder.encode(gedungFilter, StandardCharsets.UTF_8);
            } else {
                uri += "?gedung=" + URLEncoder.encode(gedungFilter, StandardCharsets.UTF_8);
            }
        }
        return URI.create(uri);
    }

    private void parseRoomData(String jsonData) {
        try {
            String[] roomObjects = jsonData.trim().split("\\},\\{");
            rooms.clear();
            if (roomObjects == null || roomObjects.length == 0 || jsonData.contains("\"data\":null")) {
                Notification.show("Tidak ada ruangan yang ditemukan", 3000, Notification.Position.MIDDLE);
                return;
            }
            for (String roomObj : roomObjects) {
                roomObj = roomObj.trim();
                if (!roomObj.startsWith("{")) {
                    roomObj = "{" + roomObj;
                }
                if (!roomObj.endsWith("}")) {
                    roomObj = roomObj + "}";
                }

                String ruanganId = extractJsonValue(roomObj, "ruanganId");
                String tipe = extractJsonValue(roomObj, "tipe");
                String nama = extractJsonValue(roomObj, "nama");
                String kapasitas = extractJsonValue(roomObj, "kapasitas");
                String fasilitas = extractJsonValue(roomObj, "fasilitas");
                String gedung = extractJsonValue(roomObj, "gedung");
                String lokasi = extractJsonValue(roomObj, "lokasi");
                String pathGambar = extractJsonValue(roomObj, "pathGambar");

                if (ruanganId != null && tipe != null && nama != null && kapasitas != null && fasilitas != null && gedung != null && lokasi != null) {
                    rooms.add(new RoomData(ruanganId, tipe, nama, kapasitas, fasilitas, gedung, lokasi, pathGambar));
                }
            }
            Notification.show("Parsed " + rooms.size() + " rooms", 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("Error parsing room data: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private void parseDistinctGedung(String jsonData) {
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(jsonData);
            org.json.JSONArray gedungArray = jsonObject.getJSONArray("data");

            if (distinctGedung != null) {
                distinctGedung.clear();
            }
            distinctGedung = new HashSet<>();
            for (int i = 0; i < gedungArray.length(); i++) {
                distinctGedung.add(gedungArray.getString(i));
            }
        } catch (Exception e) {
            Notification.show("Error parsing gedung data: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private String extractJsonValue(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":";
            int keyIndex = json.indexOf(searchKey);
            if (keyIndex == -1) {
                return null;
            }

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
            System.err.println("Error parsing JSON for key '" + key + "': " + e.getMessage());
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
}