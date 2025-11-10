package org.barberia.usuarios.domain.enums;

public enum TipoPago {
    anticipo,
    pago_final,
    pago_completo
    ;

    /**
     * Convierte una cadena a {@link TipoPago} de forma tolerante.
     * Acepta mayúsculas/minúsculas, guiones, espacios y guiones bajos.
     */
    public static TipoPago parse(String s) {
        if (s == null) throw new IllegalArgumentException("TipoPago no puede ser nulo");
        String n = s.trim().toLowerCase().replace('-', '_').replace(' ', '_');
        switch (n) {
            case "anticipo": return anticipo;
            case "pago_final":
            case "pagofinal":
            case "pago-final":
            case "pago final": return pago_final;
            case "pago_completo":
            case "pagocompleto":
            case "pago-completo":
            case "pago completo": return pago_completo;
            default: throw new IllegalArgumentException("Tipo de pago no válido: '" + s + "'");
        }
    }
}
