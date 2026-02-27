package com.castores.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductosPaginationResponse {

    private String message;
    private List<ProductoResponse> productos;
    private PaginationInfo pagination;
}