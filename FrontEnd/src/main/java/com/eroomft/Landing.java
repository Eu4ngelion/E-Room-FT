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

@Route("")
public class Landing extends VerticalLayout {

        public Landing() {
                String sessionRole = (String) UI.getCurrent().getSession().getAttribute("role");
                if (sessionRole != null) {
                        Notification.show("Log Out Terlebih Dahulu", 3000, Notification.Position.BOTTOM_END);
                        UI.getCurrent().access(() -> {
                                switch (sessionRole.toLowerCase()) {
                                        case "mahasiswa", "dosen" -> UI.getCurrent().navigate("user/beranda");
                                        case "admin" -> UI.getCurrent().navigate("admin/dashboard");
                                        default -> {
                                                Notification.show("Role tidak dikenali: " + sessionRole, 3000,
                                                                Notification.Position.MIDDLE);
                                                UI.getCurrent().navigate("");
                                        }
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

                VerticalLayout content = new VerticalLayout();
                content.setAlignItems(Alignment.CENTER);
                content.setJustifyContentMode(JustifyContentMode.CENTER);
                content.setSpacing(false);
                content.getStyle()
                                .set("padding", "20px 10px") // Reduce padding
                                .set("flex-grow", "1"); // Ensure content takes available space

                H1 judul = new H1("RoomQue");
                judul.getStyle()
                                .set("color", "#FF7700")
                                .set("font-weight", "600")
                                .set("font-family", "'poppins', sans-serif")
                                .set("font-size", "30px") // Reduce font size
                                .set("text-align", "center");

                Paragraph deskripsi = new Paragraph(
                                "Website RoomQue ini adalah sistem untuk mendukung efisiensi peminjaman ruang di Gedung Teknik Baru Fakultas Teknik Universitas Mulawarman.");
                deskripsi.getStyle()
                                .set("text-align", "center")
                                .set("font-family", "'Plus Jakarta Sans', sans-serif")
                                .set("font-size", "14px") // Reduce font size
                                .set("font-weight", "500")
                                .set("max-width", "500px") // Reduce max width
                                .set("margin-bottom", "40px"); // Add margin bottom

                HorizontalLayout kotak = new HorizontalLayout();
                kotak.setJustifyContentMode(JustifyContentMode.CENTER);
                kotak.setSpacing(false);
                kotak.getStyle().set("gap", "10px"); // Reduce gap between cards

                kotak.add(
                                kotakLogin("Mahasiswa", "/frontend/mahasiswa.png", "login?role=MAHASISWA"),
                                kotakLogin("Dosen", "/frontend/dosen.png", "login?role=DOSEN"),
                                kotakLogin("Staff Akademik", "/frontend/staff.png", "login?role=ADMIN"));

                content.add(judul, deskripsi, kotak);

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

        private Div kotakLogin(String role, String iconUrl, String route) {
                Div card = new Div();
                card.getStyle()
                                .set("width", "150px") // Reduce card width
                                .set("height", "150px") // Reduce card height
                                .set("padding", "30px 10px 20px 10px") // Reduce padding
                                .set("box-shadow", "0 2px 6px rgba(0,0,0,0.1)")
                                .set("border-radius", "10px")
                                .set("background-color", "#ffffff")
                                .set("display", "flex")
                                .set("flex-direction", "column")
                                .set("align-items", "center")
                                .set("justify-content", "space-between");

                Image icon = new Image(iconUrl, role);
                icon.getStyle()
                                .set("width", "55PX") // Reduce icon size
                                .set("height", "50px");

                Paragraph roleText = new Paragraph(role);
                roleText.getStyle()
                                .set("font-family", "'Plus Jakarta Sans', sans-serif")
                                .set("font-size", "12px") // Reduce font size
                                .set("font-weight", "500")
                                .set("margin", "10px 0 0 0");

                Button btnLogin = new Button("Login", event -> getUI().ifPresent(ui -> ui.navigate(route)));
                btnLogin.getStyle()
                                .set("background-color", "#FF7700")
                                .set("color", "white")
                                .set("font-size", "12px") // Reduce font size
                                .set("font-family", "'poppins', sans-serif")
                                .set("font-weight", "500")
                                .set("border-radius", "5px");

                card.add(icon, roleText, btnLogin);
                return card;
        }
}