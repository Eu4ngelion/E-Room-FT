package com.eroomft.restful.dto.data.auth;

public class LoginResponse {
    private String akunId;
    private String email;
    private String nama;
    private String role;

    public LoginResponse() {
    }
    public LoginResponse(String akunId, String email, String nama, String role) {
        this.akunId = akunId;
        this.email = email;
        this.nama = nama;
        this.role = role;
    }
    public String getAkunId() {
        return akunId;
    }
    public void setAkunId(String akunId) {
        this.akunId = akunId;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
