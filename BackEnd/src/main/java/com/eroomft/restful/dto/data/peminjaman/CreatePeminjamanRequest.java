package com.eroomft.restful.dto.data.peminjaman;

public class CreatePeminjamanRequest {

    private String akunId;
    private String ruanganId;
    private String keperluan;
    private String tanggalPeminjaman;
    private String waktuMulai;
    private String waktuSelesai;

    public CreatePeminjamanRequest() {
    }

    public CreatePeminjamanRequest(String akunId, String ruanganId, String keperluan, String tanggalPeminjaman, String waktuMulai, String waktuSelesai) {
        this.akunId = akunId;
        this.ruanganId = ruanganId;
        this.keperluan = keperluan;
        this.tanggalPeminjaman = tanggalPeminjaman;
        this.waktuMulai = waktuMulai;
        this.waktuSelesai = waktuSelesai;
    }
    
}
