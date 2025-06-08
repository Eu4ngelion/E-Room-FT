package com.eroomft;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
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
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.Route;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Route("user/beranda")
@Component
@Scope("prototype")
public class UserBeranda extends HorizontalLayout {
    private List<RoomData> rooms = new ArrayList<>();
    private Div roomGridContainer;
    private SidebarComponent sidebar;
    private Set<String> distinctGedung = new HashSet<>();
    
    // Add field to hold reference to the gedung filter
    private ComboBox<String> gedungFilter;

    @Value("${api.base.url:}")
    private String apiBaseUrl;

    @Autowired
    private UploadConfig uploadConfig;

    public UserBeranda() {
        // Validate active session
        String role = (String) UI.getCurrent().getSession().getAttribute("role");
        if (role == null || (!role.equalsIgnoreCase("mahasiswa") && !role.equalsIgnoreCase("dosen"))) {
            Notification.show("Anda tidak memiliki akses ke halaman ini.", 3000, Notification.Position.MIDDLE);
            UI.getCurrent().access(() -> UI.getCurrent().navigate(""));
            return;
        }

        System.out.println("UserBeranda initialized");

        setSizeFull();
        setPadding(false);
        setSpacing(false);

        sidebar = new SidebarComponent();

        VerticalLayout mainContent = new VerticalLayout();
        mainContent.setSizeFull();
        mainContent.setPadding(false);
        mainContent.getStyle().set("background-color", "#FEE6D5");
        mainContent.getStyle().set("overflow", "auto");

        mainContent.add(createHeader(), createContent());

        add(sidebar, mainContent);
    }

    @PostConstruct
    private void init() {
        System.out.println("Injected apiBaseUrl: " + apiBaseUrl);
        fetchRoomData(LocalDate.now(), null, null, null, null);
        fetchGedungData();
    }

    private HorizontalLayout createHeader() {
        H2 title = new H2("Beranda");
        title.getStyle()
                .set("margin-top", "1rem")
                .set("font-size", "1.5rem")
                .set("margin-bottom", "0.5rem");

        HorizontalLayout header = new HorizontalLayout(title);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setPadding(true);
        header.setSpacing(true);
        header.setWidthFull();
        header.getStyle()
                .set("color", "white")
                .set("padding", "1rem 2rem 0.5rem 2rem")
                .set("border-radius", "0 0 0.75rem 0.75rem");
        return header;
    }

    private VerticalLayout createContent() {
        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setPadding(true);
        contentLayout.getStyle().set("padding", "1rem 2rem");
        contentLayout.setSpacing(true);

        roomGridContainer = new Div();
        roomGridContainer.setWidthFull();

        H3 sectionTitle = new H3("Ruangan Tersedia");
        sectionTitle.getStyle()
                .set("font-size", "1.5rem")
                .set("font-weight", "500")
                .set("color", "#555")
                .set("margin", "0 0 1rem 0")
                .set("border-bottom", "2px solid #FFD1B3")
                .set("padding-bottom", "0.5rem")
                .set("text-align", "center");

        contentLayout.add(createSearchSection(), sectionTitle, roomGridContainer);

        return contentLayout;
    }

    private HorizontalLayout createSearchSection() {
        DatePicker tanggalFilter = new DatePicker("Tanggal");
        tanggalFilter.setValue(LocalDate.now());

        TimePicker waktuMulaiFilter = new TimePicker("Jam Mulai");
        waktuMulaiFilter.setStep(Duration.ofMinutes(60));
        waktuMulaiFilter.setPlaceholder("10:00");

        TimePicker waktuSelesaiFilter = new TimePicker("Jam Selesai");
        waktuSelesaiFilter.setStep(Duration.ofMinutes(60));
        waktuSelesaiFilter.setPlaceholder("12:00");

        ComboBox<String> tipeFilter = new ComboBox<>("Tipe Ruangan");
        tipeFilter.setItems("Kelas", "Laboratorium", "Seminar", "Rapat");

        // Store reference to gedung filter as instance field
        gedungFilter = new ComboBox<>("Gedung");
        List<String> gedungList = new ArrayList<>(distinctGedung);
        gedungList.add(0, "Semua");
        gedungFilter.setItems(gedungList);

        Button searchButton = new Button("Cari Ruangan", new Icon(VaadinIcon.SEARCH));
        searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchButton.getStyle()
                .set("background-color", "#FF8C42")
                .set("color", "white")
                .set("border-radius", "0.5rem")
                .set("font-weight", "600");

        searchButton.addClickListener(e ->
                fetchRoomData(
                        tanggalFilter.getValue(),
                        waktuMulaiFilter.getValue() != null ? waktuMulaiFilter.getValue().format(DateTimeFormatter.ofPattern("HH:mm")) : null,
                        waktuSelesaiFilter.getValue() != null ? waktuSelesaiFilter.getValue().format(DateTimeFormatter.ofPattern("HH:mm")) : null,
                        tipeFilter.getValue(),
                        gedungFilter.getValue()
                )
        );

        HorizontalLayout searchLayout = new HorizontalLayout(
                tanggalFilter, waktuMulaiFilter, waktuSelesaiFilter, tipeFilter, gedungFilter, searchButton
        );
        searchLayout.setWidthFull();
        searchLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
        searchLayout.setSpacing(true);
        searchLayout.getStyle()
                .set("background-color", "white")
                .set("padding", "1.5rem")
                .set("border-radius", "0.75rem")
                .set("box-shadow", "0 4px 12px rgba(0,0,0,0.08)");

        return searchLayout;
    }

    private void updateRoomGrid() {
        roomGridContainer.removeAll();
        Div grid = new Div();
        grid.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fill, minmax(280px, 1fr))")
                .set("gap", "1.5rem");

        if (rooms.isEmpty()) {
            Span message = new Span("Tidak ada ruangan yang tersedia sesuai kriteria.");
            message.getStyle().set("color", "#555").set(" Telekom Malaysia Berhad (TM) font-size", "1rem");
            grid.add(message);
        } else {
            for (RoomData room : rooms) {
                grid.add(createRoomCard(room));
            }
        }
        roomGridContainer.add(grid);
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

        card.addClickListener(e -> UI.getCurrent().navigate("user/detail-ruangan?ruanganId=" + room.getRuanganId()));

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
            String imageUrl = "/Uploads/" + room.getImage();
            Path imagePath = Paths.get(uploadConfig.getDirectory(), room.getImage());
            System.out.println("Attempting to load image: " + imageUrl + ", file: " + imagePath);
            try {
                if (Files.exists(imagePath) && Files.isReadable(imagePath) && Files.size(imagePath) > 0) {
                    Image localImage = new Image(imageUrl, room.getName());
                    localImage.getStyle()
                            .set("width", "100%")
                            .set("height", "100%")
                            .set("object-fit", "cover")
                            .set("border-radius", "8px 8px 0 0");
                    localImage.getElement().addEventListener("error", e -> {
                        System.err.println("Client-side image load failed: " + imageUrl);
                        imageDiv.removeAll();
                        imageDiv.add(new Icon(VaadinIcon.BUILDING));
                    });
                    imageDiv.add(localImage);
                    System.out.println("Image added to card: " + imageUrl);
                } else {
                    System.err.println("Image invalid: exists=" + Files.exists(imagePath) + ", readable=" + Files.isReadable(imagePath) + ", size=" + (Files.exists(imagePath) ? Files.size(imagePath) : 0));
                    imageDiv.add(new Icon(VaadinIcon.BUILDING));
                }
            } catch (IOException e) {
                System.err.println("Error accessing image file: " + imagePath + ", " + e.getMessage());
                imageDiv.add(new Icon(VaadinIcon.BUILDING));
            }
        } else {
            System.out.println("No image specified for room: " + room.getName() + ", image=" + room.getImage());
            imageDiv.add(new Icon(VaadinIcon.BUILDING));
        }

        Div content = new Div();
        content.getStyle().set("padding", "1rem");

        H3 roomName = new H3("Ruang " + (room.getTipe() != null ? toTitleCase(room.getTipe()) : "") + " " + (room.getName() != null ? room.getName() : ""));
        roomName.getStyle()
                .set("margin", "0 0 0.5rem 0")
                .set("font-size", "1.1rem")
                .set("font-weight", "600")
                .set("color", "var(--lumo-header-text-color)");

        Div details = new Div();
        details.add(
                createDetailItem("Kapasitas", room.getCapacity()),
                createDetailItem("Fasilitas", room.getFacilities()),
                createDetailItem("Lokasi", room.getGedung() + " " + room.getLocation())
        );

        Button pinjamBtn = new Button("Pinjam");
        pinjamBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        pinjamBtn.getStyle()
                .set("background-color", "#FF6B35")
                .set("color", "white")
                .set("border-radius", "4px")
                .set("font-size", "1rem")
                .set("width", "100%")
                .set("margin-top", "1rem");

        pinjamBtn.addClickListener(e -> {
            e.getSource().findAncestor(Div.class).getStyle().remove("cursor");
            UI.getCurrent().navigate("user/pengajuan?ruanganId=" + room.getRuanganId());
        });

        content.add(roomName, details, pinjamBtn);
        card.add(imageDiv, content);

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

        Span valueSpan = new Span(value != null ? value : "-");
        valueSpan.getStyle().set("color", "var(--lumo-body-text-color)");

        item.add(labelSpan, valueSpan);
        return item;
    }

    private void fetchRoomData(LocalDate tanggal, String waktuMulai, String waktuSelesai, String tipe, String gedung) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(createUri(tanggal, waktuMulai, waktuSelesai, tipe, gedung))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Room data response status: " + response.statusCode() + ", body length: " + response.body().length());

            if (response.statusCode() == 200) {
                parseAndSetRoomData(response.body());
            } else {
                System.err.println("Failed to fetch room data, status: " + response.statusCode());
                Notification.show("Gagal memuat data ruangan: " + response.statusCode(), 3000, Notification.Position.MIDDLE);
                rooms.clear();
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid API URI: " + e.getMessage());
            Notification.show("URL API tidak valid: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            rooms.clear();
        } catch (IOException | InterruptedException e) {
            System.err.println("Error fetching room data: " + e.getMessage());
            Notification.show("Error saat menghubungkan ke server: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            rooms.clear();
        }
        updateRoomGrid();
    }

    private void fetchGedungData() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiBaseUrl + "/api/v1/ruangan/gedung"))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Gedung data response status: " + response.statusCode() + ", body length: " + response.body().length());

            if (response.statusCode() == 200) {
                parseGedungData(response.body());
            } else {
                System.err.println("Failed to fetch gedung data, status: " + response.statusCode());
                Notification.show("Gagal memuat data gedung: " + response.statusCode(), 3000, Notification.Position.MIDDLE);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid URI for gedung data: " + e.getMessage());
            Notification.show("URL API tidak valid: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        } catch (IOException | InterruptedException e) {
            System.err.println("Error fetching gedung data: " + e.getMessage());
            Notification.show("Gagal memuat data gedung: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private URI createUri(LocalDate tanggal, String waktuMulai, String waktuSelesai, String tipe, String gedung) {
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

        StringBuilder uri = new StringBuilder(normalizedBaseUrl + "/api/v1/ruangan");
        boolean hasQuery = false;

        if (tanggal != null) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            uri.append(hasQuery ? "&" : "?").append("tanggal=").append(urlEncode(tanggal.format(dateFormatter)));
            hasQuery = true;
        }
        if (waktuMulai != null && !waktuMulai.isEmpty()) {
            uri.append(hasQuery ? "&" : "?").append("waktuMulai=").append(urlEncode(waktuMulai));
            hasQuery = true;
        }
        if (waktuSelesai != null && !waktuSelesai.isEmpty()) {
            uri.append(hasQuery ? "&" : "?").append("waktuSelesai=").append(urlEncode(waktuSelesai));
            hasQuery = true;
        }
        if (tipe != null && !tipe.isEmpty()) {
            String normalizedTipe = tipe.toUpperCase();
            if (normalizedTipe.equals("LABORATORIUM")) {
                normalizedTipe = "LAB";
            }
            uri.append(hasQuery ? "&" : "?").append("tipe=").append(urlEncode(normalizedTipe));
            hasQuery = true;
        }
        if (gedung != null && !gedung.isEmpty() && !gedung.equalsIgnoreCase("Semua")) {
            uri.append(hasQuery ? "&" : "?").append("gedung=").append(urlEncode(gedung));
        }

        return URI.create(uri.toString());
    }

    private void parseAndSetRoomData(String json) {
        rooms.clear();
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                System.out.println("Parsing " + dataArray.length() + " rooms from API response");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject roomJson = dataArray.getJSONObject(i);
                    RoomData room = new RoomData(
                            roomJson.optString("ruanganId"),
                            roomJson.optString("tipe"),
                            roomJson.optString("nama"),
                            String.valueOf(roomJson.optInt("kapasitas")),
                            roomJson.optString("fasilitas"),
                            roomJson.optString("gedung"),
                            roomJson.optString("lokasi"),
                            roomJson.optString("pathGambar")
                    );
                    rooms.add(room);
                    System.out.println("Added room: " + room.getName() + ", image: " + room.getImage());
                }
            } else {
                System.out.println("No data found in API response");
                Notification.show("Tidak ada ruangan yang ditemukan", 3000, Notification.Position.MIDDLE);
            }
        } catch (Exception e) {
            System.err.println("Error parsing room data: " + e.getMessage());
            Notification.show("Error parsing data ruangan: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private void parseGedungData(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                distinctGedung.clear();
                for (int i = 0; i < dataArray.length(); i++) {
                    String gedung = dataArray.getString(i);
                    if (gedung != null && !gedung.trim().isEmpty()) {
                        distinctGedung.add(gedung);
                    }
                }
                System.out.println("Parsed " + distinctGedung.size() + " gedung from API response");
                
                // Use the stored reference instead of complex traversal
                UI.getCurrent().access(() -> {
                    if (gedungFilter != null) {
                        List<String> gedungList = new ArrayList<>(distinctGedung);
                        gedungList.add(0, "Semua");
                        gedungFilter.setItems(gedungList);
                        System.out.println("Updated gedung combo box with " + gedungList.size() + " items");
                    }
                });
            }
        } catch (Exception e) {
            System.err.println("Error parsing gedung data: " + e.getMessage());
            Notification.show("Error parsing data gedung: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private String toTitleCase(String s) {
        return (s == null || s.isEmpty()) ? s : s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    private static class RoomData {
        private final String ruanganId, tipe, name, capacity, facilities, gedung, location, image;

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