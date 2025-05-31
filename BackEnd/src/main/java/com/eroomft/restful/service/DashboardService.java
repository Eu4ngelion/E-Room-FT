package com.eroomft.restful.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eroomft.restful.dto.ResponseWrapper;
import com.eroomft.restful.dto.data.dashboard.GetDashboardResponse;
import com.eroomft.restful.model.Peminjaman;
import com.eroomft.restful.repository.PeminjamanRepository;
import com.eroomft.restful.repository.RuanganRepository;

@Service
public class DashboardService {

    @Autowired
    private RuanganRepository ruanganRepo;

    @Autowired
    private PeminjamanRepository peminjamanRepo;

    public ResponseWrapper getDashboardData(){
        long jumlahRuangan = ruanganRepo.count();
        int jumlahPeminjamanHariIni = peminjamanRepo.countPeminjamanToday();
        int jumlahPeminjamanMenunggu = peminjamanRepo.countPeminjamanMenunggu(Peminjaman.Status.MENUNGGU);

        // kembalikan data dalam ResponseWrapper
        ResponseWrapper response = new ResponseWrapper();
        response.setStatus("success");
        response.setMessage("Data Dasbor berhasil diambil");
        response.setData(new GetDashboardResponse(jumlahRuangan,jumlahPeminjamanHariIni,jumlahPeminjamanMenunggu));
        return response;
    }
    
}
