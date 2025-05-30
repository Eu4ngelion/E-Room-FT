package com.eroomft.restful.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.eroomft.restful.dto.ResponseWrapper;
import com.eroomft.restful.dto.data.akun.CreateAkunRequest;
import com.eroomft.restful.model.Admin;
import com.eroomft.restful.model.Akun;
import com.eroomft.restful.model.User;
import com.eroomft.restful.repository.AdminRepository;
import com.eroomft.restful.repository.AkunRepository;
import com.eroomft.restful.repository.UserRepository;

@Service
public class AkunService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AkunRepository akunRepository;
    

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
        // Ambil Semua Akun
        // Iterable<Admin> admins = adminRepository.findAll();
        // Iterable<User> users = userRepository.findAll();
        Iterable<Akun> akun = akunRepository.findAll();
        if (!akun.iterator().hasNext()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tidak ada akun ditemukan");
        }

        // Gabungkan Hasil
        return new ResponseWrapper("success", "Daftar semua akun", new Object[]{akun});
    }

    // Update Akun (Dev Only)
    public ResponseWrapper updateAkun(String akunId, CreateAkunRequest request) {
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
    }

    // Delete Akun (Dev Only)
    public ResponseWrapper deleteAkun(String akunId) {
        // Cari Akun Berdasarkan ID
        if (adminRepository.existsById(akunId)) {
            adminRepository.deleteById(akunId);
            return new ResponseWrapper("success", "Akun Admin berhasil dihapus", null);
        } else if (userRepository.existsById(akunId)) {
            userRepository.deleteById(akunId);
            return new ResponseWrapper("success", "Akun berhasil dihapus", null);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Akun tidak ditemukan");
        }
    }

}
