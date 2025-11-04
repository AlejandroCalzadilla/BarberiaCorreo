package org.barberia.usuarios.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.barberia.usuarios.domain.Usuario;

public class UsuarioValidator {
    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public void validar(Usuario u) {
        List<String> errores = new ArrayList<>();
        if (u == null) {
            throw new IllegalArgumentException("Usuario no puede ser null");
        }
        if (isBlank(u.nombre)) errores.add("nombre es requerido");
        if (isBlank(u.apellido)) errores.add("apellido es requerido");
        if (isBlank(u.email) || !EMAIL.matcher(u.email).matches()) errores.add("email inválido");
        if (isBlank(u.username)) errores.add("username es requerido");
        if (isBlank(u.password) || u.password.length() < 6) errores.add("password mínimo 6 caracteres");
        if (!errores.isEmpty()) {
            throw new IllegalArgumentException(String.join(", ", errores));
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
