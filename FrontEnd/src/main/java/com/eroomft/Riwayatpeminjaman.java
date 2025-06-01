package com.eroomft;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("Riwayat")
@PageTitle("Riwayat Peminjaman")
public class Riwayatpeminjaman extends VerticalLayout {

    public Riwayatpeminjaman() {
        setSpacing(true);
        setPadding(true);
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        getStyle().set("background-color", "#f6f6f6");

        H2 title = new H2("Riwayat Peminjaman");
        title.getStyle()
            .set("font-weight", "bold")
            .set("font-size", "32px")
            .set("margin-top", "30px")
            .set("margin-bottom", "40px");

        add(title);

        add(createCard("Ahmad Zuhair Nur Aiman - 2309106025", "Ruang Kelas C102", 
            "09/09/2025", "10.00–12.00", "Diizinkan", "#66e36f"));

        add(createCard("Celio Arga Rumahorbo - 2309106039", "Ruang Kelas C304", 
            "10/06/2025", "09.00–12.00", "Ditolak", "#f25567"));

        add(createCard("Celio Arga Rumahorbo - 2309106039", "Ruang Kelas C306", 
            "10/05/2025", "11.00–14.00", "Dibatalkan", "#f57c1f"));

        add(createCard("Celio Arga Rumahorbo - 2309106039", "Ruang Kelas C306", 
            "10/05/2025", "11.00–14.00", "Selesai", "#c5c5c5"));
    }

    private Component createCard(String nama, String ruangan, String tanggal, String waktu, String status, String color) {
        VerticalLayout card = new VerticalLayout();
        card.setWidth("85%");
        card.setPadding(true);
        card.setSpacing(false);
        card.getStyle()
            .set("background", "white")
            .set("border-radius", "10px")
            .set("box-shadow", "0 2px 6px rgba(0,0,0,0.1)")
            .set("padding", "15px")
            .set("margin-bottom", "10px");

        Span namaLabel = new Span(nama);
        namaLabel.getStyle()
            .set("font-weight", "bold")
            .set("font-size", "16px");

        HorizontalLayout infoLayout = new HorizontalLayout();
        infoLayout.setSpacing(true);
        infoLayout.setAlignItems(Alignment.CENTER);

        infoLayout.add(createIconWithText(VaadinIcon.BUILDING, ruangan));
        infoLayout.add(createIconWithText(VaadinIcon.CALENDAR, tanggal));
        infoLayout.add(createIconWithText(VaadinIcon.CLOCK, waktu));

        Button statusButton = new Button(status);
        statusButton.getStyle()
            .set("background-color", color)
            .set("color", "black")
            .set("font-weight", "bold")
            .set("border-radius", "8px")
            .set("height", "40px")
            .set("min-width", "105px");
        statusButton.setEnabled(false); // biar tidak bisa diklik

        HorizontalLayout bottomLayout = new HorizontalLayout();
        bottomLayout.setWidthFull();
        bottomLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        bottomLayout.setAlignItems(Alignment.CENTER);
        bottomLayout.add(infoLayout, statusButton);

        card.add(namaLabel, bottomLayout);
        return card;
    }

    private HorizontalLayout createIconWithText(VaadinIcon icon, String text) {
        Icon vaadinIcon = icon.create();
        vaadinIcon.getStyle().set("margin-right", "5px");

        Span span = new Span(text);
        HorizontalLayout layout = new HorizontalLayout(vaadinIcon, span);
        layout.setAlignItems(Alignment.CENTER);
        return layout;
    }
}
