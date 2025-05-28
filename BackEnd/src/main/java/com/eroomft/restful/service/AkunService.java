package com.eroomft.restful.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eroomft.restful.dto.ResponseWrapper;
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
    public ResponseWrapper createAkun(Akun akun) {
        try {
            if (akun.getAkunId() == null || akun.getPassword() == null || akun.getRole() == null) {
                throw new IllegalArgumentException("Akun ID, Password, dan Role harus diisi");
            }
        } catch (IllegalArgumentException e) {
            return new ResponseWrapper("error", e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseWrapper("error", "Internal Server Error", null);
        }
        if (akun.getRole() != null && akun.getRole().name().equalsIgnoreCase("Admin")) {
            Admin admin = new Admin();
            admin.setAkunId(akun.getAkunId());
            admin.setPassword(akun.getPassword());
            adminRepository.save(admin);
            return new ResponseWrapper("success", "Akun Admin berhasil dibuat", null);
        } else if (akun.getRole() != null && (akun.getRole().name().equalsIgnoreCase("Mahasiswa") || akun.getRole().name().equalsIgnoreCase("Dosen"))) {
            User user = new User();
            user.setAkunId(akun.getAkunId());
            user.setPassword(akun.getPassword());
            user.setRole(akun.getRole());
            userRepository.save(user);
            return new ResponseWrapper("success", "Akun " + akun.getRole() + " berhasil dibuat", null);
        } else {
            return new ResponseWrapper("error", "Role tidak dikenali", null);
        }
    }
}
