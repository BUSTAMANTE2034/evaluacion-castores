package com.castores.backend.controller;

import com.castores.backend.dto.response.MovimientoResponse;
import com.castores.backend.dto.response.MovimientosPaginationResponse;
import com.castores.backend.dto.response.PaginationInfo;
import com.castores.backend.model.Movimiento;
import com.castores.backend.service.MovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;

/**
 * Endpoints del módulo de movimientos (ver movimientos de inventario).
 */
@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    // Ver historial (solo admin)
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<MovimientosPaginationResponse> listar(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int per_page,
            @RequestParam(required = false) Movimiento.TipoMovimiento tipo) {

        Page<MovimientoResponse> movimientosPage = movimientoService.listarPaginado(page, per_page, tipo);

        int current = movimientosPage.getNumber() + 1;

        PaginationInfo pagination = PaginationInfo.builder()
                .total(movimientosPage.getTotalElements())
                .pages(movimientosPage.getTotalPages())
                .current_page(current)
                .per_page(movimientosPage.getSize())
                .has_next(movimientosPage.hasNext())
                .has_prev(movimientosPage.hasPrevious())
                .next_page(movimientosPage.hasNext() ? current + 1 : null)
                .prev_page(movimientosPage.hasPrevious() ? current - 1 : null)
                .build();

        MovimientosPaginationResponse response = MovimientosPaginationResponse.builder()
                .message("Movimientos obtenidos correctamente")
                .movimientos(movimientosPage.getContent())
                .pagination(pagination)
                .build();

        return ResponseEntity.ok(response);
    }

}