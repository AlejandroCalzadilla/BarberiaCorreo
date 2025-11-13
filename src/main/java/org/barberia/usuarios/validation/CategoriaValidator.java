package org.barberia.usuarios.validation;

import org.barberia.usuarios.domain.Categoria;

public class CategoriaValidator {
    public void validar(Categoria c) {
        if (c == null) throw new IllegalArgumentException("Categoria no puede ser null");
        if (isBlank(c.nombre)) throw new IllegalArgumentException("nombre es requerido");
        if (isBlank(c.descripcion)) throw new IllegalArgumentException("descripcion es requerido");
    }
    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}
