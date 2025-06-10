package com.eroomft;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.router.PageTitle;

@PageTitle("RoomQue")
public class MainLayout extends AppLayout {

    public MainLayout() {
        setPrimarySection(Section.DRAWER); // Pakai drawer mode

        // Sidebar
        SidebarComponent sidebar = new SidebarComponent();
        sidebar.setWidth("250px");
        sidebar.getStyle().set("height", "100vh"); // Pastikan penuh vertikal

        // Set sidebar ke bagian drawer (kiri)
        addToDrawer(sidebar);

        // Optional: Tambahkan header/top bar jika mau
        // H1 title = new H1("E-ROOM");
        // addToNavbar(title);
    }
}