package com.eroomft.restful.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.eroomft.restful.dto.ResponseWrapper;
import com.eroomft.restful.dto.data.akun.CreateAkunRequest;
import com.eroomft.restful.model.Admin;
import com.eroomft.restful.model.Akun;
import com.eroomft.restful.model.LogPeminjaman;
import com.eroomft.restful.model.Peminjaman;
import com.eroomft.restful.model.User;
import com.eroomft.restful.repository.AdminRepository;
import com.eroomft.restful.repository.AkunRepository;
import com.eroomft.restful.repository.LogPeminjamanRepository;
import com.eroomft.restful.repository.PeminjamanRepository;
import com.eroomft.restful.repository.UserRepository;

@Service
public class AkunService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AkunRepository akunRepository;

    @Autowired
    private PeminjamanRepository peminjamanRepo;

    @Autowired
    private LogPeminjamanRepository logPeminjamanRepo;
    

    // Buat Akun (Dev Only)
    public ResponseWrapper createAkun(CreateAkunRequest request) {
        // Validasi Input
        if (request.getAkunId() == null || 
            request.getPassword() == null ||
            request.getRole() == null || 
            request.getNama() == null ||
            request.getEmail() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request tidak valid: Semua field harus diisi");
        }

        // Akun Id Unik
        if (adminRepository.existsById(request.getAkunId()) || userRepository.existsById(request.getAkunId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Akun ID sudah digunakan");
        }

        // Email Unik
        if (adminRepository.existsByEmail(request.getEmail()) || userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email sudah digunakan");
        }

        // Buat Akun Berdasarkan Role
        if (request.getRole().equalsIgnoreCase("Admin")) {
            Admin admin = new Admin();
            admin.setAkunId(request.getAkunId());
            admin.setPassword(request.getPassword());
            admin.setEmail(request.getEmail());
            admin.setNama(request.getNama());
            adminRepository.save(admin);
            return new ResponseWrapper("success", "Akun Admin berhasil dibuat", null);

        } else if (request.getRole().equalsIgnoreCase("Mahasiswa") ||
                   request.getRole().equalsIgnoreCase("Dosen")) {
            User user = new User();
            user.setAkunId(request.getAkunId());
            user.setPassword(request.getPassword());
            user.setEmail(request.getEmail());
            user.setNama(request.getNama());
            user.setRole(Akun.Role.valueOf(request.getRole().toUpperCase()));
            userRepository.save(user);
            return new ResponseWrapper("success", "Akun " + request.getRole() + " berhasil dibuat", null);
        } else {
            throw new IllegalArgumentException("Role tidak dikenali");
        }
    }

    // View All Akun (Dev Only)
    public ResponseWrapper getAllAkun() {
        try{
            // Ambil Semua Akun
            Iterable<Akun> akun = akunRepository.findAll();
            if (!akun.iterator().hasNext()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tidak ada akun ditemukan");
            }

            return new ResponseWrapper("success", "Daftar semua akun", akun);

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Terjadi kesalahan saat mengambil akun", e);
        }
        
    }

    // Update Akun (Dev Only)
    public ResponseWrapper updateAkun(String akunId, CreateAkunRequest request) {
        try {
            // Validasi Input
            if (request.getPassword() == null || request.getEmail() == null || request.getNama() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request tidak valid: Semua field harus diisi");
            }

            // Cari Akun Berdasarkan ID
            if (adminRepository.existsById(akunId)) {
                Admin admin = adminRepository.findById(akunId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Akun tidak ditemukan"));
                admin.setPassword(request.getPassword());
                admin.setEmail(request.getEmail());
                admin.setNama(request.getNama());
                adminRepository.save(admin);
                return new ResponseWrapper("success", "Akun berhasil diperbarui", null);
            } else if (userRepository.existsById(akunId)) {
                User user = userRepository.findById(akunId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Akun tidak ditemukan"));
                user.setPassword(request.getPassword());
                user.setEmail(request.getEmail());
                user.setNama(request.getNama());
                userRepository.save(user);

                return new ResponseWrapper("success", "Akun berhasil diperbarui", null);

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Akun tidak ditemukan");
            }
            
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Terjadi kesalahan saat memperbarui akun", e);
        }
    }

    
    // Delete Akun (Dev Only)
    @Transactional
    public ResponseWrapper deleteAkun(String akunId) {
        try {
            // Cari Akun Berdasarkan ID
            if (akunId == null || akunId.trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Akun ID tidak boleh kosong");
            } 
            // Validasi Akun ID
            Akun akun = akunRepository.findById(akunId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Akun tidak ditemukan"));
            
            // Hapus peminjaman aktif terkait dahulu
            Iterable<Peminjaman> peminjamans = peminjamanRepo.findByAkun(akun);

            // Proses Semua Peminjaman Terkait
            for (Peminjaman peminjaman : peminjamans) {
                // Jika menunggu, hapus
                if (peminjaman.getStatus() == Peminjaman.Status.MENUNGGU) {
                    peminjamanRepo.delete(peminjaman);
                    continue;
                }

                //jika status diizinkan, simpan ke log
                if (peminjaman.getStatus() != Peminjaman.Status.SELESAI) {
                    // buat logpeminjaman baru
                    LogPeminjaman logPeminjaman = new LogPeminjaman(
                        akunId,
                        akun.getNama(),
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
                    // simpan log
                    logPeminjamanRepo.save(logPeminjaman);

                    // hapus peminjaman lama
                    peminjamanRepo.delete(peminjaman);
                }
            }
            akunRepository.deleteById(akunId);
            return new ResponseWrapper("success", "Akun berhasil dihapus", null);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Terjadi kesalahan saat menghapus akun", e);
        }
    }
}


