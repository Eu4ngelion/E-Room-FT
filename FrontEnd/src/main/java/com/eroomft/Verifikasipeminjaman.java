package com.eroomft;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("Verifikasi")
@PageTitle("Verifikasi Peminjaman")
public class Verifikasipeminjaman extends VerticalLayout {

    public Verifikasipeminjaman() {
        setWidthFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        getStyle().set("background-color", "#f7f7f7");

        H1 title = new H1("Verifikasi Peminjaman");
        title.getStyle().set("margin", "2rem 0");
        add(title);

        // Kartu utama
        HorizontalLayout card = new HorizontalLayout();
        card.setWidth("90%");
        card.setPadding(true);
        card.setSpacing(true);
        card.getStyle()
            .set("background", "#fff")
            .set("border-radius", "10px")
            .set("box-shadow", "0 2px 8px rgba(0,0,0,0.1)")
            .set("padding", "1.5rem")
            .set("display", "flex")
            .set("justify-content", "space-between")
            .set("align-items", "center");

        // Info peminjam
        VerticalLayout info = new VerticalLayout();
        info.setPadding(false);
        info.setSpacing(true);

        Label nama = new Label("Ahmad Zuhair Nur Aiman - 2309106025");
        nama.getStyle().set("font-weight", "600").set("font-size", "16px");

        Label detail = new Label("Ruang Kelas C102 | 09/09/2025 | 10.00–12.00");

        info.add(nama, detail);

        // Tombol aksi
        HorizontalLayout tombol = new HorizontalLayout();
        tombol.setSpacing(true);

        Button status = new Button("Menunggu");
        status.getStyle()
            .set("background-color", "#fff933")
            .set("color", "#000")
            .set("font-weight", "600");

        Button detailBtn = new Button("Detail Peminjaman");
        detailBtn.getStyle()
            .set("background-color", "#f97316")
            .set("color", "#fff")
            .set("font-weight", "600");

        Dialog detailDialog = createStyledDialog();
        detailBtn.addClickListener(e -> detailDialog.open());

        tombol.add(status, detailBtn);
        card.add(info, tombol);

        add(card, detailDialog);
    }

    private Dialog createStyledDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(true);
        dialogLayout.setSpacing(false);
        dialogLayout.setWidthFull();

        // Judul - Ditengah
        Label header = new Label("Detail Peminjaman Ruangan");
        header.getStyle()
            .set("font-size", "24px")
            .set("font-weight", "800")
            .set("margin-bottom", "20px");

        dialogLayout.setAlignSelf(Alignment.CENTER, header); // Ini kunci tengahnya
        dialogLayout.add(header);

        dialogLayout.add(createLabeledField("Nama Peminjam", "Ahmad Zuhair Nur Aiman"));
        dialogLayout.add(createLabeledField("NIM", "2309106025"));
        dialogLayout.add(createLabeledField("Keperluan", "Matkul pengganti"));
        dialogLayout.add(createLabeledField("Ruangan yang ingin dipinjam", "Ruang Kelas C102"));

        // Dua kolom: Tanggal dan Jam
        HorizontalLayout tanggalJam = new HorizontalLayout();
        tanggalJam.setWidthFull();
        tanggalJam.setSpacing(true);
        tanggalJam.add(
            createLabeledField("Tanggal", "09/09/2025"),
            createLabeledField("Jam", "10.00–12.00")
        );
        dialogLayout.add(tanggalJam);

        // Tombol Aksi - Ditengah
        HorizontalLayout actions = new HorizontalLayout();
        actions.setSpacing(true);
        actions.setWidthFull(); // Tambahkan ini
        actions.setJustifyContentMode(JustifyContentMode.CENTER);

        Button tolak = new Button("Tolak", e -> dialog.close());
        tolak.getStyle()
            .set("background-color", "#ef4444")
            .set("color", "white")
            .set("font-weight", "600")
            .set("border-radius", "8px")
            .set("width", "120px");

        Button izinkan = new Button("Izinkan", e -> dialog.close());
        izinkan.getStyle()
            .set("background-color", "#22c55e")
            .set("color", "white")
            .set("font-weight", "600")
            .set("border-radius", "8px")
            .set("width", "120px");

        actions.add(tolak, izinkan);
        dialogLayout.add(actions);

        dialog.add(dialogLayout);
        return dialog;
    }


    private VerticalLayout createLabeledField(String labelText, String value) {
        Label label = new Label(labelText);
        label.getStyle().set("font-weight", "700").set("margin-bottom", "4px");

        TextField field = new TextField();
        field.setValue(value);
        field.setReadOnly(true);
        field.setWidthFull();
        field.getStyle()
            .set("background-color", "#f3f4f6")
            .set("border-radius", "8px");

        VerticalLayout layout = new VerticalLayout(label, field);
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.setWidthFull();

        return layout;
    }
}
