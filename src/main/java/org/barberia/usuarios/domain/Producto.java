package org.barberia.usuarios.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.barberia.usuarios.domain.enums.EstadoItem;
import org.barberia.usuarios.mapper.ProductoMapper;

public class Producto {
    public Integer id_producto;
    public Integer id_categoria;
    public String codigo;
    public String nombre;
    public String descripcion;
    public BigDecimal precio_compra;
    public BigDecimal precio_venta;
    public Integer stock_actual;
    public Integer stock_minimo;
    public String unidad_medida;
    public EstadoItem estado;
    public String imagenurl;
    public LocalDateTime created_at;
    public LocalDateTime updated_at;

    @Override
    public String toString() {
        return ProductoMapper.obtenerUnoTable(
                this);
    }
}
