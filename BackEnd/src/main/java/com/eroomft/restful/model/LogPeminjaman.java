package com.eroomft.restful.model;

import java.time.LocalDate;
import java.time.LocalTime;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "log_peminjaman")
public class LogPeminjaman {

    @Id
    @GeneratedValue 
    private int logPeminjamanId;
    private int peminjamanId; 
    private String akunId; 
    private String namaPeminjam;
    private int ruanganId; 
    private String namaRuangan;
    private String keperluan;
    private LocalDate tanggalPeminjaman;
    private LocalTime waktuMulai;
    private LocalTime waktuSelesai;
    private Status status;
    public enum Status {
        DITOLAK,
        SELESAI
    }

    public LogPeminjaman() {
    }

    public LogPeminjaman(int logPeminjamanId, int peminjamanId, String akunId, String namaPeminjam, 
                         int ruanganId, String namaRuangan, String keperluan, 
                         LocalDate tanggalPeminjaman, LocalTime waktuMulai, 
                         LocalTime waktuSelesai, Status status) {
        this.logPeminjamanId = logPeminjamanId;
        this.peminjamanId = peminjamanId;
        this.akunId = akunId;
        this.namaPeminjam = namaPeminjam;
        this.ruanganId = ruanganId;
        this.namaRuangan = namaRuangan;
        this.keperluan = keperluan;
        this.tanggalPeminjaman = tanggalPeminjaman;
        this.waktuMulai = waktuMulai;
        this.waktuSelesai = waktuSelesai;
        this.status = status;
    }

    public int getLogPeminjamanId() {
        return logPeminjamanId;
    }
    public void setLogPeminjamanId(int logPeminjamanId) {
        this.logPeminjamanId = logPeminjamanId;
    }
    public int getPeminjamanId() {
        return peminjamanId;
    }
    public void setPeminjamanId(int peminjamanId) {
        this.peminjamanId = peminjamanId;
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
    public int getRuanganId() {
        return ruanganId;
    }
    public void setRuanganId(int ruanganId) {
        this.ruanganId = ruanganId;
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
}
