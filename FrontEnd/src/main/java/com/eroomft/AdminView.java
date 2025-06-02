package com.eroomft;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vaadin.flow.component.Component;
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
import com.vaadin.flow.component.upload.Receiver;
import java.io.File;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("admin")
public class AdminView extends AppLayout {

    public AdminView() {
        createDrawer();
        setContent(createContent());
    }

    private Image roomImage = new Image();
    private String uploadedFileName = null;
    // private List<RoomData> rooms = new ArrayList<>();
    // private VerticalLayout roomCardsLayout = new VerticalLayout();


    // sidebar
    private void createDrawer() {
        Image logo = new Image("https://fahutan.unmul.ac.id/laboratorium/assets/images/LOGO%20UNMUL.png", "Logo");
        logo.setWidth("50px");

        Span title = new Span("E-Room Teknik");
        title.getStyle()
            .set("font-weight", "bold")
            .set("font-size", "1.2rem");

        Icon caretIcon = VaadinIcon.ANGLE_LEFT.create();
        caretIcon.getStyle()
            .set("cursor", "pointer")
            .set("margin-left", "auto")
            .set("font-size", "1.2rem");

        HorizontalLayout logoSection = new HorizontalLayout(logo, title, caretIcon);
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

        String currentPage = "";

        Button dashboardBtn = createStyledButton(VaadinIcon.DASHBOARD, "Dasbor", currentPage.equals("dashboard"));
        Button manajemenRuanganBtn = createStyledButton(VaadinIcon.BUILDING, "Manajemen Ruangan", currentPage.equals("manajemen-ruangan"));

        Span peminjamanHeader = new Span("PEMINJAMAN RUANGAN");
        peminjamanHeader.getStyle()
            .set("margin", "1rem 0 0.5rem 1rem")
            .set("font-size", "0.8rem")
            .set("font-weight", "bold")
            .set("color", "black");

        Button verifikasiBtn = createStyledButton(VaadinIcon.CHECK_SQUARE, "Verifikasi Peminjaman", currentPage.equals("verifikasi"));
        Button riwayatBtn = createStyledButton(VaadinIcon.CLOCK, "Riwayat Peminjaman", currentPage.equals("riwayat"));
        Button keluar = createMenuButton(VaadinIcon.SIGN_OUT, "Keluar");
        keluar.getStyle()
            .set("background-color", "#FF6666")
            .set("color", "black")
            .set("margin-top", "2rem")
            .set("margin-inline", "1rem")
            .set("border-radius", "10px")
            .set("width", "calc(100% - 2rem)");
        
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
    private Button createStyledButton(VaadinIcon icon, String text, boolean isActive) {
        Button btn = createMenuButton(icon, text);
        btn.getThemeNames().clear();

        btn.getStyle()
            .set("border-radius", "5px")
            .set("margin-inline", "1rem")
            .set("width", "calc(100% - 2rem)")
            .set("justify-content", "start")
            .set("color", "black")
            .set("background-color", "transparent")
            .set("border", "none");

        if (isActive) {
            btn.getStyle()
                .set("background-color", "#FB9A59")
                .set("color", "white");
        }

        // Hover
        btn.getElement().addEventListener("mouseenter", e -> {
            btn.getStyle().set("background-color", "#FB9A59");
            btn.getStyle().set("color", "white");
        });

        btn.getElement().addEventListener("mouseleave", e -> {
            if (!isActive) {
                btn.getStyle().set("background-color", "transparent");
                btn.getStyle().set("color", "black");
            }
        });

        return btn;
    }

    private Button createMenuButton(VaadinIcon icon, String text) {
        Button button = new Button(text, new Icon(icon));
        button.addClassNames(
            LumoUtility.JustifyContent.START,
            LumoUtility.Width.FULL
        );
        button.getStyle()
            .set("border-radius", "0")
            .set("border", "none")
            .set("background", "transparent")
            .set("padding", "0.75rem 1rem");
        return button;
    }

    // isi konten
    private Component createContent() {

        //INI UNTUK PERULANGAN, MISAL RUANGAN BARU DITAMBAHKAN 

        // for (RoomData room : rooms) {
        //     roomCardsLayout.add(createRoomCard(room));
        // }

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

        HorizontalLayout searchSection = createSearchSection();

        Div roomGrid = createRoomGrid();

        // INI UNTUK NAMPILKAN YANG BARU DITAMBAHKAN
        // Div roomGrid = new Div(roomCardsLayout);
        // roomGrid.getStyle().set("display", "grid").set("grid-template-columns", "repeat(auto-fill, minmax(300px, 1fr))").set("gap", "1rem");

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
        typeFilter.setItems("Semua", "Kelas", "Laboratorium", "Seminar", "Serbaguna");
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
        gedungFilter.setItems("Semua", "Gedung A", "Gedung B", "Gedung C");
        gedungFilter.getStyle()
            .set("background-color", "white")
            .set("--lumo-contrast-5pct", "white")
            .set("--lumo-contrast-10pct", "white")
            .set("--lumo-primary-color", "#FF6B35")
            .set("border", "2px solid #FF6B35")
            .set("border-radius", "8px");

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
            tipeRuanganCombo.setItems("Ruang Kelas", "Ruang Seminar", "Ruang Rapat", "Lab");
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
            gedungCombo.setItems("Gedung A", "Gedung B", "Gedung C", "Gedung D");
            gedungCombo.setPlaceholder("Contoh : Gedung A");
            gedungCombo.setWidth("50%");
            gedungCombo.getStyle()
                .set("--lumo-border-radius", "8px")
                .setHeight("57px");

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

            upload.setReceiver(new Receiver() {
            @Override
                public OutputStream receiveUpload(String filename, String mimeType) {
                    try {
                        File uploadDir = new File("uploads");
                        if (!uploadDir.exists()) {
                            uploadDir.mkdir();
                        }
                        File file = new File(uploadDir, filename);
                        uploadedFileName = filename;
                        return new FileOutputStream(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
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
                }

                // Validasi Fasilitas
                if (fasilitasField.getValue() == null || fasilitasField.getValue().trim().isEmpty()) {
                    Notification.show("Fasilitas tidak boleh kosong!", 3000, Notification.Position.MIDDLE);
                    return;
                }

                // Validasi Gedung
                if (gedungCombo.getValue() == null || gedungCombo.getValue().trim().isEmpty()) {
                    Notification.show("Pilih gedung terlebih dahulu!", 3000, Notification.Position.MIDDLE);
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

                Notification.show("Ruangan berhasil ditambahkan!", 3000, Notification.Position.MIDDLE);
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
            searchField, typeFilter, gedungFilter, addRoomBtn
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
            .set("grid-template-columns", "repeat(auto-fit, minmax(250px, 1fr))")
            .set("gap", "1.5rem")
            .set("margin-top", "0rem")
            .set("padding", "0.5rem")
            .set("box-sizing", "border-box")
            .set("margin-bottom", "1rem")
            .set("width", "100%");


        // Sample room data, untuk coba tampilan aja, ntar backend yang lanjutkan
        List<RoomData> rooms = new ArrayList<>(Arrays.asList(
            new RoomData("Ruang Kelas", "C202", "30", "AC, Proyektor, Meja, Kursi", "Gedung C", "Lantai 2", "kelas_c202.jpg"),
            new RoomData("Ruang Seminar", " D202", "50", "AC, Proyektor, Meja, Kursi", "Gedung D", "Lantai 2", "seminar_d202.jpg"),
            new RoomData("Ruang Rapat", "C102", "15", "AC, Proyektor, Meja, Kursi", "Gedung C", "Lantai 1", "rapat_c102.jpg"),
            new RoomData("Lab", "Multimedia D303", "25", "AC, Proyektor, PC", "Gedung D", "Lantai 3", "lab_d303.jpg"),
            new RoomData("Ruang Serbaguna", "C103", "50", "AC, Proyektor, Mic, Meja, Kursi", "Gedung C", "Lantai 1", "serbaguna_c103.jpg"),
            new RoomData("Ruang Kelas", "C202", "30", "AC, Proyektor, Meja, Kursi", "Gedung C", "Lantai 2", "kelas_c202.jpg"),
            new RoomData("Ruang Seminar", " D202", "50", "AC, Proyektor, Meja, Kursi", "Gedung D", "Lantai 2", "seminar_d202.jpg"),
            new RoomData("Ruang Kelas", "C202", "30", "AC, Proyektor, Meja, Kursi", "Gedung C", "Lantai 2", "kelas_c202.jpg")
        ));

        for (RoomData room : rooms) {
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

        // Room image placeholder
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
        imageDiv.add(new Icon(VaadinIcon.BUILDING));
        
        // INI KALO MAU NGAMBIL DARI FILE UPLOADS NYA, CUMA MASIH ERROR

        // if (room.getImage() != null && !room.getImage().isEmpty()) {
        //     Image roomImage = new Image("/uploads/" + room.getImage(), room.getName());
        //     roomImage.setHeight("150px");
        //     roomImage.setWidth("100%");
        //     roomImage.getStyle().set("object-fit", "cover");
        //     imageDiv.add(roomImage);
        // } else {
        //     imageDiv.add(new Icon(VaadinIcon.BUILDING));
        // }

        // Card content
        Div content = new Div();
        content.getStyle().set("padding", "1rem");

        H3 roomName = new H3(room.tipe + " " + room.name);
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
            tipeRuanganCombo.setItems("Ruang Kelas", "Ruang Seminar", "Ruang Rapat", "Lab");
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
            gedungCombo.setItems("Gedung A", "Gedung B", "Gedung C", "Gedung D");
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
            upload.setReceiver(new Receiver() {
            @Override
                public OutputStream receiveUpload(String filename, String mimeType) {
                    try {
                        File uploadDir = new File("src/java/com/eroomft/uploads");
                        if (!uploadDir.exists()) {
                            uploadDir.mkdir();
                        }
                        File file = new File(uploadDir, filename);
                        uploadedFileName = filename;
                        return new FileOutputStream(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
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
                    Notification.show("Silakan unggah gambar ruangan!", 3000, Notification.Position.MIDDLE);
                    return;
                }

                // Simpan update ke database atau service
                Notification.show("Ruangan berhasil diperbarui!", 3000, Notification.Position.MIDDLE);
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
                Notification.show("Data berhasil dihapus.", 3000, Notification.Position.MIDDLE);
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

    // Data class for room information
    private static class RoomData {
        private final String tipe;
        private final String name;
        private final String capacity;
        private final String facilities;
        private final String gedung;
        private final String location;
        private final String image;

        public RoomData(String tipe, String name, String capacity, String facilities, String gedung, String location, String image) {
            this.tipe = tipe;
            this.name = name;
            this.capacity = capacity;
            this.facilities = facilities;
            this.gedung = gedung;
            this.location = location;
            this.image = image;
        }

        public String getTipe() { return tipe; }
        public String getName() { return name; }
        public String getCapacity() { return capacity; }
        public String getFacilities() { return facilities; }
        public String getGedung() { return gedung; }
        public String getLocation() { return location; }
        public String getImage() { return image; }
    }


}
