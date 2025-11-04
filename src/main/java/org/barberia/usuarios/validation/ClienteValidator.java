package org.barberia.usuarios.validation;

import org.barberia.usuarios.domain.Cliente;

public class ClienteValidator {
    public void validar(Cliente c) {
        if (c == null) throw new IllegalArgumentException("Cliente no puede ser null");
        if (c.id_usuario == null) throw new IllegalArgumentException("id_usuario es requerido");
    }
}
