package com.eroomft.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eroomft.restful.service.RuanganService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import com.eroomft.restful.dto.ResponseWrapper;
import com.eroomft.restful.dto.data.ruangan.CreateRuanganRequest;

import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("api/v1/ruangan")
public class RuanganController {

    @Autowired
    private RuanganService ruanganService;

    // Create Ruangan
    @PostMapping
    @Operation(summary = "Create Ruangan", description = "Endpoint untuk membuat ruangan baru")
    public ResponseEntity<ResponseWrapper> createRuangan(@RequestBody CreateRuanganRequest request) {
        try {
            return ResponseEntity.ok(ruanganService.createRuangan(request));
            
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(new ResponseWrapper("error", e.getReason(), null));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", e.getMessage(), null));
        }
    }
    
    
}
