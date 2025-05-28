package com.eroomft.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.eroomft.restful.dto.ResponseWrapper;
import com.eroomft.restful.dto.data.akun.CreateAkunRequest;
import com.eroomft.restful.service.AkunService;

import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@RequestMapping("api/v1/akun")
@Tag(name = "Akun-Controller", description = "Ini cuma buat Development, dihapus pas production")
public class AkunController {

    @Autowired
    private AkunService akunService;

    @PostMapping
    public ResponseEntity<ResponseWrapper> createAkun(@RequestBody CreateAkunRequest request) {
        try {
            ResponseWrapper response = akunService.createAkun(request);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(new ResponseWrapper("error", e.getReason() + e.getMessage(), null));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", "Internal Server Error", null));
        }
    }
    
    @GetMapping
    public ResponseEntity<ResponseWrapper> getAllAkun() {
        try {
            ResponseWrapper response = akunService.getAllAkun();
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(new ResponseWrapper("error", e.getReason() + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", "Internal Server Error", null));
        }
    }

    @PutMapping("/{akunId}")
    public ResponseEntity<ResponseWrapper> updateAkun(@PathVariable("akunId") String akunId, @RequestBody CreateAkunRequest request) {
        try {
            ResponseWrapper response = akunService.updateAkun(akunId, request);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(new ResponseWrapper("error", e.getReason() + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", "Internal Server Error", null));
        }
    }

    @DeleteMapping("/{akunId}")
    public ResponseEntity<ResponseWrapper> deleteAkun(@PathVariable("akunId") String akunId) {
        try {
            ResponseWrapper response = akunService.deleteAkun(akunId);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(new ResponseWrapper("error", e.getReason() + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", "Internal Server Error", null));
        }
    }
    
    
}
