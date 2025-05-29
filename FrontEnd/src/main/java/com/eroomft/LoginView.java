package com.eroomft;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("/login")
@AnonymousAllowed
public class LoginView extends VerticalLayout {

    public LoginView() {
        setSizeFull();
        getStyle().set("background", "linear-gradient(to bottom, #FF7213, #FA812F, #FB9A59, #ffffff)");
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSpacing(true);

        Image logo = new Image("/frontend/unmul.png", "Logo UNMUL");
        logo.setWidth("100px");
        logo.setHeight("100px");
        logo.getStyle()
            .set("background-color", "white")
            .set("font-size", "20px")
            .set("margin-bottom", "20px")
            .set("display", "flex")
            .set("justify-self", "center");
            
       
        H2 login = new H2("Login");
        login.setWidth("350px");
        login.getStyle()
                .set("font-weight", "bold")
                .set("text-align", "center")
                .set("margin-bottom", "10px");
    
        Paragraph text = new Paragraph("Silahkan gunakan akun portal AIS anda untuk login");
        text.setWidth("350px");
        text.getStyle()
                .set("text-align", "center")
                .set("font-size", "14px")
                .set("font-weight", "bold")
                .set("margin-bottom", "10px");
    
        TextField txtBox = new TextField("Username");
        txtBox.setWidth("350px");
        txtBox.setPlaceholder("Masukkan NIM/NIP");
        txtBox.getStyle().set("margin-bottom", "10px");
        
        PasswordField txtPass = new PasswordField("Password");
        txtPass.setWidth("350px");
        txtPass.setPlaceholder("Masukkan Kata Sandi");
        txtPass.getStyle().set("margin-bottom", "20px");      
    
        Button btnLogin = new Button("Login");
        btnLogin.setWidth("350px");
        btnLogin.getStyle()
                .set("background-color", "#FA812F")
                .set("color", "white")
                .set("font-weight", "bold")
                .set("border", "none")
                .set("border-radius", "5px")
                .set("cursor", "pointer")
                .set("margin-bottom", "20px");

        Anchor Kembali = new Anchor();
        Kembali.setHref("/");
        Kembali.setText("Kembali Ke Beranda");
        Kembali.getStyle()
                .set("color", "black")
                .set("margin-top", "10px");
    
        btnLogin.addClickListener(e -> {
            String username = txtBox.getValue();
            String password = txtPass.getValue();
    
            if (username.isEmpty() || password.isEmpty()) {
                Notification.show("NIM/NIP dan password harus diisi!", 3500, Notification.Position.MIDDLE);
            }
            else{
                login(username, password);
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
formLogin.add(logo, login, text, txtBox, txtPass, btnLogin, Kembali);
    
        add(formLogin);
    }

    // Method untuk request api POST login
    private void login(String akunId, String password) {
        try {
            // Membuat JSON payload
            ObjectMapper mapper = new ObjectMapper();
            String jsonPayload = mapper.writeValueAsString(
                new LoginRequest(akunId, password)
            );

            // Membuat HTTP client dan request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/api/v1/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        // Kirim Request dan terima Response
        HttpResponse<String> response = client.sendAsync(
            request, HttpResponse.BodyHandlers.ofString()
        ).join();

        // Cek response status dan message
        mapper = new ObjectMapper();
        LoginResponse loginResponse = mapper.readValue(response.body(), LoginResponse.class);

        // Cek apakah login berhasil
        if ("success".equalsIgnoreCase(loginResponse.getStatus())) {
            Notification.show("Login berhasil!", 3500, Notification.Position.MIDDLE);

            // Ambil role dari response Data 
            String role = "";
            if (loginResponse.getData() instanceof java.util.Map) {
            Object roleObj = ((java.util.Map<?, ?>) loginResponse.getData()).get("role");
            if (roleObj != null) {
                role = roleObj.toString();
            }
            }

            // Route default = beranda
            String targetRoute = "beranda";
            // Hanya atur ke dasbor jika role adalah ADMIN
            if ("ADMIN".equalsIgnoreCase(role)) {
            targetRoute = "dasbor";
            }

            String finalTargetRoute = targetRoute;
            getUI().ifPresent(ui -> ui.navigate(finalTargetRoute));
        } else { // Jika login gagal
            Notification.show("Login gagal: " + loginResponse.getMessage(), 3500, Notification.Position.MIDDLE);
        }
    } 
    // Tambahkan Exception untuk jenis-jenis error lainnya kalau perlu
    catch (Exception e) {
        Notification.show("Error: " + e.getMessage(), 3500, Notification.Position.MIDDLE);
    }
}

// DTO untuk payload
static class LoginRequest {
    private String akunId;
    private String password;

    public LoginRequest(String akunId, String password) {
        this.akunId = akunId;
        this.password = password;
    }

    public String getAkunId() { return akunId; }
    public void setAkunId(String akunId) { this.akunId = akunId; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

// DTO untuk response
static class LoginResponse {
    private String Status;
    private String Message;
    private Object Data;

    public LoginResponse() {}

    public String getStatus() { return Status; }
    public void setStatus(String status) { this.Status = status; }
    public String getMessage() { return Message; }
    public void setMessage(String message) { this.Message = message; }
    public Object getData() { return Data; }
    public void setData(Object data) { this.Data = data; }
}
}