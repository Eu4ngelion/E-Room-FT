package com.eroomft;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinService;

public class SidebarComponent extends VerticalLayout {

    private boolean expanded = true;
    private final VerticalLayout menuWrapper;
    private final Button toggleButton;

    public SidebarComponent() {
        setPadding(false);
        setSpacing(false);
        setAlignItems(Alignment.STRETCH);
        setWidth(expanded ? "240px" : "72px");

        getStyle()
            .set("background-color", "white")
            .set("border-right", "1px solid #e5e7eb")
            .set("min-height", "100vh");

        toggleButton = new Button();
        toggleButton.getStyle()
            .set("background", "transparent")
            .set("border", "none");

        toggleButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) e -> toggleSidebar());

        menuWrapper = new VerticalLayout();
        menuWrapper.setPadding(false);
        menuWrapper.setSpacing(false);
        menuWrapper.setAlignItems(Alignment.START);
        menuWrapper.setWidthFull();

        add(menuWrapper);
        buildExpandedSidebar();
    }

    private void toggleSidebar() {
        expanded = !expanded;
        setWidth(expanded ? "240px" : "72px");
        menuWrapper.removeAll();
        if (expanded) {
            buildExpandedSidebar();
        } else {
            buildCollapsedSidebar();
        }
    }

    private void buildExpandedSidebar() {
        String currentRoute = getCurrentRoute();

        Image logo = new Image("https://fahutan.unmul.ac.id/laboratorium/assets/images/LOGO%20UNMUL.png", "Logo");
        logo.setWidth("64px");

        Span title = new Span("E-ROOM\nTeknik");
        title.getStyle()
            .set("font-weight", "bold")
            .set("white-space", "pre-line");

        toggleButton.setIcon(VaadinIcon.ANGLE_LEFT.create());

        HorizontalLayout branding = new HorizontalLayout(logo, title, toggleButton);
        branding.setAlignItems(Alignment.CENTER);
        branding.setSpacing(true);
        branding.getStyle().set("padding", "1rem");

        menuWrapper.add(branding);

        menuWrapper.add(createSectionTitle("UTAMA"));
        menuWrapper.add(createMenuItem("Dasbor", VaadinIcon.DASHBOARD, "landing", currentRoute.equals("landing")));
        menuWrapper.add(createMenuItem("Manajemen Ruangan", VaadinIcon.ENTER_ARROW, "Manajemen", currentRoute.equals("Manajemen")));

        menuWrapper.add(createSectionTitle("PEMINJAMAN RUANGAN"));
        menuWrapper.add(createMenuItem("Verifikasi Peminjaman", VaadinIcon.CLIPBOARD_TEXT, "Verifikasi", currentRoute.equals("Verifikasi")));
        menuWrapper.add(createMenuItem("Riwayat Peminjaman", VaadinIcon.CLOCK, "Riwayat", currentRoute.equals("Riwayat")));

        Div spacer = new Div();
        menuWrapper.add(spacer);
        menuWrapper.setFlexGrow(1, spacer);

        menuWrapper.add(createMenuItem("Keluar", VaadinIcon.SIGN_OUT, "login", currentRoute.equals("login")));
    }

    private void buildCollapsedSidebar() {
        String currentRoute = getCurrentRoute();

        toggleButton.setIcon(VaadinIcon.MENU.create());
        toggleButton.getStyle().set("margin", "1rem");

        menuWrapper.add(toggleButton);

        menuWrapper.add(createIconOnlyItem(VaadinIcon.DASHBOARD, currentRoute.equals("landing")));
        menuWrapper.add(createIconOnlyItem(VaadinIcon.ENTER_ARROW, currentRoute.equals("Manajemen")));
        menuWrapper.add(createIconOnlyItem(VaadinIcon.CLIPBOARD_TEXT, currentRoute.equals("Verifikasi")));
        menuWrapper.add(createIconOnlyItem(VaadinIcon.CLOCK, currentRoute.equals("Riwayat")));

        Div spacer = new Div();
        spacer.setHeight("100%");
        menuWrapper.add(spacer);
        menuWrapper.setFlexGrow(1, spacer);

        menuWrapper.add(createIconOnlyItem(VaadinIcon.SIGN_OUT, currentRoute.equals("login")));
    }

    private Component createSectionTitle(String title) {
        Span label = new Span(title);
        label.getStyle()
            .set("font-size", "12px")
            .set("font-weight", "700")
            .set("color", "#111827")
            .set("margin", "1rem 1rem 0.25rem 1rem");
        return label;
    }

    private Component createMenuItem(String label, VaadinIcon icon, String route, boolean active) {
        Icon vaadinIcon = new Icon(icon);
        vaadinIcon.setSize("20px");
        if (active) vaadinIcon.getStyle().set("color", "white");

        Span text = new Span(label);
        text.getStyle().set("margin-left", "10px");

        HorizontalLayout content = new HorizontalLayout(vaadinIcon, text);
        content.setPadding(true);
        content.setSpacing(true);
        content.setAlignItems(Alignment.CENTER);

        Class<? extends Component> targetClass = resolveRoute(route);
        if (targetClass == null) {
            return new Span("Invalid route: " + route);
        }

        RouterLink link = new RouterLink(targetClass);
        link.add(content);
        link.setHighlightCondition((l, e) -> false);
        link.getStyle()
            .set("display", "block")
            .set("width", "100%")
            .set("padding", "12px 20px")
            .set("text-decoration", "none")
            .set("color", active ? "white" : "#111827")
            .set("font-weight", "500")
            .set("font-size", "14px")
            .set("border-radius", "12px");

        if (active) {
            link.getStyle().set("background-color", "#f97316");
        }

        return link;
    }

    private Component createIconOnlyItem(VaadinIcon icon, boolean active) {
        Icon ic = new Icon(icon);
        ic.setSize("24px");
        if (active) ic.getStyle().set("color", "white");

        HorizontalLayout wrapper = new HorizontalLayout(ic);
        wrapper.setWidthFull();
        wrapper.setJustifyContentMode(JustifyContentMode.CENTER);
        wrapper.setPadding(true);
        wrapper.getStyle()
            .set("border-radius", "12px")
            .set("padding", "12px");

        if (active) {
            wrapper.getStyle().set("background-color", "#f97316");
        }

        return wrapper;
    }

    private Class<? extends Component> resolveRoute(String route) {
        return switch (route) {
            case "landing" -> Landing.class;
            case "Manajemen" -> ManajemenView.class;
            case "Verifikasi" -> Verifikasipeminjaman.class;
            case "Riwayat" -> Riwayatpeminjaman.class;
            case "login" -> LoginView.class;
            default -> null;
        };
    }

    private String getCurrentRoute() {
        String path = VaadinService.getCurrentRequest().getPathInfo();
        if (path == null) return "";
        return path.replaceFirst("^/", "");
    }
}
