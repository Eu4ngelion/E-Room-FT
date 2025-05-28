package com.eroomft.restful.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.eroomft.restful.dto.ResponseWrapper;
import com.eroomft.restful.dto.data.akun.CreateAkunRequest;
import com.eroomft.restful.model.Admin;
import com.eroomft.restful.model.Akun;
import com.eroomft.restful.repository.AdminRepository;
import com.eroomft.restful.repository.UserRepository;
import com.eroomft.restful.model.User;

@Service
public class AkunService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    // Buat Akun
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


}
