package org.barberia.usuarios.repository;

import org.barberia.usuarios.domain.Pago;

import java.util.List;
import java.util.Optional;

public interface PagoRepository {
    List<Pago> findAll();
    Optional<Pago> findById(Integer id);
    Pago save(Pago p);
    void deleteById(Integer id);
}
