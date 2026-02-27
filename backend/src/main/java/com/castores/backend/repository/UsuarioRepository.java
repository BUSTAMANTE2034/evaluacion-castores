package com.castores.backend.repository;

import com.castores.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // LOGIN  busca usuario por correo
    // SELECT * FROM usuario WHERE correo = ?
    Optional<Usuario> findByCorreo(String correo);

    // Verificar si ya existe ese correo antes de registrar
    // SELECT EXISTS(SELECT 1 FROM usuario WHERE correo = ?)
    boolean existsByCorreo(String correo);
    

    // Listar usuarios por estatus (activos/inactivos)
    // SELECT * FROM usuario WHERE estatus = ?
    List<Usuario> findByEstatus(Boolean estatus);
}
