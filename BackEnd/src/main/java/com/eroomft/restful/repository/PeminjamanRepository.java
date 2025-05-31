package com.eroomft.restful.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // Cek apakah ruangaan tersedia pada tanggal dan waktu tertentu
    @Query("""
        SELECT p
        FROM Peminjaman p
        WHERE
            p.ruangan = :ruangan
            AND p.tanggalPeminjaman = :tanggalPeminjaman
            AND p.status NOT IN (:statusDitolak, :statusSelesai, :statusMenunggu)
            AND (p.waktuMulai < :waktuSelesai AND p.waktuSelesai > :waktuMulai)
        """)
    Optional<Peminjaman> findRuanganSedangDipinjam(
        @Param("ruangan") Ruangan ruangan,
        @Param("tanggalPeminjaman") LocalDate tanggalPeminjaman,
        @Param("waktuMulai") LocalTime waktuMulai,
        @Param("waktuSelesai") LocalTime waktuSelesai,
        @Param("statusDitolak") Peminjaman.Status statusDitolak,
        @Param("statusSelesai") Peminjaman.Status statusSelesai,
        @Param("statusMenunggu") Peminjaman.Status statusMenunggu
    );


    // Get All Peminjaman status menunggu
    @Query("SELECT p FROM Peminjaman p WHERE p.status = :statusMenunggu")
    List<Peminjaman> findAllPeminjamanStatusMenunggu(@Param("statusMenunggu") Peminjaman.Status statusMenunggu);

    

}
