package org.barberia.usuarios.validation;

import org.barberia.usuarios.domain.Producto;

public class ProductoValidator {
    public void validar(Producto p) {
        if (p == null) throw new IllegalArgumentException("Producto no puede ser null");
        if (isBlank(p.codigo)) throw new IllegalArgumentException("codigo es requerido");
        if (isBlank(p.nombre)) throw new IllegalArgumentException("nombre es requerido");
        if (p.id_categoria == null) throw new IllegalArgumentException("id_categoria es requerido");
        if (isBlank(p.descripcion)) throw new IllegalArgumentException("descripcion es requerido");
        if (p.precio_venta == null) throw new IllegalArgumentException("precio_venta es requerido");
        if (p.precio_compra == null) throw new IllegalArgumentException("precio_compra es requerido");
      
        if (p.precio_compra != null && p.precio_compra.signum() < 0) throw new IllegalArgumentException("precio_compra no puede ser negativo");
        if (p.stock_actual != null && p.stock_actual < 0) throw new IllegalArgumentException("stock_actual es requerido y no puede ser negativo");
        if (p.stock_minimo != null && p.stock_minimo < 0) throw new IllegalArgumentException("stock_minimo es requerido y no puede ser negativo");
    }
    private boolean isBlank(String s){ return s==null||s.trim().isEmpty(); }
}
