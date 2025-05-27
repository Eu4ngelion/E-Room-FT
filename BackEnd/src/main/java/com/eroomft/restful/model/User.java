package com.eroomft.restful.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User extends Akun {

    public User() {
        super();
    }

    public User(String akunId, String email, String password, String nama, Role role) {
        super();
        this.setAkunId(akunId);
        this.setEmail(email);
        this.setPassword(password);
        this.setNama(nama);
        this.setRole(role);
    }
}
