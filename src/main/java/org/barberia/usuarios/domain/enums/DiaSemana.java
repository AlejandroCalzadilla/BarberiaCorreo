package org.barberia.usuarios.domain.enums;

public enum DiaSemana {
    lunes,
    martes,
    miercoles,
    jueves,
    viernes,
    sabado,
    domingo
    ;

    /**
     * Convierte una cadena a {@link DiaSemana} de forma tolerante.
     * Acepta mayúsculas/minúsculas, espacios, y abreviaturas de 3 letras (ej. "lun", "mar").
     * Lanza IllegalArgumentException si no puede mapearla.
     */
    public static DiaSemana parse(String s) {
        if (s == null) {
            throw new IllegalArgumentException("El día de la semana no puede ser nulo");
        }
        String n = s.trim().toLowerCase();
        // intentar valor exacto (por ejemplo "lunes" o "Lunes")
        try {
            return DiaSemana.valueOf(n);
        } catch (IllegalArgumentException e) {
            // intentar con abreviatura de 3 letras
            if (n.length() >= 3) {
                String prefix = n.substring(0, 3);
                switch (prefix) {
                    case "lunes": return lunes;
                    case "martes": return martes;
                    case "miercoles": return miercoles;
                    case "jueves": return jueves;
                    case "viernes": return viernes;
                    case "sabado": return sabado;
                    case "domingo": return domingo;
                    default: break;
                }
            }
            throw new IllegalArgumentException("Día de la semana no válido: '" + s + "'");
        }
    }
}
