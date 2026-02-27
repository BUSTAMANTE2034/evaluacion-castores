package com.castores.backend.security;

import com.castores.backend.model.Usuario;
import com.castores.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Spring Security necesita saber cómo cargar un usuario desde la BD.
 * Esta clase le dice: "busca el usuario por correo en la tabla usuario".
 *
 * Cuando alguien hace login, Spring Security llama a loadUserByUsername()
 * y luego compara la contraseña que ingresaron con el hash en BD.
 *
 * El "username" en Spring Security = correo en nuestro sistema.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        // Busca el usuario en BD por correo
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con correo: " + correo));

        // Verifica que el usuario esté activo
        if (!usuario.getEstatus()) {
            throw new UsernameNotFoundException("Usuario inactivo: " + correo);
        }

        /**
         * Convertimos nuestro Usuario a un UserDetails de Spring Security.
         * - username - correo
         * - password - contrasena (ya es BCrypt hash)
         * - authorities - rol con prefijo ROLE_ (convención de Spring Security)
         *   Ej: "Administrador" - "ROLE_Administrador"
         *   Esto permite usar @PreAuthorize("hasRole('Administrador')")
         */
        return new org.springframework.security.core.userdetails.User(
                usuario.getCorreo(),
                usuario.getContrasena(),
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre()))
        );
    }
}
