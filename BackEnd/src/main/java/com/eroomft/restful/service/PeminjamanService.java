package com.eroomft.restful.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.eroomft.restful.dto.ResponseWrapper;
import com.eroomft.restful.dto.data.peminjaman.CreatePeminjamanRequest;
import com.eroomft.restful.dto.data.peminjaman.GetAllPeminjamanResponse;
import com.eroomft.restful.dto.data.peminjaman.GetSinglePeminjamanResponse;
import com.eroomft.restful.model.Akun;
import com.eroomft.restful.model.LogPeminjaman;
import com.eroomft.restful.model.Peminjaman;
import com.eroomft.restful.model.Ruangan;
import com.eroomft.restful.repository.AkunRepository;
import com.eroomft.restful.repository.LogPeminjamanRepository;
import com.eroomft.restful.repository.PeminjamanRepository;
import com.eroomft.restful.repository.RuanganRepository;


@Service
public class PeminjamanService {

    @Autowired
    private PeminjamanRepository peminjamanRepo;

    @Autowired
    private LogPeminjamanRepository logPeminjamanRepo;

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
        if (tanggalPeminjaman.isEqual(LocalDate.now()) && waktuMulai.isBefore(LocalTime.now(ZoneId.of("Asia/Makassar")))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Waktu mulai tidak boleh sebelum waktu sekarang");
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
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ruangan " + ruangan.getNama() + " sudah dipinjam");
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
    public ResponseWrapper getAllPeminjaman(String status, String akunId) {
        try {
            List<Peminjaman> peminjamanList;

            // Validasi Status
            if (status != null && !status.isEmpty()) {
                try {
                    Peminjaman.Status.valueOf(status.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status peminjaman tidak valid");
                }
            }

            // Validate akunId
            Akun akun = null;
            if (akunId != null && !akunId.isEmpty()) {
                Optional<Akun> akunOpt = akunRepo.findById(akunId);
                if (akunOpt.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Akun tidak ditemukan");
                }
                akun = akunOpt.get();
            }

            // Ambil Data Sesuai Filter
            if (status != null && !status.isEmpty() && akun != null) { // Filter status dan akun
                peminjamanList = peminjamanRepo.findByStatusAndAkun(Peminjaman.Status.valueOf(status.toUpperCase()), akun);
            } else if (status != null && !status.isEmpty()) { // Filter status only
                peminjamanList = peminjamanRepo.findByStatus(Peminjaman.Status.valueOf(status.toUpperCase()));
            } else if (akun != null) {
                peminjamanList = peminjamanRepo.findByAkun(akun); // Filter akun only
            } else {
                peminjamanList = peminjamanRepo.findAll(); // No filter, get All
            }

            if (peminjamanList.isEmpty()) {
                return new ResponseWrapper("success", "Tidak ada peminjaman ditemukan", null);
            }

            // Simpan Data ke ResponseWrapper
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

    // Get ALl Riwayat Peminjaman
    public ResponseWrapper getAllRiwayatPeminjaman(String akunId){
        try {
            List<LogPeminjaman> LogPeminjamanList; 
            if (akunId == null || akunId.isEmpty()){
                LogPeminjamanList = logPeminjamanRepo.findAll();
            } else {
                LogPeminjamanList = logPeminjamanRepo.findByAkunIdAndIsDeletedFalse(akunId);
            }

            // Cek apakah ada riwayat peminjaman
            if (LogPeminjamanList.isEmpty()) {
                if (akunId == null || akunId.isEmpty()) {
                    return new ResponseWrapper("success", "Riwayat Peminjaman Kosong", null);
                } else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tidak ditemukan riwayat peminjaman untuk akun ID: " + akunId);
                }
            }

            // Simpan data riwayat peminjaman ke dalam ResponseWrapper
            return new ResponseWrapper("success", "Daftar riwayat peminjaman berhasil diambil", LogPeminjamanList);

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
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID peminjaman: " + peminjamanId + " tidak valid");
            }

            Optional<Peminjaman> peminjamanOpt = peminjamanRepo.findById(peminjamanId);
            if (peminjamanOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Peminjaman tidak ditemukan");
            }
            Peminjaman peminjaman = peminjamanOpt.get();

            // Set status sesuai dengan parameter
            if (isSetuju) {
                // Ubah Status Menjadi Berhasil
                peminjaman.setStatus(Peminjaman.Status.DIIZINKAN);
                peminjamanRepo.save(peminjaman);

                LocalDate tanggalPeminjaman = peminjaman.getTanggalPeminjaman();
                LocalTime waktuMulai = peminjaman.getWaktuMulai();
                LocalTime waktuSelesai = peminjaman.getWaktuSelesai();
                // Hapus pengajuan menunggu lain pada tanggal dan waktu sama dengan peminjaman ini
                List<Peminjaman> peminjamanMenunggu = peminjamanRepo.findByStatusAndTanggalPeminjamanAndWaktuMulai(
                    Peminjaman.Status.MENUNGGU, 
                    tanggalPeminjaman, 
                    waktuMulai,
                    waktuSelesai
                );
                // Hapus Setiap Pengajuan Yang Konflik dengan yang disetujui
                for (Peminjaman p : peminjamanMenunggu) {
                    // Tolak Peminjaman (Hapus)
                    peminjamanRepo.delete(p);
                    // Simpan Log Peminjaman DITOLAK
                    LogPeminjaman logPeminjaman = new LogPeminjaman(
                        p.getAkun().getAkunId(), 
                        p.getAkun().getNama(),
                        LogPeminjaman.Tipe.valueOf(p.getRuangan().getTipe().name()),
                        p.getRuangan().getGedung(),
                        p.getRuangan().getNama(), 
                        p.getKeperluan(), 
                        p.getTanggalPeminjaman(), 
                        p.getWaktuMulai(), 
                        p.getWaktuSelesai(), 
                        LogPeminjaman.Status.DITOLAK, 
                        false
                    );
                    logPeminjamanRepo.save(logPeminjaman);
                }
                return new ResponseWrapper("success", "Peminjaman berhasil disetujui", null);
            } else {
                //  Ubah Status Menjadi Ditolak
                LogPeminjaman logPeminjaman = new LogPeminjaman(
                    peminjaman.getAkun().getAkunId(), 
                    peminjaman.getAkun().getNama(),
                    LogPeminjaman.Tipe.valueOf(peminjaman.getRuangan().getTipe().name()),
                    peminjaman.getRuangan().getGedung(),
                    peminjaman.getRuangan().getNama(), 
                    peminjaman.getKeperluan(), 
                    peminjaman.getTanggalPeminjaman(), 
                    peminjaman.getWaktuMulai(), 
                    peminjaman.getWaktuSelesai(), 
                    LogPeminjaman.Status.DITOLAK, 
                    false
                );

                // Simpan Log Peminjaman
                logPeminjamanRepo.save(logPeminjaman);

                // Hapus dari peminjaman aktif
                peminjamanRepo.delete(peminjaman);
                return new ResponseWrapper("success", "Peminjaman berhasil ditolak dan dicatat dalam log", null);
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Terjadi kesalahan pada server: " + e.getMessage(), e);
        }
    }

    // Batalkan Peminjaman (DIIZINKAN -> DIBATALKAN)
    public ResponseWrapper batalkanPeminjaman(int peminjamanId) {
        try {
            // Validasi Peminjaman ID
            if (peminjamanId <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID peminjaman(" + peminjamanId + ") tidak valid");
            }

            // Cari Peminjaman Berdasarkan Id
            Optional<Peminjaman> peminjamanOpt = peminjamanRepo.findById(peminjamanId);
            if (peminjamanOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Peminjaman dengan ID" + peminjamanId + " tidak ditemukan");
            }
            Peminjaman peminjaman = peminjamanOpt.get();

            // Cek status
            if (!(peminjaman.getStatus() == Peminjaman.Status.DIIZINKAN || peminjaman.getStatus() == Peminjaman.Status.MENUNGGU)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hanya peminjaman dengan status DIIZINKAN atau MENUNGGU yang dapat dibatalkan");
            }
            // Proses Pembatalan Peminjaman
            switch (peminjaman.getStatus()) {
                case MENUNGGU: {
                    peminjamanRepo.delete(peminjaman);
                    return new ResponseWrapper("success", "Peminjaman berhasil dibatalkan", null);
                }
                case DIIZINKAN: {
                    // Log Peminjaman Status DIBATALKAN
                    LogPeminjaman logPeminjamanDibatalkan = new LogPeminjaman(
                    peminjaman.getAkun().getAkunId(),
                    peminjaman.getAkun().getNama(),
                    LogPeminjaman.Tipe.valueOf(peminjaman.getRuangan().getTipe().name()),
                    peminjaman.getRuangan().getGedung(),
                    peminjaman.getRuangan().getNama(),
                    peminjaman.getKeperluan(),
                    peminjaman.getTanggalPeminjaman(),
                    peminjaman.getWaktuMulai(),
                    peminjaman.getWaktuSelesai(),
                    LogPeminjaman.Status.DIBATALKAN,
                    false
                    );
                    
                    logPeminjamanRepo.save(logPeminjamanDibatalkan);
                    peminjamanRepo.delete(peminjaman);
                    return new ResponseWrapper("success", "Peminjaman berhasil dibatalkan dan dicatat dalam log", null);

                }
                default: throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Peminjaman gagal dibatalkan karena status(" + peminjaman.getStatus() + ") tidak valid");
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Terjadi kesalahan pada server: " + e.getMessage(), e);
        }
    }





    // Interval 24 Jam WITA
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Makassar")


    // interval 1 Jam WITA
    // @Scheduled(cron = "0 0 * * * *", zone = "Asia/Makassar")


    // interval 15 Menit WITA
    // @Scheduled(cron = "0 0/15 * * * *", zone = "Asia/Makassar")

    // Interval 10 detik WITA
    // @Scheduled(cron = "0/10 * * * * *", zone = "Asia/Makassar")
    
    // Update Peminjaman -> LogPeminjaman Realtime2
    public void updatePeminjamanStatusScheduled() {
        try {
            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now();

            List<Peminjaman> peminjamanList = peminjamanRepo.findByTanggalPeminjamanAndWaktuSelesai(today, now);
            for (Peminjaman peminjaman : peminjamanList) {
                // Pilih Status Baru
                Peminjaman.Status newStatus;
                switch (peminjaman.getStatus()) {
                    case MENUNGGU:
                        newStatus = Peminjaman.Status.DITOLAK;
                        break;
                    case DIIZINKAN:
                        newStatus = Peminjaman.Status.SELESAI;
                        break;
                    default:
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status peminjaman tidak valid untuk update");
                }

                // Tambahkan Log Peminjaman Baru sesuai dengan status baru
                LogPeminjaman logPeminjaman = new LogPeminjaman();
                logPeminjaman.setAkunId(peminjaman.getAkun().getAkunId());
                logPeminjaman.setNamaPeminjam(peminjaman.getAkun().getNama());
                logPeminjaman.setTipeRuangan(LogPeminjaman.Tipe.valueOf(peminjaman.getRuangan().getTipe().name()));
                logPeminjaman.setGedung(peminjaman.getRuangan().getGedung());
                logPeminjaman.setNamaRuangan(peminjaman.getRuangan().getNama());
                logPeminjaman.setKeperluan(peminjaman.getKeperluan());
                logPeminjaman.setTanggalPeminjaman(peminjaman.getTanggalPeminjaman());
                logPeminjaman.setWaktuMulai(peminjaman.getWaktuMulai());
                logPeminjaman.setWaktuSelesai(peminjaman.getWaktuSelesai());
                logPeminjaman.setStatus(LogPeminjaman.Status.valueOf(newStatus.name()));
                logPeminjaman.setIsDeleted(false);
            
                logPeminjamanRepo.save(logPeminjaman);

                // Hapus Peminjaman Lama
                peminjamanRepo.delete(peminjaman);
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Terjadi kesalahan pada server saat memperbarui status peminjaman: " + e.getMessage(), e);
        }
    }


}
