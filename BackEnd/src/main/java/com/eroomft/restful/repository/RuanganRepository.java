package com.eroomft.restful.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eroomft.restful.model.Ruangan;

@Repository
public interface RuanganRepository extends JpaRepository<Ruangan, String> {

    boolean existsByNama(String nama);
}
