package org.barberia.usuarios.repository;

import org.barberia.usuarios.domain.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository {
    List<Cliente> findAll();
    Optional<Cliente> findById(Integer id);
    Cliente save(Cliente c);
    void deleteById(Integer id);

    Optional<Cliente> findByUsuarioId(Integer usuarioId);
}
