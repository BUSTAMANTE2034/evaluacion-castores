package com.castores.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Representa la tabla `movimiento`.
 *
 * Registra cada ENTRADA o SALIDA de productos del inventario.
 * Es inmutable: una vez creado un movimiento NO se edita ni se borra.
 * Auditoría: guarda qué usuario hizo el movimiento.
 *
 * El ENUM de MySQL (ENTRADA/SALIDA) se mapea con @Enumerated(EnumType.STRING).
 */
@Entity
@Table(name = "movimiento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMovimiento")
    private Integer idMovimiento;

    /**
     * Mapea el ENUM de MySQL: ENUM('ENTRADA','SALIDA')
     * EnumType.STRING guarda el texto "ENTRADA" o "SALIDA" en BD.
     * (EnumType.ORDINAL guardaría 0 o 1 — nunca uses eso, es frágil)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false, length = 10)
    private TipoMovimiento tipoMovimiento;

    // Siempre mayor a 0. La validación se hace en el Service.
    @Column(nullable = false)
    private Integer cantidad;

    // Solo fecha de creación, los movimientos no se actualizan
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    /**
     * FK: idProducto - Producto afectado por el movimiento.
     * LAZY: No cargamos el producto completo si solo necesitamos el id.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idProducto", nullable = false)
    private Producto producto;

    /**
     * FK: idUsuario - Usuario que realizó el movimiento.
     * LAZY: Igual, no siempre necesitamos todos los datos del usuario.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    // Vive dentro de Movimiento para que esté claro a qué tabla pertenece.
    // Se usa como: Movimiento.TipoMovimiento.ENTRADA
    public enum TipoMovimiento {
        ENTRADA,
        SALIDA
    }
}
