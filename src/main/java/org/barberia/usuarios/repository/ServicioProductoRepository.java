package org.barberia.usuarios.repository;

import org.barberia.usuarios.domain.ServicioProducto;

import java.util.List;
import java.util.Optional;

public interface ServicioProductoRepository {
    List<ServicioProducto> findAll();
    Optional<ServicioProducto> findById(Integer id);
    ServicioProducto save(ServicioProducto sp);
    void deleteById(Integer id);
}
