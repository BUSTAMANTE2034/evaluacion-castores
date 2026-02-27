package com.castores.backend.repository;

import com.castores.backend.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Integer> {

    // Historial completo de un producto
    List<Movimiento> findByProducto_IdProducto(Integer idProducto);

    // Filtrar historial por tipo: ENTRADA o SALIDA
    List<Movimiento> findByTipoMovimiento(Movimiento.TipoMovimiento tipo);

    // Filtrar por tipo Y producto
    List<Movimiento> findByTipoMovimientoAndProducto_IdProducto(
        Movimiento.TipoMovimiento tipo,
        Integer idProducto
    );

    // Historial de un usuario específico
    List<Movimiento> findByUsuario_IdUsuario(Integer idUsuario);

    Page<Movimiento> findByTipoMovimiento(Movimiento.TipoMovimiento tipo, Pageable pageable);

}
