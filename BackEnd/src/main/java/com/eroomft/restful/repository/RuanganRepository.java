package com.eroomft.restful.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eroomft.restful.model.Ruangan;

@Repository
public interface RuanganRepository extends JpaRepository<Ruangan, Integer> {

    boolean existsByNama(String nama);

    // find all by nama containing ignore case
    List<Ruangan> findByNamaContainingIgnoreCase(String keyword);

    // get all distinct gedung
    @Query("SELECT DISTINCT r.gedung FROM Ruangan r")
    List<String> getAllDistinctGedung();
}
