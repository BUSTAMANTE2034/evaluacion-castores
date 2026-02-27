package com.castores.backend.repository;

import com.castores.backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // Inventario: ver solo activos o solo inactivos
    // SELECT * FROM producto WHERE estatus = ?
    List<Producto> findByEstatus(Boolean estatus);
    Page<Producto> findByEstatus(Boolean status,Pageable pageable);
    // Salida de productos: solo se pueden ver activos
}
