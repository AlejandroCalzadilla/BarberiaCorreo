package org.barberia.usuarios.domain.enums;

public enum EstadoPago {
    pendiente,
    pagado,
    cancelado,
    reembolsado;

    public static EstadoPago   parse(String s) {
        if (s == null) throw new IllegalArgumentException("EstadoPago no puede ser nulo");
        String n = s.trim().toLowerCase().replace('-', '_').replace(' ', '_');
        switch (n) {
            case "pendiente": return pendiente;
            case "pagado" : return pagado;
            case "cancelado" : return cancelado;
            case "reembolsado" : return reembolsado;
            default: throw new IllegalArgumentException("EstadoReserva no válido: '" + s + "'");
        }
    }
}
