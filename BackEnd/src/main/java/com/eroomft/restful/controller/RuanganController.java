package com.eroomft.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eroomft.restful.dto.ResponseWrapper;
import com.eroomft.restful.service.RuanganService;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/ruangan")
@Tag(name = "Ruangan-Controller", description = "API untuk manajemen data ruangan di E-Room FT")
public class RuanganController {

    @Autowired
    private RuanganService ruanganService;

    // Create Ruangan Success Wrapper
    @Schema(
        example = "{\n" +
                  "    \"status\": \"success\",\n" +
                  "    \"message\": \"Ruangan berhasil dibuat\",\n" +
                  "    \"data\": {}\n" +
                  "}"
    )
    static class CreateRuanganSuccessWrapper extends ResponseWrapper {
        public CreateRuanganSuccessWrapper(String status, String message, Object data) {
            super(status, message, data);
        }
    }

    @Schema(
        example = "{\n" +
                  "    \"status\": \"success\",\n" +
                  "    \"message\": \"Daftar ruangan berhasil diambil\",\n" +
                  "    \"data\": [\n" +
                  "        {\n" +
                  "            \"ruanganId\": 1,\n" +
                  "            \"nama\": \"D404\",\n" +
                  "            \"tipe\": \"LAB\",\n" +
                  "            \"gedung\": \"Gedung Baru D\",\n" +
                  "            \"kapasitas\": 30,\n" +
                  "            \"fasilitas\": \"AC, Proyektor\"\n" +
                  "        },\n" +
                  "        {\n" +
                  "            \"ruanganId\": 2,\n" +
                  "            \"nama\": \"C201\",\n" +
                  "            \"tipe\": \"KELAS\",\n" +
                  "            \"gedung\": \"Gedung C\",\n" +
                  "            \"kapasitas\": 50,\n" +
                  "            \"fasilitas\": \"AC, Whiteboard\"\n" +
                  "        }\n" +
                  "    ]\n" +
                  "}"
    )
    static class GetAllRuanganSuccessWrapper extends ResponseWrapper {
        public GetAllRuanganSuccessWrapper(String status, String message, Object data) {
            super(status, message, data);
        }
    }

    @Schema(
        example = "{\n" +
                  "    \"status\": \"success\",\n" +
                  "    \"message\": \"Ruangan ditemukan\",\n" +
                  "    \"data\": {\n" +
                  "        \"ruanganId\": 1,\n" +
                  "        \"nama\": \"D404\",\n" +
                  "        \"tipe\": \"LAB\",\n" +
                  "        \"gedung\": \"Gedung Baru D\",\n" +
                  "        \"kapasitas\": 30,\n" +
                  "        \"fasilitas\": \"AC, Proyektor\"\n" +
                  "    }\n" +
                  "}"
    )
    static class GetRuanganByIdSuccessWrapper extends ResponseWrapper {
        public GetRuanganByIdSuccessWrapper(String status, String message, Object data) {
            super(status, message, data);
        }
    }

    @Schema(
        example = "{\n" +
                  "    \"status\": \"success\",\n" +
                  "    \"message\": \"Daftar gedung berhasil diambil\",\n" +
                  "    \"data\": [\"Gedung A\", \"Gedung B\", \"Gedung C\"]\n" +
                  "}"
    )
    static class GetDistinctGedungSuccessWrapper extends ResponseWrapper {
        public GetDistinctGedungSuccessWrapper(String status, String message, Object data) {
            super(status, message, data);
        }
    }

    @Schema(
        example = "{\n" +
                  "    \"status\": \"success\",\n" +
                  "    \"message\": \"Ruangan berhasil diperbarui\",\n" +
                  "    \"data\": null\n" +
                  "}"
    )
    static class UpdateRuanganByIdSuccessWrapper extends ResponseWrapper {
        public UpdateRuanganByIdSuccessWrapper(String status, String message, Object data) {
            super(status, message, data);
        }
    }

    @Schema(
        example = "{\n" +
                  "    \"status\": \"success\",\n" +
                  "    \"message\": \"Ruangan berhasil dihapus\",\n" +
                  "    \"data\": null\n" +
                  "}"
    )
    static class DeleteRuanganByIdSuccessWrapper extends ResponseWrapper {
        public DeleteRuanganByIdSuccessWrapper(String status, String message, Object data) {
            super(status, message, data);
        }
    }
}



