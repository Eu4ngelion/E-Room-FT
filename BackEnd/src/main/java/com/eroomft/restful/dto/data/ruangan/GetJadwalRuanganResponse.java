package com.eroomft.restful.dto.data.ruangan;

import io.swagger.v3.oas.annotations.media.Schema;

public class GetJadwalRuanganResponse {
    @Schema (description = "Jam mulai peminjaman ruangan", example = "08:00")
    private String jamMulai;
    @Schema (description = "Jam selesai peminjaman ruangan", example = "10:00")
    private String jamSelesai;
    @Schema (description = "Status peminjaman ruangan", example = "DIPINJAM")
    private String Status;

    public GetJadwalRuanganResponse(){
    }

    public GetJadwalRuanganResponse(String jamMulai, String jamSelesai, String status) {
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        Status = status;
    }

    public String getJamMulai() {
        return jamMulai;
    }
    public void setJamMulai(String jamMulai) {
        this.jamMulai = jamMulai;
    }
    public String getJamSelesai() {
        return jamSelesai;
    }
    public void setJamSelesai(String jamSelesai) {
        this.jamSelesai = jamSelesai;
    }
    public String getStatus() {
        return Status;
    }
    public void setStatus(String status) {
        Status = status;
    }
}
