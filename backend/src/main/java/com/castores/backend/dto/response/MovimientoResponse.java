package com.castores.backend.dto.response;

import com.castores.backend.model.Movimiento;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MovimientoResponse {

    private Integer idMovimiento;
    private Movimiento.TipoMovimiento tipoMovimiento;
    private Integer cantidad;
    private LocalDateTime fechaCreacion;

    private ProductoSimpleResponse producto;
    private UsuarioSimpleResponse usuario;
}