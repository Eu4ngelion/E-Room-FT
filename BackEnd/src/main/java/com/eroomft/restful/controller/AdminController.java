package com.eroomft.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eroomft.restful.dto.ResponseWrapper;
import com.eroomft.restful.dto.data.admin.CreateAdminRequest;
import com.eroomft.restful.service.AdminService;


@RestController
@RequestMapping("api/v1/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping
    public ResponseEntity<ResponseWrapper> createAdmin(@RequestBody CreateAdminRequest request) {
        try {
            ResponseWrapper response = adminService.createAdmin(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(new ResponseWrapper("test", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", "Internal Server Error", null));
        }
    }
}
