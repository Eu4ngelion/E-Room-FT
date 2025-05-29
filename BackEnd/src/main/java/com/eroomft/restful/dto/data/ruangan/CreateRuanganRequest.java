package com.eroomft.restful.dto.data.ruangan;

public class CreateRuanganRequest {
    private String tipe;
    private String nama;
    private int kapasitas;
    private String fasilitas;
    private String gedung;
    private String lokasi;
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
