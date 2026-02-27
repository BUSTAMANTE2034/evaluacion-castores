package com.castores.backend.service;

import com.castores.backend.dto.request.LoginRequest;
import com.castores.backend.dto.response.AuthResponse;
import com.castores.backend.model.Usuario;
import com.castores.backend.repository.UsuarioRepository;
import com.castores.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * Lógica del login.
 * Flujo:
 * 1. AuthenticationManager verifica correo + contraseña contra la BD
 * 2. Si son correctos - generamos JWT
 * 3. Devolvemos token + datos del usuario
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    public AuthResponse login(LoginRequest request) {
        // 1. Autenticar: lanza BadCredentialsException si son incorrectos
        //    (el GlobalExceptionHandler la captura y devuelve 401)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getCorreo(),
                        request.getContrasena()
                )
        );

        // 2. Si llegamos aquí, las credenciales son correctas - buscar usuario
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow();

        // 3. Generar token con correo y rol
        String token = jwtUtil.generateToken(
                usuario.getCorreo(),
                usuario.getRol().getNombre()
        );

        // 4. Devolver token + datos básicos del usuario
        return AuthResponse.builder()
                .token(token)
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol().getNombre())
                .estatus(usuario.getEstatus())
                .build();
    }
}
