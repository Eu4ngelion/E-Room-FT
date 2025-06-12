package com.eroomft;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("/tentang-kami")
public class aboutUs extends VerticalLayout {

    public aboutUs() {
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
                .set("width", "50px")
                .set("height", "50px")
                .set("cursor", "pointer");
        logo.addClickListener(e -> UI.getCurrent().navigate(""));

        Span garis = new Span("|");
        garis.getStyle()
                .set("font-size", "30px");

        Div blokNamaWeb = new Div();
        blokNamaWeb.getStyle()
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("line-height", "1");

        Paragraph namaWeb = new Paragraph("RoomQue");
        namaWeb.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.FontWeight.BOLD);
        namaWeb.getStyle()
                .set("margin", "0")
                .set("font-family", "'poppins', sans-serif")
                .set("font-size", "20px")
                .set("font-weight", "600");

        Paragraph subText = new Paragraph("Sistem Peminjaman Ruangan");
        subText.addClassNames(LumoUtility.FontSize.SMALL);
        subText.getStyle()
                .set("margin", "0")
                .set("font-family", "'Plus Jakarta Sans', sans-serif")
                .set("font-size", "14px")
                .set("font-weight", "500");

        blokNamaWeb.add(namaWeb, subText);
        headerKiri.add(logo, garis, blokNamaWeb);

        Div headerKanan = new Div();
        headerKanan.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.ROW);
        headerKanan.getStyle().set("gap", "10px");

        headerKanan.add(
                btnNavbar("Halaman Utama", ""),
                btnNavbar("Tentang Kami", "tentang-kami"),
                btnNavbar("Kontak", "contact-us"));

        header.add(headerKiri, headerKanan);

        VerticalLayout content = new VerticalLayout();
        content.setAlignItems(Alignment.CENTER);
        content.setJustifyContentMode(JustifyContentMode.START);
        content.setSpacing(false); // Remove extra spacing
        content.getStyle()
                .set("padding", "20px 10px") // Reduce padding
                .set("flex-grow", "1") // Ensure content takes available space
                .set("overflow", "hidden"); // Prevent scrolling inside content

        H1 judul = new H1("TENTANG KAMI");
        judul.getStyle()
                .set("color", "#FF7700")
                .set("font-weight", "600")
                .set("font-family", " 'poppins', sans-serif")
                .set("font-size", "30px") // Reduce font size
                .set("text-align", "center");

        Paragraph deskripsi = new Paragraph(
                "Sistem Peminjaman Ruangan ini mendukung efisiensi dan transparansi pemanfaatan ruang di Fakultas Teknik Universitas Mulawarman. Pengguna dapat meminjam ruangan secara daring dengan mudah dan terorganisir untuk keperluan kuliah, laboratorium, maupun kegiatan lainnya.");
        deskripsi.getStyle()
                .set("text-align", "center")
                .set("font-family", "'Plus Jakarta Sans', sans-serif")
                .set("font-size", "14px") // Reduce font size
                .set("font-weight", "500")
                .set("max-width", "700px"); // Reduce max width

        VerticalLayout infoGrid = new VerticalLayout();
        infoGrid.setAlignItems(Alignment.CENTER);
        infoGrid.setSpacing(false); // Remove extra spacing
        infoGrid.getStyle().set("gap", "15px"); // Reduce gap between rows

        HorizontalLayout row1 = new HorizontalLayout();
        row1.setJustifyContentMode(JustifyContentMode.CENTER);
        row1.setSpacing(false); // Remove extra spacing
        row1.getStyle().set("gap", "15px"); // Reduce gap between cards
        row1.add(
                infoCard("Pencarian dan Filter Ruangan", "Pesan ruangan secara mudah tanpa harus datang langsung."),
                infoCard("Cek Ketersediaan Ruangan", "Lihat jadwal penggunaan ruangan secara real-time."));

        HorizontalLayout row2 = new HorizontalLayout();
        row2.setJustifyContentMode(JustifyContentMode.CENTER);
        row2.setSpacing(false); // Remove extra spacing
        row2.getStyle().set("gap", "15px"); // Reduce gap between cards
        row2.add(
                infoCard("Login Mahasiswa & Dosen", "Akses sistem dengan NIM/NIP yang sudah terdaftar."),
                infoCard("Riwayat Peminjaman", "Pantau semua peminjaman yang pernah dilakukan."));

        infoGrid.add(row1, row2);
        content.add(judul, deskripsi, infoGrid);

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

        add(header, content, footer);
        expand(content); // Ensure content expands to fill available space
    }

    private Button btnNavbar(String text, String route) {
        Button button = new Button(text);
        button.getStyle()
                .set("background-color", "transparent")
                .set("color", "black")
                .set("font-family", " 'poppins', sans-serif ")
                .set("font-size", "14px") // Reduce font size
                .set("font-weight", "500")
                .set("border", "none")
                .set("border-radius", "0")
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

    private Div infoCard(String title, String description) {
        Div card = new Div();
        card.getStyle()
                .set("width", "300px") // Reduce card width
                .set("min-height", "120px") // Reduce card height
                .set("padding", "15px") // Reduce padding
                .set("box-shadow", "0 2px 6px rgba(0,0,0,0.1)") // Adjust shadow
                .set("border-radius", "10px") // Reduce border radius
                .set("border", "1px solid rgba(255, 119, 0, 0.3)")
                .set("background-color", "rgba(255, 255, 255, 0.85)")
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("justify-content", "center");

        Paragraph cardTitle = new Paragraph(title);
        cardTitle.getStyle()
                .set("font-family", "'poppins', sans-serif")
                .set("font-weight", "600")
                .set("font-size", "16px") // Reduce font size
                .set("color", "#D46500")
                .set("margin", "0 0 5px 0"); // Reduce margin

        Paragraph cardDescription = new Paragraph(description);
        cardDescription.getStyle()
                .set("font-family", "'Plus Jakarta Sans', sans-serif")
                .set("font-size", "12px") // Reduce font size
                .set("font-weight", "500")
                .set("color", "black")
                .set("margin", "0")
                .set("line-height", "1.2"); // Adjust line height

        card.add(cardTitle, cardDescription);
        return card;
    }
}