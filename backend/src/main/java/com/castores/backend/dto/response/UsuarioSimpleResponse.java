package com.castores.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UsuarioSimpleResponse {

    private Integer idUsuario;
    private String nombre;
    private String correo;
    private Boolean estatus;
    private LocalDateTime fechaCreacion;
}