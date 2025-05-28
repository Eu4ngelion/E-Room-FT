package com.eroomft;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        setSizeFull();
        getStyle().set("background", "linear-gradient(to bottom, #FF7213, #FA812F, #FB9A59, #ffffff)");
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSpacing(true);

        Avatar avatar = new Avatar("UNMUL");
        avatar.setImage("/frontend/unmul.png");
        avatar.setWidth("100px");
        avatar.setHeight("100px");
        avatar.getStyle()
            .set("background-color", "white")
            .set("font-size", "20px")
            .set("margin-bottom", "20px")
            .set("display", "flex")
            .set("justify-self", "center");        
       
        H2 heading2 = new H2("Login");
        heading2.setWidth("350px");
        heading2.getStyle().set("font-weight", "bold")
                           .set("text-align", "center")
                           .set("margin-bottom", "10px");
    
        H6 heading6 = new H6("Silahkan gunakan akun anda untuk login");
        heading6.setWidth("350px");
        heading6.getStyle().set("font-weight", "bold")
                           .set("text-align", "center")
                           .set("margin-bottom", "20px");
    
        TextField usernameField = new TextField("Username");
        usernameField.setWidth("350px");
        usernameField.getStyle().set("margin-bottom", "10px");
    
        PasswordField passwordField = new PasswordField("Password");
        passwordField.setWidth("350px");
        passwordField.getStyle().set("margin-bottom", "20px");
    
        Button loginButton = new Button("Login");
        loginButton.setWidth("350px");
        loginButton.getStyle().set("background-color", "#FA812F")
                              .set("color", "white")
                              .set("font-weight", "bold")
                              .set("border", "none")
                              .set("border-radius", "5px")
                              .set("cursor", "pointer")
                              .set("margin-bottom", "20px");

        Anchor BackHome = new Anchor();
        BackHome.setHref("/");
        BackHome.setText("Back To Home");
        BackHome.getStyle().set("color", "#FA812F")
                           .set("margin-top", "10px");
    
        loginButton.addClickListener(e -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();
    
            if (username.isEmpty() || password.isEmpty()) {
                Notification.show("Username dan password harus diisi!", 3500, Notification.Position.MIDDLE);
                return;
            }
    
            if ("admin".equals(username) && "admin".equals(password)) {
                Notification.show("Login berhasil!", 2000, Notification.Position.MIDDLE);
                // TODO: navigasi ke halaman lain
            } else {
                Notification.show("Username atau password salah!", 3500, Notification.Position.MIDDLE);
            }
        });
    
        Div formLogin = new Div();
        formLogin.setWidth("350px");
        formLogin.getStyle()
            .set("background-color", "white")
            .set("padding", "20px")
            .set("border-radius", "10px")
            .set("box-shadow", "0 4px 8px rgba(0,0,0,0.2)")
            .set("text-align", "center");
formLogin.add(avatar, heading2, heading6, usernameField, passwordField, loginButton, BackHome);
    
        add(formLogin);
    }
}