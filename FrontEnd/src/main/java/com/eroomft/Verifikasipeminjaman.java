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

@Route("verifikasipeminjaman")
@PageTitle("Verifikasi Peminjaman")
public class Verifikasipeminjaman extends HorizontalLayout {

    public Verifikasipeminjaman() {
        setSizeFull();
        getStyle().set("background-color", "#fdebd0");

        VerticalLayout sidebar = createSidebar();
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
        sidebar.add(createSidebarButton(VaadinIcon.FILE_TEXT, true)); // Active
        sidebar.add(createSidebarButton(VaadinIcon.REFRESH, false));

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
            .set("cursor", "pointer")
            .set("box-shadow", isActive ? "0 0 0 2px #f58220" : "none");

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

        Div header = new Div();
        header.getStyle()
            .set("background-color", "#ffffff")
            .set("padding", "1.5em")
            .set("border-radius", "10px")
            .setWidth("1750px");

        H2 title = new H2("Verifikasi Peminjaman");
        title.getStyle().set("color", "#e65c00").set("margin", "0");

        Paragraph desc = new Paragraph("Tinjau dan validasi setiap peminjaman yang diajukan.");
        desc.getStyle().set("margin", "0");

        header.add(title, desc);
        content.add(header);

        for (int i = 0; i < 5; i++) {
            content.add(createRequestCard(
                "MUHAMMAD AGILL FIRMANSYAH",
                "2309106000",
                "Ruang Kelas C202",
                "2025-09-09",
                "12.00–15.00"
            ));
        }

        return content;
    }

    private Component createRequestCard(String nama, String nim, String ruang, String tanggal, String waktu) {
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

        Button detailBtn = new Button("Detail Permohonan");
        detailBtn.getStyle()
            .set("background-color", "#f58220")
            .set("color", "#ffffff")
            .set("border-radius", "5px");

        detailBtn.getElement().addEventListener("mouseenter", e -> {
            detailBtn.getStyle().set("background-color", "#cc5200");
        });

        detailBtn.getElement().addEventListener("mouseleave", e -> {
            detailBtn.getStyle().set("background-color", "#FF7700");
        });

        HorizontalLayout bottomRow = new HorizontalLayout(infoRow, detailBtn);
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
