package com.eroomft.restful.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "akun")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Akun {

    @Id
    @Column(length = 50)
    private String akunId;
    @Column(unique=true, nullable = false, length = 50)
    private String email;
    private String password;
    @Column(nullable = false, length = 50)
    private String nama;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        ADMIN, MAHASISWA, DOSEN
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
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
}
