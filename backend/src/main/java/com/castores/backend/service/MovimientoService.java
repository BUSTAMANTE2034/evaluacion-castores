package com.castores.backend.service;

import com.castores.backend.dto.response.ProductoResponse;
import com.castores.backend.dto.response.ProductoSimpleResponse;
import com.castores.backend.dto.response.UsuarioSimpleResponse;
import com.castores.backend.dto.response.MovimientoResponse;
import com.castores.backend.exception.BusinessException;
import com.castores.backend.exception.ResourceNotFoundException;
import com.castores.backend.model.Movimiento;
import com.castores.backend.model.Producto;
import com.castores.backend.model.Usuario;
import com.castores.backend.repository.MovimientoRepository;
import com.castores.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
public class MovimientoService {

        private final MovimientoRepository movimientoRepository;
        private final ProductoService productoService;
        private final UsuarioRepository usuarioRepository;

        // ENTRADA (Solo ADMINISTRADOR)
        @Transactional
        public ProductoResponse registrarEntrada(Integer idProducto,
                        int cantidad,
                        String correoUsuario) {

                Producto producto = productoService.getProducto(idProducto);
                Usuario usuario = getUsuario(correoUsuario);

                if (!producto.getEstatus()) {
                        throw new BusinessException("No se puede mover un producto dado de baja");
                }

                if (cantidad <= 0) {
                        throw new BusinessException("La cantidad debe ser mayor a 0");
                }

                productoService.aumentarCantidad(producto, cantidad, usuario);

                guardarMovimiento(producto, usuario,
                                Movimiento.TipoMovimiento.ENTRADA, cantidad);

                return productoService.obtener(idProducto);
        }

        // SALIDA (Solo ALMACENISTA)
        @Transactional
        public ProductoResponse registrarSalida(Integer idProducto,
                        int cantidad,
                        String correoUsuario) {

                Producto producto = productoService.getProducto(idProducto);
                Usuario usuario = getUsuario(correoUsuario);

                if (!producto.getEstatus()) {
                        throw new BusinessException("No se puede mover un producto dado de baja");
                }

                if (cantidad <= 0) {
                        throw new BusinessException("La cantidad debe ser mayor a 0");
                }

                productoService.disminuirCantidad(producto, cantidad, usuario);

                guardarMovimiento(producto, usuario,
                                Movimiento.TipoMovimiento.SALIDA, cantidad);

                // DEVOLVER PRODUCTO ACTUALIZADO
                return productoService.obtener(idProducto);
        }

        // HISTORIAL (Solo ADMINISTRADOR)
        public List<MovimientoResponse> listarTodos() {
                return movimientoRepository.findAll()
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        public List<MovimientoResponse> filtrarPorTipo(Movimiento.TipoMovimiento tipo) {
                return movimientoRepository.findByTipoMovimiento(tipo)
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        // HELPERS
        private void guardarMovimiento(Producto producto,
                        Usuario usuario,
                        Movimiento.TipoMovimiento tipo,
                        int cantidad) {

                Movimiento movimiento = Movimiento.builder()
                                .tipoMovimiento(tipo)
                                .cantidad(cantidad)
                                .producto(producto)
                                .usuario(usuario)
                                .build();

                movimientoRepository.save(movimiento);
        }

        private Usuario getUsuario(String correo) {
                return usuarioRepository.findByCorreo(correo)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Usuario no encontrado: " + correo));
        }

        private MovimientoResponse toResponse(Movimiento m) {
                return MovimientoResponse.builder()
                                .idMovimiento(m.getIdMovimiento())
                                .tipoMovimiento(m.getTipoMovimiento())
                                .cantidad(m.getCantidad())
                                .fechaCreacion(m.getFechaCreacion())

                                .producto(
                                                ProductoSimpleResponse.builder()
                                                                .idProducto(m.getProducto().getIdProducto())
                                                                .nombre(m.getProducto().getNombre())
                                                                .precio(m.getProducto().getPrecio())
                                                                .cantidad(m.getProducto().getCantidad())
                                                                .estatus(m.getProducto().getEstatus())
                                                                .fechaCreacion(m.getProducto().getFechaCreacion())
                                                                .fechaActualizacion(
                                                                                m.getProducto().getFechaActualizacion())
                                                                .build())

                                .usuario(
                                                UsuarioSimpleResponse.builder()
                                                                .idUsuario(m.getUsuario().getIdUsuario())
                                                                .nombre(m.getUsuario().getNombre())
                                                                .correo(m.getUsuario().getCorreo())
                                                                .estatus(m.getUsuario().getEstatus())
                                                                .fechaCreacion(m.getUsuario().getFechaCreacion())
                                                                .build())

                                .build();
        }

        public Page<MovimientoResponse> listarPaginado(int page,
                        int perPage,
                        Movimiento.TipoMovimiento tipo) {

                Pageable pageable = PageRequest.of(page - 1, perPage,Sort.by(Sort.Direction.DESC, "fechaCreacion"));

                Page<Movimiento> movimientos;

                if (tipo == null) {
                        movimientos = movimientoRepository.findAll(pageable);
                } else {
                        movimientos = movimientoRepository.findByTipoMovimiento(tipo, pageable);
                }

                return movimientos.map(this::toResponse);
        }
}