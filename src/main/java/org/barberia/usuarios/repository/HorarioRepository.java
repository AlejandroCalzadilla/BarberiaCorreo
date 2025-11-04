package org.barberia.usuarios.repository;

import org.barberia.usuarios.domain.Horario;

import java.util.List;
import java.util.Optional;

public interface HorarioRepository {
    List<Horario> findAll();
    Optional<Horario> findById(Integer id);
    Horario save(Horario h);
    void deleteById(Integer id);
}
