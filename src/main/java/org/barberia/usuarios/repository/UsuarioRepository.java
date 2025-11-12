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
    
    /**
     * Obtiene usuarios que no están asociados a ningún barbero ni cliente.
     * @return Lista de usuarios disponibles para asociar
     */
    List<Usuario> findAvailableUsuarios();
}
