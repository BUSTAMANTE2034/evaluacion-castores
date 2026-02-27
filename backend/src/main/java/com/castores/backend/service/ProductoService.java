package com.castores.backend.service;

import com.castores.backend.dto.request.ProductoRequest;
import com.castores.backend.dto.response.ProductoResponse;
import com.castores.backend.exception.BusinessException;
import com.castores.backend.exception.ResourceNotFoundException;
import com.castores.backend.model.Producto;
import com.castores.backend.model.Usuario;
import com.castores.backend.repository.ProductoRepository;
import com.castores.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    // CREAR PRODUCTO (Solo ADMINISTRADOR)
    // Cantidad SIEMPRE inicia en 0
    public ProductoResponse crear(ProductoRequest request, String correoUsuario) {

        Usuario usuario = getUsuario(correoUsuario);

        Producto producto = Producto.builder()
                .nombre(request.getNombre())
                .precio(request.getPrecio())
                .cantidad(0)
                .estatus(true)
                .creadoPor(usuario)
                .build();

        return toResponse(productoRepository.save(producto));
    }

    // LISTAR PRODUCTOS

    public List<ProductoResponse> listarTodos() {
        return productoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ProductoResponse> listarActivos() {
        return productoRepository.findByEstatus(true)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ProductoResponse> listarInactivos() {
        return productoRepository.findByEstatus(false)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // OBTENER POR ID

    public ProductoResponse obtener(Integer id) {
        return toResponse(getProducto(id));
    }

    // ACTUALIZAR NOMBRE / PRECIO (Solo ADMIN)

    public ProductoResponse actualizar(Integer id,
                                       ProductoRequest request,
                                       String correoUsuario) {

        Producto producto = getProducto(id);
        Usuario usuario = getUsuario(correoUsuario);

        producto.setNombre(request.getNombre());
        producto.setPrecio(request.getPrecio());
        producto.setActualizadoPor(usuario);

        return toResponse(productoRepository.save(producto));
    }

    // DAR DE BAJA (estatus = false)

    public ProductoResponse darBaja(Integer id, String correoUsuario) {

        Producto producto = getProducto(id);


        if (!producto.getEstatus()) {
            throw new BusinessException("El producto ya está dado de baja");
        }

        producto.setEstatus(false);
        producto.setActualizadoPor(getUsuario(correoUsuario));

        return toResponse(productoRepository.save(producto));
    }

    // REACTIVAR

    public ProductoResponse activar(Integer id, String correoUsuario) {

        Producto producto = getProducto(id);

        if (producto.getEstatus()) {
            throw new BusinessException("El producto ya está activo");
        }

        producto.setEstatus(true);
        producto.setActualizadoPor(getUsuario(correoUsuario));

        return toResponse(productoRepository.save(producto));
    }

    // AUMENTAR INVENTARIO (ENTRADA)
    // Solo ADMIN puede llamar esto (controlado en Controller)

    public void aumentarCantidad(Producto producto,
                                 int cantidad,
                                 Usuario usuario) {

        if (cantidad <= 0) {
            throw new BusinessException("La cantidad debe ser mayor a 0");
        }

        producto.setCantidad(producto.getCantidad() + cantidad);
        producto.setActualizadoPor(usuario);

        productoRepository.save(producto);
    }

    // DISMINUIR INVENTARIO (SALIDA)
    // Solo ALMACENISTA puede llamar esto (controlado en Controller)

    public void disminuirCantidad(Producto producto,
                                  int cantidad,
                                  Usuario usuario) {

        if (cantidad <= 0) {
            throw new BusinessException("La cantidad debe ser mayor a 0");
        }

        if (producto.getCantidad() < cantidad) {
            throw new BusinessException(
                    "Stock insuficiente. Disponible: " + producto.getCantidad()
            );
        }

        producto.setCantidad(producto.getCantidad() - cantidad);
        producto.setActualizadoPor(usuario);

        productoRepository.save(producto);
    }

    
    // HELPERS INTERNOS

    public Producto getProducto(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Producto no encontrado con id: " + id));
    }

    private Usuario getUsuario(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario no encontrado: " + correo));
    }

    // Nunca devolvemos entidad directa
    private ProductoResponse toResponse(Producto p) {
        return ProductoResponse.builder()
                .idProducto(p.getIdProducto())
                .nombre(p.getNombre())
                .precio(p.getPrecio())
                .cantidad(p.getCantidad())
                .estatus(p.getEstatus())
                .creadoPorNombre(
                        p.getCreadoPor() != null ?
                                p.getCreadoPor().getNombre() : null)
                .actualizadoPorNombre(
                        p.getActualizadoPor() != null ?
                                p.getActualizadoPor().getNombre() : null)
                .fechaCreacion(p.getFechaCreacion())
                .fechaActualizacion(p.getFechaActualizacion())
                .build();
    }

    public Page<ProductoResponse> listarPaginado(int page, int perPage, Boolean estatus) {

    Pageable pageable = PageRequest.of(page - 1, perPage);

    Page<Producto> productos;

    if (estatus == null) {
        //  No filtrar → traer todos
        productos = productoRepository.findAll(pageable);
    } else {
        //  Filtrar por estatus true o false
        productos = productoRepository.findByEstatus(estatus, pageable);
    }

    return productos.map(this::toResponse);
}
}