package com.eroomft;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.RouterLink;

public class Sidebar extends VerticalLayout {

    private boolean isExpanded = true;
    private VerticalLayout menuContainer;
    private Button toggleButton;

    public Sidebar(String currentView) {
        setWidth(null);
        setHeightFull();
        getStyle().set("background-color", "white")
                  .set("padding", "1rem")
                  .set("box-shadow", "2px 0 5px rgba(0,0,0,0.1)");

        // Header
        Image logo = new Image("images/logo.png", "Logo");
        logo.setWidth("60px");

        H4 title = new H4("E-ROOM");
        Span subtitle = new Span("Teknik");
        subtitle.getStyle().set("font-weight", "bold");

        VerticalLayout header = new VerticalLayout(logo, title, subtitle);
        header.setPadding(false);
        header.setSpacing(false);
        header.setAlignItems(Alignment.CENTER);
        header.setVisible(isExpanded);

        // Toggle Button
        toggleButton = new Button(new Icon(VaadinIcon.ANGLE_LEFT));
        toggleButton.getStyle()
            .set("background", "none")
            .set("border", "none")
            .set("cursor", "pointer")
            .set("margin-bottom", "1rem");
        toggleButton.addClickListener(e -> toggleSidebar(header));

        // Menu Container
        menuContainer = new VerticalLayout();
        menuContainer.setPadding(false);
        menuContainer.setSpacing(false);
        menuContainer.setWidthFull();

        // Add Menu Items
        addMenuLabel("UTAMA");
        addMenuItem("Dasbor", VaadinIcon.DASHBOARD, "dashboard", currentView.equals("dashboard"));
        addMenuItem("Manajemen Ruangan", VaadinIcon.DOOR, "manajemen", currentView.equals("manajemen"));

        addMenuLabel("PEMINJAMAN RUANGAN");
        addMenuItem("Verifikasi Peminjaman", VaadinIcon.CLIPBOARD_TEXT, "verifikasi", currentView.equals("verifikasi"));
        addMenuItem("Riwayat Peminjaman", VaadinIcon.HISTORY, "riwayat", currentView.equals("riwayat"));

        // Logout
        Div logout = createMenuItem("Keluar", VaadinIcon.SIGN_OUT, "logout", false);
        logout.getStyle().set("margin-top", "auto");
        menuContainer.add(logout);

        add(header, toggleButton, menuContainer);
    }

    private void toggleSidebar(VerticalLayout header) {
        isExpanded = !isExpanded;
        header.setVisible(isExpanded);

        Icon icon = (Icon) toggleButton.getIcon();
        icon.getElement().setAttribute("icon", isExpanded ? VaadinIcon.ANGLE_LEFT.name().toLowerCase() : VaadinIcon.ANGLE_RIGHT.name().toLowerCase());

        // Refresh menu to update layout
        menuContainer.removeAll();

        if (isExpanded) {
            addMenuLabel("UTAMA");
            addMenuItem("Dasbor", VaadinIcon.DASHBOARD, "dashboard", false);
            addMenuItem("Manajemen Ruangan", VaadinIcon.DOOR, "manajemen", false);

            addMenuLabel("PEMINJAMAN RUANGAN");
            addMenuItem("Verifikasi Peminjaman", VaadinIcon.CLIPBOARD_TEXT, "verifikasi", false);
            addMenuItem("Riwayat Peminjaman", VaadinIcon.HISTORY, "riwayat", false);

            menuContainer.add(createMenuItem("Keluar", VaadinIcon.SIGN_OUT, "logout", false));
        } else {
            menuContainer.add(
                createMenuItem(null, VaadinIcon.DASHBOARD, "dashboard", false),
                createMenuItem(null, VaadinIcon.DOOR, "manajemen", false),
                createMenuItem(null, VaadinIcon.CLIPBOARD_TEXT, "verifikasi", false),
                createMenuItem(null, VaadinIcon.HISTORY, "riwayat", false),
                createMenuItem(null, VaadinIcon.SIGN_OUT, "logout", false)
            );
        }
    }

    private void addMenuLabel(String label) {
        if (isExpanded) {
            H5 sectionLabel = new H5(label);
            sectionLabel.getStyle().set("margin", "1rem 0 0.5rem 0").set("font-size", "12px");
            menuContainer.add(sectionLabel);
        }
    }

    private void addMenuItem(String label, VaadinIcon icon, String route, boolean active) {
        menuContainer.add(createMenuItem(label, icon, route, active));
    }

    private Div createMenuItem(String label, VaadinIcon iconType, String route, boolean active) {
        Icon icon = iconType.create();
        icon.setSize("20px");

        HorizontalLayout layout = new HorizontalLayout();
        layout.setAlignItems(Alignment.CENTER);
        layout.setPadding(true);

        if (label != null && isExpanded) {
            Span text = new Span(label);
            text.getStyle().set("margin-left", "10px");
            layout.add(icon, text);
        } else {
            layout.add(icon);
        }

        Div wrapper = new Div(layout);
        wrapper.getStyle()
            .set("padding", "10px")
            .set("border-radius", "10px")
            .set("cursor", "pointer");

        if (active) {
            wrapper.getStyle().set("background-color", "#f97316");
            icon.getStyle().set("color", "white");
            layout.getChildren().forEach(c -> c.getElement().getStyle().set("color", "white"));
        }

        wrapper.addClickListener(e -> {
            if (!route.equals("logout")) {
                UI.getCurrent().navigate(route);
            } else {
                // handle logout
            }
        });

        return wrapper;
    }
}
