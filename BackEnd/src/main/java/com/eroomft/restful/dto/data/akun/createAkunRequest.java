package com.eroomft.restful.dto.data.akun;

public class CreateAkunRequest {
    private String akunId;
    private String password;
    private String role;
    private String nama;
    private String email;

    public CreateAkunRequest() {
    }

    public CreateAkunRequest(String akunId, String password, String role, String nama, String email) {
        this.akunId = akunId;
        this.password = password;
        this.role = role;
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
