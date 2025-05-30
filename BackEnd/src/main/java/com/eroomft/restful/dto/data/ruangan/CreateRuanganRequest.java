package com.eroomft.restful.dto.data.ruangan;

import io.swagger.v3.oas.annotations.media.Schema;

public class CreateRuanganRequest {

    @Schema(example = "KELAS")
    private String tipe;

    @Schema(example = "C301")
    private String nama;

    @Schema(example = "30")
    private int kapasitas;

    @Schema(example = "AC, Proyektor, Papan Tulis")
    private String fasilitas;

    @Schema(example = "Gedung C")
    private String gedung;

    @Schema(example = "Lantai 3, Ruang C301")
    private String lokasi;

    @Schema(example = "/images/ruangan/c301.jpg")
    private String pathGambar;

    public CreateRuanganRequest() {
    }
    
    public CreateRuanganRequest(String tipe, String nama, int kapasitas, String fasilitas, String gedung, String lokasi, String pathGambar) {
        this.tipe = tipe;
        this.nama = nama;
        this.kapasitas = kapasitas;
        this.fasilitas = fasilitas;
        this.gedung = gedung;
        this.lokasi = lokasi;
        this.pathGambar = pathGambar;
    }

    public String getTipe() {
        return tipe;
    }
    public void setTipe(String tipe) {
        this.tipe = tipe;
    }
    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public int getKapasitas() {
        return kapasitas;
    }
    public void setKapasitas(int kapasitas) {
        this.kapasitas = kapasitas;
    }
    public String getFasilitas() {
        return fasilitas;
    }
    public void setFasilitas(String fasilitas) {
        this.fasilitas = fasilitas;
    }
    public String getGedung() {
        return gedung;
    }
    public void setGedung(String gedung) {
        this.gedung = gedung;
    }
    public String getLokasi() {
        return lokasi;
    }
    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }
    public String getPathGambar() {
        return pathGambar;
    }
    public void setPathGambar(String pathGambar) {
        this.pathGambar = pathGambar;
    }
}
