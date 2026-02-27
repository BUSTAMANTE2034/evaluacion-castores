package com.castores.backend.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Lo que devuelve la API cuando piden datos de un producto.
 * NUNCA devolvemos la entidad Producto directamente porque:
 * - Expone relaciones lazy que pueden causar errores
 * - Podría exponer datos que no queremos (ej: contraseñas del usuario que creó)
 */
@Data
@Builder
public class ProductoResponse {
    private Integer idProducto;
    private String nombre;
    private BigDecimal precio;
    private Integer cantidad;
    private Boolean estatus;
    private String creadoPorNombre;     // Solo el nombre, no todo el objeto Usuario
    private String actualizadoPorNombre;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}

