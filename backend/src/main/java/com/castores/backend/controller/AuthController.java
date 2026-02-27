package com.castores.backend.controller;

import com.castores.backend.dto.request.LoginRequest;
import com.castores.backend.dto.response.AuthResponse;
import com.castores.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints de autenticación.
 * Ruta pública (sin token) - definida en SecurityConfig.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /auth/login
     * Body: { "correo": "admin@castores.com", "contrasena": "123456" }
     * Response: { "token": "eyJ...", "nombre": "Admin", "rol": "Administrador" }
     *
     * @Valid - activa las validaciones del DTO (@NotBlank, @Email, etc.)
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
