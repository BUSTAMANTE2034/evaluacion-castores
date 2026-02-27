package com.castores.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Representa la tabla `producto`.
 *
 * Reglas de negocio importantes:
 * - cantidad inicia en 0 siempre al crear
 * - No se eliminan productos, solo se cambia estatus a false (baja lógica)
 * - Tiene auditoría: quién creó (creadoPor) y quién actualizó (actualizadoPor)
 * - Dos FK a usuario: creado_por_idUsuario y actualizado_por_idUsuario
 */
@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProducto")
    private Integer idProducto;

    @Column(nullable = false, length = 100)
    private String nombre;

    // DECIMAL(10,2) en SQL - BigDecimal en Java para no perder centavos
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    // Siempre inicia en 0. Solo sube con movimientos de ENTRADA.
    @Column(nullable = false)
    @Builder.Default
    private Integer cantidad = 0;

    // true = activo, false = dado de baja. NUNCA se hace DELETE.
    @Column(nullable = false)
    @Builder.Default
    private Boolean estatus = true;

    /**
     * FK: creado_por_idUsuario
     * Usuario que creó el producto. No cambia nunca (updatable=false en la columna).
     * LAZY: No carga el usuario si no se necesita (optimización).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por_idUsuario", nullable = false, updatable = false)
    private Usuario creadoPor;

    /**
     * FK: actualizado_por_idUsuario
     * Usuario que hizo el último cambio. Puede ser null si nadie ha modificado aún.
     * ON DELETE SET NULL - por eso es nullable aquí.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actualizado_por_idUsuario")
    private Usuario actualizadoPor;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        // Garantizamos que cantidad siempre empiece en 0
        if (cantidad == null) cantidad = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
