package org.barberia.usuarios.domain.enums;

public enum MetodoPago {
    efectivo,
    tarjeta,
    transferencia,
    otro;

     public static MetodoPago parse(String s) {
        if (s == null) throw new IllegalArgumentException("MetodoPago no puede ser nulo");
        String n = s.trim().toLowerCase().replace('-', '_').replace(' ', '_');
        switch (n) {
            case "efectivo": return efectivo;
            case "tarjeta" : return tarjeta;
            case "transferencia" : return transferencia;
            case "otro" : return otro;
            default: throw new IllegalArgumentException("MetodoPago no válido: '" + s + "'");
        }
    }
}



