package com.castores.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
@Builder
public class ProductoSimpleResponse {

    private Integer idProducto;
    private String nombre;
    private BigDecimal precio;
    private Integer cantidad;
    private Boolean estatus;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}