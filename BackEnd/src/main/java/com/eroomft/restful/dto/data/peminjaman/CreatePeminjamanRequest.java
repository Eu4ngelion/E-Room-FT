package com.eroomft.restful.dto.data.peminjaman;

import io.swagger.v3.oas.annotations.media.Schema;

public class CreatePeminjamanRequest {

    @Schema(description = "ID akun yang melakukan peminjaman", example = "2309106001")
    private String akunId;

    @Schema(description = "ID ruangan yang dipinjam", example = "A101")
    private String namaRuangan;

    @Schema(description = "Keperluan peminjaman", example = "Kelas Pengganti Matkul OOP")
    private String keperluan;

    @Schema(description = "Tanggal peminjaman (format: YYYY-MM-DD)", example = "2025-05-31")
    private String tanggalPeminjaman;

    @Schema(description = "Waktu mulai peminjaman (format: HH:mm)", example = "08:00")
    private String waktuMulai;

    @Schema(description = "Waktu selesai peminjaman (format: HH:mm)", example = "10:00")
    private String waktuSelesai;

    public CreatePeminjamanRequest() {
    }

    public CreatePeminjamanRequest(String akunId, String namaRuangan, String keperluan, String tanggalPeminjaman, String waktuMulai, String waktuSelesai) {
        this.akunId = akunId;
        this.namaRuangan = namaRuangan;
        this.keperluan = keperluan;
        this.tanggalPeminjaman = tanggalPeminjaman;
        this.waktuMulai = waktuMulai;
        this.waktuSelesai = waktuSelesai;
    }

    public String getAkunId() {
        return akunId;
    }
    public void setAkunId(String akunId) {
        this.akunId = akunId;
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
    public String getTanggalPeminjaman() {
        return tanggalPeminjaman;
    }
    public void setTanggalPeminjaman(String tanggalPeminjaman) {
        this.tanggalPeminjaman = tanggalPeminjaman;
    }
    public String getWaktuMulai() {
        return waktuMulai;
    }
    public void setWaktuMulai(String waktuMulai) {
        this.waktuMulai = waktuMulai;
    }
    public String getWaktuSelesai() {
        return waktuSelesai;
    }
    public void setWaktuSelesai(String waktuSelesai) {
        this.waktuSelesai = waktuSelesai;
    }
}
