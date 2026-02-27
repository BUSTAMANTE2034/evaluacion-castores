package com.castores.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Utilidad para manejar JWT.
 *
 * El flujo es:
 * 1. Usuario hace login - generamos token con generateToken()
 * 2. Usuario manda el token en cada request - validamos con isTokenValid()
 * 3. Extraemos el correo del token - getUsernameFromToken()
 */
@Component
public class JwtUtil {

    // Se inyecta desde application.properties: jwt.secret
    @Value("${jwt.secret}")
    private String secret;

    // Se inyecta desde application.properties: jwt.expiration (86400000 = 24h)
    @Value("${jwt.expiration}")
    private long expiration;

    // Construye la clave de firma a partir del secret
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Genera un token JWT para el usuario.
     * El "subject" del token es el correo del usuario.
     *
     * El token contiene:
     * - sub: correo del usuario
     * - rol: nombre del rol (para autorización)
     * - iat: fecha de creación
     * - exp: fecha de expiración
     */
    public String generateToken(String correo, String rol) {
        return Jwts.builder()
                .subject(correo)
                .claim("rol", rol)           // Guardamos el rol en el token
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extrae el correo (subject) del token.
     * Se usa en JwtFilter para saber qué usuario es.
     */
    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Extrae el rol del token.
     * Se usa para verificar permisos en los endpoints.
     */
    public String getRolFromToken(String token) {
        return getClaims(token).get("rol", String.class);
    }

    /**
     * Verifica que el token:
     * 1. Tenga firma válida
     * 2. No haya expirado
     */
    public boolean isTokenValid(String token) {
        try {
            getClaims(token); // Si falla, lanza excepción
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Parsea el token y devuelve todos los claims (datos internos)
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
