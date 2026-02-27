-- BASE DE DATOS: inventario_castores
-- Sistema de control de inventario

CREATE DATABASE inventario_castores;
USE inventario_castores;


-- TABLA: rol
-- Almacena los tipos de roles del sistema (admin, empleado)

CREATE TABLE rol(
    idRol INT AUTO_INCREMENT PRIMARY KEY, -- Identificador único del rol
    nombre VARCHAR(100) NOT NULL, -- Nombre del rol (administrador/Almacenista)
    descripcion VARCHAR(150), -- Descripción opcional
    estatus BOOLEAN DEFAULT TRUE, -- TRUE = activo, FALSE = inactivo
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP, -- Fecha de creación automática
    fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP 
        ON UPDATE CURRENT_TIMESTAMP -- Se actualiza automáticamente al modificar
);


-- TABLA: usuario
-- Almacena los usuarios que acceden al sistema

CREATE TABLE usuario(
    idUsuario INT AUTO_INCREMENT PRIMARY KEY, -- Identificador único del usuario
    nombre VARCHAR(100) NOT NULL, -- Nombre completo
    correo VARCHAR(100) NOT NULL UNIQUE, -- Correo único
    contrasena VARCHAR(255) NOT NULL, -- Contraseña encriptada (hash)
    estatus BOOLEAN DEFAULT TRUE, -- TRUE = activo, FALSE = inactivo
    idRol INT NOT NULL, -- Rol asignado al usuario
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP 
        ON UPDATE CURRENT_TIMESTAMP,

    -- Relación con tabla rol (1:N)
    CONSTRAINT fk_usuario_rol
        FOREIGN KEY (idRol) 
        REFERENCES rol(idRol)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);


-- TABLA: producto
-- Almacena los productos del inventario
-- La cantidad inicial será 0
-- No se eliminan productos, solo se cambian de estatus

CREATE TABLE producto(
    idProducto INT AUTO_INCREMENT PRIMARY KEY, -- Identificador único del producto
    nombre VARCHAR(100) NOT NULL, -- Nombre del producto
    precio DECIMAL(10,2) NOT NULL, -- Precio del producto
    cantidad INT NOT NULL DEFAULT 0, -- Stock actual (inicia en 0)
    estatus BOOLEAN DEFAULT TRUE, -- TRUE = activo, FALSE = inactivo
    creado_por_idUsuario INT NOT NULL, -- Usuario que creó el producto
    actualizado_por_idUsuario INT, -- Usuario que realizó la última actualización
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP 
        ON UPDATE CURRENT_TIMESTAMP,

    -- Relación de auditoría (usuario que creó)
    CONSTRAINT fk_producto_creado
        FOREIGN KEY (creado_por_idUsuario) 
        REFERENCES usuario(idUsuario)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    -- Relación de auditoría (usuario que actualizó)
    CONSTRAINT fk_producto_actualizado
        FOREIGN KEY (actualizado_por_idUsuario) 
        REFERENCES usuario(idUsuario)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);


-- TABLA: movimiento
-- Registra todas las entradas y salidas de inventario
-- Cada movimiento registra:
-- - Tipo (ENTRADA / SALIDA)
-- - Cantidad
-- - Usuario que lo realizó
-- - Producto afectado
-- - Fecha y hora automática

CREATE TABLE movimiento(
    idMovimiento INT AUTO_INCREMENT PRIMARY KEY, -- Identificador único del movimiento
    tipo_movimiento ENUM('ENTRADA','SALIDA') NOT NULL, -- Tipo de movimiento
    cantidad INT NOT NULL, -- Cantidad movida (mayor a 0)
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP, -- Fecha y hora del movimiento
    idProducto INT NOT NULL, -- Producto afectado
    idUsuario INT NOT NULL, -- Usuario que realizó el movimiento

    -- Relación con usuario (1:N)
    CONSTRAINT fk_movimiento_usuario
        FOREIGN KEY (idUsuario) 
        REFERENCES usuario(idUsuario)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    -- Relación con producto (1:N)
    CONSTRAINT fk_movimiento_producto
        FOREIGN KEY (idProducto) 
        REFERENCES producto(idProducto)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);