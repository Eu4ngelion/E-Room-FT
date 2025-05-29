package com.eroomft;

import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {
    public MainView() {
        add(new Span("Hello from Vaadin Frontend!"));
HorizontalLayout horizontallayout = new HorizontalLayout();
horizontallayout.getStyle().setFlexGrow("1");
add(horizontallayout);
H4 helloWorld = new H4("HELLO WORLD!!");
add(helloWorld);
    }
}
