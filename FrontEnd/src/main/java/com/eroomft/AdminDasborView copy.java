// package com.eroomft;

// import com.vaadin.flow.component.Component;
// import com.vaadin.flow.component.html.*;
// import com.vaadin.flow.component.icon.Icon;
// import com.vaadin.flow.component.icon.VaadinIcon;
// import com.vaadin.flow.component.orderedlayout.*;
// import com.vaadin.flow.router.Route;

// import jakarta.annotation.security.RolesAllowed;

// import java.util.List;

// @Route("/admin")


// public class AdminDasborView extends VerticalLayout {

//     public AdminDasborView() {
//         setSizeFull();
//         setPadding(false);
//         setSpacing(false);
//         setDefaultHorizontalComponentAlignment(Alignment.START);
//         getStyle().set("background", "#FFE6CC");

//         HorizontalLayout mainLayout = new HorizontalLayout();
//         mainLayout.setSizeFull();

//         VerticalLayout sidebar = createSidebar();
//         VerticalLayout content = createContent();

//         mainLayout.add(sidebar, content);
//         add(mainLayout);
//     }

//     private VerticalLayout createSidebar() {
//         VerticalLayout sidebar = new VerticalLayout();
//         sidebar.setWidth("60px");
//         sidebar.getStyle().set("background", "#fff").set("border-right", "1px solid #ccc");
//         sidebar.setAlignItems(Alignment.CENTER);
//         sidebar.add(
//             new Icon(VaadinIcon.MENU),
//             new Icon(VaadinIcon.DASHBOARD),
//             new Icon(VaadinIcon.CLIPBOARD_TEXT),
//             new Icon(VaadinIcon.FILE_TEXT_O),
//             new Icon(VaadinIcon.ROTATE_LEFT)
//         );
//         return sidebar;
//     }

//     private VerticalLayout createContent() {
//         VerticalLayout content = new VerticalLayout();
//         content.setSizeFull();
//         content.setPadding(true);
//         content.setSpacing(true);

//         content.add(createHeaderBar(), createStatCards(), createTableHeader(), createRows());

//         return content;
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
