package com.eroomft.restful.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eroomft.restful.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.eroomft.restful.model.Akun;

@Repository

public interface UserRepository extends JpaRepository<User, String> {
    
    // Login Check
    @Query("SELECT u FROM User u WHERE u.akunId = ?1 AND u.role = ?2")
    Optional<User> findByAkunId(String akunId, Akun.Role role);

}
