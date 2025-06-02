package com.eroomft.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.eroomft.restful.dto.ResponseWrapper;
import com.eroomft.restful.dto.data.peminjaman.CreatePeminjamanRequest;
import com.eroomft.restful.service.PeminjamanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("api/v1/peminjaman")
public class PeminjamanController {

    @Autowired
    private PeminjamanService peminjamanService;
    
    @PostMapping
    @Operation(summary = "Create Peminjaman", description = "Endpoint untuk membuat peminjaman ruangan.")
    @ApiResponse(responseCode = "200", description = "Peminjaman berhasil dibuat", content = @Content(schema = @Schema(implementation = CreatePeminjamanSuccessSchema.class)))
    @ApiResponse(responseCode = "400", description = "Request tidak valid")
    @ApiResponse(responseCode = "404", description = "Akun atau Ruangan tidak ditemukan")
    @ApiResponse(responseCode = "409", description = "Ruangan sudah dipinjam pada waktu yang diminta", content = @Content(schema = @Schema(implementation = RuanganSudahDipinjamSchema.class)))
    @ApiResponse(responseCode = "500", description = "Terjadi kesalahan pada server")
    public ResponseEntity<ResponseWrapper> createPeminjaman(@RequestBody CreatePeminjamanRequest request) {
        try {
            return ResponseEntity.ok(peminjamanService.createPeminjaman(request));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(new ResponseWrapper("error", e.getReason(), null));
        }catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", "Internal Server Error", null));
        }
    }

    @GetMapping
    @Operation(summary = "Get All Peminjaman", description = "Endpoint untuk mengambil semua data peminjaman dengan status menunggu.")
    @ApiResponse(responseCode = "200", description = "Data peminjaman berhasil diambil", content = @Content(schema = @Schema(implementation = GetAllPeminjamanSchema.class)))
    @ApiResponse(responseCode = "500", description = "Terjadi kesalahan pada server")
    public ResponseEntity<ResponseWrapper> getAllPeminjaman(
        @Parameter(description = "Status peminjaman yang ingin diambil. Jika tidak diberikan, akan mengambil semua peminjaman.", example = "MENUNGGU")
        @RequestParam(value = "status", required = false) String status,
        @Parameter(description = "Filter ID akun, Jika kosong, akan mengambil peminjaman dari semua akun.", example = "2309106028")
        @RequestParam(value = "akunId", required = false) String akunId
    ) {
        try {
            return ResponseEntity.ok(peminjamanService.getAllPeminjaman(status, akunId));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(new ResponseWrapper("error", e.getReason(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", "Internal Server Error", null));
        }
    }


    @GetMapping("detail/{peminjamanId}")
    @Operation(summary = "Get Single Peminjaman", description = "Endpoint untuk mengambil detail peminjaman berdasarkan ID peminjaman.")
    @ApiResponse(responseCode = "200", description = "Detail peminjaman berhasil diambil", content = @Content(schema = @Schema(implementation = GetSinglePeminjamanSchema.class)))
    public ResponseEntity<ResponseWrapper> getSinglePeminjaman(
        @Parameter(description = "ID peminjaman yang ingin diambil", example = "1")    
        @PathVariable("peminjamanId") int peminjamanId) {
        try {
            return ResponseEntity.ok(peminjamanService.getPeminjamanByPeminjamanId(peminjamanId));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(new ResponseWrapper("error", e.getReason(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", "Internal Server Error", null));
        }
    }


    @GetMapping("/riwayat")
    @Operation(summary = "Get Riwayat Peminjaman", description = "Endpoint untuk mengambil riwayat peminjaman berdasarkan akun ID.")
    public ResponseEntity<ResponseWrapper> getRiwayatPeminjaman(
        @Parameter(description = "Filter ID akun, Jika kosong, akan mengambil riwayat peminjaman dari semua akun.", example = "2309106028")
        @RequestParam(value= "akunId", required = false) String akunId
    ) {
        try {
            return ResponseEntity.ok(peminjamanService.getAllRiwayatPeminjaman(akunId));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(new ResponseWrapper("error", e.getReason(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", "Internal Server Error", null));
        }
    }


    @PatchMapping("/{peminjamanId}")
    @Operation(summary = "Update Peminjaman", description = "Endpoint untuk memperbarui status peminjaman berdasarkan ID peminjaman.")
    public ResponseEntity<ResponseWrapper> updatePeminjaman(
        @Parameter(description = "ID peminjaman yang ingin diperbarui", example = "1")
        @PathVariable("peminjamanId") Integer peminjamanId, 
        @Parameter(description = "Status peminjaman yang ingin diperbarui", example = "true")
        @RequestParam("isSetuju") Boolean status) {
        try {
            return ResponseEntity.ok(peminjamanService.updatePeminjamanStatus(peminjamanId, status));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(new ResponseWrapper("error", e.getReason(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", "Internal Server Error", null));
        }
    }
    

    @DeleteMapping("/{peminjamanId}")
    @Operation(summary = "Batalkan Peminjaman", description = "Endpoint untuk membatalkan peminjaman aktif berdasarkan ID peminjaman.")
    @ApiResponse(responseCode = "200", description = "Peminjaman berhasil dibatalkan", content = @Content(schema = @Schema(implementation = BatalkanPeminjamanSchema.class)))
    @ApiResponse(responseCode = "404", description = "Peminjaman tidak ditemukan")
    @ApiResponse(responseCode = "500", description = "Terjadi kesalahan pada server")
    public ResponseEntity<ResponseWrapper> batalkanPeminjaman(
        @Parameter(description = "ID peminjaman yang ingin dibatalkan", example = "1")
        @PathVariable("peminjamanId") Integer peminjamanId
    ) {
        try {
            return ResponseEntity.ok(peminjamanService.batalkanPeminjaman(peminjamanId));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(new ResponseWrapper("error", e.getReason(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", "Internal Server Error", null));
        }
    }




    
    // SCHEMA Ruangan Berhasil Dipinjam
    @Schema(example = """
        {
            "status": "success",
            "message": "Peminjaman berhasil dibuat",
            "data": {
                "id": 1,
                "akunId": 123,
                "namaRuangan": "Ruang Rapat A",
                "tanggalPeminjaman": "2023-10-01",
                "waktuMulai": "10:00:00",
                "waktuSelesai": "12:00:00",
                "status": "DIPINJAM"
            }
        }
        """)    
    public static class CreatePeminjamanSuccessSchema extends ResponseWrapper {
        public CreatePeminjamanSuccessSchema(String status, String message, Object data) {
            super(status, message, data);
        }
    }

    // SCHEMA Ruangan Sudah dpinjam
    @Schema(example = """
        {
              "status": "error",
              "message": "Ruangan A202 sudah dipinjam dari 07:00 sampai 08:00 pada tanggal 2025-05-30",
              "data": null
        }
        """)
    public static class RuanganSudahDipinjamSchema extends ResponseWrapper {
        public RuanganSudahDipinjamSchema(String status, String message, Object data) {
            super(status, message, data);
        }
    }

    // SCHEMA Get All Peminjaman Response
    @Schema(example = """
        [
            {
                "idPeminjaman": 1,
                "namaAkun": "John Doe",
                "akunId": "123",
                "tipeRuangan": "Ruang Rapat",
                "namaRuangan": "Ruang Rapat A",
                "tanggalPeminjaman": "2023-10-01",
                "waktuMulai": "10:00",
                "waktuSelesai": "12:00",
                "status": "MENUNGGU"
            },
            {
                "idPeminjaman": 2,
                "namaAkun": "Jane Smith",
                "akunId": "456",
                "tipeRuangan": "Ruang Kelas",
                "namaRuangan": "Ruang Kelas B",
                "tanggalPeminjaman": "2023-10-02",
                "waktuMulai": "14:00",
                "waktuSelesai": "16:00",
                "status": "DITERIMA"
            }
        ]
        """)
    public static class GetAllPeminjamanSchema extends ResponseWrapper {
        public GetAllPeminjamanSchema(String status, String message, Object data) {
            super(status, message, data);
        }
    }


    @Schema(example = """
        {
            "status": "success",
            "message": "Detail peminjaman berhasil diambil",
            "data": {
                "peminjamanId": 2,
                "namaPeminjam": "john doe",
                "emailPeminjam": "email@gmail.com",
                "keperluan": "Kelas Pengganti Matkul OOP",
                "tipeRuangan": "KELAS",
                "namaRuangan": "A202",
                "kapasitas": 1,
                "tanggalPeminjaman": "2025-05-30",
                "waktuMulai": "07:00",
                "waktuSelesai": "08:00",
                "status": "MENUNGGU"
            }
        }
    """)
    public static class GetSinglePeminjamanSchema extends ResponseWrapper {
        public GetSinglePeminjamanSchema(String status, String message, Object data) {
            super(status, message, data);
        }
    }

    @Schema(example = """
        {
            "status": "success",
            "message": "Peminjaman berhasil dibatalkan",
            "data": null
        }
        """)
    public static class BatalkanPeminjamanSchema extends ResponseWrapper {
        public BatalkanPeminjamanSchema(String status, String message, Object data) {
            super(status, message, data);
        }
    }
}
