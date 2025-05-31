package com.eroomft.restful.dto.data.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;

public class GetDashboardResponse {

    @Schema(description = "Jumlah total ruangan di database", example = "999")
    private Long jumlahRuangan;

    @Schema(description = "Jumlah peminjaman hari ini", example = "99")
    private int jumlahPeminjamanHariIni;

    @Schema(description = "Jumlah peminjaman yang masih menunggu persetujuan", example = "89")
    private int jumlahPeminjamanMenunggu;

    public GetDashboardResponse(long jumlahRuangan, int jumlahPeminjamanHariIni, int jumlahPeminjamanMenunggu) {
        this.jumlahRuangan = jumlahRuangan;
        this.jumlahPeminjamanHariIni = jumlahPeminjamanHariIni;
        this.jumlahPeminjamanMenunggu = jumlahPeminjamanMenunggu;
    }

    public Long getJumlahRuangan() {
        return jumlahRuangan;
    }
    public void setJumlahRuangan(Long jumlahRuangan) {
        this.jumlahRuangan = jumlahRuangan;
    }

    public int getJumlahPeminjamanHariIni() {
        return jumlahPeminjamanHariIni;
    }
    public void setJumlahPeminjamanHariIni(int jumlahPeminjamanHariIni) {
        this.jumlahPeminjamanHariIni = jumlahPeminjamanHariIni;
    }

    public int getJumlahPeminjamanMenunggu() {
        return jumlahPeminjamanMenunggu;
    }
    public void setJumlahPeminjamanMenunggu(int jumlahPeminjamanMenunggu) {
        this.jumlahPeminjamanMenunggu = jumlahPeminjamanMenunggu;
    }
}