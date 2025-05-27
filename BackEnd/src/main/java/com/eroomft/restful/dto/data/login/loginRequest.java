package com.eroomft.restful.dto.data.login;

public class loginRequest {
    private String akunId;
    private String password;
    private String role;


    public loginRequest() {
    }

    public loginRequest(String akunId, String password, String role) {
        this.akunId = akunId;
        this.password = password;
        this.role = role;
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

}
