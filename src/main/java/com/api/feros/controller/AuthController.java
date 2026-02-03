package com.api.feros.controller;

import com.api.feros.dto.LoginRequest;
import com.api.feros.dto.LoginResponse;
import com.api.feros.dto.RefreshRequest;
import com.api.feros.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            LoginResponse loginResponse = authService.login(request);
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("data", loginResponse);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refresh(@RequestBody RefreshRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            LoginResponse loginResponse = authService.refresh(request.getRefreshToken());
            response.put("success", true);
            response.put("message", "Token refreshed successfully");
            response.put("data", loginResponse);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Refresh failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
