package com.eroomft.restful.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eroomft.restful.model.LogPeminjaman;

@Repository
public interface LogPeminjamanRepository extends JpaRepository<LogPeminjaman, Integer> {
    // Repository untuk LogPeminjaman, bisa ditambahkan method khusus jika diperlukan
    // Misalnya, jika ingin mencari log berdasarkan akunId atau tanggal tertentu

    // Find By AkunId where isDeleted is false
    List<LogPeminjaman> findByAkunIdAndIsDeletedFalse (String akunid);

    // peminjamanRepo.findByStatusAndIsDeletedFalse(Peminjaman.Status.valueOf(status.toUpperCase())
    List<LogPeminjaman> findByStatusAndIsDeletedFalse(LogPeminjaman.Status status);


    //

}


