package com.castores.backend.repository;

import com.castores.backend.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository para Rol.
 * Métodos gratis de JpaRepository<Rol, Integer>:
 *   - save(rol)         - INSERT / UPDATE
 *   - findById(id)      - SELECT WHERE idRol = ?
 *   - findAll()         - SELECT * FROM rol
 *   - delete(rol)       - DELETE (no lo usaremos, usamos estatus)
 *   - existsById(id)    - SELECT EXISTS WHERE idRol = ?
 */
@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

    // SELECT * FROM rol WHERE nombre = ?
    Optional<Rol> findByNombre(String nombre);

}
