package org.barberia.usuarios.repository;

import org.barberia.usuarios.domain.Reserva;

import java.util.List;
import java.util.Optional;

public interface ReservaRepository {
    List<Reserva> findAll();
    Optional<Reserva> findById(Integer id);
    Reserva save(Reserva r);
    void deleteById(Integer id);
}
