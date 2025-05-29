package com.eroomft.restful.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eroomft.restful.dto.ResponseWrapper;
import com.eroomft.restful.repository.PeminjamanRepository;

@Service
public class PeminjamanService {

    @Autowired
    private PeminjamanRepository peminjamanRepository;

    // public ResponseWrapper createPeminjaman(CreatePeminjamanRequest request) {
    //     // Validasi Input
    //     if (request.getAkunId() == null || 
    //         request.getRuanganId() == null || 
    //         request.getTanggalMulai() == null || 
    //         request.getTanggalSelesai() == null) {
    //         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request tidak valid: Semua field harus diisi");
    //     }

    //     // Cek apakah ruangan tersedia
    //     Ruangan ruangan = ruanganRepository.findById(request.getRuanganId())
    //         .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ruangan tidak ditemukan"));

    //     if (!ruangan.isTersedia(request.getTanggalMulai(), request.getTanggalSelesai())) {
    //         throw new ResponseStatusException(HttpStatus.CONFLICT, "Ruangan tidak tersedia pada tanggal tersebut");
    //     }

    //     // Buat Peminjaman Baru
    //     Peminjaman peminjaman = new Peminjaman();
    //     peminjaman.setAkun(akunRepository.findById(request.get
    // }
}
