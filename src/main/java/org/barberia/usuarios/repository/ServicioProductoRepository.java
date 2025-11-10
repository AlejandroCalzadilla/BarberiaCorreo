package org.barberia.usuarios.repository;

import org.barberia.usuarios.domain.ServicioProducto;

import java.util.List;
import java.util.Optional;

public interface ServicioProductoRepository {
    List<ServicioProducto> findAll();
    List<ServicioProducto> findByServicioId(Integer id);
    List<ServicioProducto> findByProductoId(Integer id);
    ServicioProducto insert(ServicioProducto sp);
    ServicioProducto update(ServicioProducto sp);
    void delete(Integer id_servicio, Integer id_producto);
}
