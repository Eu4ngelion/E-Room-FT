package com.eroomft.restful.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eroomft.restful.dto.ResponseWrapper;
import com.eroomft.restful.dto.data.admin.CreateAdminRequest;
import com.eroomft.restful.model.Admin;
import com.eroomft.restful.repository.AdminRepository;
import com.eroomft.restful.repository.UserRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    // Buat Akun
    public ResponseWrapper createAdmin(CreateAdminRequest request) {
        try {
            if (request.getAkunId() == null || request.getEmail() == null || request.getPassword() == null || request.getNama() == null) {
                throw new IllegalArgumentException("Akun ID, Email, Password, Nama tidak boleh kosong");
            }
            // Cek apakah akun sudah ada di admin
            if (adminRepository.existsById(request.getAkunId())) {
                throw new IllegalArgumentException("Admin dengan ID " + request.getAkunId() + " sudah ada");
            }

            // Cek apakah akun sudah ada di user
            if (userRepository.existsById(request.getAkunId())) {
                throw new IllegalArgumentException("Akun dengan ID " + request.getAkunId() + " sudah ada di User");
            }
            Admin admin = new Admin();
            admin.setAkunId(request.getAkunId());
            admin.setPassword(request.getPassword());
            admin.setNama(request.getNama());
            admin.setEmail(request.getEmail());
            adminRepository.save(admin);
            return new ResponseWrapper("success", "Akun Admin berhasil dibuat", null);

            } catch (IllegalArgumentException e) {

            return new ResponseWrapper("error", e.getMessage(), null); 

            } catch (Exception e) {
            return new ResponseWrapper("error", "Internal Server Error", null);
        }
    }
    
}
