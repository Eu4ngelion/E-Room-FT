package com.eroomft;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("riwayatpeminjaman")
@PageTitle("Riwayat Peminjaman")
public class Riwayatpeminjaman extends HorizontalLayout {

    public Riwayatpeminjaman() {
        setSizeFull();
        getStyle().set("background-color", "#fdebd0");

        // Sidebar
        VerticalLayout sidebar = createSidebar();
        // Main Content
        VerticalLayout content = createContent();

        add(sidebar, content);
    }

    private VerticalLayout createSidebar() {
        VerticalLayout sidebar = new VerticalLayout();
        sidebar.setWidth("70px");
        sidebar.setPadding(false);
        sidebar.setSpacing(false);
        sidebar.setAlignItems(Alignment.CENTER);
        sidebar.getStyle().set("background-color", "#ffffff");

        sidebar.add(createSidebarButton(VaadinIcon.MENU, false));
        sidebar.add(createSidebarButton(VaadinIcon.DASHBOARD, false));
        sidebar.add(createSidebarButton(VaadinIcon.CLIPBOARD_TEXT, false));
        sidebar.add(createSidebarButton(VaadinIcon.FILE_TEXT, false));
        sidebar.add(createSidebarButton(VaadinIcon.REFRESH, true)); // Active

        return sidebar;
    }

    private Button createSidebarButton(VaadinIcon icon, boolean isActive) {
        Icon ic = icon.create();
        ic.setSize("24px");
        ic.getStyle().set("color", "#000000");

        Button button = new Button(ic);
        button.getStyle()
            .set("background-color", isActive ? "#f58220" : "transparent")
            .set("border", "none")
            .set("border-radius", "8px")
            .set("padding", "12px")
            .set("cursor", "pointer");

        button.setWidth("48px");
        button.setHeight("48px");

        return button;
    }

    private VerticalLayout createContent() {
        VerticalLayout content = new VerticalLayout();
        content.setWidthFull();
        content.setPadding(true);
        content.setSpacing(true);
        content.getStyle().set("padding", "1em");

        // Header
        Div header = new Div();
        header.getStyle().set("background-color", "#ffffff").set("padding", "1.5em").set("border-radius", "10px").setWidth("1750px");

        H2 title = new H2("Riwayat Peminjaman");
        title.getStyle().set("color", "#e65c00").set("margin", "0");

        Paragraph desc = new Paragraph("Pantau seluruh riwayat peminjaman ruangan secara menyeluruh.");
        desc.getStyle().set("margin", "0");

        header.add(title, desc);
        content.add(header);

        // Cards (contoh dengan status campuran)
        content.add(createHistoryCard("MUHAMMAD AGILL FIRMANSYAH", "2309106000", "Ruang Kelas C202", "2025-09-09", "12.00–15.00", "disetujui"));
        content.add(createHistoryCard("MUHAMMAD AGILL FIRMANSYAH", "2309106000", "Ruang Kelas C202", "2025-09-09", "12.00–15.00", "disetujui"));
        content.add(createHistoryCard("MUHAMMAD AGILL FIRMANSYAH", "2309106000", "Ruang Kelas C202", "2025-09-09", "12.00–15.00", "ditolak"));
        content.add(createHistoryCard("MUHAMMAD AGILL FIRMANSYAH", "2309106000", "Ruang Kelas C202", "2025-09-09", "12.00–15.00", "ditolak"));
        content.add(createHistoryCard("MUHAMMAD AGILL FIRMANSYAH", "2309106000", "Ruang Kelas C202", "2025-09-09", "12.00–15.00", "ditolak"));

        return content;
    }

    private Component createHistoryCard(String nama, String nim, String ruang, String tanggal, String waktu, String status) {
        VerticalLayout card = new VerticalLayout();
        card.getStyle()
            .set("background-color", "#ffffff")
            .set("border", "10px solid #f58220")
            .set("border-radius", "10px")
            .set("padding", "1em")
            .set("margin", "0.5em 0");
        card.setPadding(false);
        card.setSpacing(false);

        H4 name = new H4(nama + " – " + nim);
        name.getStyle().set("margin", "0").set("font-weight", "bold");

        HorizontalLayout infoRow = new HorizontalLayout(
            createIconText(VaadinIcon.BUILDING, ruang),
            createIconText(VaadinIcon.CALENDAR, tanggal),
            createIconText(VaadinIcon.CLOCK, waktu)
        );
        infoRow.setAlignItems(Alignment.CENTER);
        infoRow.setSpacing(true);

        Button statusBtn = new Button();
        statusBtn.setText(status.equals("disetujui") ? "Setujui" : "Tolak");
        statusBtn.getStyle()
            .set("background-color", status.equals("disetujui") ? "#28a745" : "#f44336")
            .set("color", "#ffffff")
            .set("border-radius", "5px");

        HorizontalLayout bottomRow = new HorizontalLayout(infoRow, statusBtn);
        bottomRow.setWidthFull();
        bottomRow.setAlignItems(Alignment.CENTER);
        bottomRow.expand(infoRow);

        card.add(name, bottomRow);
        return card;
    }

    private Component createIconText(VaadinIcon icon, String text) {
        Icon ic = icon.create();
        ic.setSize("18px");
        Span label = new Span(text);
        HorizontalLayout wrapper = new HorizontalLayout(ic, label);
        wrapper.setAlignItems(Alignment.CENTER);
        return wrapper;
    }
}
