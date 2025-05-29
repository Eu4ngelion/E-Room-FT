package com.eroomft.restful.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eroomft.restful.model.Akun;

@Repository
public interface AkunRepository extends JpaRepository<Akun, String> {
    

}