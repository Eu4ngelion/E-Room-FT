package com.eroomft.restful.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import com.eroomft.restful.dto.ResponseWrapper;
import com.eroomft.restful.dto.data.ruangan.CreateRuanganRequest;
import com.eroomft.restful.dto.data.ruangan.GetAllRuanganResponse;
import com.eroomft.restful.model.Peminjaman;
import com.eroomft.restful.model.Ruangan;
import com.eroomft.restful.repository.PeminjamanRepository;
import com.eroomft.restful.repository.RuanganRepository;

@Service
public class RuanganService {

    @Autowired
    private RuanganRepository ruanganRepo;

    @Autowired
    private PeminjamanRepository peminjamanRepo;

    // Create Ruangan
    public ResponseWrapper createRuangan(CreateRuanganRequest request) {

        // Validasi Input
        if (request.getNama() == null || 
            request.getKapasitas() <= 0 ||
            request.getFasilitas() == null ||
            request.getGedung() == null ||
            request.getLokasi() == null ||
            request.getPathGambar() == null) 
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request tidak valid: Semua field harus diisi");
        }

        // Cek apakah tipe ruangan valid
        try {
            Ruangan.Tipe.valueOf(request.getTipe().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipe ruangan '" + request.getTipe() + "' tidak valid");
        }

        // Cek apakah nama ruangan unique
        if (ruanganRepo.existsByNama(request.getNama())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ruangan dengan nama '" + request.getNama() + "' sudah ada");
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
        ruanganRepo.save(ruangan);
        
        return new ResponseWrapper("success", "Ruangan berhasil dibuat", null);
    }
    
    // Get All Ruangan
    public ResponseWrapper getAllRuangan(String keyword, String tipe, String gedung, int kapasitas) {
        try{
            List<Ruangan> ruanganList;
            if (keyword == null || keyword.isEmpty()) {
                // Jika tidak ada keyword, ambil semua ruangan
                ruanganList = ruanganRepo.findAll();
            } else {
                // Cari Ruangan Berdasarkan nama sesuai keyword
                ruanganList = ruanganRepo.findByNamaContainingIgnoreCase(keyword);
            }

            // Filter daftar ruangan berdasarkan tipe
            if (tipe != null && !tipe.isEmpty()) {
                try {
                    Ruangan.Tipe.valueOf(tipe.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipe ruangan '" + tipe + "' tidak valid");
                }
                ruanganList.removeIf(ruangan -> !ruangan.getTipe().name().equalsIgnoreCase(tipe));
            }

            // Filter daftar ruangan berdasarkan gedung
            if (gedung != null && !gedung.isEmpty()) {
                ruanganList.removeIf(ruangan -> !ruangan.getGedung().equalsIgnoreCase(gedung));
            }

            // Jika Ruangan Kosong
            if (ruanganList.isEmpty()) {
                return new ResponseWrapper("success", "Tidak ada ruangan yang ditemukan", null);
            }

            // FIlter daftar ruangan berdasarkan kapasitas
            if (kapasitas > 0) {
                ruanganList.removeIf(ruangan -> ruangan.getKapasitas() < kapasitas);
            }

            // Jika Ruangan kosong setelah difilter
            if (ruanganList.isEmpty()) {
                return new ResponseWrapper("success", "Tidak ada ruangan yang ditemukan", null);
            }

            List<GetAllRuanganResponse> responseList = new ArrayList<>();
            for (Ruangan ruangan : ruanganList) {
                GetAllRuanganResponse response = new GetAllRuanganResponse(
                    ruangan.getRuanganId(),
                    ruangan.getTipe().name(),
                    ruangan.getNama(),
                    ruangan.getKapasitas(),
                    ruangan.getFasilitas(),
                    ruangan.getGedung(),
                    ruangan.getLokasi(),
                    ruangan.getPathGambar()
                );
                responseList.add(response);
            }
            return new ResponseWrapper("success", "Daftar ruangan berhasil diambil", responseList);

        } catch (ResponseStatusException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Gagal mengambil daftar ruangan: " + e.getMessage());
        }

    }

    // Get Ruangan by ID
    public ResponseWrapper  getRuanganById(@PathVariable int ruanganId) {
        try{
            Optional<Ruangan> optionalRuangan = ruanganRepo.findById(ruanganId);
            // Cek apakah ruangan dengan ID tersebut ada
            if (optionalRuangan.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ruangan dengan ID " + ruanganId + " tidak ditemukan");
            }
            
            // Kembalikan detail ruangan sesuai id
            Ruangan ruangan = optionalRuangan.get();
            GetAllRuanganResponse response = new GetAllRuanganResponse(
                ruangan.getRuanganId(),
                ruangan.getTipe().name(),
                ruangan.getNama(),
                ruangan.getKapasitas(),
                ruangan.getFasilitas(),
                ruangan.getGedung(),
                ruangan.getLokasi(),
                ruangan.getPathGambar()
            );
            return new ResponseWrapper("success", "Ruangan ditemukan", response);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Gagal mengambil ruangan: " + e.getMessage());
        }
    }

    // Get Unique Gedung
    public ResponseWrapper getDistinctGedung() {
        try {
            List<String> uniqueGedungList = ruanganRepo.getAllDistinctGedung();
            if (uniqueGedungList.isEmpty()) {
                return new ResponseWrapper("success", "Tidak ada gedung yang ditemukan", null);
            }
            return new ResponseWrapper("success", "Daftar gedung berhasil diambil", uniqueGedungList);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Gagal mengambil daftar gedung: " + e.getMessage());
        }
    }

    // Update Ruangan By Id
    public ResponseWrapper updateRuanganById(int ruanganId, CreateRuanganRequest request) {
        try{
            // Validasi input
            if (request.getTipe() == null || 
                request.getNama() == null || 
                request.getKapasitas() <= 0 ||
                request.getFasilitas() == null ||
                request.getGedung() == null ||
                request.getLokasi() == null ||
                request.getPathGambar() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request tidak valid: Semua field harus diisi");
            }
            // Cek apakah tipe ruangan valid
            try {
                Ruangan.Tipe.valueOf(request.getTipe().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipe ruangan '" + request.getTipe() + "' tidak valid");
            }

            // Cari Ruangan Berdasarkan ID
            Optional<Ruangan> optionalRuangan = ruanganRepo.findById(ruanganId);
            if (optionalRuangan.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ruangan dengan ID " + ruanganId + " tidak ditemukan");
            }

            Ruangan ruangan = optionalRuangan.get();
            // Cek apakah nama ruangan unique
            if (!ruangan.getNama().equals(request.getNama()) && ruanganRepo.existsByNama(request.getNama())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ruangan dengan nama '" + request.getNama() + "' sudah ada");
            }

            // Update Ruangan di database
            ruangan.setTipe(Ruangan.Tipe.valueOf(request.getTipe()));
            ruangan.setNama(request.getNama());
            ruangan.setKapasitas(request.getKapasitas());
            ruangan.setFasilitas(request.getFasilitas());
            ruangan.setGedung(request.getGedung());
            ruangan.setLokasi(request.getLokasi());
            ruangan.setPathGambar(request.getPathGambar());
            ruanganRepo.save(ruangan);

            return new ResponseWrapper("success", "Ruangan berhasil diperbarui", null);

        } catch (ResponseStatusException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Gagal memperbarui ruangan: " + e.getMessage());
        }

    }

    // Delete Ruangan By Id (Auto Delete Peminjaman)
    public ResponseWrapper deleteRuanganById(int ruanganId){
        try {
            // Cek apakah ruangan dengan ID tersebut ada
            Optional<Ruangan> optionalRuangan = ruanganRepo.findById(ruanganId);
            if (optionalRuangan.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ruangan dengan ID " + ruanganId + " tidak ditemukan");
            }
            Ruangan ruangan = optionalRuangan.get();

            // Hapus semua peminjaman yang terkait dengan ruangan ini
            List<Peminjaman> peminjamanList = peminjamanRepo.findByRuangan(ruangan);
            for (Peminjaman peminjaman : peminjamanList) {
                peminjamanRepo.delete(peminjaman);
            }

            // Hapus Ruangan
            ruanganRepo.deleteById(ruanganId);

            return new ResponseWrapper("success", "Ruangan berhasil dihapus", null);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Gagal menghapus ruangan: " + e.getMessage());
        }
    }


}