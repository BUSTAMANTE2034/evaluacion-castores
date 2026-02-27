package com.castores.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Representa la tabla `usuario`.
 * Tiene relación ManyToOne con Rol.
 * La contraseña se guarda encriptada (BCrypt).
 * Implementa los campos de auditoría fecha_creacion y fecha_actualizacion.
 */
@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private Integer idUsuario;

    // Nombre completo del usuario
    @Column(nullable = false, length = 100)
    private String nombre;

    // Correo único - se usará como "username" para el login
    @Column(nullable = false, unique = true, length = 100)
    private String correo;

    // Contraseña en hash BCrypt.
    @Column(nullable = false, length = 255)
    private String contrasena;

    @Column(nullable = false)
    @Builder.Default
    private Boolean estatus = true;

    /**
     * Relación N:1  Muchos usuarios pueden tener el mismo rol.
     * EAGER: Carga el rol siempre junto con el usuario (necesario para Spring Security).
     * @JoinColumn  columna FK en la tabla usuario: idRol
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idRol", nullable = false)
    private Rol rol;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
