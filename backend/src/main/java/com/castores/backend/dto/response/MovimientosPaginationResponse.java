package com.castores.backend.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class MovimientosPaginationResponse {

    private String message;
    private List<MovimientoResponse> movimientos;
    private PaginationInfo pagination;
}