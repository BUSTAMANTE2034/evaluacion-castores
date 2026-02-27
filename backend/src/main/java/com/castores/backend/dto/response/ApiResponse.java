package com.castores.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiResponse<T> {

    private int status;
    private String mensaje;
    private LocalDateTime timestamp;
    private T data;
}