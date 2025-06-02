package com.eroomft;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("")
public class Landing extends VerticalLayout {

    public Landing() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        getStyle()
                .set("background", "linear-gradient(rgba(255, 255, 255, 0.9)), url('/frontend/background.png') no-repeat center center / cover")
                .set("flex-direction", "column")
                .set("min-height", "100vh");

        Div header = new Div();
        header.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.ROW);
        header.getStyle()
                .set("width", "100%")
                .set("height", "80px")
                .set("background-color", "transparent")
                .set("align-items", "center")
                .set("justify-content", "space-between")
                .set("padding", "0 20px")
                .set("box-sizing", "border-box");

        Div headerKiri = new Div();
        headerKiri.getStyle()
                .set("display", "flex")
                .set("flex-direction", "row")
                .set("align-items", "center")
                .set("gap", "10px");

        Image logo = new Image("/frontend/unmul.png", "Logo Unmul");
        logo.getStyle()
                .set("width", "55px")
                .set("height", "55px")
                .set("margin-top", "10px");
                

        Span garis = new Span("|");
        garis.getStyle()
                .set("font-size", "50px");

        Div blokNamaWeb = new Div();
        blokNamaWeb.getStyle()
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("line-height", "1")
                .set("margin-top", "10px");

        Paragraph namaWeb = new Paragraph("E-ROOM FT");
        namaWeb.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.FontWeight.BOLD);
        namaWeb.getStyle()
                .set("margin", "0")
                .set("font-family", "'poppins', sans-serif")
                .set("font-size", "30px")
                .set("font-weight", "600");

        Paragraph subText = new Paragraph("Sistem Peminjaman Ruangan");
        subText.addClassNames(LumoUtility.FontSize.SMALL);
        subText.getStyle()
                .set("margin", "0")
                .set("font-family", "'Plus Jakarta Sans', sans-serif")
                .set("font-size", "20px")
                .set("font-weight", "600");

        blokNamaWeb.add(namaWeb, subText);
        headerKiri.add(logo, garis, blokNamaWeb);

        Div headerKanan = new Div();
        headerKanan.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.ROW);
        headerKanan.getStyle().set("gap", "20px");

        headerKanan.add(btnNavbar("Halaman Utama"),
                        btnNavbar("Tentang Kami"),
                        btnNavbar("Peminjaman"),
                        btnNavbar("Kontak"));

        header.add(headerKiri, headerKanan);
        add(header);

        VerticalLayout content = new VerticalLayout();
        content.setAlignItems(Alignment.CENTER);
        content.setJustifyContentMode(JustifyContentMode.CENTER);
        content.setSizeFull();
        content.setSpacing(true);
        content.getStyle().set("padding-top", "40px");

        H1 judul = new H1("E-ROOM FT");
        judul.getStyle()
                .set("color", "#FF7700")
                .set("font-weight", "600")
                .set("font-family", " 'poppins', sans-serif")
                .set("text-shadow", "2px 2px 4px rgba(0, 0, 0, 0.3)")
                .set("font-size", "50px")
                .set("text-align", "center");

        Paragraph deskripsi = new Paragraph("Aplikasi E-ROOM ini hadir untuk mendukung efisiensi pemanfaatan ruang di Gedung Teknik Baru Fakultas Teknik Universitas Mulawarman. Klik tombol di bawah untuk mulai meminjam ruangan.");
        deskripsi.getStyle()
                .set("text-align", "center")
                .set("font-family", "'Plus Jakarta Sans', sans-serif")
                .set("font-size", "17.5px")
                .set("font-weight", "600")
                .set("max-width", "600px");

        HorizontalLayout kotak = new HorizontalLayout();
        kotak.setJustifyContentMode(JustifyContentMode.CENTER);
        kotak.setSpacing(true);

        kotak.add(
                kotakLogin("Mahasiswa", "/frontend/mahasiswa.png", "login?role=MAHASISWA"),
                kotakLogin("Dosen", "/frontend/dosen.png", "login?role=DOSEN"),
                kotakLogin("Staff Akademik", "/frontend/staff.png", "login?role=ADMIN")
        );

        content.add(judul, deskripsi, kotak);
        add(content);

        Div footer = new Div();
            footer.setText("Copyright © 2025 E–Room FT");
            footer.getStyle()
                    .set("display", "flex")
                    .set("align-items", "center")
                    .set("justify-content", "center")
                    .set("height", "80px")
                    .set("font-family", "'Plus Jakarta Sans', sans-serif")
                    .set("font-size", "16px")
                    .set("font-weight", "500")
                    .set("background-color", "#FF7700")
                    .set("color", "black")
                    .set("width", "100%")
                    .set("box-sizing", "border-box");
            add(footer);

    }

    private Button btnNavbar(String text) {
        Button button = new Button(text);
        button.getStyle()
                .set("background-color", "transparent")
                .set("color", "black")
                .set("font-family", " 'poppins', sans-serif ")
                .set("font-size", "16px")
                .set("font-weight", "500")
                .set("border", "none")
                .set("border-radius", "0")
                .set("cursor", "pointer");

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
                .set("width", "200px")
                .set("height", "220px")
                .set("padding", "20px")
                .set("box-shadow", "0 2px 8px rgba(0,0,0,0.1)")
                .set("border-radius", "10px")
                .set("background-color", "#ffffff")
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("align-items", "center")
                .set("justify-content", "space-between");
    
        Div borderLogo = new Div();
        borderLogo.getStyle()
                .set("width", "100px")
                .set("height", "100px")
                .set("background-color", "#FF8B26")
                .set("border-radius", "50%")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("overflow", "hidden");
    
        Image icon = new Image(iconUrl, role);
        icon.getStyle()
                .set("object-fit", "cover");
    
        borderLogo.add(icon);
    
        Paragraph roleText = new Paragraph(role);
        roleText.getStyle()
                .set("font-family", "'Plus Jakarta Sans', sans-serif")
                .set("font-weight", "600")
                .set("margin", "10px 0 0 0");
    
        Button btnLogin = new Button("Login", event ->
                getUI().ifPresent(ui -> ui.navigate(route))
        );
        btnLogin.getStyle()
                .set("background-color", "#FF7700")
                .set("font-family", " 'poppins', sans-serif ")
                .set("font-size", "16px")
                .set("font-weight", "500")
                .set("color", "white")
                .set("cursor", "pointer")
                .set("border-radius", "10px")
                .set("box-shadow", "0px 6px 8px rgba(0,0,0,0.3)");

        btnLogin.getElement().addEventListener("mouseenter", e -> {
            btnLogin.getStyle().set("background-color", "#cc5200");
        });
        
        btnLogin.getElement().addEventListener("mouseleave", e -> {
            btnLogin.getStyle().set("background-color", "#FF7700");
        });
    
        card.add(borderLogo, roleText, btnLogin);
        return card;
    }    
}