package com.castores.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * ESTE ES TU MIDDLEWARE / INTERCEPTOR.
 * Equivalente al @login_required que mencionaste.
 *
 * Se ejecuta en CADA request antes de llegar al Controller.
 * Su trabajo:
 * 1. Leer el header Authorization: Bearer <token>
 * 2. Validar el token
 * 3. Si es válido - autenticar al usuario en Spring Security
 * 4. Si no hay token o es inválido - dejar pasar (Spring Security
 * rechazará el request si la ruta requiere autenticación)
 *
 * OncePerRequestFilter garantiza que este filtro se ejecuta solo UNA VEZ por
 * request.
 */
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. Leer el header Authorization
        String authHeader = request.getHeader("Authorization");

        // Si no hay header o no empieza con "Bearer ", ignorar y continuar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extraer el token (quitar el prefijo "Bearer ")
        String token = authHeader.substring(7);

        // 3. Validar el token
        if (!jwtUtil.isTokenValid(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("""
                        {
                            "status": 401,
                            "error": "Token inválido o expirado"
                        }
                    """);
            return;
        }

        // 4. Extraer el correo del token
        String correo = jwtUtil.getUsernameFromToken(token);

        // 5. Solo procesar si no hay ya una autenticación activa
        if (correo != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Cargar usuario desde BD
            UserDetails userDetails = userDetailsService.loadUserByUsername(correo);

            // Crear objeto de autenticación con los roles del usuario
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities() // ← aquí van los roles
            );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 6. Registrar al usuario como autenticado en el contexto de Spring Security
            // A partir de aquí, cualquier Controller puede leer quién es con
            // @AuthenticationPrincipal
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // Continuar al siguiente filtro o al Controller
        filterChain.doFilter(request, response);
    }
}
