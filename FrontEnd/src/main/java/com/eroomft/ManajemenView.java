package com.eroomft;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("Manajemen")
public class ManajemenView extends VerticalLayout {

    public ManajemenView() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // Layout Utama
        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSizeFull();

        // Sidebar
        SidebarComponent sidebar = new SidebarComponent();

        // Konten utama
        VerticalLayout content = new VerticalLayout();
        content.setWidthFull();
        content.setPadding(true);
        content.setAlignItems(Alignment.CENTER);
        content.setJustifyContentMode(JustifyContentMode.START);
        content.getStyle().set("background-color", "#f7f7f7");

        H1 title = new H1("Manajemen Ruangan");
        title.getStyle().set("margin", "2rem 0");
        content.add(title);

        // Tambahkan komponen lain sesuai kebutuhan

        mainLayout.add(sidebar, content);
        add(mainLayout);
    }
    
}
