package com.castores.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Lo que devuelve el servidor al hacer login exitoso.
 * El frontend guarda el token y lo manda en cada request:
 *   Header: Authorization: Bearer <token>
 */
@Data
@Builder
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String nombre;
    private String correo;
    private String rol;           // "Administrador" o "ALMACENISTA"
    private Boolean estatus;
}
