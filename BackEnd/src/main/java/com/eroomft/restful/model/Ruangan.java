package com.eroomft.restful.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ruangan")
public class Ruangan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ruanganId;

    @Column(nullable = false, unique = true)
    private String nama;

    @Column(nullable = false)
    private Tipe tipe;
    public enum Tipe {
        LAB,
        KELAS,
        SEMINAR,
        RAPAT
    }

    @Column(nullable = false)
    private int kapasitas;

    @Column(nullable = false)
    private String fasilitas;

    @Column(nullable = false)
    private String gedung;

    @Column(nullable = false)
    private String lokasi;

    @Column(nullable = false)
    private String pathGambar;

    public Ruangan() {
    }

    public Ruangan(String nama, Tipe tipe, int kapasitas, String fasilitas, String gedung, String lokasi, String pathGambar) {
        this.nama = nama;
        this.tipe = tipe;
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
    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public Tipe getTipe() {
        return tipe;
    }
    public void setTipe(Tipe tipe) {
        this.tipe = tipe;
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
