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
import com.eroomft.restful.dto.data.auth.LoginResponse;
import com.eroomft.restful.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Success Schema for Login Response
    class LoginResponseWrapper {
        private final String status;
        private final String message;
        private final LoginResponse data;

        public LoginResponseWrapper(String status, String message, LoginResponse data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public LoginResponse getData() {
            return data;
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Endpoint untuk login ke sistem")
    @ApiResponse(responseCode = "200", description = "Login berhasil", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = LoginResponseWrapper.class)))
    @ApiResponse(responseCode = "400", description = "Invalid Request -Request tidak valid")
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