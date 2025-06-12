package com.eroomft;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

import jakarta.annotation.PostConstruct;

@Route("user/pengajuan")
@org.springframework.stereotype.Component
@Scope("prototype")
public class UserPengajuanView extends HorizontalLayout implements HasUrlParameter<String> {

    private SidebarComponent sidebar;
    private VerticalLayout mainContent;
    private String ruanganId;

    @Value("${api.base.url:}")
    private String apiBaseUrl;

    public UserPengajuanView() {
        String role = (String) UI.getCurrent().getSession().getAttribute("role");
        if (role == null || (!role.equalsIgnoreCase("mahasiswa") && !role.equalsIgnoreCase("dosen"))) {
            Notification.show("Anda tidak memiliki akses ke halaman ini.", 3000, Notification.Position.BOTTOM_END);
            UI.getCurrent().access(() -> UI.getCurrent().navigate(""));
            return;
        }

        setSizeFull();
        setPadding(false);
        setSpacing(false);

        sidebar = new SidebarComponent();

        mainContent = new VerticalLayout();
        mainContent.setSizeFull();
        mainContent.setPadding(false);
        mainContent.getStyle().set("background-color", "#FEE6D5");
        mainContent.getStyle().set("overflow", "auto");

        add(sidebar, mainContent);
    }

    @PostConstruct
    private void init() {
        System.out.println("Injected apiBaseUrl: " + apiBaseUrl);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        ruanganId = event.getLocation().getQueryParameters().getParameters()
                .getOrDefault("ruanganId", List.of())
                .stream()
                .findFirst()
                .orElse(null);

        System.out.println("Extracted ruanganId: " + ruanganId);

        mainContent.add(createContent());
    }

    private Component createContent() {
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);
        content.setHeightFull();
        content.setWidthFull();
        content.getStyle()
                .set("background-color", "#FEE6D5");

        Span title = new Span("Ajukan Peminjaman Ruangan");
        title.getStyle()
                .set("font-size", "1.5rem")
                .set("font-weight", "bold")
                .set("margin-bottom", "1rem");
        content.add(title);

        VerticalLayout formLayout = new VerticalLayout();
        formLayout.setPadding(true);
        formLayout.setWidth("70%");
        formLayout.getStyle()
                .set("background-color", "white")
                .set("border-radius", "8px")
                .set("padding", "1.5rem")
                .set("box-shadow", "var(--lumo-box-shadow-xs)");

        String namaPeminjam = (String) UI.getCurrent().getSession().getAttribute("nama");
        Div namaPeminjamDiv = new Div();
        namaPeminjamDiv.setText("Nama Peminjam:");
        namaPeminjamDiv.getStyle().set("font-weight", "bold");
        TextField namaPeminjamField = new TextField();
        namaPeminjamField.setValue(namaPeminjam != null ? namaPeminjam : "");
        namaPeminjamField.setReadOnly(true);
        namaPeminjamField.setWidthFull();
        namaPeminjamField.getStyle().set("margin-bottom", "0.5rem");
        formLayout.add(namaPeminjamDiv, namaPeminjamField);

        Div keperluanDiv = new Div();
        keperluanDiv.setText("Keperluan:");
        keperluanDiv.getStyle().set("font-weight", "bold");
        TextField keperluanField = new TextField();
        keperluanField.setPlaceholder("Masukkan keperluan peminjaman");
        keperluanField.setWidthFull();
        keperluanField.getStyle().set("margin-bottom", "0.5rem");
        formLayout.add(keperluanDiv, keperluanField);

        Div tanggalDiv = new Div();
        tanggalDiv.setText("Tanggal Peminjaman:");
        tanggalDiv.getStyle().set("font-weight", "bold");
        DatePicker tanggalPicker = new DatePicker();
        tanggalPicker.setPlaceholder("Pilih tanggal peminjaman");
        tanggalPicker.setWidthFull();
        tanggalPicker.getStyle().set("margin-bottom", "0.5rem");
        tanggalPicker.setValue(LocalDate.now());
        tanggalPicker.setMin(LocalDate.now());
        formLayout.add(tanggalDiv, tanggalPicker);

        HorizontalLayout jamLayout = new HorizontalLayout();
        jamLayout.setWidthFull();
        jamLayout.setSpacing(true);
        jamLayout.setAlignItems(FlexComponent.Alignment.BASELINE);

        VerticalLayout jamMulaiLayout = new VerticalLayout();
        jamMulaiLayout.setSpacing(false);
        jamMulaiLayout.setPadding(false);
        Div jamMulaiLabel = new Div();
        jamMulaiLabel.setText("Jam Mulai:");
        jamMulaiLabel.getStyle().set("font-weight", "bold");
        TimePicker jamMulaiField = new TimePicker();
        jamMulaiField.setPlaceholder("Pilih Jam Mulai");
        jamMulaiField.setStep(java.time.Duration.ofMinutes(15));
        jamMulaiField.setWidthFull();
        jamMulaiField.getStyle().set("margin-bottom", "1rem");
        jamMulaiLayout.add(jamMulaiLabel, jamMulaiField);

        VerticalLayout jamSelesaiLayout = new VerticalLayout();
        jamSelesaiLayout.setSpacing(false);
        jamSelesaiLayout.setPadding(false);
        Div jamSelesaiLabel = new Div();
        jamSelesaiLabel.setText("Jam Selesai:");
        jamSelesaiLabel.getStyle().set("font-weight", "bold");
        TimePicker jamSelesaiField = new TimePicker();
        jamSelesaiField.setPlaceholder("Pilih Jam Selesai");
        jamSelesaiField.setWidthFull();
        jamSelesaiField.setStep(java.time.Duration.ofMinutes(15));
        jamSelesaiField.getStyle().set("margin-bottom", "1rem");
        jamSelesaiLayout.add(jamSelesaiLabel, jamSelesaiField);

        jamLayout.add(jamMulaiLayout, jamSelesaiLayout);
        formLayout.add(jamLayout);

        Div ruanganDiv = new Div();
        ruanganDiv.setText("Nama Ruangan:");
        ruanganDiv.getStyle().set("font-weight", "bold");
        TextField ruanganField = new TextField();
        ruanganField.setPlaceholder("Masukkan nama ruangan (misal: A101)");
        ruanganField.setWidthFull();
        ruanganField.getStyle().set("margin-bottom", "0.5rem");
        String namaRuangan = (String) UI.getCurrent().getSession().getAttribute("pengajuan_room_name");
        if (namaRuangan != null && !namaRuangan.isEmpty()) {
            ruanganField.setValue(namaRuangan);
        }
        if (ruanganId != null) {
            fetchRoomName();
        }
        formLayout.add(ruanganDiv, ruanganField);

        Button submitButton = new Button("Ajukan");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submitButton.getStyle()
                .set("margin-top", "1rem")
                .set("align-self", "flex-end")
                .set("background-color", "#FF6B35")
                .set("color", "white")
                .set("border", "none")
                .set("padding", "0.5rem 2rem")
                .set("border-radius", "4px")
                .set("font-size", "1rem");

        submitButton.addClickListener(event -> {
            String keperluan = keperluanField.getValue();
            LocalDate tanggal = tanggalPicker.getValue();
            LocalTime jamMulai = jamMulaiField.getValue();
            LocalTime jamSelesai = jamSelesaiField.getValue();
            String namaRuanganValue = ruanganField.getValue();
            String akunId = (String) UI.getCurrent().getSession().getAttribute("akunId");

            // Validation: Ensure all fields are filled
            if (tanggal == null || jamMulai == null || jamSelesai == null || namaRuanganValue.isEmpty() || akunId == null) {
                Notification.show("Semua field harus diisi.", 3000, Notification.Position.BOTTOM_END);
                return;
            }

            // Validation: Ensure the selected date is today or later
            if (tanggal.isBefore(LocalDate.now())) {
                Notification.show("Tanggal peminjaman hanya boleh hari ini atau setelahnya.", 3000, Notification.Position.BOTTOM_END);
                return;
            }

            // Validation: Ensure the start time is after the current time if the date is today
            if (tanggal.equals(LocalDate.now())) {
                ZoneId zoneId = ZoneId.of("Asia/Makassar");
                LocalTime nowWITA = LocalTime.now(zoneId);
                if (!jamMulai.isAfter(nowWITA)) {
                    Notification.show("Jam mulai harus lebih besar dari jam sekarang.", 3000, Notification.Position.BOTTOM_END);
                    return;
                }
            }

            // Validation: Ensure the start time is before the end time
            if (!jamMulai.isBefore(jamSelesai)) {
                Notification.show("Jam mulai harus lebih kecil dari jam selesai.", 3000, Notification.Position.BOTTOM_END);
                return;
            }

            // Validation: Ensure times are in 15-minute intervals
            if (jamMulai.getMinute() % 15 != 0 || jamSelesai.getMinute() % 15 != 0) {
                Notification.show("Jam hanya boleh diisi pada interval 15 menit (00, 15, 30, 45).", 3000, Notification.Position.BOTTOM_END);
                return;
            }

            // Prepare JSON payload
            String jsonPayload = String.format(
                "{\"akunId\": \"%s\", \"namaRuangan\": \"%s\", \"keperluan\": \"%s\", \"tanggalPeminjaman\": \"%s\", \"waktuMulai\": \"%s\", \"waktuSelesai\": \"%s\"}",
                akunId, namaRuanganValue, keperluan, tanggal.format(DateTimeFormatter.ISO_LOCAL_DATE), jamMulai, jamSelesai
            );

            try {
                if (apiBaseUrl == null || apiBaseUrl.trim().isEmpty()) {
                    Notification.show("Error: API base URL is not configured!", 3000, Notification.Position.MIDDLE);
                    throw new IllegalStateException("API base URL is not configured in application.properties");
                }
                String normalizedBaseUrl = apiBaseUrl.trim();
                if (!normalizedBaseUrl.startsWith("http://") && !normalizedBaseUrl.startsWith("https://")) {
                    Notification.show("Error: API base URL missing scheme (http:// or https://)!", 3000, Notification.Position.MIDDLE);
                    throw new IllegalArgumentException("API base URL missing scheme: " + normalizedBaseUrl);
                }
                if (normalizedBaseUrl.endsWith("/")) {
                    normalizedBaseUrl = normalizedBaseUrl.substring(0, normalizedBaseUrl.length() - 1);
                }

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(normalizedBaseUrl + "/api/v1/peminjaman"))
                        .header("Content-Type", "application/json")
                        .header("accept", "*/*")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    Notification.show("Peminjaman berhasil diajukan!", 3000, Notification.Position.BOTTOM_END);
                    UI.getCurrent().navigate("user/daftar-peminjaman");
                } else {
                    String responseBody = response.body();
                    String errorMessage = extractJsonValue(responseBody, "message");
                    if (errorMessage != null) {
                        Notification.show(errorMessage, 3000, Notification.Position.BOTTOM_END);
                    } else {
                        Notification.show("Gagal mengajukan peminjaman: " + responseBody, 3000, Notification.Position.BOTTOM_END);
                    }
                }
            } catch (IOException | InterruptedException e) {
                Notification.show("Error connecting to server: " + e.getMessage(), 3000, Notification.Position.BOTTOM_END);
            } catch (IllegalStateException | IllegalArgumentException e) {
                Notification.show("Error: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        formLayout.add(submitButton);
        formLayout.setSpacing(false);
        content.add(formLayout);
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        content.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        return content;
    }

    private void fetchRoomName() {
        try {
            if (apiBaseUrl == null || apiBaseUrl.trim().isEmpty()) {
                Notification.show("Error: API base URL is not configured!", 3000, Notification.Position.MIDDLE);
                throw new IllegalStateException("API base URL is not configured in application.properties");
            }
            String normalizedBaseUrl = apiBaseUrl.trim();
            if (!normalizedBaseUrl.startsWith("http://") && !normalizedBaseUrl.startsWith("https://")) {
                Notification.show("Error: API base URL missing scheme (http:// or https://)!", 3000, Notification.Position.MIDDLE);
                throw new IllegalArgumentException("API base URL missing scheme: " + normalizedBaseUrl);
            }
            if (normalizedBaseUrl.endsWith("/")) {
                normalizedBaseUrl = normalizedBaseUrl.substring(0, normalizedBaseUrl.length() - 1);
            }

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(normalizedBaseUrl + "/api/v1/ruangan/" + ruanganId))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String dataStr = extractJsonObject(response.body(), "data");
                if (dataStr != null) {
                    String nama = extractJsonValue(dataStr, "nama");
                    if (nama != null) {
                        UI.getCurrent().getSession().setAttribute("pengajuan_room_name", nama);
                    }
                }
            } else {
                Notification.show("Failed to fetch room name: " + response.statusCode(), 3000, Notification.Position.MIDDLE);
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error connecting to server: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        } catch (IllegalStateException | IllegalArgumentException e) {
            Notification.show("Error: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private String extractJsonObject(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":";
            int keyIndex = json.indexOf(searchKey);
            if (keyIndex == -1) return null;

            int valueStart = keyIndex + searchKey.length();
            while (valueStart < json.length() && Character.isWhitespace(json.charAt(valueStart))) {
                valueStart++;
            }

            if (json.charAt(valueStart) == '{') {
                int braceCount = 1;
                int valueEnd = valueStart + 1;
                while (braceCount > 0 && valueEnd < json.length()) {
                    if (json.charAt(valueEnd) == '{') braceCount++;
                    if (json.charAt(valueEnd) == '}') braceCount--;
                    valueEnd++;
                }
                if (braceCount == 0) {
                    return json.substring(valueStart, valueEnd);
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private String extractJsonValue(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":";
            int keyIndex = json.indexOf(searchKey);
            if (keyIndex == -1) return null;

            int valueStart = keyIndex + searchKey.length();
            while (valueStart < json.length() && Character.isWhitespace(json.charAt(valueStart))) {
                valueStart++;
            }

            if (valueStart < json.length() && json.charAt(valueStart) == '"') {
                valueStart++;
                int valueEnd = json.indexOf('"', valueStart);
                if (valueEnd != -1) {
                    return json.substring(valueStart, valueEnd);
                }
            } else {
                int valueEnd = valueStart;
                while (valueEnd < json.length() &&
                        json.charAt(valueEnd) != ',' &&
                        json.charAt(valueEnd) != '}' &&
                        json.charAt(valueEnd) != ']') {
                    valueEnd++;
                }
                return json.substring(valueStart, valueEnd).trim();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}