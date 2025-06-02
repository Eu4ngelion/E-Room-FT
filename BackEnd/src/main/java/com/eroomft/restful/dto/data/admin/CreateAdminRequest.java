package com.eroomft.restful.dto.data.admin;

import io.swagger.v3.oas.annotations.media.Schema;

public class CreateAdminRequest {
    @Schema(description = "Id akun yang akan dibuat (NIP)", example = "11111")
    private String akunId;

    @Schema(description = "Password untuk akun admin", example = "Admin123")
    private String password;

    @Schema(description = "Nama lengkap admin", example = "Admin Adminan")
    private String nama;

    @Schema(description = "Email admin", example = "admin@gmail.com")
    private String email;

    public CreateAdminRequest() {
    }

    public CreateAdminRequest(String akunId, String password, String role, String nama, String email) {
        this.akunId = akunId;
        this.password = password;
        this.nama = nama;
        this.email = email;
    }

    public String getAkunId() {
        return akunId;
    }
    public void setAkunId(String akunId) {
        this.akunId = akunId;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

}
