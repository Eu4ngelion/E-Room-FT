// package com.eroomft;

// import com.vaadin.flow.component.Component;
// import com.vaadin.flow.component.UI;
// import com.vaadin.flow.component.button.Button;
// import com.vaadin.flow.component.html.*;
// import com.vaadin.flow.component.icon.Icon;
// import com.vaadin.flow.component.icon.VaadinIcon;
// import com.vaadin.flow.component.notification.Notification;
// import com.vaadin.flow.component.orderedlayout.*;
// import com.vaadin.flow.router.Route;
// import com.vaadin.flow.theme.lumo.LumoUtility;


// import java.util.List;

// import com.vaadin.flow.component.applayout.AppLayout;

// @Route("/admin/dashboard")
// public class AdminDasborView extends AppLayout {

//     public AdminDasborView() {
//         createDrawer();
//         SetContent(CreateContent());
//     }

//         // sidebar
//     private void createDrawer() {
//         String currentPage = "admin/dasbor";

//         Image logo = new Image("https://fahutan.unmul.ac.id/laboratorium/assets/images/LOGO%20UNMUL.png", "Logo");
//         logo.setWidth("50px");

//         Span title = new Span("E-Room Teknik");
//         title.getStyle()
//             .set("font-weight", "bold")
//             .set("font-size", "1.2rem");

//         HorizontalLayout logoSection = new HorizontalLayout(logo, title);
//         logoSection.setAlignItems(FlexComponent.Alignment.CENTER);
//         logoSection.setWidthFull();
//         logoSection.setSpacing(true);
//         logoSection.getStyle()
//             .set("padding", "1rem")
//             .set("border-bottom", "1px solid #e0e0e0");

//         VerticalLayout navigation = new VerticalLayout();
//         navigation.setPadding(false);
//         navigation.setSpacing(false);

//         Span utamaHeader = new Span("UTAMA");
//         utamaHeader.getStyle()
//             .set("margin", "1rem 0 0.5rem 1rem")
//             .set("font-size", "0.8rem")
//             .set("font-weight", "bold")
//             .set("color", "black");


//         Button dashboardBtn = createStyledButton(VaadinIcon.DASHBOARD, "Dasbor", currentPage.equals("admin/dashboard"), "admin/dashboard");
//         Button manajemenRuanganBtn = createStyledButton(VaadinIcon.BUILDING, "Manajemen Ruangan", currentPage.equals("admin/manajemen"), "admin/manajemen");

//         Span peminjamanHeader = new Span("PEMINJAMAN RUANGAN");
//         peminjamanHeader.getStyle()
//             .set("margin", "1rem 0 0.5rem 1rem")
//             .set("font-size", "0.8rem")
//             .set("font-weight", "bold")
//             .set("color", "black");

//         Button verifikasiBtn = createStyledButton(VaadinIcon.CHECK_SQUARE, "Verifikasi Peminjaman", currentPage.equals("verifikasi"), "admin/verifikasi");
//         Button riwayatBtn = createStyledButton(VaadinIcon.CLOCK, "Riwayat Peminjaman", currentPage.equals("riwayat"), "admin/riwayat");
//         Button keluar = createExitButton(VaadinIcon.SIGN_OUT, "Keluar");
        
//         navigation.add(
//             utamaHeader,
//             dashboardBtn,
//             manajemenRuanganBtn,
//             peminjamanHeader,
//             verifikasiBtn,
//             riwayatBtn,
//             keluar
//         );

//         addToDrawer(logoSection, navigation);
//     }
//     // button hover
//     private Button createStyledButton(VaadinIcon icon, String text, boolean isActive, String targetPage) {
//         Button btn = new Button(text, new Icon(icon));

//         btn.getStyle()
//             .set("margin-inline", "1rem") // Horizontal margin
//             .set("padding", "0.5rem") // Optional: Add padding for better spacing
//             .set("gap", "0.5rem"); // Space between icon and text

//         // Set initial styles immediately
//         if (isActive) {
//             btn.getStyle()
//                 .set("background-color", "#FF6B35")
//                 .set("width", "calc(100% - 2rem)") // Full width minus margins
//                 .set("color", "white");
//         } else {
//             btn.getStyle()
//                 .set("color", "black")
//                 .set("background-color", "transparent");
//         }

//         // Hover effects
//         btn.getElement().addEventListener("mouseenter", e -> {
//             btn.getStyle()
//                 .set("background-color", "#FB9A59")
//                 .set("width", "calc(100% - 2rem)") 
//                 .set("color", "white");
//         });

//         btn.getElement().addEventListener("mouseleave", e -> {
//             if (!isActive) {
//                 btn.getStyle()
//                     .set("background-color", "transparent")
//                     .remove("width") 
//                     .set("color", "black");
//             } else {
//                 btn.getStyle()
//                     .set("background-color", "#FF6B35")
//                     .set("color", "white");
//             };
//         });

//         // Click listener
//         btn.addClickListener(event -> {
//             // Logika untuk navigasi ke halaman yang sesuai
//             if (!isActive) {
//                 UI.getCurrent().navigate(targetPage);
//             }
//         });
//         return btn;
//     }

//     private Button createExitButton(VaadinIcon icon, String text) {
//         Button button = new Button(text, new Icon(icon));
//         button.addClassNames(
//             LumoUtility.JustifyContent.START,
//             LumoUtility.AlignItems.START,
//             LumoUtility.Width.FULL
//         );
//         button.getStyle()
//         // .set("border", "none")
//             // vertical margin
//             .set("margin-top", "100px")
//             .set("border-radius", "5px")
//             .set("margin-inline", "1rem")
//             .set("width", "calc(100% - 2rem)")
//             .set("background", "#FF6666")
//             .set("color", "white")
//             .set("padding", "0.75rem 1rem")
//             .set("justify-content", "flex-start")
//             .set("text-align", "left")
//             .set("display", "flex")
//             .set("align-items", "center");

//         button.addClickListener(event -> {
//             // Logika untuk keluar dari aplikasi
//             Notification.show("Anda telah keluar dari aplikasi.", 3000, Notification.Position.MIDDLE);
//             // Tambahkan logika untuk mengarahkan ke halaman login atau melakukan logout
//             UI.getCurrent().navigate("login");
//         });
//         return button;

//     }

//     private Component createContent() {

//         VerticalLayout content = new VerticalLayout();
//     }


//     private Component createHeaderBar() {
//         HorizontalLayout header = new HorizontalLayout();
//         header.setWidthFull();
//         header.setPadding(true);
//         header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
//         header.setAlignItems(FlexComponent.Alignment.CENTER);
//         header.getStyle()
//             .set("background-color", "white")
//             .set("border-radius", "20px")
//             .set("box-shadow", "0 2px 6px rgba(0,0,0,0.1)");

//         // Kiri: Judul
//         Span title = new Span("Dasbor E-ROOM");
//         title.getStyle()
//             .set("font-size", "24px")
//             .set("font-weight", "bold")
//             .set("color", "#FF6600");

//         // Kanan: Profile
//         Span inisial = new Span("AD");
//         inisial.getStyle()
//             .set("background-color", "#FF6600")
//             .set("color", "white")
//             .set("border-radius", "50%")
//             .set("width", "50px")
//             .set("height", "50px")
//             .set("display", "flex")
//             .set("align-items", "center")
//             .set("justify-content", "center")
//             .set("font-weight", "bold")
//             .set("font-size", "18px");

//         Div circle = new Div(inisial);
//         circle.getStyle().set("display", "flex");

//         Span nama = new Span("Administrator");
//         nama.getStyle()
//             .set("font-weight", "bold")
//             .set("color", "#FF6600")
//             .set("font-size", "14px");

//         Span email = new Span("admin12@gmail.com");
//         email.getStyle()
//             .set("font-size", "13px")
//             .set("color", "black");

//         VerticalLayout userInfo = new VerticalLayout(nama, email);
//         userInfo.setPadding(false);
//         userInfo.setSpacing(false);
//         userInfo.setAlignItems(FlexComponent.Alignment.START);

//         HorizontalLayout profile = new HorizontalLayout(circle, userInfo);
//         profile.setAlignItems(FlexComponent.Alignment.CENTER);
//         profile.setSpacing(true);

//         header.add(title, profile);

//         return header;
//     }


//     private HorizontalLayout createStatCards() {
//         HorizontalLayout stats = new HorizontalLayout();
//         stats.setWidthFull();
//         stats.setJustifyContentMode(FlexComponent.JustifyContentMode.EVENLY);
//         stats.add(
//             createStatCard("Total Ruangan", "42"),
//             createStatCard("Peminjaman Hari Ini", "8"),
//             createStatCard("Menunggu Persetujuan", "3")
            
//         );
//         return stats;
//     }

//     private Component createTableHeader() {
//         HorizontalLayout header = new HorizontalLayout();
//         header.setWidthFull();
//         header.setPadding(true);
//         header.getStyle().set("background", "#FFA769").set("border-radius", "10px");

//         header.add(
//             createHeaderCell("No", "5%"),
//             createHeaderCell("Ruangan", "20%"),
//             createHeaderCell("Jam", "20%"),
//             createHeaderCell("Tanggal", "25%"),
//             createHeaderCell("Status", "20%")
//         );
//         return header;
//     }

//     private Component createHeaderCell(String text, String width) {
//         Span span = new Span(text);
//         span.getStyle().set("font-weight", "bold");
//         Div wrapper = new Div(span);
//         wrapper.setWidth(width);
//         return wrapper;
//     }


//     private Component createRows() {
//         VerticalLayout rows = new VerticalLayout();
//         rows.setWidthFull();
//         rows.setPadding(false);
//         rows.setSpacing(true);

//         List<Peminjaman> data = List.of(
//             new Peminjaman(1, "C102", "10.00–15.00", "2025-09-09", "Menunggu"),
//             new Peminjaman(2, "D202", "08.00–10.00", "2025-07-09", "Menunggu")
//         );

//         for (Peminjaman p : data) {
//             HorizontalLayout row = new HorizontalLayout();
//             row.setWidthFull();
//             row.setPadding(true);
//             row.getStyle().set("background", "white").set("border-radius", "10px").set("box-shadow", "0 2px 4px #ccc");

//             row.add(
//                 createRowCell(String.valueOf(p.getNo()), "5%"),
//                 createRowCell(p.getRuangan(), "20%", true),
//                 createRowCell(p.getJam(), "20%", true),
//                 createRowCell(p.getTanggal(), "25%", true),
//                 createStatusCell(p.getStatus(), "20%")
//             );

//             rows.add(row);
//         }

//         return rows;
//     }

//     private Component createRowCell(String text, String width) {
//         return createRowCell(text, width, false);
//     }

//     private Component createRowCell(String text, String width, boolean bold) {
//         Span span = new Span(text);
//         if (bold) span.getStyle().set("font-weight", "bold");
//         Div wrapper = new Div(span);
//         wrapper.setWidth(width);
//         return wrapper;
//     }

//     private Component createStatusCell(String statusText, String width) {
//         Span status = new Span(statusText);
//         status.getStyle()
//             .set("background", "#F7B733")
//             .set("color", "black")
//             .set("border-radius", "20px")
//             .set("padding", "4px 12px")
//             .set("font-weight", "bold");

//         Div wrapper = new Div(status);
//         wrapper.setWidth(width);
//         return wrapper;
//     }


//     private VerticalLayout createStatCard(String title, String value) {
//         VerticalLayout card = new VerticalLayout();
//         card.setPadding(true);
//         card.setSpacing(false);
//         card.setAlignItems(Alignment.CENTER);
//         card.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
//         card.getStyle()
//             .set("background", "white")
//             .set("border-radius", "10px")
//             .set("box-shadow", "4px 6px 8px #ccc")
//             .set("width", "450px")
//             .set("height", "150px");

//         Span titleSpan = new Span(title);
//         titleSpan.getStyle().set("font-size", "14px");

//         H2 valueText = new H2(value);
//         valueText.getStyle().set("margin", "0");

//         card.add(titleSpan, valueText);
//         return card;
//     }

//     public static class Peminjaman {
//         int no;
//         String ruangan, jam, tanggal, status;
//         public Peminjaman(int no, String ruangan, String jam, String tanggal, String status) {
//             this.no = no;
//             this.ruangan = ruangan;
//             this.jam = jam;
//             this.tanggal = tanggal;
//             this.status = status;
//         }
//         public int getNo() { return no; }
//         public String getRuangan() { return ruangan; }
//         public String getJam() { return jam; }
//         public String getTanggal() { return tanggal; }
//         public String getStatus() { return status; }
//     }
// }
