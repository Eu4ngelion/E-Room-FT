package com.eroomft.restful.dto.data.peminjaman;

import io.swagger.v3.oas.annotations.media.Schema;

public class GetAllPeminjamanResponse {
    @Schema(description = "ID peminjaman", example = "999")
    private int idPeminjaman;

    @Schema(description = "Nama akun yang melakukan peminjaman", example = "Contoh Contohan")
    private String namaAkun;
    
    @Schema(description = "Akun Id yang melakukan peminjaman", example = "2309106999")
    private String akunId;

    @Schema(description = "Tipe ruangan yang dipinjam", example = "KELAS")
    private String tipeRuangan;

    @Schema(description = "Nama ruangan yang dipinjam", example = "A999")
    private String namaRuangan;

    @Schema(description = "Tanggal peminjaman (format: YYYY-MM-DD)", example = "2025-06-30")
    private String tanggalPeminjaman;

    @Schema(description = "Waktu mulai peminjaman (format: HH:mm)", example = "10:00")
    private String waktuMulai;

    @Schema(description = "Waktu selesai peminjaman (format: HH:mm)", example = "12:00")
    private String waktuSelesai;

    @Schema(description = "Status peminjaman", example = "MENUNGGU")
    private String status;

    public GetAllPeminjamanResponse() {
    }

    public GetAllPeminjamanResponse(int idPeminjaman, String namaAkun, String akunId, String tipeRuangan, 
                                    String namaRuangan, String tanggalPeminjaman, String waktuMulai, 
                                    String waktuSelesai, String status) {
        this.idPeminjaman = idPeminjaman;
        this.namaAkun = namaAkun;
        this.akunId = akunId;
        this.tipeRuangan = tipeRuangan;
        this.namaRuangan = namaRuangan;
        this.tanggalPeminjaman = tanggalPeminjaman;
        this.waktuMulai = waktuMulai;
        this.waktuSelesai = waktuSelesai;
        this.status = status;
    }

    public int getIdPeminjaman() {
        return idPeminjaman;
    }
    public void setIdPeminjaman(int idPeminjaman) {
        this.idPeminjaman = idPeminjaman;
    }
    public String getNamaAkun() {
        return namaAkun;
    }
    public void setNamaAkun(String namaAkun) {
        this.namaAkun = namaAkun;
    }
    public String getAkunId() {
        return akunId;
    }
    public void setAkunId(String akunId) {
        this.akunId = akunId;
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
