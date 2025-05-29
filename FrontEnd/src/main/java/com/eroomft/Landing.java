package com.eroomft;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class Landing extends VerticalLayout {
    // button that goes to login page
    public Landing() {
        setSizeFull();
        getStyle().set("background", "linear-gradient(to bottom, #FF7213, #FA812F, #FB9A59, #ffffff)");
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSpacing(true);

        // Button to go to login page
        Button loginButton = new Button("Login", event -> 
            getUI().ifPresent(ui -> ui.navigate("login"))
        );
        add(loginButton);
    }

}