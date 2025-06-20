package com.eroomft.restful.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.eroomft.restful.dto.ResponseWrapper;
import com.eroomft.restful.dto.data.auth.LoginRequest;
import com.eroomft.restful.dto.data.auth.LoginResponse;
import com.eroomft.restful.model.Akun;
import com.eroomft.restful.repository.AkunRepository;


@Service
public class AuthService {
    
    @Autowired
    private AkunRepository akunRepo;

    // Service Autentikasi

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
    public ResponseWrapper login(LoginRequest request) {
        try{

            // Validasi Request
            if (request.getAkunId() == null || request.getAkunId().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Akun ID tidak boleh kosong");
            } else if (request.getPassword() == null || request.getPassword().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password tidak boleh kosong");
            }

            // Autentikasi Akun
            Optional<Akun> akunOpt = akunRepo.findById(request.getAkunId());
            if (akunOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Akun tidak ditemukan");
            }  

            Akun akun = akunOpt.get();
            String password = akun.getPassword();
            if (!(password.equals(hashPassword(request.getPassword())))) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password salah");
            }

            // Tipe Response Berdasarkan Role
            return new ResponseWrapper("success","Login Berhasil", 
                new LoginResponse(akun.getAkunId(), akun.getEmail(), akun.getNama(), akun.getRole().name()));

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Terjadi kesalahan pada server");
        }
    

    }
}