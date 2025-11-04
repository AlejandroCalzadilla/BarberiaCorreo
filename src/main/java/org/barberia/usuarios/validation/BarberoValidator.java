package org.barberia.usuarios.validation;

import org.barberia.usuarios.domain.Barbero;

public class BarberoValidator {
    public void validar(Barbero b){
        if (b == null) throw new IllegalArgumentException("Barbero no puede ser null");
        if (b.id_usuario == null) throw new IllegalArgumentException("id_usuario es requerido");
    }
}
