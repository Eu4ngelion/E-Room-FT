package com.eroomft.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.eroomft.restful.dto.ResponseWrapper;
import com.eroomft.restful.dto.data.ruangan.CreateRuanganRequest;
import com.eroomft.restful.service.RuanganService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("api/v1/ruangan")
@Tag(name = "Ruangan", description = "API untuk manajemen data ruangan di E-Room FT")
public class RuanganController {

    @Autowired
    private RuanganService ruanganService;


    // Create Ruangan
    @PostMapping
    @Operation(
        summary = "Create Ruangan",
        description = "Endpoint untuk membuat ruangan baru. Semua field wajib diisi."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ruangan berhasil dibuat"),
        @ApiResponse(responseCode = "400", description = "Request tidak valid"),
        @ApiResponse(responseCode = "500", description = "Terjadi kesalahan pada server")
    })
    public ResponseEntity<ResponseWrapper> createRuangan(
        @RequestBody CreateRuanganRequest request
    ) {
        try {
            return ResponseEntity.ok(ruanganService.createRuangan(request));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(new ResponseWrapper("error", e.getReason(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", e.getMessage(), null));
        }
    }


    // GET All Ruangan
    @GetMapping
    @Operation(
        summary = "Get All Ruangan",
        description = "Endpoint untuk mendapatkan semua ruangan. Bisa menggunakan filter keyword, tipe, dan gedung."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Daftar ruangan berhasil diambil"),
        @ApiResponse(responseCode = "500", description = "Terjadi kesalahan pada server")
    })
    public ResponseEntity<ResponseWrapper> getAllRuangan(
        @Parameter(description = "Keyword pencarian nama ruangan", example = "D404")
        @RequestParam(name = "keyword", required = false) String keyword,
        @Parameter(description = "Tipe ruangan (LAB, KELAS, SEMINAR, RAPAT)", example = "LAB")
        @RequestParam(name = "tipe", required = false) String tipe,
        @Parameter(description = "Nama gedung", example = "Gedung Baru D")
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
    @Operation(
        summary = "Get Ruangan By Id",
        description = "Endpoint untuk mendapatkan detail ruangan berdasarkan ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ruangan ditemukan"),
        @ApiResponse(responseCode = "404", description = "Ruangan tidak ditemukan"),
        @ApiResponse(responseCode = "500", description = "Terjadi kesalahan pada server")
    })
    public ResponseEntity<ResponseWrapper> getRuanganById(
        @Parameter(description = "ID ruangan", example = "1")
        @PathVariable("ruanganId") int ruanganId
    ) {
        try {
            return ResponseEntity.ok(ruanganService.getRuanganById(ruanganId));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(new ResponseWrapper("error", e.getReason(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", e.getMessage(), null));
        }
    }


    // Get Distinct Gedung
    @GetMapping("/gedung")
    @Operation(
        summary = "Get Distinct Gedung",
        description = "Endpoint untuk mendapatkan daftar nama gedung yang tersedia."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Daftar gedung berhasil diambil"),
        @ApiResponse(responseCode = "500", description = "Terjadi kesalahan pada server")
    })
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


    // Update Ruangan by ID
    @PutMapping("/{ruanganId}")
    @Operation(
        summary = "Update Ruangan By Id",
        description = "Endpoint untuk memperbarui data ruangan berdasarkan ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ruangan berhasil diperbarui"),
        @ApiResponse(responseCode = "400", description = "Request tidak valid"),
        @ApiResponse(responseCode = "404", description = "Ruangan tidak ditemukan"),
        @ApiResponse(responseCode = "500", description = "Terjadi kesalahan pada server")
    })
    public ResponseEntity<ResponseWrapper> updateRuanganById(
        @Parameter(description = "ID ruangan", example = "1")
        @PathVariable("ruanganId") int ruanganId,
        @RequestBody CreateRuanganRequest request
    ) {
        try {
            return ResponseEntity.ok(ruanganService.updateRuanganById(ruanganId, request));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(new ResponseWrapper("error", e.getReason(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", e.getMessage(), null));
        }
    }


    // Delete Ruangan by ID
    @DeleteMapping
    @Operation(
        summary = "Delete Ruangan By Id",
        description = "Endpoint untuk menghapus ruangan berdasarkan ID. Akan menghapus juga semua peminjaman aktif terkait ruangan ini."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ruangan berhasil dihapus"),
        @ApiResponse(responseCode = "404", description = "Ruangan tidak ditemukan"),
        @ApiResponse(responseCode = "500", description = "Terjadi kesalahan pada server")
    })
    public ResponseEntity<ResponseWrapper> deleteRuanganById(
        @Parameter(description = "ID ruangan yang akan dihapus", example = "1")
        @RequestParam("ruanganId") int ruanganId
    ) {
        try {
            return ResponseEntity.ok(ruanganService.deleteRuanganById(ruanganId));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(new ResponseWrapper("error", e.getReason(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", e.getMessage(), null));
        }
    }
}
