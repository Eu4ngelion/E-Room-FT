package com.eroomft.restful.dto.data.user;

import io.swagger.v3.oas.annotations.media.Schema;

public class CreateUserRequest {
    @Schema(description = "ID akun yang akan dibuat", example = "2309106001")
    private String akunId;

    @Schema(description = "Password untuk akun", example = "Password123")
    private String password;

    @Schema(description = "Role akun", example = "MAHASISWA")
    private String role;

    @Schema(description = "Nama lengkap pengguna", example = "Budi Budian")
    private String nama;

    @Schema(description = "Email pengguna", example = "Budi@gmail.com")
    private String email;

    public CreateUserRequest() {
    }

    public CreateUserRequest(String akunId, String password, String role, String nama, String email) {
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
