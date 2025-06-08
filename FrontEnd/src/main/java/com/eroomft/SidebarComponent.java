package com.eroomft;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.RouterLink;

public class SidebarComponent extends VerticalLayout implements AfterNavigationObserver {

    // --- PERUBAHAN DI SINI: Mengubah nilai awal menjadi false ---
    private boolean isExpanded = false;
    private final Button toggleButton;

    public SidebarComponent() {
        setPadding(false);
        setSpacing(false);
        getStyle()
            .set("background-color", "white")
            .set("border-right", "1px solid #e0e0e0")
            .set("transition", "width 0.2s ease-in-out")
            .set("height", "100vh");
        
        toggleButton = new Button();
        toggleButton.getStyle()
            .set("background", "transparent")
            .set("border", "none")
            .set("color", "#6b7280")
            .set("cursor", "pointer");
        toggleButton.addClickListener(e -> toggle());
        
        buildSidebar();
    }

    private void buildSidebar() {
        removeAll();
        setWidth(isExpanded ? "260px" : "72px");
        
        String currentPath = UI.getCurrent().getActiveViewLocation().getPath();
        
        if (isExpanded) {
            setAlignItems(Alignment.STRETCH);

            toggleButton.setIcon(new Icon(VaadinIcon.ANGLE_LEFT));

            Image logo = new Image("/frontend/RoomQue.png", "Logo RoomQue");
            logo.setWidth("40px");
            logo.setHeight("40px");

            Span title = new Span("RoomQue");
            title.getStyle()
                .set("font-weight", "bold")
                .set("font-size", "1.1rem");
            
            Div spacer = new Div();

            HorizontalLayout branding = new HorizontalLayout(logo, title, spacer, toggleButton);
            branding.setAlignItems(Alignment.CENTER);
            branding.setSpacing(true);
            branding.getStyle().set("padding", "1rem");
            branding.setWidthFull();
            branding.expand(spacer);

            add(branding);
            add(createSection("UTAMA"));
            add(createMenuLink("user/beranda", "material-symbols--dashboard-outline.png", "Beranda", currentPath));
            add(createMenuLink("user/ruangan", "system-uicons--door-alt.png", "Daftar Ruangan", currentPath));
            
            add(createSection("PEMINJAMAN RUANGAN"));
            add(createMenuLink("user/pengajuan", "tdesign--form.png", "Ajukan Peminjaman", currentPath));
            add(createMenuLink("user/daftar-peminjaman", "mdi--eye-outline.png", "Daftar Peminjaman", currentPath));
            add(createMenuLink("user/riwayat", "material-symbols--history.png", "Riwayat Peminjaman", currentPath));

            Div bottomSpacer = new Div();
            setFlexGrow(1, bottomSpacer);
            add(bottomSpacer);

            add(createLogoutButton("Keluar", true));
        } else {
            setAlignItems(Alignment.CENTER);

            toggleButton.setIcon(new Icon(VaadinIcon.MENU));
            add(toggleButton);

            add(createIconLink("user/beranda", "material-symbols--dashboard-outline.png", currentPath));
            add(createIconLink("user/ruangan", "system-uicons--door-alt.png", currentPath));
            add(createIconLink("user/pengajuan", "tdesign--form.png", currentPath));
            add(createIconLink("user/daftar-peminjaman", "mdi--eye-outline.png", currentPath));
            add(createIconLink("user/riwayat", "material-symbols--history.png", currentPath));

            Div bottomSpacer = new Div();
            setFlexGrow(1, bottomSpacer);
            add(bottomSpacer);

            add(createLogoutButton(null, false));
        }
    }

    public void toggle() {
        this.isExpanded = !this.isExpanded;
        buildSidebar();
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        buildSidebar();
    }
    
    private Span createSection(String title) {
        Span sectionTitle = new Span(title);
        sectionTitle.getStyle()
            .set("font-size", "0.8rem")
            .set("font-weight", "bold")
            .set("color", "#6b7280")
            .set("padding", "0.75rem 1.5rem");
        return sectionTitle;
    }

    private RouterLink createMenuLink(String route, String iconFileName, String text, String currentPath) {
        Class<? extends Component> targetClass = resolveRouteTarget(route);
        if (targetClass == null) {
            Image icon = new Image("/frontend/" + iconFileName, text + " icon");
            icon.setWidth("24px");
            icon.setHeight("24px");
            HorizontalLayout content = new HorizontalLayout(icon, new Span(text));
            content.setAlignItems(Alignment.CENTER);
            content.setSpacing(true);
            content.getStyle().set("color", "#aab2bd");
            
            RouterLink disabledLink = new RouterLink();
            disabledLink.add(content);
            disabledLink.getStyle()
                .set("text-decoration", "none")
                .set("padding", "0.75rem 1.5rem")
                .set("display", "flex")
                .set("align-items", "center")
                .set("border-radius", "0.5rem")
                .set("margin", "0.25rem 1rem")
                .set("cursor", "not-allowed");
            return disabledLink;
        }

        RouterLink link = new RouterLink(targetClass);
        
        Image linkIcon = new Image("/frontend/" + iconFileName, text + " icon");
        linkIcon.setWidth("24px");
        linkIcon.setHeight("24px");
        
        Span linkText = new Span(text);
        
        HorizontalLayout content = new HorizontalLayout(linkIcon, linkText);
        content.setAlignItems(Alignment.CENTER);
        content.setSpacing(true);
        link.add(content);

        link.getStyle()
            .set("text-decoration", "none")
            .set("padding", "0.75rem 1.5rem")
            .set("display", "flex")
            .set("align-items", "center")
            .set("border-radius", "0.5rem")
            .set("margin", "0.25rem 1rem");

        boolean isActive = currentPath.equals(route);
        if (isActive) {
            link.getStyle().set("background-color", "#FEE6D5").set("color", "#FF6B35");
        } else {
            link.getStyle().set("color", "black");
        }
        return link;
    }

    private RouterLink createIconLink(String route, String iconFileName, String currentPath) {
        Class<? extends Component> targetClass = resolveRouteTarget(route);
        if (targetClass == null) {
            Image linkIcon = new Image("/frontend/" + iconFileName, "Icon");
            linkIcon.setWidth("24px");
            linkIcon.setHeight("24px");
            
            RouterLink disabledLink = new RouterLink();
            disabledLink.add(linkIcon);
            disabledLink.getStyle()
                .set("color", "#aab2bd")
                .set("cursor", "not-allowed")
                .set("display", "flex")
                .set("justify-content", "center")
                .set("align-items", "center")
                .set("padding", "1rem 0")
                .set("border-radius", "0.5rem")
                .set("margin", "0.5rem")
                .set("width", "48px");
            return disabledLink;
        }

        RouterLink link = new RouterLink(targetClass);
        Image linkIcon = new Image("/frontend/" + iconFileName, "Icon");
        linkIcon.setWidth("24px");
        linkIcon.setHeight("24px");
        link.add(linkIcon);
        
        link.getStyle()
            .set("display", "flex")
            .set("justify-content", "center")
            .set("align-items", "center")
            .set("padding", "1rem 0")
            .set("border-radius", "0.5rem")
            .set("margin", "0.5rem")
            .set("width", "48px");

        boolean isActive = currentPath.equals(route);
        if (isActive) {
            link.getStyle().set("background-color", "#FEE6D5");
        }
        return link;
    }

    private Button createLogoutButton(String text, boolean expanded) {
        Button logoutButton;
        Icon logoutIcon = new Icon(VaadinIcon.SIGN_OUT);
        logoutIcon.getStyle().set("color", "#D9534F");

        if (expanded) {
            logoutButton = new Button(text, logoutIcon);
            logoutButton.setWidth("calc(100% - 2rem)");
            logoutButton.getStyle()
                .set("margin", "1rem")
                .set("justify-content", "flex-start");
        } else {
            logoutButton = new Button(logoutIcon);
            logoutButton.getStyle()
                .set("margin", "0.5rem auto")
                .set("width", "fit-content");
        }
        
        logoutButton.getStyle()
            .set("background-color", "#FFE4E4")
            .set("border", "none")
            .set("border-radius", "0.5rem");
        
        logoutButton.addClickListener(e -> {
            UI.getCurrent().getSession().close();
            UI.getCurrent().access(() -> {
                UI.getCurrent().navigate("");
            });
        });
        return logoutButton;
    }

    private Class<? extends Component> resolveRouteTarget(String route) {
        return switch (route) {
            // view tapi extend classnya
            case "user/beranda" -> userBeranda.class;
            case "user/ruangan" -> UserDaftarRuanganView.class;
            case "user/pengajuan" -> UserPengajuanView.class;
            case "user/daftar-peminjaman" -> UserPeminjamanView.class;
            case "user/riwayat" -> UserRiwayatPeminjamanView.class;
            default -> null;
        };
    }
}