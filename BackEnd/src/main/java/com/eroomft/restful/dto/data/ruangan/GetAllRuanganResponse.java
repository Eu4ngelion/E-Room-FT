package com.eroomft.restful.dto.data.ruangan;

import io.swagger.v3.oas.annotations.media.Schema;

public class GetAllRuanganResponse {

    @Schema(example = "1")
    private int ruanganId;

    @Schema(example = "KELAS")
    private String tipe;

    @Schema(example = "B101")
    private String nama;

    @Schema(example = "30")
    private int kapasitas;

    @Schema(example = "AC, Proyektor, Papan Tulis")
    private String fasilitas;

    @Schema(example = "Gedung B")
    private String gedung;

    @Schema(example = "Lantai 3")
    private String lokasi;

    @Schema(example = "/uploads/B101.jpg")
    private String pathGambar;

    public GetAllRuanganResponse() {
    }

    public GetAllRuanganResponse(int ruanganId, String tipe, String nama, int kapasitas, String fasilitas, String gedung, String lokasi, String pathGambar) {
        this.ruanganId = ruanganId;
        this.tipe = tipe;
        this.nama = nama;
        this.kapasitas = kapasitas;
        this.fasilitas = fasilitas;
        this.gedung = gedung;
        this.lokasi = lokasi;
        this.pathGambar = pathGambar;
    }

    public int getRuanganId() {
        return ruanganId;
    }
    public void setRuanganId(int ruanganId) {
        this.ruanganId = ruanganId;
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

