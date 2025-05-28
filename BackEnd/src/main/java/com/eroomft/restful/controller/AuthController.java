package com.eroomft.restful.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

import com.eroomft.restful.dto.ResponseWrapper;
import com.eroomft.restful.dto.data.login.loginRequest;
import com.eroomft.restful.service.AuthService;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseWrapper> login(@RequestBody loginRequest request) {
        try {
            ResponseWrapper loginResult = authService.login(request);
            return ResponseEntity.ok(loginResult);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(new ResponseWrapper("error", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper("error", e.getMessage(), null));
        }
    }

}