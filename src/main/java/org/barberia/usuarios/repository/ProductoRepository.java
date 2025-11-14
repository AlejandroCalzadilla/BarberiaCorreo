package org.barberia.usuarios.repository;

import org.barberia.usuarios.domain.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository {
    List<Producto> findAll();
    Optional<Producto> findById(Integer id);
    Producto save(Producto p);
    void deleteById(Integer id);
    void activateById(Integer id);
}
