package org.barberia.usuarios.domain.enums;

public enum EstadoBarbero {
    disponible,
    nodisponible;

    public static EstadoBarbero   parse(String s) {
        if (s == null) throw new IllegalArgumentException("EstadoReserva no puede ser nulo");
        String n = s.trim().toLowerCase().replace('-', '_').replace(' ', '_');
        switch (n) {
            case "disponible": return disponible;
            case "nodisponible" : return nodisponible;
            default: throw new IllegalArgumentException("EstadoReserva no válido: '" + s + "'");
        }
    }
}
