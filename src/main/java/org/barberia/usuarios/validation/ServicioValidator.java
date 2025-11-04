package org.barberia.usuarios.validation;

import org.barberia.usuarios.domain.Servicio;

public class ServicioValidator {
    public void validar(Servicio s){
        if (s == null) throw new IllegalArgumentException("Servicio no puede ser null");
        if (isBlank(s.nombre)) throw new IllegalArgumentException("nombre es requerido");
        if (s.duracion_minutos_aprox == null || s.duracion_minutos_aprox <= 0) throw new IllegalArgumentException("duracion_minutos_aprox > 0");
        if (s.precio == null || s.precio.signum() < 0) throw new IllegalArgumentException("precio >= 0");
    }
    private boolean isBlank(String s){ return s==null||s.trim().isEmpty(); }
}
