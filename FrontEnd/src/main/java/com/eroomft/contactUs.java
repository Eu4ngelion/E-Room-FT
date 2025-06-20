package com.eroomft;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("/contact-us")
public class contactUs extends VerticalLayout {

    public contactUs() {
        String checkSession = (String) UI.getCurrent().getSession().getAttribute("role");
        if (checkSession != null) {
            Notification.show("Log Out Terlebih Dahulu", 3000, Notification.Position.BOTTOM_END);
            UI.getCurrent().access(() -> {
                switch (checkSession.toLowerCase()) {
                    case "mahasiswa":
                    case "dosen":
                        UI.getCurrent().navigate("user/beranda");
                        break;
                    case "admin":
                        UI.getCurrent().navigate("admin/dashboard");
                        break;
                    default:
                        Notification.show("Role tidak dikenali: " + checkSession, 3000, Notification.Position.MIDDLE);
                        UI.getCurrent().navigate("");
                        break;
                }
            });
            return;
        }

        // Set layout properties to prevent scrolling
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        getStyle()
                .set("background",
                        "linear-gradient(rgba(245, 238, 232, 0.85), rgba(245, 238, 232, 0.85)), url('/frontend/background.png') no-repeat center center / cover")
                .set("flex-direction", "column")
                .set("height", "100vh") // Set height to viewport height
                .set("overflow", "hidden"); // Prevent scrolling

        Div header = createHeader();

        VerticalLayout content = new VerticalLayout();
        content.setAlignItems(Alignment.CENTER);
        content.setJustifyContentMode(JustifyContentMode.CENTER);
        content.setSpacing(false); // Remove extra spacing
        content.getStyle()
                .set("padding", "20px 10px") // Reduce padding
                .set("flex-grow", "1"); // Ensure content takes available space

        H1 judul = new H1("KONTAK");
        judul.getStyle()
                .set("color", "#FF7700")
                .set("font-weight", "600")
                .set("font-family", "'poppins', sans-serif")
                .set("font-size", "30px") // Reduce font size
                .set("text-align", "center")
                .set("margin-bottom", "20px"); // Reduce margin

        HorizontalLayout contactLayout = new HorizontalLayout();
        contactLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        contactLayout.setSpacing(false); // Remove extra spacing
        contactLayout.getStyle().set("gap", "15px"); // Reduce gap between cards

        contactLayout.add(
                contactCard(VaadinIcon.ENVELOPE_O, "Email", "info@ft.unmul.ac.id", "mailto:info@ft.unmul.ac.id",
                        "Kami siap membantu Anda melalui email!"),
                contactCard(VaadinIcon.PHONE, "Telepon", "(0541) 736834", "tel:+62541736834",
                        "Hubungi kami melalui telepon!"),
                contactCard(VaadinIcon.MAP_MARKER, "Alamat", "Jalan Sambaliung No.9",
                        "https://maps.google.com/?q=Fakultas+Teknik+Universitas+Mulawarman",
                        "Kunjungi kami di lokasi ini!"));

        content.add(judul, contactLayout);

        Div footer = createFooter();

        add(header, content, footer);
        expand(content); // Ensure content expands to fill available space
    }

    private Div contactCard(VaadinIcon icon, String title, String detail, String link, String description) {
        Div card = new Div();
        card.getStyle()
                .set("width", "250px") // Reduce card width
                .set("height", "200px") // Reduce card height
                .set("padding", "15px") // Reduce padding
                .set("box-shadow", "0 2px 6px rgba(0,0,0,0.1)") // Adjust shadow
                .set("border-radius", "10px") // Reduce border radius
                .set("background-color", "rgba(255, 255, 255, 0.9)")
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("align-items", "center")
                .set("text-align", "center");

        Div iconCircle = new Div();
        iconCircle.getStyle()
                .set("width", "60px") // Reduce icon circle size
                .set("height", "60px")
                .set("background-color", "#FCE0A8")
                .set("border-radius", "50%")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("margin-bottom", "10px"); // Reduce margin

        Icon vaadinIcon = new Icon(icon);
        vaadinIcon.setSize("30px"); // Reduce icon size
        vaadinIcon.setColor("#FF7700");
        iconCircle.add(vaadinIcon);

        Paragraph cardTitle = new Paragraph(title);
        cardTitle.getStyle()
                .set("font-family", "'poppins', sans-serif")
                .set("font-weight", "600")
                .set("font-size", "16px") // Reduce font size
                .set("color", "black")
                .set("margin", "0");

        Anchor detailLink = new Anchor(link, detail);
        detailLink.setTarget("_blank");
        detailLink.getStyle()
                .set("font-family", "'Plus Jakarta Sans', sans-serif")
                .set("font-size", "12px") // Reduce font size
                .set("font-weight", "500")
                .set("color", "#FF7700")
                .set("text-decoration", "underline")
                .set("margin", "5px 0");

        Paragraph cardDescription = new Paragraph(description);
        cardDescription.getStyle()
                .set("font-family", "'Plus Jakarta Sans', sans-serif")
                .set("font-size", "12px") // Reduce font size
                .set("color", "#666666")
                .set("margin", "5px 0 0 0"); // Reduce margin

        card.add(iconCircle, cardTitle, detailLink, cardDescription);
        return card;
    }

    private Div createHeader() {
        Div header = new Div();
        header.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.ROW);
        header.getStyle()
                .set("width", "100%")
                .set("height", "60px") // Reduce header height
                .set("background-color", "transparent")
                .set("align-items", "center")
                .set("justify-content", "space-between")
                .set("padding", "0 15px") // Reduce padding
                .set("box-sizing", "border-box");

        Div headerKiri = new Div();
        headerKiri.getStyle()
                .set("display", "flex")
                .set("flex-direction", "row")
                .set("align-items", "center")
                .set("gap", "10px");

        Image logo = new Image("/frontend/RoomQue.png", "Logo RoomQue");
        logo.getStyle()
                .set("width", "50px") // Reduce logo size
                .set("height", "50px")
                .set("cursor", "pointer");
        logo.addClickListener(e -> UI.getCurrent().navigate(""));

        Span garis = new Span("|");
        garis.getStyle()
                .set("font-size", "30px"); // Reduce separator size

        Div blokNamaWeb = new Div();
        blokNamaWeb.getStyle()
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("line-height", "1");

        Paragraph namaWeb = new Paragraph("RoomQue");
        namaWeb.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.BOLD);
        namaWeb.getStyle()
                .set("margin", "0")
                .set("font-family", "'poppins', sans-serif")
                .set("font-size", "20px") // Reduce font size
                .set("font-weight", "600");

        Paragraph subText = new Paragraph("Sistem Peminjaman Ruangan");
        subText.addClassNames(LumoUtility.FontSize.SMALL);
        subText.getStyle()
                .set("margin", "0")
                .set("font-family", "'Plus Jakarta Sans', sans-serif")
                .set("font-size", "14px") // Reduce font size
                .set("font-weight", "500");

        blokNamaWeb.add(namaWeb, subText);
        headerKiri.add(logo, garis, blokNamaWeb);

        Div headerKanan = new Div();
        headerKanan.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.ROW);
        headerKanan.getStyle().set("gap", "10px"); // Reduce gap between buttons

        headerKanan.add(
                btnNavbar("Halaman Utama", ""),
                btnNavbar("Tentang Kami", "tentang-kami"),
                btnNavbar("Kontak", "contact-us"));

        header.add(headerKiri, headerKanan);
        return header;
    }

    private Div createFooter() {
        Div footer = new Div();
        footer.setText("Copyright © 2025 RoomQue");
        footer.getStyle()
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("height", "50px") // Reduce footer height
                .set("font-family", "'Plus Jakarta Sans', sans-serif")
                .set("font-size", "12px") // Reduce font size
                .set("font-weight", "500")
                .set("background-color", "#FF7700")
                .set("color", "black")
                .set("width", "100%")
                .set("flex-shrink", "0");
        return footer;
    }

    private Button btnNavbar(String text, String route) {
        Button button = new Button(text);
        button.getStyle()
                .set("background-color", "transparent")
                .set("color", "black")
                .set("font-family", "'poppins', sans-serif")
                .set("font-size", "14px") // Reduce font size
                .set("font-weight", "500")
                .set("border", "none")
                .set("cursor", "pointer");

        button.addClickListener(e -> UI.getCurrent().navigate(route));

        button.getElement().addEventListener("mouseenter", e -> {
            button.getStyle().set("border-bottom", "2px solid #FF7700");
        });

        button.getElement().addEventListener("mouseleave", e -> {
            button.getStyle().remove("border-bottom");
        });

        return button;
    }
}