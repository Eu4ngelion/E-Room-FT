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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.eroomft.restful.dto.ResponseWrapper;
import com.eroomft.restful.dto.data.akun.CreateAkunRequest;
import com.eroomft.restful.service.AkunService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("api/v1/akun")
@Tag(name = "Akun-Controller", description = "Endpoint untuk manajemen akun (khusus development, hapus saat production)")
public class AkunController {

    @Autowired
    private AkunService akunService;


    // Create Akun
    @PostMapping
    @Operation(summary = "Create Akun", description = "Membuat akun baru. Hanya untuk development.")
    @ApiResponse(responseCode = "200", description = "Akun berhasil dibuat", 
    content = @Content(schema = @Schema(implementation = CreateAkunSuccessSchema.class)))
    @ApiResponse(responseCode = "400", description = "Request tidak valid")
    @ApiResponse(responseCode = "500", description = "Terjadi kesalahan pada server")

    public ResponseEntity<ResponseWrapper> createAkun(@RequestBody CreateAkunRequest request) {
        try {
            ResponseWrapper response = akunService.createAkun(request);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(new ResponseWrapper("error", e.getReason() + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", "Internal Server Error", null));
        }
    }

    // Get Data All Akun
    @GetMapping
    @Operation(summary = "Get All Akun",description = "Mengambil semua akun yang terdaftar. Hanya untuk development.")
    @ApiResponse(responseCode = "200",description = "Daftar akun berhasil diambil",
        content = @Content(schema = @Schema(implementation = GetAllAkunSuccessSchema.class))
    )
    @ApiResponse(responseCode = "404", description = "Tidak ada akun ditemukan")
    @ApiResponse(responseCode = "500", description = "Terjadi kesalahan pada server")
    public ResponseEntity<ResponseWrapper> getAllAkun() {
        try {
            ResponseWrapper response = akunService.getAllAkun();
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(new ResponseWrapper("error", e.getReason() + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", "Internal Server Error", null));
        }
    }


    // Update Akun By Id
    @PutMapping("/{akunId}")
    @Operation(
        summary = "Update Akun",
        description = "Memperbarui data akun berdasarkan ID. Hanya untuk development."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Akun berhasil diperbarui",
        content = @Content(schema = @Schema(implementation = UpdateAkunSuccessSchema.class))
    )
    @ApiResponse(responseCode = "404", description = "Akun tidak ditemukan")
    @ApiResponse(responseCode = "500", description = "Terjadi kesalahan pada server")
    public ResponseEntity<ResponseWrapper> updateAkun(
        @PathVariable("akunId") String akunId,
        @RequestBody CreateAkunRequest request
    ) {
        try {
            ResponseWrapper response = akunService.updateAkun(akunId, request);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(new ResponseWrapper("error", e.getReason() + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", "Internal Server Error", null));
        }
    }


    // Hapus Akun By Id
    @DeleteMapping("/{akunId}")
    @Operation(summary = "Delete Akun",description = "Menghapus akun berdasarkan ID. Hanya untuk development.",parameters = {@Parameter(name = "akunId", description = "ID akun yang akan dihapus", required = true, example = "123")}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Akun berhasil dihapus",
        content = @Content(schema = @Schema(implementation = ResponseWrapper.class))
    )
    @ApiResponse(responseCode = "404", description = "Akun tidak ditemukan")
    @ApiResponse(responseCode = "500", description = "Terjadi kesalahan pada server")
    public ResponseEntity<ResponseWrapper> deleteAkun(@PathVariable("akunId") String akunId) {
        try {
            ResponseWrapper response = akunService.deleteAkun(akunId);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(new ResponseWrapper("error", e.getReason() + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", "Internal Server Error", null));
        }
    }


    //  ## Schema
    @Schema(
        example = "{\n" +
                  "    \"status\": \"success\",\n" +
                  "    \"message\": \"Akun berhasil dibuat\",\n" +
                  "    \"data\": {}\n" +
                  "}"
    )
    class CreateAkunSuccessSchema extends ResponseWrapper {
        public CreateAkunSuccessSchema(String status, String message, Object data) {
            super(status, message, data);
        }
    }

    @Schema(
        example = "{\n" +
                  "    \"status\": \"success\",\n" +
                  "    \"message\": \"Daftar akun berhasil diambil\",\n" +
                  "    \"data\": [\n" +
                  "        {\n" +
                  "            \"akunId\": \"2309106001\",\n" +
                  "            \"nama\": \"Mahasiswa 1\",\n" +
                  "            \"email\": \"mahasiswa1@gmail.com\",\n" +
                  "            \"role\": \"MAHASISWA\"\n" +
                  "        },\n" +
                  "        {\n" +
                  "            \"akunId\": \"2309106002\",\n" +
                  "            \"nama\": \"Dosen 1\",\n" +
                  "            \"email\": \"admin1@gmail.com\",\n" +
                  "            \"role\": \"DOSEN\"\n" +
                  "        },\n" +
                  "        {\n" +
                  "            \"akunId\": \"2309106003\",\n" +
                  "            \"nama\": \"Admin 1\",\n" +
                  "            \"email\": \"Admin@gmail.com\",\n" +
                  "            \"role\": \"ADMIN\"\n" +
                  "        }\n" +
                  "    ]\n" +
                  "}"
    )
    class GetAllAkunSuccessSchema extends ResponseWrapper {
        public GetAllAkunSuccessSchema(String status, String message, Object data) {
            super(status, message, data);
        }
    }

    @Schema(
        example = "{\n" +
                  "    \"status\": \"success\",\n" +
                  "    \"message\": \"Akun berhasil diperbarui\",\n" +
                  "    \"data\": {}\n" +
                  "}"
    )
    class UpdateAkunSuccessSchema extends ResponseWrapper {
        public UpdateAkunSuccessSchema(String status, String message, Object data) {
            super(status, message, data);
        }
    }
}
