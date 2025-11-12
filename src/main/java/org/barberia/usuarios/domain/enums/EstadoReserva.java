package org.barberia.usuarios.domain.enums;

public enum EstadoReserva {
    confirmada,
    en_proceso,
    completada,
    cancelada,
    no_asistio;

     public static EstadoReserva   parse(String s) {
        if (s == null) throw new IllegalArgumentException("EstadoReserva no puede ser nulo");
        String n = s.trim().toLowerCase().replace('-', '_').replace(' ', '_');
        switch (n) {
            case "confirmada": return confirmada;
            case "completada" : return completada;
            case "cancelada" : return cancelada;
            case "no_asistio" : return no_asistio;
            default: throw new IllegalArgumentException("EstadoReserva no válido: '" + s + "'");
        }
    }

     
}
