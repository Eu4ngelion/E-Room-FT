package com.eroomft.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eroomft.restful.dto.ResponseWrapper;
import com.eroomft.restful.model.Akun;

import com.eroomft.restful.service.AkunService;

@RestController
@RequestMapping("api/v1/akun")
public abstract class AkunController {

    @Autowired
    private AkunService akunService;

    @PostMapping
    public ResponseEntity<ResponseWrapper> createAkun(@RequestBody Akun akun) {
        try {
            ResponseWrapper response = akunService.createAkun(akun);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWrapper("error", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", "Internal Server Error", null));
        }
    }
    
}
