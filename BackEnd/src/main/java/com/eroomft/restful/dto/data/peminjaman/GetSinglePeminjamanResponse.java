package com.eroomft.restful.dto.data.peminjaman;

import io.swagger.v3.oas.annotations.media.Schema;

public class GetSinglePeminjamanResponse {

    @Schema(description = "ID peminjaman", example = "999")
    private int peminjamanId;

    @Schema(description = "Nama peminjam", example = "Contoh Contohan")
    private String namaPeminjam;

    @Schema(description = "Email peminjam", example = "contoh@gmail.com")
    private String emailPeminjam;

    @Schema(description = "Keperluan peminjaman", example = "Kelas Pengganti Matkul OOP")
    private String keperluan;

    @Schema(description = "Tipe ruangan yang dipinjam", example = "KELAS")
    private String tipeRuangan;

    @Schema(description = "Nama ruangan yang dipinjam", example = "A999")
    private String namaRuangan;

    @Schema(description = "Kapasitas Maksimal Ruangan", example = "30")
    private int kapasitas;

    @Schema(description = "Tanggal peminjaman (format: YYYY-MM-DD)", example = "2025-06-30")
    private String tanggalPeminjaman;

    @Schema(description = "Waktu mulai peminjaman (format: HH:mm)", example = "10:00")
    private String waktuMulai;

    @Schema(description = "Waktu selesai peminjaman (format: HH:mm)", example = "12:00")
    private String waktuSelesai;

    @Schema(description = "Status peminjaman", example = "MENUNGGU")
    private String status;

    public GetSinglePeminjamanResponse() {
    }

    public GetSinglePeminjamanResponse(int peminjamanId, String namaPeminjam, String emailPeminjam, 
                                       String keperluan, String tipeRuangan, String namaRuangan,
                                       String kapasitas, String tanggalPeminjaman, String waktuMulai, 
                                       String waktuSelesai, String status) {
        this.peminjamanId = peminjamanId;
        this.namaPeminjam = namaPeminjam;
        this.emailPeminjam = emailPeminjam;
        this.keperluan = keperluan;
        this.tipeRuangan = tipeRuangan;
        this.namaRuangan = namaRuangan;
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
    public String getNamaPeminjam() {
        return namaPeminjam;
    }
    public void setNamaPeminjam(String namaPeminjam) {
        this.namaPeminjam = namaPeminjam;
    }
    public String getEmailPeminjam() {
        return emailPeminjam;
    }
    public void setEmailPeminjam(String emailPeminjam) {
        this.emailPeminjam = emailPeminjam;
    }
    public String getKeperluan() {
        return keperluan;
    }
    public void setKeperluan(String keperluan) {
        this.keperluan = keperluan;
    }
    public String getTipeRuangan() {
        return tipeRuangan;
    }
    public void setTipeRuangan(String tipeRuangan) {
        this.tipeRuangan = tipeRuangan;
    }
    public String getNamaRuangan() {
        return namaRuangan;
    }
    public void setNamaRuangan(String namaRuangan) {
        this.namaRuangan = namaRuangan;
    }
    public int getKapasitas() {
        return kapasitas;
    }
    public void setKapasitas(int kapasitas) {
        this.kapasitas = kapasitas;
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
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
