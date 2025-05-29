package com.eroomft.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.eroomft.restful.dto.ResponseWrapper;
import com.eroomft.restful.dto.data.ruangan.CreateRuanganRequest;
import com.eroomft.restful.service.RuanganService;

import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("api/v1/ruangan")
public class RuanganController {

    @Autowired
    private RuanganService ruanganService;

    // Create Ruangan
    @PostMapping
    @Operation(summary = "Create Ruangan", description = "Endpoint untuk membuat ruangan baru")
    public ResponseEntity<ResponseWrapper> createRuangan(@RequestBody CreateRuanganRequest request) {
        try {
            return ResponseEntity.ok(ruanganService.createRuangan(request));
            
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(new ResponseWrapper("error", e.getReason(), null));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", e.getMessage(), null));
        }
    }

    // Get All Ruangan
    @GetMapping
    @Operation(summary = "Get All Ruangan", description = "Endpoint untuk mendapatkan semua ruangan")
    public ResponseEntity<ResponseWrapper> getAllRuangan(
        @RequestParam(name = "keyword", required = false) String keyword,
        @RequestParam(name = "tipe", required = false) String tipe,
        @RequestParam(name = "gedung", required = false) String gedung
    ) {
        try {
            return ResponseEntity.ok(ruanganService.getAllRuangan(keyword, tipe, gedung));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(new ResponseWrapper("error", e.getReason(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", e.getMessage(), null));
        }
    }
    
    // Get Ruangan By Id
    @GetMapping("/{ruanganId}")
    @Operation(summary = "Get Ruangan By Id", description = "Endpoint untuk mendapatkan ruangan berdasarkan ID")
    public ResponseEntity<ResponseWrapper> getRuanganById(@PathVariable("ruanganId") int ruanganId) {
        try {
            return ResponseEntity.ok(ruanganService.getRuanganById(ruanganId));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(new ResponseWrapper("error", e.getReason(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", e.getMessage(), null));
        }
    }

    // GET discntinct gedung
    @GetMapping("/gedung")
    @Operation(summary = "Get Distinct Gedung", description = "Endpoint untuk mendapatkan daftar gedung yang tersedia")
    public ResponseEntity<ResponseWrapper> getDistinctGedung() {
        try {
            return ResponseEntity.ok(ruanganService.getDistinctGedung());
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(new ResponseWrapper("error", e.getReason(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", e.getMessage(), null));
        }
    }

    // Update Ruangan & Delete Ruangan setelah controller peminjaman selesai
}
