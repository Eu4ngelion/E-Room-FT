package com.eroomft.restful.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "peminjaman")
public class Peminjaman {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int peminjamanId;

    @ManyToOne
    @JoinColumn(name = "akun_id", referencedColumnName = "akunId", nullable = false)
    private Akun akun;
    
    @ManyToOne
    @JoinColumn(name = "ruangan_id", referencedColumnName = "ruanganId", nullable = false)
    private Ruangan ruangan;

    @Column(nullable = true)
    private String keperluan; 

    @Column(nullable = false)
    private LocalDate tanggalPeminjaman;

    @Column(nullable = false)
    private LocalTime waktuMulai; 

    @Column(nullable = false)
    private LocalTime waktuSelesai; 

    @Column(nullable = false)
    private Status status; 
    public enum Status {
        DITOLAK,
        DIBATALKAN,
        MENUNGGU,
        BERHASIL,
        SELESAI
    } 

    public Peminjaman() {
    }

    public Peminjaman(int peminjamanId, Akun akun, Ruangan ruangan, String keperluan, LocalDate tanggalPeminjaman, LocalTime waktuMulai, LocalTime waktuSelesai, Status status) {
            this.peminjamanId = peminjamanId;
            this.akun = akun;
            this.ruangan = ruangan;
            this.keperluan = keperluan;
            this.tanggalPeminjaman = tanggalPeminjaman;
            this.waktuMulai = waktuMulai;
            this.waktuSelesai = waktuSelesai;
            this.status = status;
        }
        

    public int getPeminjamanId() {
        return peminjamanId;
    }
    public void setPeminjamanId(int peminjamanId) {
        this.peminjamanId = peminjamanId;
    }
    public Akun getAkun() {
        return akun;
    }
    public void setAkun(Akun akun) {
        this.akun = akun;
    }
    public Ruangan getRuangan() {
        return ruangan;
    }
    public void setRuangan(Ruangan ruangan) {
        this.ruangan = ruangan;
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
