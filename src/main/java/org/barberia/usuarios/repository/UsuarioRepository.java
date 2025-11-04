package org.barberia.usuarios.repository;

import java.util.List;
import java.util.Optional;

import org.barberia.usuarios.domain.Usuario;

public interface UsuarioRepository {
    List<Usuario> findAll();
    Optional<Usuario> findById(Integer id);
    Usuario save(Usuario usuario);

    void softDeleteById(Integer id);

    void activateById(Integer id);
    void deleteById(Integer id);
}
