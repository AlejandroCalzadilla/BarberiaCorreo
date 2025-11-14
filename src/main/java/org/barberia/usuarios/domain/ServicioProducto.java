package org.barberia.usuarios.domain;

import java.math.BigDecimal;
import java.security.Provider.Service;

import org.barberia.usuarios.mapper.ServicioProductoMapper;

public class ServicioProducto {
    public Integer id_servicio;
    public Integer id_producto;
    public Integer cantidad;

    @Override
    public String toString() {
        return ServicioProductoMapper.obtenerUnoTable(this);
    }
}
