// package com.eroomft;

// import com.vaadin.flow.component.button.Button;
// import com.vaadin.flow.component.html.Div;
// import com.vaadin.flow.component.html.Image;
// import com.vaadin.flow.component.html.Span;
// import com.vaadin.flow.component.icon.Icon;
// import com.vaadin.flow.component.icon.VaadinIcon;
// import com.vaadin.flow.component.orderedlayout.FlexComponent;
// import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
// import com.vaadin.flow.component.orderedlayout.VerticalLayout;
// import com.vaadin.flow.theme.lumo.LumoUtility;

// public class SidebarComponent2 {

//     private void createDrawer() {
//         Image logo = new Image("https://fahutan.unmul.ac.id/laboratorium/assets/images/LOGO%20UNMUL.png", "Logo");
//         logo.setWidth("50px");

//         Span title = new Span("E-Room Teknik");
//         title.getStyle()
//             .set("font-weight", "bold")
//             .set("font-size", "1.2rem");

//         Icon caretIcon = VaadinIcon.ANGLE_LEFT.create();
//         caretIcon.getStyle()
//             .set("cursor", "pointer")
//             .set("margin-left", "auto")
//             .set("font-size", "1.2rem");

//         HorizontalLayout logoSection = new HorizontalLayout(logo, title, caretIcon);
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

//         String currentPage = "";

//         Button dashboardBtn = createStyledButton(VaadinIcon.DASHBOARD, "Dasbor", currentPage.equals("dashboard"));
//         Button manajemenRuanganBtn = createStyledButton(VaadinIcon.BUILDING, "Manajemen Ruangan", currentPage.equals("manajemen-ruangan"));

//         Span peminjamanHeader = new Span("PEMINJAMAN RUANGAN");
//         peminjamanHeader.getStyle()
//             .set("margin", "1rem 0 0.5rem 1rem")
//             .set("font-size", "0.8rem")
//             .set("font-weight", "bold")
//             .set("color", "black");

//         Button verifikasiBtn = createStyledButton(VaadinIcon.CHECK_SQUARE, "Verifikasi Peminjaman", currentPage.equals("verifikasi"));
//         Button riwayatBtn = createStyledButton(VaadinIcon.CLOCK, "Riwayat Peminjaman", currentPage.equals("riwayat"));
//         Button keluar = createMenuButton(VaadinIcon.SIGN_OUT, "Keluar");
//         keluar.getStyle()
//             .set("background-color", "#FF6666")
//             .set("color", "black")
//             .set("margin-top", "2rem")
//             .set("margin-inline", "1rem")
//             .set("border-radius", "10px")
//             .set("width", "calc(100% - 2rem)");
        
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
//     private Button createStyledButton(VaadinIcon icon, String text, boolean isActive) {
//         Button btn = createMenuButton(icon, text);
//         btn.getThemeNames().clear();

//         btn.getStyle()
//             .set("border-radius", "5px")
//             .set("margin-inline", "1rem")
//             .set("width", "calc(100% - 2rem)")
//             .set("justify-content", "start")
//             .set("color", "black")
//             .set("background-color", "transparent")
//             .set("border", "none");

//         if (isActive) {
//             btn.getStyle()
//                 .set("background-color", "#FB9A59")
//                 .set("color", "white");
//         }

//         // Hover
//         btn.getElement().addEventListener("mouseenter", e -> {
//             btn.getStyle().set("background-color", "#FB9A59");
//             btn.getStyle().set("color", "white");
//         });

//         btn.getElement().addEventListener("mouseleave", e -> {
//             if (!isActive) {
//                 btn.getStyle().set("background-color", "transparent");
//                 btn.getStyle().set("color", "black");
//             }
//         });

//         return btn;
//     }

//     private Button createMenuButton(VaadinIcon icon, String text) {
//         Button button = new Button(text, new Icon(icon));
//         button.addClassNames(
//             LumoUtility.JustifyContent.START,
//             LumoUtility.Width.FULL
//         );
//         button.getStyle()
//             .set("border-radius", "0")
//             .set("border", "none")
//             .set("background", "transparent")
//             .set("padding", "0.75rem 1rem");
//         return button;
//     }

// }


