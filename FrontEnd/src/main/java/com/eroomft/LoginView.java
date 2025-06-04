package com.eroomft;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    // variable Global
    String pickedRole;
    private TextField txtBox;
    private String idLabel = "NIM/NIP";


    public LoginView() {
        // jika masih ada session,
        String sessionRole = (String) UI.getCurrent().getSession().getAttribute("role");
        if (sessionRole != null) {
                Notification.show("Log Out Terlebih Dahulu", 3000, Notification.Position.BOTTOM_END);
                UI.getCurrent().access(() -> {
                switch (sessionRole.toLowerCase()) {
                    case "mahasiswa", "dosen" -> UI.getCurrent().navigate("user/beranda");
                    case "admin" -> UI.getCurrent().navigate("admin/dashboard");
                    default -> {
                        Notification.show("Role tidak dikenali: " + sessionRole, 3000, Notification.Position.MIDDLE);
                        UI.getCurrent().navigate("");
                    }
                }
                });
                return;
        }


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
                .set("font-family", " 'poppins', sans-serif")
                .set("font-weight", "550")
                .set("font-size", "36px")
                .set("text-align", "center")
                .set("margin-bottom", "10px")
                .setWidth("400px");
    
        Paragraph text = new Paragraph("Silahkan gunakan akun portal AIS anda untuk login");
        text.setWidth("400px");
        text.getStyle()
                .set("text-align", "center")
                .set("font-family", " 'Plus Jakarta Sans', sans-serif")
                .set("font-weight", "550")
                .set("font-size", "16px")
                .set("margin-bottom", "10px");
    
        txtBox = new TextField("NIM/NIP");
        txtBox.setWidth("400px");
        txtBox.setPlaceholder("Masukkan NIM/NIP");
        txtBox.getStyle()
                .set("margin-bottom", "10px")
                .set("font-size", "16px")
                .set("font-color", "black")
                .set("font-family", "'Poppins', sans-serif")
                .set("font-weight", "500")
                .set("font-size", "16px");
        
        PasswordField txtPass = new PasswordField("Kata Sandi");
        txtPass.setWidth("400px");
        txtPass.setPlaceholder("Masukkan Kata Sandi");
        txtPass.getStyle()
                .set("margin-bottom", "20px")
                .set("font-size", "16px")
                .set("font-color", "black")
                .set("font-family", "'Poppins', sans-serif")
                .set("font-weight", "500")
                .set("font-size", "16px");
    
        Button btnLogin = new Button("Login");
        btnLogin.setWidth("400px");
        btnLogin.getStyle()
                .set("background-color", "#FF9F4C")
                .set("color", "white")
                .set("font-family", "'Poppins', sans-serif")
                .set("font-weight", "500")
                .set("font-size", "16px")
                .set("border", "none")
                .set("border-radius", "5px")
                .set("cursor", "pointer")
                .set("margin-bottom", "20px");

        btnLogin.getElement().addEventListener("mouseenter", e -> {
            btnLogin.getStyle().set("background-color", "#cc5200");
        });
        
        btnLogin.getElement().addEventListener("mouseleave", e -> {
            btnLogin.getStyle().set("background-color", "#FF9F4C");
        });

        Anchor Kembali = new Anchor();
        Kembali.setHref("/");
        Kembali.setText("Kembali Ke Beranda");
        Kembali.getStyle()
                .set("color", "black")
                .set("font-family", "'Plus Jakarta Sans', sans-serif")
                .set("font-weight", "550")
                .set("font-size", "12px")
                .set("margin-top", "10px");
    
        btnLogin.addClickListener(e -> {
            String username = txtBox.getValue();
            String password = txtPass.getValue();
    
            if (username.isEmpty() || password.isEmpty()) {
                errorPopup(idLabel + " dan password harus diisi!");
            }
            else{
                login(username, password);
            }
    
        });
    
        Div formLogin = new Div();
        formLogin.setWidth("400px");
        formLogin.getStyle()
            .set("background-color", "white")
            .set("padding", "20px")
            .set("border-radius", "10px")
            .set("box-shadow", "0 4px 8px rgba(0,0,0,0.2)")
            .set("text-align", "center");
        formLogin.add(logo, login, text, txtBox, txtPass, btnLogin, Kembali);
    
        add(formLogin);
    }

    private void errorPopup(String message) {
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);
    
        Icon errorIcon = VaadinIcon.CLOSE_CIRCLE.create();
        errorIcon.setSize("63px");
        errorIcon.getStyle()
            .set("color", "#FF7700")
            .set("margin-bottom", "10px");
    
        Span text = new Span(message);
        text.getStyle()
            .set("font-size", "24px")
            .set("font-weight", "600")
            .set("text-align", "center")
            .set("font-family", " 'Plus Jakarta Sans', sans-serif")
            .set("color", "Black")
            .set("display", "block")
            .set("margin-bottom", "10px")
            .set("white-space", "pre-line");
            
    
        Button btnTutup = new Button("Tutup", event -> dialog.close());
        btnTutup.getStyle()
            .set("background-color", "#FF7700")
            .set("color", "white")
            .set("font-size", "16px")
            .set("font-family", " 'Plus Jakarta Sans', sans-serif")
            .set("font-weight", "600")
            .set("border", "none")
            .set("border-radius", "5px")
            .set("cursor", "pointer");
    
        VerticalLayout lapisan = new VerticalLayout(errorIcon, text, btnTutup);
        lapisan.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        lapisan.setPadding(true);
        lapisan.setSpacing(false);
        lapisan.getStyle()
            .set("background", "#ffffff")
            .set("border-radius", "10px")
            .set("text-align", "center");
    
        dialog.add(lapisan);
        dialog.open();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
    QueryParameters queryParameters = event.getLocation().getQueryParameters();

    // Check if the "role" parameter exists
    if (queryParameters.getParameters().containsKey("role")) {
        // Get the value of the "role" parameter
        String role = queryParameters.getParameters().get("role").get(0);


        // You can now use the role value as needed
        pickedRole = role;
        if ("MAHASISWA".equalsIgnoreCase(role)) {
            idLabel = "NIM";
        } else if ("DOSEN".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role)) {
            idLabel = "NIP";
        } else if ("ADMIN".equalsIgnoreCase(role)) {
            idLabel = "NIM/NIP";
        }
        txtBox.setLabel(idLabel);
        txtBox.setPlaceholder("Masukkan " + idLabel);        
    } else
        //   kembalikan ke route index
        getUI().ifPresent(ui -> ui.navigate(""));
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
            // cek apakah role sesuai
            // Ambil role dari response Data 
            String role = "";
                if (loginResponse.getData() instanceof java.util.Map) {
                Object roleObj = ((java.util.Map<?, ?>) loginResponse.getData()).get("role");
                if (roleObj != null) {
                    role = roleObj.toString();
                }
            }

            // jika role tidak sesai getROle
            if (!role.equals(pickedRole)){
               errorPopup("Akun anda tidak ditemukan");
                return;
            }

            // Route default = beranda
            String targetRoute = "user/beranda";
            // Hanya atur ke dasbor jika role adalah ADMIN
            if ("ADMIN".equalsIgnoreCase(role)) {
                targetRoute = "admin/dashboard";
            }
            
            // simpan role di session
            final String finalRole = role;
            getUI().ifPresent(ui -> {
                ui.getSession().setAttribute("role", finalRole);
                ui.getSession().setAttribute("akunId", akunId);
                ui.getSession().setAttribute("email", ((Map<String, Object>) loginResponse.getData()).get("email"));
                ui.getSession().setAttribute("nama", ((Map<String, Object>) loginResponse.getData()).get("nama"));
            });
            String finalTargetRoute = targetRoute;
            getUI().ifPresent(ui -> ui.navigate(finalTargetRoute));
        } else { // Jika login gagal
            errorPopup(idLabel + " atau Kata Sandi salah");

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
