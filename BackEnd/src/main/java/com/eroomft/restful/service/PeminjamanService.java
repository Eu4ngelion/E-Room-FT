package com.eroomft.restful.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.eroomft.restful.dto.ResponseWrapper;
import com.eroomft.restful.dto.data.peminjaman.CreatePeminjamanRequest;
import com.eroomft.restful.dto.data.peminjaman.GetAllPeminjamanResponse;
import com.eroomft.restful.dto.data.peminjaman.GetSinglePeminjamanResponse;
import com.eroomft.restful.model.Akun;
import com.eroomft.restful.model.Peminjaman;
import com.eroomft.restful.model.Ruangan;
import com.eroomft.restful.repository.AkunRepository;
import com.eroomft.restful.repository.PeminjamanRepository;
import com.eroomft.restful.repository.RuanganRepository;


@Service
public class PeminjamanService {

    @Autowired
    private PeminjamanRepository peminjamanRepo;

    @Autowired
    private AkunRepository akunRepo;

    @Autowired
    private RuanganRepository ruanganRepo;

    // Create Peminjaman Ruangan
    public ResponseWrapper createPeminjaman(CreatePeminjamanRequest request){
        try {

        // Validasi Input
        if (request.getAkunId() == null || 
            request.getNamaRuangan() == null || 
            request.getTanggalPeminjaman() == null || 
            request.getWaktuMulai() == null || 
            request.getWaktuSelesai() == null) 
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Semua field harus diisi");
        }

        // Validasi Akun ID
        Optional<Akun> akunOpt = akunRepo.findById(request.getAkunId());
        if (akunOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Akun tidak ditemukan");
        }

        // Validasi dan simpan ruangan 
        Optional<Ruangan> ruanganOpt = ruanganRepo.findByNama(request.getNamaRuangan());
        if (ruanganOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ruangan tidak ditemukan");
        }

        // Validasi Tanggal
        LocalDate tanggalPeminjaman;
        try {
            tanggalPeminjaman = LocalDate.parse(request.getTanggalPeminjaman());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Format tanggal tidak valid");
        }
        if (tanggalPeminjaman.isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tanggal peminjaman tidak boleh sebelum hari ini");
        }

        // Validasi Waktu
        LocalTime waktuMulai;
        LocalTime waktuSelesai;
        try {
            waktuMulai = LocalTime.parse(request.getWaktuMulai());
            waktuSelesai = LocalTime.parse(request.getWaktuSelesai());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Format waktu tidak valid");
        }
        if (waktuMulai.isAfter(waktuSelesai)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Waktu mulai tidak boleh setelah waktu selesai");
        }

        // Cek Ketersediaan Ruangan
        Ruangan ruangan = ruanganOpt.get();
        if (peminjamanRepo.findRuanganSedangDipinjam(
                ruangan, 
                tanggalPeminjaman, 
                waktuMulai, 
                waktuSelesai, 
                Peminjaman.Status.DITOLAK, 
                Peminjaman.Status.SELESAI,
                Peminjaman.Status.MENUNGGU
            ).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ruangan " + ruangan.getNama() + " sudah dipinjam dari " + waktuMulai + " sampai " + waktuSelesai + " pada tanggal " + tanggalPeminjaman);
        }

        // Buat Peminjaman Baru
        Peminjaman peminjaman = new Peminjaman();
        peminjaman.setAkun(akunOpt.get());
        peminjaman.setRuangan(ruangan);
        peminjaman.setKeperluan(request.getKeperluan());
        peminjaman.setTanggalPeminjaman(tanggalPeminjaman);
        peminjaman.setWaktuMulai(waktuMulai);
        peminjaman.setWaktuSelesai(waktuSelesai);
        peminjaman.setStatus(Peminjaman.Status.MENUNGGU); // Status awal
        peminjamanRepo.save(peminjaman);

        // Kembalikan Response
        return new ResponseWrapper("success", "Peminjaman berhasil dibuat", null);

        } catch (ResponseStatusException e) {
            throw e; 
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Terjadi kesalahan pada server: " + e.getMessage(), e);
        }
    }

    // Get All Peminjaman For Admin
    public ResponseWrapper getAllPeminjaman(String status) {
        List<Peminjaman> peminjamanList;
        try {
            // Validasi Status
            if (status == null || status.isEmpty()) {
                peminjamanList = peminjamanRepo.findAll();
            } else {
                try {
                    // Menggunakan enum untuk status
                    Peminjaman.Status statusEnum = Peminjaman.Status.valueOf(status.toUpperCase());
                    peminjamanList = peminjamanRepo.findByStatus(statusEnum);
                } catch (IllegalArgumentException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status peminjaman tidak valid");
                }
            }

            // Cek apakah ada peminjaman
            if (peminjamanList.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tidak ada peminjaman ditemukan");
            }

            // Simpan data peminjaman ke dalam ResponseWrapper
            List<GetAllPeminjamanResponse> responseList = peminjamanList.stream()
                .map(p -> new GetAllPeminjamanResponse(
                    p.getPeminjamanId(),
                    p.getAkun().getNama(),
                    p.getAkun().getAkunId(),
                    p.getRuangan().getTipe().toString(),
                    p.getRuangan().getNama(),
                    p.getTanggalPeminjaman().toString(),
                    p.getWaktuMulai().toString(),
                    p.getWaktuSelesai().toString(),
                    p.getStatus().toString()
                )).toList();

            return new ResponseWrapper("success", "Daftar peminjaman berhasil diambil", responseList);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Terjadi kesalahan pada server: " + e.getMessage(), e);
        }
    }

    // Get Detail Peminjaman By PinjamanId
    public ResponseWrapper getPeminjamanByPeminjamanId(int peminjamanId) {
        try {

            // Validasi Peminjaman ID
            if (peminjamanId <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID peminjaman tidak valid");
            }

            Optional<Peminjaman> peminjamanOpt = peminjamanRepo.findById(peminjamanId);
            if (peminjamanOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Peminjaman tidak ditemukan");
            }
            Peminjaman peminjaman = peminjamanOpt.get();
            GetSinglePeminjamanResponse response = new GetSinglePeminjamanResponse();
            response.setPeminjamanId(peminjaman.getPeminjamanId());
            response.setNamaPeminjam(peminjaman.getAkun().getNama());
            response.setEmailPeminjam(peminjaman.getAkun().getEmail());
            response.setKeperluan(peminjaman.getKeperluan());
            response.setTipeRuangan(peminjaman.getRuangan().getTipe().toString());
            response.setNamaRuangan(peminjaman.getRuangan().getNama());
            response.setKapasitas(peminjaman.getRuangan().getKapasitas());
            response.setTanggalPeminjaman(peminjaman.getTanggalPeminjaman().toString());
            response.setWaktuMulai(peminjaman.getWaktuMulai().toString());
            response.setWaktuSelesai(peminjaman.getWaktuSelesai().toString());
            response.setStatus(peminjaman.getStatus().toString());

            // Kembalikan Response
            return new ResponseWrapper("success", "Detail peminjaman berhasil diambil", response);

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Terjadi kesalahan pada server: " + e.getMessage(), e);
        }
    }

    // Patch Setujui atau Tolak Peminjaman
    public ResponseWrapper updatePeminjamanStatus(int peminjamanId, boolean isSetuju) {
        try {
            // Validasi Peminjaman ID
            if (peminjamanId <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID peminjaman tidak valid");
            }

            Optional<Peminjaman> peminjamanOpt = peminjamanRepo.findById(peminjamanId);
            if (peminjamanOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Peminjaman tidak ditemukan");
            }
            Peminjaman peminjaman = peminjamanOpt.get();

            // Set status sesuai dengan parameter
            if (isSetuju) {
                peminjaman.setStatus(Peminjaman.Status.BERHASIL);
            } else {
                peminjaman.setStatus(Peminjaman.Status.DITOLAK);
            }
            peminjamanRepo.save(peminjaman);

            // Kembalikan Response
            return new ResponseWrapper("success", "Peminjaman berhasil " + (isSetuju ? "disetujui" : "ditolak"), null);

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Terjadi kesalahan pada server: " + e.getMessage(), e);
        }
    }
}
