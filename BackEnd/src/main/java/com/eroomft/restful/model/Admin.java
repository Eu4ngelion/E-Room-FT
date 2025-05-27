package com.eroomft.restful.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin")
public class Admin extends Akun {

    public Admin() {
        super();
        this.setRole(Role.ADMIN);
    }

    public Admin(String akunId, String email, String password, String nama) {
        super();
        this.setAkunId(akunId);
        this.setEmail(email);
        this.setPassword(password);
        this.setNama(nama);
        this.setRole(Role.ADMIN);
    }
}
