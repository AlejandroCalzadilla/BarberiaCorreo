package org.barberia.usuarios.validation;

import org.barberia.usuarios.domain.Reserva;

public class ReservaValidator {
    public void validar(Reserva r){
        if (r == null) throw new IllegalArgumentException("Reserva no puede ser null");
        if (r.id_cliente == null) throw new IllegalArgumentException("id_cliente es requerido");
        if (r.id_barbero == null) throw new IllegalArgumentException("id_barbero es requerido");
        if (r.id_servicio == null) throw new IllegalArgumentException("id_servicio es requerido");
        if (r.hora_inicio == null || r.hora_fin == null) throw new IllegalArgumentException("hora_inicio y hora_fin son requeridos");
        if (!r.hora_fin.isAfter(r.hora_inicio)) throw new IllegalArgumentException("hora_fin debe ser mayor a hora_inicio");
        if (r.precio_servicio != null && r.precio_servicio.signum() < 0) throw new IllegalArgumentException("precio_servicio >= 0");
        if (r.monto_anticipo != null && r.monto_anticipo.signum() < 0) throw new IllegalArgumentException("monto_anticipo >= 0");
       // if (r.porcentaje_anticipo != null && (r.porcentaje_anticipo.signum() < 0 || r.porcentaje_anticipo.doubleValue() > 100.0)) throw new IllegalArgumentException("porcentaje_anticipo en [0,100]");
    }
}
