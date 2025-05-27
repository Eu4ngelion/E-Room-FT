package com.eroomft.restful.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eroomft.restful.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {

}