package org.barberia.usuarios.validation;

import org.barberia.usuarios.domain.Horario;

public class HorarioValidator {
    public void validar(Horario h){
        if (h == null) throw new IllegalArgumentException("Horario no puede ser null");
        if (h.id_barbero == null) throw new IllegalArgumentException("id_barbero es requerido");
        if (h.hora_inicio == null || h.hora_fin == null) throw new IllegalArgumentException("hora_inicio y hora_fin son requeridos");
        if (!h.hora_fin.isAfter(h.hora_inicio)) throw new IllegalArgumentException("hora_fin debe ser mayor a hora_inicio");
    }
}
