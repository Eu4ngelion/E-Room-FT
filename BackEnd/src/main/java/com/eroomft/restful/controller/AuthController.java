package com.eroomft.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.eroomft.restful.dto.ResponseWrapper;
import com.eroomft.restful.dto.data.auth.LoginRequest;
import com.eroomft.restful.service.AuthService;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseWrapper> login(@RequestBody LoginRequest request) {
        try {
            ResponseWrapper loginResult = authService.login(request);
            return ResponseEntity.ok(loginResult);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(new ResponseWrapper("error", e.getMessage(), null));

        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == org.springframework.http.HttpStatus.UNAUTHORIZED) {
                return ResponseEntity.status(401).body(new ResponseWrapper("error", e.getReason(), null));
            } else if (e.getStatusCode() == org.springframework.http.HttpStatus.NOT_FOUND) {
                return ResponseEntity.status(404).body(new ResponseWrapper("error", e.getReason(), null));
            } else {
                return ResponseEntity.status(500).body(new ResponseWrapper("error", "Internal Server Error", null));
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", e.getMessage(), null));
        }
    }

}