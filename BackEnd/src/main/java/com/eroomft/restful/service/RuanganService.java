package com.eroomft.restful.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.eroomft.restful.dto.ResponseWrapper;
import com.eroomft.restful.dto.data.ruangan.CreateRuanganRequest;
import com.eroomft.restful.model.Ruangan;
import com.eroomft.restful.repository.RuanganRepository;

@Service
public class RuanganService {

    @Autowired
    private RuanganRepository ruanganRepository;

    // Create Ruangan
    public ResponseWrapper createRuangan(CreateRuanganRequest request) {

        // Validasi Input
        if (request.getNama() == null || 
        request.getKapasitas() <= 0 ||
        request.getFasilitas() == null ||
        request.getGedung() == null ||
        request.getLokasi() == null ||
        request.getPathGambar() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request tidak valid: Semua field harus diisi");
        }

        // Cek apakah tipe ruangan valid
        try {
            Ruangan.Tipe.valueOf(request.getTipe());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipe ruangan '" + request.getTipe() + "' tidak valid");
        }

        // Cek apakah nama ruangan unique
        if (ruanganRepository.existsByNama(request.getNama())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ruangan dengan nama '" + request.getNama() + "' sudah ada");
        }

        // Buat Ruangan Baru
        Ruangan ruangan = new Ruangan();
        ruangan.setTipe(Ruangan.Tipe.valueOf(request.getTipe()));
        ruangan.setNama(request.getNama());
        ruangan.setKapasitas(request.getKapasitas());
        ruangan.setFasilitas(request.getFasilitas());
        ruangan.setGedung(request.getGedung());
        ruangan.setLokasi(request.getLokasi());
        ruangan.setPathGambar(request.getPathGambar());
        ruanganRepository.save(ruangan);
        
        return new ResponseWrapper("success", "Ruangan berhasil dibuat", null);
    }
    
    
}
