package com.eroomft.restful.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "log_peminjaman")
public class LogPeminjaman {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int logPeminjamanId;
    private String akunId; 
    private String namaPeminjam;
    private Tipe tipeRuangan;
    public enum Tipe {
        LAB,
        KELAS,
        SEMINAR,
        RAPAT
    }
    private String gedung;
    private String namaRuangan;
    private String keperluan;
    private LocalDate tanggalPeminjaman;
    @Column(columnDefinition = "TIME")
    private LocalTime waktuMulai;
    @Column(columnDefinition = "TIME")
    private LocalTime waktuSelesai;
    private Status status;
    public enum Status {
        DITOLAK,
        SELESAI,
        DIBATALKAN
    }
    private boolean isDeleted;

    public LogPeminjaman() {
    }

    public LogPeminjaman(String akunId,
                        String namaPeminjam, Tipe tipeRuangan, String gedung,
                        String namaRuangan, String keperluan,
                        LocalDate tanggalPeminjaman, LocalTime waktuMulai,
                        LocalTime waktuSelesai, Status status,
                        boolean isDeleted) {
        this.akunId = akunId;
        this.namaPeminjam = namaPeminjam;
        this.tipeRuangan = tipeRuangan;
        this.gedung = gedung;
        this.namaRuangan = namaRuangan;
        this.keperluan = keperluan;
        this.tanggalPeminjaman = tanggalPeminjaman;
        this.waktuMulai = waktuMulai;
        this.waktuSelesai = waktuSelesai;
        this.status = status;
        this.isDeleted = isDeleted;
    }

    public int getLogPeminjamanId() {
        return logPeminjamanId;
    }
    public void setLogPeminjamanId(int logPeminjamanId) {
        this.logPeminjamanId = logPeminjamanId;
    }
    public String getAkunId() {
        return akunId;
    }
    public void setAkunId(String akunId) {
        this.akunId = akunId;
    }
    public String getNamaPeminjam() {
        return namaPeminjam;
    }
    public void setNamaPeminjam(String namaPeminjam) {
        this.namaPeminjam = namaPeminjam;
    }
    public Tipe getTipeRuangan() {
        return tipeRuangan;
    }
    public void setTipeRuangan(Tipe tipeRuangan) {
        this.tipeRuangan = tipeRuangan;
    }
    public String getGedung() {
        return gedung;
    }
    public void setGedung(String gedung) {
        this.gedung = gedung;
    }
    public String getNamaRuangan() {
        return namaRuangan;
    }
    public void setNamaRuangan(String namaRuangan) {
        this.namaRuangan = namaRuangan;
    }
    public String getKeperluan() {
        return keperluan;
    }
    public void setKeperluan(String keperluan) {
        this.keperluan = keperluan;
    }
    public LocalDate getTanggalPeminjaman() {
        return tanggalPeminjaman;
    }
    public void setTanggalPeminjaman(LocalDate tanggalPeminjaman) {
        this.tanggalPeminjaman = tanggalPeminjaman;
    }
    public LocalTime getWaktuMulai() {
        return waktuMulai;
    }
    public void setWaktuMulai(LocalTime waktuMulai) {
        this.waktuMulai = waktuMulai;
    }
    public LocalTime getWaktuSelesai() {
        return waktuSelesai;
    }
    public void setWaktuSelesai(LocalTime waktuSelesai) {
        this.waktuSelesai = waktuSelesai;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public boolean isDeleted() {
        return isDeleted;
    }
    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
