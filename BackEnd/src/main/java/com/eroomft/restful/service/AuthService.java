package com.eroomft.restful.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eroomft.restful.repository.AdminRepository;
import com.eroomft.restful.repository.UserRepository;
import com.eroomft.restful.dto.data.login.loginRequest;
import com.eroomft.restful.dto.ResponseWrapper;
import com.eroomft.restful.model.Admin;
import com.eroomft.restful.model.Akun;
import com.eroomft.restful.model.User;


@Service
public class AuthService {
    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private UserRepository userRepo;


    public ResponseWrapper login(loginRequest request) {
        if (request.getRole() == null || request.getRole().isEmpty()) {
            throw new IllegalArgumentException("Mohon menginput role yang valid");
        }

        if (request.getRole().equalsIgnoreCase("ADMIN")) {
            Optional<Admin> adminOpt = adminRepo.findById(request.getAkunId());
            if (adminOpt.isPresent()) {
                Admin admin = adminOpt.get();
                if (admin.getPassword().equals(request.getPassword())) {
                    return new ResponseWrapper("success", "Login Berhasil", "Admin");
                }
            }

        } else if (request.getRole().equalsIgnoreCase("MAHASISWA")) {
            Optional<User> mahasiswaOpt = userRepo.findById(request.getAkunId());
            if (mahasiswaOpt.isPresent()) {
                User mahasiswa = mahasiswaOpt.get();
                if (mahasiswa.getPassword().equals(request.getPassword())) {
                    return new ResponseWrapper("success", "Login Berhasil", "Mahasiswa");
                }
            }

        } else if (request.getRole().equalsIgnoreCase("DOSEN")) {
            Optional<User> dosenOpt = userRepo.findByAkunId(request.getAkunId(), Akun.Role.DOSEN);
            if (dosenOpt.isPresent()) {
                User dosen = dosenOpt.get();
                if (dosen.getPassword().equals(request.getPassword())) {
                    return new ResponseWrapper("success", "Login Berhasil", "Dosen");
                }
            }
        }

        if (request.getRole().equalsIgnoreCase("Admin")) {
            return new ResponseWrapper("error", "NIP atau Password Salah", null);

        } else if (request.getRole().equalsIgnoreCase("Mahasiswa")) {
            return new ResponseWrapper("error", "NIM atau Password Salah", null);

        } else if (request.getRole().equalsIgnoreCase("Dosen")) {
        return new ResponseWrapper("error", "NIP atau Password Salah", null);

        } else {
            return new ResponseWrapper("error", "Role tidak dikenali", null);
        }            
    }

}