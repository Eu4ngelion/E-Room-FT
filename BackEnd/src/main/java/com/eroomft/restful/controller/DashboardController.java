package com.eroomft.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.eroomft.restful.dto.ResponseWrapper;
import com.eroomft.restful.service.DashboardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("api/v1/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;
    
    // Endpoint untuk mendapatkan data dashboard
    @GetMapping
    @Operation(summary = "Get Dashboard Data", description = "Mengambil data dashboard yang berisi jumlah ruangan, peminjaman hari ini, dan peminjaman menunggu persetujuan.")
    @ApiResponse(responseCode = "200", description = "Data dashboard berhasil diambil", content = @Content(schema = @Schema(implementation = GetDashboardSuccessSchema.class)))
    @ApiResponse(responseCode = "400", description = "Request tidak valid")
    @ApiResponse(responseCode = "500", description = "Terjadi kesalahan pada server")
    public ResponseWrapper getDashboardData() {
        try {
            return dashboardService.getDashboardData();
        } catch (ResponseStatusException e) {
            return new ResponseWrapper("error", e.getReason() + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseWrapper("error", "Internal Server Error: " + e.getMessage(), null);
        }
    }


    
    // SCHEMA
    @Schema(example = 
        "{\n" +
        "    \"status\": \"success\",\n" +
        "    \"message\": \"Data Dasbor berhasil diambil\",\n" +
        "    \"data\": {\n" +
        "        \"jumlahRuangan\": 99,\n" +
        "        \"jumlahPeminjamanHariIni\": 50,\n" +
        "        \"jumlahPeminjamanMenunggu\": 15\n" +
        "    }\n" +
        "}")
    public static class GetDashboardSuccessSchema extends ResponseWrapper {
        public GetDashboardSuccessSchema() {
            super("success", "Dashboard data retrieved successfully", null);
        }
    }
}
