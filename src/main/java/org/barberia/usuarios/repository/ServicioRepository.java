package org.barberia.usuarios.repository;

import org.barberia.usuarios.domain.Servicio;

import java.util.List;
import java.util.Optional;

public interface ServicioRepository {
    List<Servicio> findAll();
    Optional<Servicio> findById(Integer id);
    Servicio save(Servicio s);
    void deleteById(Integer id);
    void softDeleteById(Integer id);
    void activateById(Integer id);
}
