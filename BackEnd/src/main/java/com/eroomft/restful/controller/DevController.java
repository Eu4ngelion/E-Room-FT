package com.eroomft.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.eroomft.restful.dto.ResponseWrapper;
import com.eroomft.restful.service.PeminjamanService;

@RestController
@RequestMapping("api/dev")
public class DevController {

    @Autowired
    private PeminjamanService peminjamanService;

    // Update Status Peminjaman Manual
    @GetMapping("/update-status-peminjaman")
    public ResponseEntity<ResponseWrapper> updateStatusPeminjaman() {
        try {
            // Logic to update status of peminjaman
            peminjamanService.updatePeminjamanStatusScheduled();
            return ResponseEntity.ok(new ResponseWrapper("success", "Status peminjaman berhasil diperbarui", null));
    
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(new ResponseWrapper("error", e.getReason(), null));
        } 
        catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", "Terjadi kesalahan pada server: " + e.getMessage(), null));
        }
    }
}
