package org.barberia.usuarios.validation;

import org.barberia.usuarios.domain.ServicioProducto;

public class ServicioProductoValidator {
    public void validar(ServicioProducto sp){
        if (sp == null) throw new IllegalArgumentException("ServicioProducto no puede ser null");
        if (sp.id_producto == null) throw new IllegalArgumentException("id_producto es requerido");
        if (sp.cantidad == null || sp.cantidad <= 0) throw new IllegalArgumentException("cantidad > 0");
      
    }
}
