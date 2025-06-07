package com.eroomft;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
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

@Route("user/beranda")
public class userBeranda extends HorizontalLayout {
    private List<RoomData> rooms = new ArrayList<>();
    private Div roomGridContainer;
    private SidebarComponent sidebar;

    public userBeranda() {

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
        // Membuat area konten bisa di-scroll
        mainContent.getStyle().set("overflow", "auto");
        
        mainContent.add(createHeader(), createContent());

        add(sidebar, mainContent);
    }

    private HorizontalLayout createHeader() {

        H2 title = new H2("Beranda");
        title.getStyle().set("margin-top", "1rem").set("font-size", "1.5rem").set("margin-bottom", "0.5rem");

        HorizontalLayout header = new HorizontalLayout(title);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setPadding(true);
        header.setSpacing(true);
        header.setWidthFull();
        header.getStyle()
            .set("color", "white")
            .set("padding", "1rem 2rem 0.5rem 2rem") // Reduced bottom padding
            .set("border-radius", "0 0 0.75rem 0.75rem");
        return header;
    }
    
    private VerticalLayout createContent() {
        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setPadding(true);
        contentLayout.getStyle().set("padding", "1rem 2rem"); // Reduced top padding
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

        // Memuat data awal dari API saat halaman dibuka
        fetchRoomData(LocalDate.now(), null, null, null, null);
        
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
        
        ComboBox<String> gedungFilter = new ComboBox<>("Gedung");
        fetchGedungData(gedungFilter);

        Button searchButton = new Button("Cari Ruangan");
        searchButton.setIcon(new Icon(VaadinIcon.SEARCH));
        searchButton.getStyle()
            .set("background-color", "#FF8C42")
            .set("color", "white")
            .set("border", "none")
            .set("border-radius", "0.5rem")
            .set("font-weight", "600")
            .set("cursor", "pointer");
        
        searchButton.addClickListener(e -> 
            fetchRoomData(
                tanggalFilter.getValue(),
                waktuMulaiFilter.getValue() != null ? waktuMulaiFilter.getValue().format(DateTimeFormatter.ofPattern("HH:mm")) : null,
                waktuSelesaiFilter.getValue() != null ? waktuSelesaiFilter.getValue().format(DateTimeFormatter.ofPattern("HH:mm")) : null,
                tipeFilter.getValue(),
                gedungFilter.getValue()
            )
        );
        searchButton.getStyle().set("align-self", "end");

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
            grid.add(new Span("Tidak ada ruangan yang tersedia sesuai kriteria."));
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
            .set("border", "1px solid #E0E0E0")
            .set("border-radius", "0.5rem")
            .set("padding", "0")
            .set("background", "white")
            .set("box-shadow", "var(--lumo-box-shadow-xs)")
            .set("overflow", "hidden");

        card.addClickListener(e -> {
            UI.getCurrent().navigate("user/detail-ruangan?ruanganId=" + room.getRuanganId());
        });
        card.getStyle().set("cursor", "pointer");

        Div imageDiv = new Div();
        imageDiv.getStyle()
            .set("height", "150px")
            .set("overflow", "hidden");
        
        Image roomImage = new Image(
            (room.getImage() != null && !room.getImage().isEmpty())
                ? "/uploads/" + room.getImage()
                : "https://images.unsplash.com/photo-1593084895822-bf562a22d6b3?q=80&w=2070&auto=format&fit=crop",
            "Room Image"
        );
        roomImage.getStyle()
            .set("width", "100%")
            .set("height", "100%")
            .set("object-fit", "cover");
        imageDiv.add(roomImage);

        Div content = new Div();
        content.getStyle().set("padding", "1rem");

        H3 roomName = new H3("Ruang " + room.getTipe() + " " + room.getName());
        roomName.getStyle()
            .set("margin", "0 0 0.5rem 0")
            .set("font-size", "1.1rem")
            .set("font-weight", "600");

        Div details = new Div();
        details.add(
            createDetailItem("Kapasitas", room.getCapacity()),
            createDetailItem("Fasilitas", room.getFacilities()),
            createDetailItem("Lokasi", room.getGedung() + " " + room.getLocation())
        );

        Button pinjamBtn = new Button("Pinjam");
        pinjamBtn.getStyle()
            .set("background-color", "#FF6B35")
            .set("color", "white")
            .set("border", "none")
            .set("padding", "0.5rem 1rem")
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

        Span valueSpan = new Span(value);
        valueSpan.getStyle().set("color", "var(--lumo-body-text-color)");

        item.add(labelSpan, valueSpan);
        return item;
    }
    
    private void fetchRoomData(LocalDate tanggal, String waktuMulai, String waktuSelesai, String tipe, String gedung) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String uri = buildApiUri(tanggal, waktuMulai, waktuSelesai, tipe, gedung);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .GET().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                parseAndSetRoomData(response.body());
            } else {
                Notification.show("Gagal memuat data ruangan: " + response.statusCode(), 3000, Notification.Position.MIDDLE);
                rooms.clear();
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error saat menghubungkan ke server: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            rooms.clear();
        }
        updateRoomGrid();
    }

    private void fetchGedungData(ComboBox<String> gedungComboBox) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/api/v1/ruangan/gedung"))
                .GET().build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(body -> {
                    Set<String> gedungSet = parseGedungData(body);
                    UI.getCurrent().access(() -> gedungComboBox.setItems(gedungSet));
                });
        } catch (Exception e) {
             Notification.show("Gagal memuat data gedung: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private String buildApiUri(LocalDate tanggal, String waktuMulai, String waktuSelesai, String tipe, String gedung) {
        StringBuilder uriBuilder = new StringBuilder("http://localhost:8081/api/v1/ruangan?");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (tanggal != null) uriBuilder.append("tanggal=").append(urlEncode(tanggal.format(dateFormatter))).append("&");
        if (waktuMulai != null && !waktuMulai.isEmpty()) uriBuilder.append("waktuMulai=").append(urlEncode(waktuMulai)).append("&");
        if (waktuSelesai != null && !waktuSelesai.isEmpty()) uriBuilder.append("waktuSelesai=").append(urlEncode(waktuSelesai)).append("&");
        if (tipe != null && !tipe.isEmpty()) uriBuilder.append("tipe=").append(urlEncode(tipe.toUpperCase())).append("&");
        if (gedung != null && !gedung.isEmpty()) uriBuilder.append("gedung=").append(urlEncode(gedung)).append("&");
        
        return uriBuilder.toString();
    }
    
    private void parseAndSetRoomData(String json) {
        rooms.clear();
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject roomJson = dataArray.getJSONObject(i);
                    rooms.add(new RoomData(
                        roomJson.optString("ruanganId"),
                        roomJson.optString("tipe"),
                        roomJson.optString("nama"),
                        String.valueOf(roomJson.optInt("kapasitas")),
                        roomJson.optString("fasilitas"),
                        roomJson.optString("gedung"),
                        roomJson.optString("lokasi"),
                        roomJson.optString("pathGambar")
                    ));
                }
            }
        } catch (Exception e) {
            Notification.show("Error parsing data ruangan: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private Set<String> parseGedungData(String json) {
        Set<String> gedungSet = new HashSet<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    gedungSet.add(dataArray.getString(i));
                }
            }
        } catch (Exception e) {
             Notification.show("Error parsing data gedung: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
        return gedungSet;
    }

    private String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
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