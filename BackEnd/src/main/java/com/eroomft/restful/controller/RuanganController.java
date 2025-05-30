package com.eroomft.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.eroomft.restful.dto.ResponseWrapper;
import com.eroomft.restful.dto.data.ruangan.CreateRuanganRequest;
import com.eroomft.restful.service.RuanganService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/ruangan")
@Tag(name = "Ruangan-Controller", description = "API untuk manajemen data ruangan di E-Room FT")
public class RuanganController {

    @Autowired
    private RuanganService ruanganService;


    // Create Ruangan
    @PostMapping
    @Operation(
        summary = "Create Ruangan",
        description = "Endpoint untuk membuat ruangan baru. Semua field wajib diisi."
    )
    @ApiResponse(responseCode = "200", description = "Ruangan berhasil dibuat",content = @Content( mediaType = "application/json",schema = @Schema(implementation = CreateRuanganSuccessWrapper.class)))
    @ApiResponse(responseCode = "400", description = "Request tidak valid")
    @ApiResponse(responseCode = "409", description = "Ruangan dengan nama yang sama sudah ada")
    @ApiResponse(responseCode = "500", description = "Terjadi kesalahan pada server")
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
    @Operation(summary = "Get All Ruangan",description = "Endpoint untuk mendapatkan semua ruangan. Bisa menggunakan filter keyword, tipe, dan gedung."
    )
    @ApiResponse(responseCode = "200", description = "Daftar ruangan berhasil diambil", content= @Content(mediaType = "application/json", schema= @Schema(implementation = GetAllRuanganSuccessWrapper.class)))
    @ApiResponse(responseCode = "400", description = "Request tidak valid")
    @ApiResponse(responseCode = "404", description = "Ruangan tidak ditemukan")
    @ApiResponse(responseCode = "500", description = "Terjadi kesalahan pada server")

    public ResponseEntity<ResponseWrapper> getAllRuangan(
        @RequestParam(name = "keyword", required = false) String keyword,
        @RequestParam(name = "tipe", required = false) String tipe,
        @RequestParam(name = "gedung", required = false) String gedung,
        @RequestParam(name = "minKapasitas", required = false) Integer kapasitas
    ) {
        try {
            Integer minKapasitas = kapasitas != null ? kapasitas : 0;
            return ResponseEntity.ok(ruanganService.getAllRuangan(keyword, tipe, gedung, minKapasitas));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(new ResponseWrapper("error", e.getReason(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", e.getMessage(), null));
        }
    }
    
    // Get Ruangan By Id
    @GetMapping("/{ruanganId}")
    @Operation(summary = "Get Ruangan By Id",description = "Endpoint untuk mendapatkan detail ruangan berdasarkan ID.")
    @ApiResponse(responseCode = "200", description = "Ruangan ditemukan", content = @Content(mediaType = "application/json",schema= @Schema(implementation = GetRuanganByIdSuccessWrapper.class)))
    @ApiResponse(responseCode = "400", description = "Request tidak valid")
    @ApiResponse(responseCode = "404", description = "Ruangan tidak ditemukan")

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
    @Operation(summary = "Get Distinct Gedung",description = "Endpoint untuk mendapatkan daftar nama gedung yang tersedia.")
    @ApiResponse(responseCode = "200", description = "Daftar gedung berhasil diambil", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetDistinctGedungSuccessWrapper.class)))
    @ApiResponse(responseCode = "400", description = "Request tidak valid")
    @ApiResponse(responseCode = "404", description = "Gedung tidak ditemukan")
    @ApiResponse(responseCode = "500", description = "Terjadi kesalahan pada server")

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
    @Operation(summary = "Update Ruangan By Id", description = "Endpoint untuk memperbarui data ruangan berdasarkan ID.")
    @ApiResponse(responseCode = "200", description = "Ruangan berhasil diperbarui", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateRuanganByIdSuccessWrapper.class)))
    @ApiResponse(responseCode = "400", description = "Request tidak valid")
    @ApiResponse(responseCode = "404", description = "Ruangan tidak ditemukan")
    @ApiResponse(responseCode = "500", description = "Terjadi kesalahan pada server")

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
    @DeleteMapping("/{ruanganId}")
    @Operation(summary = "Delete Ruangan By Id",description = "Endpoint untuk menghapus ruangan berdasarkan ID. Akan menghapus juga semua peminjaman aktif terkait ruangan ini.")
    @ApiResponse(responseCode = "200", description = "Ruangan berhasil dihapus", content = @Content(mediaType = "application/json",schema = @Schema(implementation = DeleteRuanganByIdSuccessWrapper.class)))
    @ApiResponse(responseCode = "404", description = "Ruangan tidak ditemukan")
    @ApiResponse(responseCode = "500", description = "Terjadi kesalahan pada server")

    public ResponseEntity<ResponseWrapper> deleteRuanganById(
        @Parameter(description = "ID ruangan yang akan dihapus", example = "1")
        @PathVariable("ruanganId") int ruanganId
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


    //### List Schema ###
    // Create Ruangan Success Wrapper
    @Schema(
        example = """
        {
            "status": "success",
            "message": "Ruangan berhasil dibuat",
            "data": {}
        }
        """
    )
    static class CreateRuanganSuccessWrapper extends ResponseWrapper {
        public CreateRuanganSuccessWrapper(String status, String message, Object data) {
            super(status, message, data);
        }
    }



    @Schema(
        example = """
        {
            "status": "success",
            "message": "Daftar ruangan berhasil diambil",
            "data": [
                {
                    "ruanganId": 1,
                    "nama": "D404",
                    "tipe": "LAB",
                    "gedung": "Gedung Baru D",
                    "kapasitas": 30,
                    "fasilitas": "AC, Proyektor"
                },
                {
                    "ruanganId": 2,
                    "nama": "C201",
                    "tipe": "KELAS",
                    "gedung": "Gedung C",
                    "kapasitas": 50,
                    "fasilitas": "AC, Whiteboard"
                }
            ]
        }
        """
    )
    static class GetAllRuanganSuccessWrapper extends ResponseWrapper {
        public GetAllRuanganSuccessWrapper(String status, String message, Object data) {
            super(status, message, data);
        }
    }


    @Schema(
        example = """
        {
            "status": "success",
            "message": "Ruangan ditemukan",
            "data": {
                "ruanganId": 1,
                "nama": "D404",
                "tipe": "LAB",
                "gedung": "Gedung Baru D",
                "kapasitas": 30,
                "fasilitas": "AC, Proyektor"
            }
        }
        """
    )
    static class GetRuanganByIdSuccessWrapper extends ResponseWrapper {
        public GetRuanganByIdSuccessWrapper(String status, String message, Object data) {
            super(status, message, data);
        }
    }

    @Schema(
        example = """
        {
            "status": "success",
            "message": "Daftar gedung berhasil diambil",
            "data": ["Gedung A", "Gedung B", "Gedung C"]
        }
        """
    )
    static class GetDistinctGedungSuccessWrapper extends ResponseWrapper {
        public GetDistinctGedungSuccessWrapper(String status, String message, Object data) {
            super(status, message, data);
        }
    }

    @Schema(
        example = """
        {
            "status": "success",
            "message": "Ruangan berhasil diperbarui",
            "data": null
        }
        """
    )
    static class UpdateRuanganByIdSuccessWrapper extends ResponseWrapper {
        public UpdateRuanganByIdSuccessWrapper(String status, String message, Object data) {
            super(status, message, data);
        }
    }

    @Schema(
        example = """
        {
            "status": "success",
            "message": "Ruangan berhasil dihapus",
            "data": null
        }
        """
    )
    static class DeleteRuanganByIdSuccessWrapper extends ResponseWrapper {
        public DeleteRuanganByIdSuccessWrapper(String status, String message, Object data) {
            super(status, message, data);
        }
    }
}



