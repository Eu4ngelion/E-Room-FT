package com.eroomft.restful.dto.data.auth;

public class LoginRequest {
    private String akunId;
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String akunId, String password) {
        this.akunId = akunId;
        this.password = password;
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
}
