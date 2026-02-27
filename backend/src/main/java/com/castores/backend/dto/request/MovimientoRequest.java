package com.castores.backend.dto.request;

import com.castores.backend.model.Movimiento;
import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * DTO para registrar un movimiento de inventario (ENTRADA o SALIDA).
 * El usuario que lo realiza se saca del JWT (no lo manda el frontend).
 */
@Data
public class MovimientoRequest {

    @NotNull(message = "El id del producto es obligatorio")
    private Integer idProducto;

    @NotNull(message = "El tipo de movimiento es obligatorio")
    private Movimiento.TipoMovimiento tipoMovimiento; // "ENTRADA" o "SALIDA"

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
}
