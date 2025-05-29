package com.eroomft.restful.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eroomft.restful.model.Akun;
import com.eroomft.restful.model.Peminjaman;
import com.eroomft.restful.model.Ruangan;

@Repository
public interface PeminjamanRepository extends JpaRepository<Peminjaman, Integer> {

    // Method  find all peminjaman by akunId
    List<Peminjaman> findByAkun(Akun akun);

    // Method  find peminjaman by status
    List<Peminjaman> findByStatus(Peminjaman.Status status);

    // Method find all peminjaman by ruangan
    List<Peminjaman> findByRuangan(Ruangan ruangan);

    

}
