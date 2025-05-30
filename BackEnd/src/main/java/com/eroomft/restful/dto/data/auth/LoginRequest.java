package com.eroomft.restful.dto.data.auth;

import io.swagger.v3.oas.annotations.media.Schema;

public class LoginRequest {
    @Schema(example = "2309106001")
    private String akunId;
    @Schema(example = "Password123")
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
