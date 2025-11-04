package org.barberia.usuarios.repository;

import org.barberia.usuarios.domain.Barbero;

import java.util.List;
import java.util.Optional;

public interface BarberoRepository {
    List<Barbero> findAll();
    Optional<Barbero> findById(Integer id);
    Barbero save(Barbero b);
    Optional<Barbero> findByUsuarioId(Integer usuarioId);
    void deleteById(Integer id);
}
