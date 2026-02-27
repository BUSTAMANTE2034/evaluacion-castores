package com.castores.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.castores.backend.model.Rol;
import com.castores.backend.model.Usuario;
import com.castores.backend.repository.RolRepository;
import com.castores.backend.repository.UsuarioRepository;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
	@Bean
    CommandLineRunner init(RolRepository rolRepo,
                           UsuarioRepository userRepo,
                           PasswordEncoder encoder) {
        return args -> {

            if (rolRepo.count() == 0) {

                Rol adminRol = rolRepo.save(
                        Rol.builder()
                                .nombre("ADMINISTRADOR")
                                .descripcion("Administrador del sistema")
                                .build()
                );

                Rol almacenistaRol = rolRepo.save(
                        Rol.builder()
                                .nombre("ALMACENISTA")
                                .descripcion("Usuario de almacén")
                                .build()
                );

                userRepo.save(
                        Usuario.builder()
                                .nombre("Administrador")
                                .correo("administrador@yopmail.com")
                                .contrasena(encoder.encode("Password1@"))
                                .rol(adminRol)
                                .build()
                );

                userRepo.save(
                        Usuario.builder()
                                .nombre("Almacenista")
                                .correo("almacenista@yopmail.com")
                                .contrasena(encoder.encode("Password1@"))
                                .rol(almacenistaRol)
                                .build()
                );

                System.out.println("✔ Datos iniciales creados");
            }
        };
    }

}
