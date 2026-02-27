package com.castores.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * DTO para el login.
 * El frontend manda esto - POST /api/auth/login
 * @NotBlank - no puede ser null ni vacío
 * @Email    - valida formato de correo
 */
@Data
public class LoginRequest {

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Formato de correo inválido")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contrasena;
}
