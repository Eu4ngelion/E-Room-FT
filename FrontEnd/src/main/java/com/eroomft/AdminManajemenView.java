package com.eroomft;

import java.io.File;
import java.io.FileOutputStream;
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
import java.util.UUID;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;


@Route("admin/manajemen")
public class AdminManajemenView extends AppLayout {

    public AdminManajemenView() {
        // Cek Role
        String role = (String) UI.getCurrent().getSession().getAttribute("role");
        if (role == null || !role.equalsIgnoreCase("admin")) {
            Notification.show("Anda tidak memiliki akses ke halaman ini.", 3000, Notification.Position.MIDDLE);
            UI.getCurrent().navigate("");
            return;
        }
        createDrawer();
        setContent(createContent());
    }

    private Image roomImage = new Image();
    private String uploadedFileName = null;

    // sidebar
    private void createDrawer() {
        String currentPage = "admin/manajemen";

        Image logo = new Image("/frontend/RoomQue.png", "Logo RoomQue");
        logo.setWidth("50px");
        
        Span title = new Span("RoomQue Admin");
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


    // Variabel Global room
    List<RoomData> rooms = new ArrayList<>();
    Div roomGrid;
    Set<String> distinctGedung;
    // variabel global menyimpan distinct gedung

    // Isi Konten
    private Component createContent() {

        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);
        content.setWidthFull();
        content.getStyle()
            .set("min-height", "100vh")
            .set("background-color", "#FEE6D5");

        // judul
        Div headerBox = new Div();
        headerBox.getStyle()
            .set("background-color", "white")
            .set("padding", "1.5rem")
            .set("box-sizing", "border-box")
            .set("border-radius", "var(--lumo-border-radius-m)")
            .set("box-shadow", "var(--lumo-box-shadow-xs)")
            .set("width", "100%");


        H2 title = new H2("Manajemen Ruangan");
        title.getStyle()
            .set("margin", "0 0 0.5rem 0")
            .set("color", "#FF6B35")
            .set("border-bottom", "3px solid #FF6B35")
            .set("padding-bottom", "0.5rem")
            .set("display", "inline-block");
        Paragraph subtitle = new Paragraph("Kelola dan pantau semua ruangan yang tersedia.");
        subtitle.getStyle().set("margin", "0");

        headerBox.add(title, subtitle);

        // Fetch data ruangan
        fetchRoomData(null, null, null);

        // Fetch Distinct Gedung
        fetchDistinctGedung();

        // Search Section
        HorizontalLayout searchSection = createSearchSection();

        // Membuat grid untuk menampilkan ruangan
        roomGrid = createRoomGrid();

        content.add(headerBox, searchSection, roomGrid);
        return content;
    }

    // search + tambah ruangan
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
        // set items pada gedungFilter dengan distinct gedung
        if (distinctGedung == null || distinctGedung.isEmpty()) {
            distinctGedung = new HashSet<>();
            for (RoomData room : rooms) {
                if (room != null && room.getGedung() != null) {
                    distinctGedung.add(room.getGedung());
                }
            }
        }
        // Convert Set to List and add "Semua" option
        List<String> gedungList = new ArrayList<>(distinctGedung);
        gedungList.add(0, "Semua"); // Add "Semua" as the first item
        gedungFilter.setItems(gedungList.toArray(String[]::new));
        gedungFilter.getStyle()
            .set("background-color", "white")
            .set("--lumo-contrast-5pct", "white")
            .set("--lumo-contrast-10pct", "white")
            .set("--lumo-primary-color", "#FF6B35")
            .set("border", "2px solid #FF6B35")
            .set("border-radius", "8px");

        // Button Cari
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

            // Logika filter berdasarkan input
            roomGrid.removeAll();
            fetchRoomData(searchText, selectedType, selectedGedung);
            for (RoomData room : rooms) {
                if (room == null) {
                    continue; // Skip null room data
                }
                roomGrid.add(createRoomCard(room));
            }
            roomGrid.setVisible(true);
        });


        Button addRoomBtn = new Button("+ Tambah Ruangan");
        addRoomBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addRoomBtn.getStyle()
            .set("background-color", "#FF6B35")
            .set("border-color", "#FF6B35")
            .set("color", "white")
            .set("height", "40px");

        // FORM TAMBAH RUANGAN
            Dialog tambahRuanganDialog = new Dialog();

            H3 dialogTitle = new H3("Tambah Ruangan Baru");
            dialogTitle.getStyle()
                .set("margin", "0")
                .set("padding", "0 0 10px 0")
                .set("border-bottom", "3px solid #FF6B35")
                .set("color", "#333")
                .set("font-weight", "600")
                .set("margin", "0 auto")
                .set("text-align", "center");

            // Layout
            VerticalLayout mainLayout = new VerticalLayout();
            mainLayout.setPadding(true);
            mainLayout.setSpacing(true);
            mainLayout.setWidth("450px");
            mainLayout.getStyle().set("background-color", "white");

            // Tipe Ruangan
            ComboBox<String> tipeRuanganCombo = new ComboBox<>("Tipe Ruangan");
            tipeRuanganCombo.setItems("Kelas", "Seminar", "Rapat", "Lab");
            tipeRuanganCombo.setPlaceholder("Pilih Tipe Ruangan");
            tipeRuanganCombo.setWidthFull();
            tipeRuanganCombo.getStyle()
                .set("--lumo-border-radius", "8px");

            // Nama Ruangan
            TextField namaRuanganField = new TextField("Nama Ruangan");
            namaRuanganField.setPlaceholder("Masukkan Nama Ruangan");
            namaRuanganField.setWidthFull();
            namaRuanganField.getStyle()
                .set("--lumo-border-radius", "8px");

            // Kapasitas
            TextField kapasitasField = new TextField("Kapasitas");
            kapasitasField.setPlaceholder("Masukkan Jumlah Kapasitas");
            kapasitasField.setPattern("[0-9]*");
            kapasitasField.setWidthFull();
            kapasitasField.getStyle()
                .set("--lumo-border-radius", "8px");

            // Fasilitas
            TextArea fasilitasField = new TextArea("Fasilitas");
            fasilitasField.setPlaceholder("Contoh : Wifi, Proyektor, AC");
            fasilitasField.setWidthFull();
            fasilitasField.setHeight("60px");
            fasilitasField.getStyle()
                .set("--lumo-border-radius", "8px");

            // Layout Gedung dan Lokasi
            HorizontalLayout gedungLokasiLayout = new HorizontalLayout();
            gedungLokasiLayout.setWidthFull();
            gedungLokasiLayout.setSpacing(true);

            // Gedung
            ComboBox<String> gedungCombo = new ComboBox<>("Gedung");
            // Set items pada gedungCombo dengan distinct gedung
            if (distinctGedung == null || distinctGedung.isEmpty()) {
                distinctGedung = new HashSet<>();
                for (RoomData room : rooms) {
                    if (room != null && room.getGedung() != null) {
                        distinctGedung.add(room.getGedung());
                    }
                }
            }
            // Convert Set to List and add "Semua" option
            List<String> listGedung = new ArrayList<>(distinctGedung);
            gedungList.add(0, "Semua"); // Add "Semua" as the first item
            gedungCombo.setItems(listGedung.toArray(String[]::new));
            gedungCombo.setPlaceholder("Contoh : Gedung A");
            gedungCombo.setWidth("50%");
            gedungCombo.getStyle()
                .set("--lumo-border-radius", "8px")
                .setHeight("57px");
            gedungCombo.setAllowCustomValue(true);
            gedungCombo.addCustomValueSetListener(event -> {
            String customGedung = event.getDetail();
            if (customGedung != null && !customGedung.trim().isEmpty()) {
                gedungCombo.setValue(customGedung);
            }
});

            // Lokasi
            TextField lokasiField = new TextField("Lokasi");
            lokasiField.setPlaceholder("Contoh : Lt. 2");
            lokasiField.setWidth("50%");
            lokasiField.getStyle()
                .set("--lumo-border-radius", "8px");

            gedungLokasiLayout.add(gedungCombo, lokasiField);

            // Upload Gambar
            Div uploadSection = new Div();
            uploadSection.setWidthFull();

            Span uploadgambar = new Span("Upload Gambar");
            uploadgambar.getStyle()
                .set("font-weight", "500")
                .set("color", "#8A8A8A")
                .set("margin-bottom", "8px")
                .set("display", "block")
                .set("font-size", "14px");

            Upload upload = new Upload();
            File uploadDir = new File("FrontEnd/src/main/resources/static/uploads");
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            upload.setReceiver((filename, mimeType) -> {
                try {
                    String uniqueId = UUID.randomUUID().toString();
                    filename = uniqueId + "_" + filename; 
                    File file = new File(uploadDir, filename);
                    uploadedFileName = filename;
                    return new FileOutputStream(file);
                } catch (IOException e) {
                    Notification.show("Error uploading file: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
                    return null;
                }
            });

            upload.setWidthFull();
            upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/jpg");
            upload.setMaxFiles(1);
            upload.setWidthFull();
            upload.getStyle()
                .set("--lumo-border-radius", "8px")
                .set("border", "2px dashed #ddd")
                .set("background-color", "#fafafa");
            
            upload.getElement().executeJs(
                "this.querySelector('[part=\"drop-label\"]').textContent = 'Choose File';"
            );

            uploadSection.add(uploadgambar, upload);

            HorizontalLayout buttonLayout = new HorizontalLayout();
            buttonLayout.setWidthFull();
            buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
            buttonLayout.setSpacing(true);
            buttonLayout.getStyle().set("margin-top", "5px");

            // Tombol Batal
            Button batalBtn = new Button("Batal");
            batalBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            batalBtn.getStyle()
                .set("color", "white")
                .set("border-color", "#FB5959")
                .set("background-color", "#FB5959")
                .set("padding", "10px 20px")
                .set("border-radius", "8px")
                .set("font-weight", "500");
            
            batalBtn.addClickListener(e -> {
                // Clear semua inputan
                tipeRuanganCombo.clear();
                namaRuanganField.clear();
                kapasitasField.clear();
                fasilitasField.clear();
                gedungCombo.clear();
                lokasiField.clear();

                upload.getElement().callJsFunction("clearFileList");
                tambahRuanganDialog.close();
            });

            // Tombol Simpan
            Button simpanBtn = new Button("Simpan");
            simpanBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            simpanBtn.getStyle()
                .set("background-color", "#FF6B35")
                .set("border-color", "#FF6B35")
                .set("color", "white")
                .set("padding", "10px 20px")
                .set("border-radius", "8px")
                .set("font-weight", "500");

            simpanBtn.addClickListener(e -> {
                // Validasi Tipe Ruangan
                if (tipeRuanganCombo.getValue() == null || tipeRuanganCombo.getValue().isEmpty()) {
                    Notification.show("Pilih tipe ruangan terlebih dahulu!", 3000, Notification.Position.MIDDLE);
                    return;
                }

                // Validasi Nama Ruangan
                if (namaRuanganField.getValue() == null || namaRuanganField.getValue().trim().isEmpty()) {
                    Notification.show("Nama ruangan tidak boleh kosong!", 3000, Notification.Position.MIDDLE);
                    return;
                }

                // Validasi Kapasitas
                if (kapasitasField.getValue() == null || kapasitasField.getValue().trim().isEmpty()) {
                    Notification.show("Kapasitas tidak boleh kosong!", 3000, Notification.Position.MIDDLE);
                    return;
                } else if (!kapasitasField.getValue().matches("\\d+")) {
                    Notification.show("Kapasitas harus berupa angka!", 3000, Notification.Position.MIDDLE);
                    return;
                }

                // Validasi Fasilitas
                if (fasilitasField.getValue() == null || fasilitasField.getValue().trim().isEmpty()) {
                    Notification.show("Fasilitas tidak boleh kosong!", 3000, Notification.Position.MIDDLE);
                    return;
                }

                // Validasi Gedung
                String selectedGedung = gedungCombo.getValue();
                if (selectedGedung == null || selectedGedung.trim().isEmpty() || selectedGedung.equalsIgnoreCase("Lainnya")) {
                    Notification.show("Pilih gedung terlebih dahulu, atau masukkan nama gedung custom!", 3000, Notification.Position.MIDDLE);
                    return;
                }

                // Validasi Lokasi
                if (lokasiField.getValue() == null || lokasiField.getValue().trim().isEmpty()) {
                    Notification.show("Lokasi tidak boleh kosong!", 3000, Notification.Position.MIDDLE);
                    return;
                }

                if (uploadedFileName == null || uploadedFileName.isEmpty()) {
                    Notification.show("Silakan unggah gambar ruangan!", 3000, Notification.Position.MIDDLE);
                    return;
                }

                // Validasi Upload Gambar
                if (uploadedFileName == null || uploadedFileName.isEmpty()) {
                    Notification.show("Silakan unggah gambar ruangan!", 3000, Notification.Position.MIDDLE);
                    return;
                }
                // INI BUAT SIMPAN DATA YANG KITA INPUTKAN, COCOK SUDAH (MASIH BERUPA LIST)

                //     String tipe = tipeRuanganCombo.getValue();
                //     String nama = namaRuanganField.getValue();
                //     String kapasitas = kapasitasField.getValue();
                //     String fasilitas = fasilitasField.getValue();
                //     String gedung = gedungCombo.getValue();
                //     String lokasi = lokasiField.getValue();
                //     String image = uploadedFileName; // dari upload yang sudah kamu simpan sebelumnya
                    
                //     // Tambah data baru ke list rooms
                //     RoomData newRoom = new RoomData(tipe, nama, kapasitas, fasilitas, gedung, lokasi, image);
                //     rooms.add(newRoom);

                // roomCardsLayout.add(createRoomCard(newRoom));

                // gunakan API POST /api/v1/ruangan untuk membuat ruangan baru
                postNewRoom(
                    tipeRuanganCombo.getValue(),
                    namaRuanganField.getValue(),
                    kapasitasField.getValue(),
                    fasilitasField.getValue(),
                    gedungCombo.getValue(),
                    lokasiField.getValue(),
                    uploadedFileName
                );
                // Setelah berhasil, bisa menampilkan notifikasi atau melakukan tindakan lain
                roomGrid.removeAll();

                fetchRoomData(null, null, null);
                // Tambahkan semua ruangan ke grid
                for (RoomData room : rooms) {
                    if (room == null) {
                        continue; // Skip null room data
                    }
                    roomGrid.add(createRoomCard(room));
                }

                // Notification.show("Ruangan berhasil ditambahkan!", 3000, Notification.Position.MIDDLE);
                tambahRuanganDialog.close();
            });

            batalBtn.setWidth("150px");
            simpanBtn.setWidth("150px");

            Div tombolContainer = new Div();
            tombolContainer.getStyle()
                .set("display", "flex")
                .set("justify-content", "center")
                .set("gap", "20px");

            tombolContainer.add(batalBtn, simpanBtn);
            buttonLayout.add(tombolContainer);

            mainLayout.setPadding(false);
            mainLayout.setSpacing(false);
            mainLayout.getStyle().set("padding", "10px");

            tipeRuanganCombo.getStyle().set("margin-bottom", "0px");
            namaRuanganField.getStyle().set("margin-bottom", "0px");
            kapasitasField.getStyle().set("margin-bottom", "0px");
            fasilitasField.getStyle().set("margin-bottom", "0px");
            gedungLokasiLayout.getStyle().set("margin-bottom", "0px");
            uploadSection.getStyle().set("margin-bottom", "0px");
            uploadgambar.getStyle().set("margin-top", "5px");
            tombolContainer.getStyle().set("margin-top", "10px").setWidth("431px");

            mainLayout.add(
                dialogTitle,
                tipeRuanganCombo,
                namaRuanganField,
                kapasitasField,
                fasilitasField,
                gedungLokasiLayout,
                uploadSection,
                buttonLayout,
                tombolContainer
            );

            tambahRuanganDialog.add(mainLayout);

            // Form kebuka pas klik tambah
            addRoomBtn.addClickListener(e -> tambahRuanganDialog.open());
    // -------------------------------------------------------------------------------------------

        HorizontalLayout searchLayout = new HorizontalLayout(
            searchField, typeFilter, gedungFilter, searchBtn, addRoomBtn
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

    // Card Ruangan
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

        // Sample room data, untuk coba tampilan aja
        // List<RoomData> rooms = new ArrayList<>(Arrays.asList(
        //     new RoomData("Ruang Kelas", "C202", "30", "AC, Proyektor, Meja, Kursi", "Gedung C", "Lantai 2", "kelas_c202.jpg"),
        //     new RoomData("Ruang Seminar", " D202", "50", "AC, Proyektor, Meja, Kursi", "Gedung D", "Lantai 2", "seminar_d202.jpg"),
        //     new RoomData("Ruang Rapat", "C102", "15", "AC, Proyektor, Meja, Kursi", "Gedung C", "Lantai 1", "rapat_c102.jpg"),
        //     new RoomData("Lab", "Multimedia D303", "25", "AC, Proyektor, PC", "Gedung D", "Lantai 3", "lab_d303.jpg"),
        //     new RoomData("Ruang Rapat", "C103", "50", "AC, Proyektor, Mic, Meja, Kursi", "Gedung C", "Lantai 1", "rapat_c103.jpg"),
        //     new RoomData("Ruang Kelas", "C202", "30", "AC, Proyektor, Meja, Kursi", "Gedung C", "Lantai 2", "kelas_c202.jpg"),
        //     new RoomData("Ruang Seminar", " D202", "50", "AC, Proyektor, Meja, Kursi", "Gedung D", "Lantai 2", "seminar_d202.jpg"),
        //     new RoomData("Ruang Kelas", "C202", "30", "AC, Proyektor, Meja, Kursi", "Gedung C", "Lantai 2", "kelas_c202.jpg")
        // ));

        // cek apakah ada data = null
        if (rooms == null || rooms.isEmpty()) {
            Notification.show("Tidak ada ruangan yang ditemukan", 3000, Notification.Position.MIDDLE);
            grid.getElement().removeAllChildren();
            return grid;
        }
        // Kosongkan grid, jangan sampai ada kartu tersisa jika room = null
        grid.getElement().removeAllChildren();
        for (RoomData room : rooms) {
            if (room == null) {
                continue;
            }
            grid.add(createRoomCard(room));
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
            .set("overflow", "hidden");

        // Room image 
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
        
        // Use the image from uploads if available
        if (room.getImage() != null && !room.getImage().isEmpty()) {

            // Corrected path for serving static resources in Vaadin
            Image localImage = new Image("/uploads/" + room.getImage(), room.getImage());
            imageDiv.add(localImage);
            localImage.getStyle()
                .set("width", "100%")
                .set("height", "100%")
                .set("object-fit", "cover")
                .set("border-radius", "8px 8px 0 0");
        } else {
            // Placeholder icon if no image is available
            imageDiv.add(new Icon(VaadinIcon.BUILDING));
        }

        // gambar
        card.add(imageDiv);

        // Card content
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

        // Buttons
        Button editBtn = new Button("Edit");
        editBtn.getStyle()
            .set("background-color", "#FFB84D")
            .set("color", "white")
            .set("border", "none")
            .set("padding", "0.5rem 2rem")
            .set("border-radius", "4px")
            .set("margin-right", "0.5rem")
            .set("font-size", "1rem");

            // Dialog Edit Ruangan
            Dialog editRuanganDialog = new Dialog();

            H3 editDialogTitle = new H3("Edit Ruangan");
            editDialogTitle.getStyle()
                .set("margin", "0")
                .set("padding", "0 0 10px 0")
                .set("border-bottom", "3px solid #FF6B35")
                .set("color", "#333")
                .set("font-weight", "600")
                .set("margin", "0 auto")
                .set("text-align", "center");

            // Layout utama
            VerticalLayout mainLayout = new VerticalLayout();
            mainLayout.setPadding(true);
            mainLayout.setSpacing(true);
            mainLayout.setWidth("450px");
            mainLayout.getStyle().set("background-color", "white");

            // Fields (sama kayak tambah form)
            ComboBox<String> tipeRuanganCombo = new ComboBox<>("Tipe Ruangan");
            tipeRuanganCombo.setItems("Kelas", "Seminar", "Rapat", "Lab");
            tipeRuanganCombo.setPlaceholder("Pilih Tipe Ruangan");
            tipeRuanganCombo.setWidthFull();
            tipeRuanganCombo.getStyle().set("--lumo-border-radius", "8px");

            TextField namaRuanganField = new TextField("Nama Ruangan");
            namaRuanganField.setPlaceholder("Masukkan Nama Ruangan");
            namaRuanganField.setWidthFull();
            namaRuanganField.getStyle().set("--lumo-border-radius", "8px");

            TextField kapasitasField = new TextField("Kapasitas");
            kapasitasField.setPlaceholder("Masukkan Jumlah Kapasitas");
            kapasitasField.setPattern("[0-9]*");
            kapasitasField.setWidthFull();
            kapasitasField.getStyle().set("--lumo-border-radius", "8px");

            TextArea fasilitasField = new TextArea("Fasilitas");
            fasilitasField.setPlaceholder("Contoh : Wifi, Proyektor, AC");
            fasilitasField.setWidthFull();
            fasilitasField.setHeight("60px");
            fasilitasField.getStyle().set("--lumo-border-radius", "8px");

            HorizontalLayout gedungLokasiLayout = new HorizontalLayout();
            gedungLokasiLayout.setWidthFull();
            gedungLokasiLayout.setSpacing(true);

            ComboBox<String> gedungCombo = new ComboBox<>("Gedung");
            // Set items pada gedungCombo dengan distinct gedung
            if (distinctGedung == null || distinctGedung.isEmpty()) {
                distinctGedung = new HashSet<>();
                for (RoomData r : rooms) {
                    if (r != null && r.getGedung() != null) {
                        distinctGedung.add(r.getGedung());
                    }
                }
            }

            gedungCombo.setAllowCustomValue(true);
            gedungCombo.addCustomValueSetListener(event -> {
                String customGedung = event.getDetail();
                if (customGedung != null && !customGedung.trim().isEmpty()) {
                    gedungCombo.setValue(customGedung);
                }
            });

            List<String> gedungList = new ArrayList<>(distinctGedung);
            gedungCombo.setItems(gedungList.toArray(String[]::new));
            gedungCombo.setPlaceholder("Contoh : Gedung A");
            gedungCombo.setWidth("50%");
            gedungCombo.getStyle()
                .set("--lumo-border-radius", "8px")
                .setHeight("57px");

            TextField lokasiField = new TextField("Lokasi");
            lokasiField.setPlaceholder("Contoh : Lt. 2");
            lokasiField.setWidth("50%");
            lokasiField.getStyle().set("--lumo-border-radius", "8px");

            gedungLokasiLayout.add(gedungCombo, lokasiField);

            Div uploadSection = new Div();
            uploadSection.setWidthFull();
            Span uploadgambar = new Span("Upload Gambar");
            uploadgambar.getStyle()
                .set("font-weight", "500")
                .set("color", "#8A8A8A")
                .set("margin-bottom", "8px")
                .set("display", "block")
                .set("font-size", "14px");

            Upload upload = new Upload();
            upload.setReceiver((filename, mimeType) -> {
                try {
                    File uploadDir = new File("FrontEnd/src/main/resources/static/uploads");
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }

                    String uniqueId = UUID.randomUUID().toString();
                    filename = uniqueId + "_" + filename; 
                    File file = new File(uploadDir, filename);
                    uploadedFileName = filename;
                    return new FileOutputStream(file);
                } catch (IOException e) {
                    Notification.show("Error uploading file: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
                    return null;
                }
            });

            upload.setWidthFull();
            upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/jpg");
            upload.setMaxFiles(1);
            upload.setWidthFull();
            upload.getStyle()
                .set("--lumo-border-radius", "8px")
                .set("border", "2px dashed #ddd")
                .set("background-color", "#fafafa");

            upload.getElement().executeJs(
                "this.querySelector('[part=\"drop-label\"]').textContent = 'Choose File';"
            );

            upload.addFileRejectedListener(event -> {
                File file = new File("FrontEnd/src/main/resources/static/uploads/" + uploadedFileName);
                if (file.exists()) {
                    file.delete();
                }
            });

            upload.addFailedListener(event -> {
                File file = new File("FrontEnd/src/main/resources/static/uploads/" + uploadedFileName);
                if (file.exists()) {
                    file.delete();
                }
            });

            upload.addSucceededListener(event -> {
                // WIP
            });


            uploadSection.add(uploadgambar, upload);

            // Add current image display
            if (room.getImage() != null && !room.getImage().isEmpty()) {
                Div currentImageDiv = new Div();
                currentImageDiv.getStyle()
                    .set("margin-bottom", "10px")
                    .set("text-align", "center");
                
                Span currentImageLabel = new Span("Current Image:");
                currentImageLabel.getStyle()
                    .set("font-weight", "500")
                    .set("color", "#8A8A8A")
                    .set("display", "block")
                    .set("margin-bottom", "5px");
                
                Image currentImage = new Image("/uploads/" + room.getImage(), "Current room image");
                currentImage.getStyle()
                    .set("max-width", "200px")
                    .set("max-height", "150px")
                    .set("border-radius", "8px")
                    .set("border", "1px solid #ddd");
                
                currentImageDiv.add(currentImageLabel, currentImage);
                uploadSection.add(currentImageLabel, currentImage);
                
                // Set the current filename so it won't be required to upload new image
                uploadedFileName = room.getImage();
            }

            // Tombol Batal dan Simpan
            HorizontalLayout buttonLayout = new HorizontalLayout();
            buttonLayout.setWidthFull();
            buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
            buttonLayout.setSpacing(true);

            Button batalBtn = new Button("Batal");
            batalBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            batalBtn.getStyle()
                .set("color", "white")
                .set("border-color", "#FB5959")
                .set("background-color", "#FB5959")
                .set("padding", "10px 20px")
                .set("border-radius", "8px")
                .set("font-weight", "500");

            batalBtn.addClickListener(e -> {
                editRuanganDialog.close();
                // hapus gambar dari upload
                // if (uploadedFileName != null && !uploadedFileName.isEmpty()) {
                //     File file = new File("FrontEnd/src/main/resources/static/uploads/" + uploadedFileName);
                //     if (file.exists()) {
                //         file.delete();
                //     }
                // }
            });

            Button simpanBtn = new Button("Simpan");
            simpanBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            simpanBtn.getStyle()
                .set("background-color", "#FF6B35")
                .set("border-color", "#FF6B35")
                .set("color", "white")
                .set("padding", "10px 20px")
                .set("border-radius", "8px")
                .set("font-weight", "500");

            simpanBtn.addClickListener(e -> {
                // Validasi & simpan data update ruangan di sini
                if (tipeRuanganCombo.getValue() == null || tipeRuanganCombo.getValue().isEmpty()) {
                    Notification.show("Pilih tipe ruangan terlebih dahulu!", 3000, Notification.Position.MIDDLE);
                    return;
                }
                if (namaRuanganField.getValue() == null || namaRuanganField.getValue().trim().isEmpty()) {
                    Notification.show("Nama ruangan tidak boleh kosong!", 3000, Notification.Position.MIDDLE);
                    return;
                }
                if (kapasitasField.getValue() == null || kapasitasField.getValue().trim().isEmpty()) {
                    Notification.show("Kapasitas tidak boleh kosong!", 3000, Notification.Position.MIDDLE);
                    return;
                }
                if (fasilitasField.getValue() == null || fasilitasField.getValue().trim().isEmpty()) {
                    Notification.show("Fasilitas tidak boleh kosong!", 3000, Notification.Position.MIDDLE);
                    return;
                }
                if (gedungCombo.getValue() == null || gedungCombo.getValue().trim().isEmpty()) {
                    Notification.show("Pilih gedung terlebih dahulu!", 3000, Notification.Position.MIDDLE);
                    return;
                }
                if (lokasiField.getValue() == null || lokasiField.getValue().trim().isEmpty()) {
                    Notification.show("Lokasi tidak boleh kosong!", 3000, Notification.Position.MIDDLE);
                    return;
                }
                if (uploadedFileName == null || uploadedFileName.isEmpty()) {
                    uploadedFileName = room.getImage(); // Use current image if no new image uploaded
                }
                if (uploadedFileName == null || uploadedFileName.isEmpty()) {
                    Notification.show("Silakan unggah gambar ruangan!", 3000, Notification.Position.MIDDLE);
                    return;
                }

                // Simpan update ke database 
                putUpdateRoom(
                    room.getRuanganId(),
                    tipeRuanganCombo.getValue().toUpperCase(),
                    namaRuanganField.getValue(),
                    kapasitasField.getValue(),
                    fasilitasField.getValue(),
                    gedungCombo.getValue(),
                    lokasiField.getValue(),
                    uploadedFileName
                );

                // refresh the page
                roomGrid.removeAll();
                fetchRoomData("", "", "");
                for (RoomData r : rooms) {
                    if (r == null) {
                        continue; // Skip null room data
                    }
                    roomGrid.add(createRoomCard(r));
                }
                editRuanganDialog.close();
            });

            batalBtn.setWidth("150px");
            simpanBtn.setWidth("150px");

            Div tombolContainer = new Div();
            tombolContainer.getStyle()
                .set("display", "flex")
                .set("justify-content", "center")
                .set("gap", "20px");

            tombolContainer.add(batalBtn, simpanBtn);
            buttonLayout.add(tombolContainer);

            mainLayout.setPadding(false);
            mainLayout.setSpacing(false);
            mainLayout.getStyle().set("padding", "10px");

            tipeRuanganCombo.getStyle().set("margin-bottom", "0px");
            namaRuanganField.getStyle().set("margin-bottom", "0px");
            kapasitasField.getStyle().set("margin-bottom", "0px");
            fasilitasField.getStyle().set("margin-bottom", "0px");
            gedungLokasiLayout.getStyle().set("margin-bottom", "0px");
            uploadSection.getStyle().set("margin-bottom", "0px");
            uploadgambar.getStyle().set("margin-top", "5px");
            tombolContainer.getStyle().set("margin-top", "10px").setWidth("431px");

            mainLayout.add(
                editDialogTitle,
                tipeRuanganCombo,
                namaRuanganField,
                kapasitasField,
                fasilitasField,
                gedungLokasiLayout,
                uploadSection,
                buttonLayout,
                tombolContainer
            );

            editRuanganDialog.add(mainLayout);

            editBtn.addClickListener(e -> {
                tipeRuanganCombo.setValue(room.tipe);
                namaRuanganField.setValue(room.name);
                kapasitasField.setValue(room.capacity);
                fasilitasField.setValue(room.facilities);
                gedungCombo.setValue(room.gedung);
                lokasiField.setValue(room.location);

                if (room.image != null && !room.image.isEmpty()) {
                    String imagePath = "/uploads/" + room.image;
                    roomImage.setSrc(imagePath);
                } else {
                    roomImage.setSrc(""); 
                }

                editRuanganDialog.open();
            });


        Button deleteBtn = new Button("Hapus");
        deleteBtn.getStyle()
            .set("background-color", "#FF6B6B")
            .set("color", "white")
            .set("border", "none")
            .set("padding", "0.5rem 2rem")
            .set("border-radius", "4px")
            .set("font-size", "1rem");

            Dialog confirmDialog = new Dialog();
            confirmDialog.setCloseOnOutsideClick(false);
            confirmDialog.setDraggable(false);
            confirmDialog.setResizable(false);
            confirmDialog.getElement().getStyle()
                .set("background", "transparent")
                .set("box-shadow", "none")
                .set("padding", "0px")
                .set("background-color", "transparent")
                .set("margin", "0px");
            
            confirmDialog.setModal(true);
            confirmDialog.getElement().executeJs(
                "this.$.overlay.$.overlay.style.backgroundColor = '#FEF3E2'"
            );

            Div trashicon = new Div();
            trashicon.getElement().setProperty("title", "Hapus");
            trashicon.setWidth("70px");
            trashicon.setHeight("70px");

            trashicon.add(VaadinIcon.TRASH.create());

            trashicon.getStyle()
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("color", "#FF6700");

            Div confirmText = new Div();
            confirmText.setText("Apakah Kamu Yakin Ingin\nMenghapus Ruangan ini?");
            confirmText.getStyle()
                .set("font-weight", "bold")
                .set("font-size", "18px")
                .set("text-align", "center")
                .set("background-color", "transparent");


            // Tombol Hapus
            Button hapusBtn = new Button("Hapus", e -> {
                // Hapus data ruangan dari database
                deleteRoomById(Integer.parseInt(room.getRuanganId()));

                // Hapus gambar dari uploads
                if (room.getImage() != null && !room.getImage().isEmpty()) {
                    File file = new File("FrontEnd/src/main/resources/static/uploads/" + room.getImage());
                    if (file.exists()) {
                        file.delete();
                    }
                }

                // fetch ulang data grid
                roomGrid.removeAll();
                fetchRoomData("", "", "");
                for (RoomData r : rooms) {
                    if (r == null) {
                        continue; // Skip null 
                    }
                    roomGrid.add(createRoomCard(r));
                }
                confirmDialog.close();
            });
            hapusBtn.getStyle()
                .set("background-color", "#FF6B6B")
                .set("color", "white")
                .set("border", "none")
                .set("padding", "0.5rem 1.5rem")
                .set("border-radius", "8px")
                .set("font-weight", "bold");

            // Tombol Batal
            Button cancelBtn = new Button("Batal", e -> confirmDialog.close());
            cancelBtn.getStyle()
                .set("background-color", "#808080")
                .set("color", "white")
                .set("padding", "0.5rem 1.5rem")
                .set("border-radius", "8px")
                .set("font-weight", "bold");

            cancelBtn.setWidth("110px");
            hapusBtn.setWidth("110px");

            HorizontalLayout btnLayout = new HorizontalLayout(hapusBtn, cancelBtn);

            btnLayout.setJustifyContentMode(JustifyContentMode.CENTER);
            btnLayout.setSpacing(true);

            // Tombol tutup (X) kanan atas
            // Button closeBtn = new Button(new Icon(VaadinIcon.CLOSE));
            // closeBtn.addClickListener(e -> confirmDialog.close());
            // closeBtn.getElement().getStyle()
            //     .set("position", "absolute")
            //     .set("top", "8px")
            //     .set("right", "8px")
            //     .set("background", "none")
            //     .set("border", "none");

            // Icon closeIcon = (Icon) closeBtn.getIcon();
            // closeIcon.setColor("#FF6700");

            // Layout utama
            VerticalLayout boxLayout = new VerticalLayout();
            boxLayout.setAlignItems(FlexComponent.Alignment.CENTER);
            boxLayout.setPadding(true);
            boxLayout.setSpacing(true);
            boxLayout.setSizeFull();
            boxLayout.getStyle()
                .set("border-radius", "12px")
                .set("background-color", "#FEF3E2")
                .set("width", "320px")
                .set("position", "relative");

            boxLayout.add(confirmText, trashicon, btnLayout);

            confirmDialog.add(boxLayout);

            deleteBtn.addClickListener(e -> confirmDialog.open());


        HorizontalLayout actionButton = new HorizontalLayout();
        actionButton.setWidthFull();
        actionButton.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        actionButton.getStyle().set("margin-top", "1rem");

        deleteBtn.setWidth("110px");
        editBtn.setWidth("110px");

        actionButton.add(editBtn, deleteBtn);

        content.add(roomName, details, actionButton);
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

    // GET All data ruangan
    private void fetchRoomData(String searchQuery, String typeFilter, String gedungFilter) {
        try {
            // Create HTTP CLIENT
            HttpClient client = HttpClient.newHttpClient();
            
            // Create URI Request
            HttpRequest request = HttpRequest.newBuilder()
                .uri(createUri(searchQuery, typeFilter, gedungFilter))
                .GET()
                .header("Accept", "application/json")
                .build();
            
            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            // Check if the response is valid
            if (response.statusCode() == 200) {

                // Check if the response is valid
                parseRoomData(response.body());
            } else {
                // Show an error notification if the response is invalid
                Notification.show("Failed to fetch room data: " + response.statusCode(), 
                                 3000, Notification.Position.MIDDLE);
            }
        } catch (IOException | InterruptedException e) {
            // Show an error notification if there is an error connecting to the server
            Notification.show("Error connecting to server: " + e.getMessage(), 
                     3000, Notification.Position.MIDDLE);
        }
    }
    
    // GET Distinct Gedung
    private void fetchDistinctGedung() {
        try {
            // Create HTTP CLIENT
            HttpClient client = HttpClient.newHttpClient();
            
            // Create URI Request
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/api/v1/ruangan/gedung"))
                .GET()
                .header("Accept", "application/json")
                .build();
            
            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            // Check if the response is valid
            if (response.statusCode() == 200) {
                // Parse the JSON response to get distinct gedung
                String jsonData = response.body();
                parseDistinctGedung(jsonData);
            } else {
                // Show an error notification if the response is invalid
                Notification.show("Failed to fetch gedung data: " + response.statusCode(), 
                                 3000, Notification.Position.MIDDLE);
            }
        } catch (IOException | InterruptedException e) {
            // Show an error notification if there is an error connecting to the server
            Notification.show("Error connecting to server: " + e.getMessage(), 
                     3000, Notification.Position.MIDDLE);
        }
    }

    private URI createUri(String searchQuery, String typeFilter, String gedungFilter) {
        
        String uri = "http://localhost:8081/api/v1/ruangan";
        boolean hasQuery = false;
        if (searchQuery != null && !searchQuery.isEmpty()) {
            uri += "?keyword=" + URLEncoder.encode(searchQuery, StandardCharsets.UTF_8);
            hasQuery = true;
        }
        if (typeFilter != null && !typeFilter.isEmpty()) {
            typeFilter = typeFilter.toUpperCase();
            if (typeFilter.equals("KELAS") || typeFilter.equals("SEMINAR") || 
                typeFilter.equals("RAPAT") || typeFilter.equals("LABORATORIUM")) {
                if (typeFilter.equals("LABORATORIUM")) {
                    typeFilter = "LAB";
                }
                if (hasQuery) {
                    uri += "&tipe=" + URLEncoder.encode(typeFilter, StandardCharsets.UTF_8);
                } else {
                    uri += "?tipe=" + URLEncoder.encode(typeFilter, StandardCharsets.UTF_8);
                    hasQuery = true;
                }
            } 
        }
        if (gedungFilter != null && !gedungFilter.isEmpty() && !gedungFilter.equalsIgnoreCase("semua")) {
            if (hasQuery) {
                uri += "&gedung=" + URLEncoder.encode(gedungFilter, StandardCharsets.UTF_8);
            } else {
                uri += "?gedung=" + URLEncoder.encode(gedungFilter, StandardCharsets.UTF_8);
            }
        }
        return URI.create(uri);
    }
    
    // POST Ruangan Baru
    private void postNewRoom(String tipe, String nama, String kapasitas, String fasilitas, String gedung, String lokasi, String image) {
        try {
            // Create HTTP CLIENT
            HttpClient client = HttpClient.newHttpClient();
            
            // Create JSON body
            String jsonBody = String.format(
                "{\"tipe\":\"%s\",\"nama\":\"%s\",\"kapasitas\":%s,\"fasilitas\":\"%s\",\"gedung\":\"%s\",\"lokasi\":\"%s\",\"pathGambar\":\"%s\"}",
                tipe.toUpperCase(), nama, kapasitas, fasilitas, gedung, lokasi, image
            );

            // Create URI Request
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/api/v1/ruangan"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();
            
            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            // Check if the response is valid
            if (response.statusCode() == 200) {
                Notification.show("Ruangan berhasil dibuat", 3000, Notification.Position.MIDDLE);
            } else if (response.statusCode() == 500) {
                Notification.show("Gagal Membuat Ruangan Baru: Kesalahan Server", 
                                 3000, Notification.Position.MIDDLE);
                Notification.show(jsonBody, 3000, Notification.Position.MIDDLE);
            } else {
                Notification.show("Gagal Membuat Ruangan Baru: " + response.statusCode(), 
                                 3000, Notification.Position.MIDDLE);
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error connecting to server: " + e.getMessage(), 
                     3000, Notification.Position.MIDDLE);
        }
    }

    // PUT Update Ruangan
    private void putUpdateRoom(String ruanganId, String tipe, String nama, String kapasitas, String fasilitas, String gedung, String lokasi, String image) {
        try {
            // Create HTTP CLIENT
            HttpClient client = HttpClient.newHttpClient();

            // Create JSON body
            String jsonBody = String.format(
                "{\"tipe\":\"%s\",\"nama\":\"%s\",\"kapasitas\":%s,\"fasilitas\":\"%s\",\"gedung\":\"%s\",\"lokasi\":\"%s\",\"pathGambar\":\"%s\"}",
                tipe, nama, kapasitas, fasilitas, gedung, lokasi, image
            );

            // Create URI Request
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/api/v1/ruangan/" + ruanganId))
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();

            
            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            // Check if the response is valid
            if (response.statusCode() == 200) {
                Notification.show("Ruangan berhasil diperbarui", 3000, Notification.Position.MIDDLE);
                fetchRoomData(null, null, null); // Refresh room data
            } else {
                Notification.show("Gagal memperbarui ruangan: " + response.statusCode(), 
                                 3000, Notification.Position.MIDDLE);
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error connecting to server: " + e.getMessage(), 
                     3000, Notification.Position.MIDDLE);
        }
        
    }

    // DELETE Ruangan
    private void deleteRoomById(int ruanganId) {
        try {
            // Create HTTP CLIENT
            HttpClient client = HttpClient.newHttpClient();
            
            // Create URI Request
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/api/v1/ruangan/" + ruanganId))
                .DELETE()
                .header("Accept", "application/json")
                .build();
            
            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            // Check if the response is valid
            if (response.statusCode() == 200) {
                Notification.show("Ruangan berhasil dihapus", 3000, Notification.Position.MIDDLE);
                // Refresh room data 
                roomGrid.removeAll();
                fetchRoomData(null, null, null); 
            } else {
                Notification.show("Gagal menghapus ruangan: " + response.statusCode(), 
                                 3000, Notification.Position.MIDDLE);
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error connecting to server: " + e.getMessage(), 
                     3000, Notification.Position.MIDDLE);
        }
        
    }

    // Parser
    private void parseRoomData(String jsonData) {
        try {
            // Simple JSON parsing - assuming response is a JSON array
            String[] roomObjects = jsonData.trim().split("\\},\\{");
            
            rooms.clear();
            if (roomObjects == null || roomObjects.length == 0 || roomObjects[0].contains("\"data\":null")) {
                Notification.show("Tidak ada ruangan yang ditemukan", 3000, Notification.Position.MIDDLE);
                return;
            }
            for (String roomObj : roomObjects) {
                // Clean up the object string
                roomObj = roomObj.trim();
                if (!roomObj.startsWith("{")) {
                    roomObj = "{" + roomObj;
                }
                if (!roomObj.endsWith("}")) {
                    roomObj = roomObj + "}";
                }
                
                // Extract JSON values using simple string parsing
                String ruanganId = extractJsonValue(roomObj, "ruanganId");
                String tipe = extractJsonValue(roomObj, "tipe");
                String nama = extractJsonValue(roomObj, "nama");
                String kapasitas = extractJsonValue(roomObj, "kapasitas");
                String fasilitas = extractJsonValue(roomObj, "fasilitas");
                String gedung = extractJsonValue(roomObj, "gedung");
                String lokasi = extractJsonValue(roomObj, "lokasi");
                String pathGambar = extractJsonValue(roomObj, "pathGambar");
                
                rooms.add(new RoomData(ruanganId, tipe, nama, kapasitas, fasilitas, gedung, lokasi, pathGambar));
            }
        } catch (Exception e) {
            Notification.show("Error parsing room data: " + e.getMessage(), 
                3000, Notification.Position.MIDDLE);
        }
    }
    
    private void parseDistinctGedung(String jsonData) {
        try {
            // Parse JSON response to a list of distinct gedung using org.json library
            org.json.JSONObject jsonObject = new org.json.JSONObject(jsonData);
            org.json.JSONArray gedungArray = jsonObject.getJSONArray("data");

            // Simpan ke distinct gedung
            if (distinctGedung != null) {
                distinctGedung.clear();
            } 
            distinctGedung = new HashSet<>();
            for (int i = 0; i < gedungArray.length(); i++) {
                distinctGedung.add(gedungArray.getString(i));
            }
        } catch (Exception e) {
            Notification.show("Error parsing gedung data: " + e.getMessage(),
                3000, Notification.Position.MIDDLE);
        }
    }

    
    private String extractJsonValue(String json, String key) {
        try {
            // Find the key in the JSON string
            String searchKey = "\"" + key + "\":";
            int keyIndex = json.indexOf(searchKey);
            if (keyIndex == -1) {
                return null;
            }
            
            // Find the start of the value (after the colon)
            int valueStart = keyIndex + searchKey.length();
            
            // Skip whitespace
            while (valueStart < json.length() && Character.isWhitespace(json.charAt(valueStart))) {
                valueStart++;
            }
            
            // Check if value is a string (starts with quote)
            if (valueStart < json.length() && json.charAt(valueStart) == '"') {
                valueStart++; // Skip opening quote
                int valueEnd = json.indexOf('"', valueStart);
                if (valueEnd != -1) {
                    return json.substring(valueStart, valueEnd);
                }
            } else {
                // Handle non-string values (numbers, booleans)
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
            System.err.println("Error parsing JSON untul key '" + key + "': " + e.getMessage());
            return null;
        }
    }

    // function
    private String toTitleCase(String s) { return (s == null || s.isEmpty()) ? s : s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase(); }


    // Data class for room information
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
