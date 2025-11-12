package org.barberia.usuarios.seeders;

import org.barberia.usuarios.service.UsuarioService;

import java.util.ArrayList;
import java.util.List;

import org.barberia.usuarios.domain.Usuario;
import org.barberia.usuarios.domain.enums.EstadoUsuario;
import org.barberia.usuarios.repository.UsuarioRepository;

public class UsuarioSeeder {
        private UsuarioService usuarioService;
        private UsuarioRepository usuarioRepository;

        UsuarioSeeder(
                        UsuarioService usuarioService,
                        UsuarioRepository usuarioRepository) {
                this.usuarioService = usuarioService;
                this.usuarioRepository = usuarioRepository;
        }

        public List<Usuario> seed() {

                List<Usuario> usuarios = new ArrayList<>();

                // numero 1
                Usuario adminUser = new Usuario(null, "camilo",
                                "sarmiento",
                                "camilo.sarmiento@example.com",
                                "1234567890",
                                "Calle Falsa 123",
                                EstadoUsuario.activo,
                                "camilo.sarmiento",
                                "password");

                // numero 2
                Usuario regularUser = new Usuario(null, "juan",
                                "perez",
                                "juan.perez@example.com",
                                "0987654321",
                                "Avenida Siempre Viva 456",
                                EstadoUsuario.activo,
                                "juan.perez",
                                "password");

                // numero 3
                Usuario clienteUser = new Usuario(null, "maria",
                                "lopez",
                                "maria.lopez@example.com",
                                "1122334455",
                                "Boulevard de los Sueños Rotos 789",
                                EstadoUsuario.activo,
                                "maria.lopez",
                                "password");

                // numero 4
                Usuario clienteUser2 = new Usuario(null, "sofia",
                                "fernandez",
                                "sofia.fernandez@example.com",
                                "5566778899",
                                "Calle de la Ilusión 123",
                                EstadoUsuario.activo,
                                "sofia.fernandez",
                                "password");

                // numero 5
                Usuario clienteUser3 = new Usuario(null, "diego",
                                "gonzalez",
                                "diego.gonzalez@example.com",
                                "6677889900",
                                "Avenida del Sol 456",
                                EstadoUsuario.activo,
                                "diego.gonzalez",
                                "password");

                // numero 6
                Usuario empleadoUser = new Usuario(null, "ana",
                                "gomez",
                                "ana.gomez@example.com",
                                "2233445566",
                                "Calle de la Amargura 321",
                                EstadoUsuario.activo,
                                "ana.gomez",
                                "password");

                // numero 7
                Usuario empleadoUser2 = new Usuario(null, "luis",
                                "martinez",
                                "luis.martinez@example.com",
                                "3344556677",
                                "Calle de la Alegría 654",
                                EstadoUsuario.activo,
                                "luis.martinez",
                                "password");

                // numero 8
                Usuario empleadoUser3 = new Usuario(null, "carlos",
                                "ramirez",
                                "carlos.ramirez@example.com",
                                "4455667788",
                                "Avenida de la Esperanza 987",
                                EstadoUsuario.activo,
                                "carlos.ramirez",
                                "password");

                usuarios.add(usuarioRepository.save(adminUser));
                usuarios.add(usuarioRepository.save(regularUser));

                usuarios.add(usuarioRepository.save(clienteUser));
                usuarios.add(usuarioRepository.save(clienteUser2));
                usuarios.add(usuarioRepository.save(clienteUser3));

                usuarios.add(usuarioRepository.save(empleadoUser));
                usuarios.add(usuarioRepository.save(empleadoUser2));
                usuarios.add(usuarioRepository.save(clienteUser2));
                usuarios.add(usuarioRepository.save(empleadoUser3));
                return usuarios;
        }

        public void rollback() {
                // Los usuarios se eliminan en cascada desde otras entidades
                System.out.println("  Rollback de usuarios completado");
        }

}
