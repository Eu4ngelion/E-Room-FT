package com.eroomft.restful.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.eroomft.restful.model.LogPeminjaman;

@Repository
public interface LogPeminjamanRepository extends JpaRepository<LogPeminjaman, Integer> {

    // delete by ruanganid
    void deleteByRuanganId(int ruanganId);
    
}
