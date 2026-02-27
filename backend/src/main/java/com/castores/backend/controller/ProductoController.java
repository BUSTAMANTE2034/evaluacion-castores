package com.castores.backend.controller;

import com.castores.backend.dto.request.CantidadRequest;
import com.castores.backend.dto.request.ProductoRequest;
import com.castores.backend.dto.response.ApiResponse;
import com.castores.backend.dto.response.PaginationInfo;
import com.castores.backend.dto.response.ProductoResponse;
import com.castores.backend.dto.response.ProductosPaginationResponse;

import com.castores.backend.service.ProductoService;
import com.castores.backend.service.MovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;

/**
 * Endpoints del módulo de inventario.
 *
 * @AuthenticationPrincipal UserDetails ud - inyecta el usuario autenticado del
 *                          JWT.
 *                          ud.getUsername() - devuelve el correo del usuario
 *                          logueado.
 *
 *                          @PreAuthorize("hasRole('ADMINISTRADOR')") - solo el
 *                          admin puede usar ese endpoint.
 *                          Si un Almacenista intenta acceder - 403 Forbidden
 *                          automático.
 */
@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {

        private final ProductoService productoService;
        private final MovimientoService movimientoService;

        // Ver inventario (ambos roles)
        @GetMapping
        @PreAuthorize("hasAnyRole('ADMINISTRADOR','ALMACENISTA')")
        public ResponseEntity<ProductosPaginationResponse> listar(
                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "10") int per_page,
                        @RequestParam(required = false) Boolean estatus) {

                Page<ProductoResponse> productosPage = productoService.listarPaginado(page, per_page, estatus);
                int current = productosPage.getNumber() + 1;
                PaginationInfo pagination = PaginationInfo.builder()
                                .total(productosPage.getTotalElements())
                                .pages(productosPage.getTotalPages())
                                .current_page(current)
                                .per_page(productosPage.getSize())
                                .has_next(productosPage.hasNext())
                                .has_prev(productosPage.hasPrevious())
                                .next_page(productosPage.hasNext() ? current + 1 : null)
                                .prev_page(productosPage.hasPrevious() ? current - 1 : null)
                                .build();

                ProductosPaginationResponse response = ProductosPaginationResponse.builder()
                                .message("Productos obtenidos correctamente")
                                .productos(productosPage.getContent())
                                .pagination(pagination)
                                .build();

                return ResponseEntity.ok(response);
        }

        // Crear producto (solo admin)
        @PostMapping
        @PreAuthorize("hasRole('ADMINISTRADOR')")
        public ResponseEntity<ProductoResponse> crear(
                        @Valid @RequestBody ProductoRequest request,
                        @AuthenticationPrincipal UserDetails ud) {

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(productoService.crear(request, ud.getUsername()));
        }

        // Aumentar inventario (solo admin)
        @PatchMapping("/{id}/entrada")
        @PreAuthorize("hasRole('ADMINISTRADOR')")
        public ResponseEntity<ApiResponse<ProductoResponse>> entrada(
                        @PathVariable Integer id,
                        @RequestBody CantidadRequest request,
                        @AuthenticationPrincipal UserDetails ud) {

                ProductoResponse producto = movimientoService.registrarEntrada(id, request.getCantidad(),
                                ud.getUsername());

                return ResponseEntity.ok(
                                ApiResponse.<ProductoResponse>builder()
                                                .status(200)
                                                .mensaje("Inventario aumentado correctamente")
                                                .timestamp(LocalDateTime.now())
                                                .data(producto)
                                                .build());
        }

        // Dar baja/reactivar (solo admin)
        @PatchMapping("/{id}/baja")
        @PreAuthorize("hasRole('ADMINISTRADOR')")
        public ResponseEntity<ApiResponse<ProductoResponse>> baja(
                        @PathVariable Integer id,
                        @AuthenticationPrincipal UserDetails ud) {

                ProductoResponse producto = productoService.darBaja(id, ud.getUsername());

                return ResponseEntity.ok(
                                ApiResponse.<ProductoResponse>builder()
                                                .status(200)
                                                .mensaje("Producto dado de baja correctamente")
                                                .timestamp(LocalDateTime.now())
                                                .data(producto)
                                                .build());
        }

        @PatchMapping("/{id}/reactivar")
        @PreAuthorize("hasRole('ADMINISTRADOR')")
        public ResponseEntity<ApiResponse<ProductoResponse>> activar(
                        @PathVariable Integer id,
                        @AuthenticationPrincipal UserDetails ud) {

                ProductoResponse producto = productoService.activar(id, ud.getUsername());

                return ResponseEntity.ok(
                                ApiResponse.<ProductoResponse>builder()
                                                .status(200)
                                                .mensaje("Producto activado correctamente")
                                                .timestamp(LocalDateTime.now())
                                                .data(producto)
                                                .build());
        }

        // Sacar inventario (solo ALMACENISTA)
        @PatchMapping("/{id}/salida")
        @PreAuthorize("hasRole('ALMACENISTA')")
        public ResponseEntity<ApiResponse<ProductoResponse>> salida(
                        @PathVariable Integer id,
                        @RequestBody CantidadRequest request,
                        @AuthenticationPrincipal UserDetails ud) {

                ProductoResponse producto = movimientoService.registrarSalida(id, request.getCantidad(),
                                ud.getUsername());

                return ResponseEntity.ok(
                                ApiResponse.<ProductoResponse>builder()
                                                .status(200)
                                                .mensaje("Inventario reducido correctamente")
                                                .timestamp(LocalDateTime.now())
                                                .data(producto)
                                                .build());
        }
}
