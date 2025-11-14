package org.barberia.usuarios.validation;

import org.barberia.usuarios.domain.Pago;

public class PagoValidator {
    public void validar(Pago p){
        if (p == null) throw new IllegalArgumentException("Pago no puede ser null");
        if (p.monto_total == null || p.monto_total.signum() < 0) throw new IllegalArgumentException("monto_total >= 0");
        if (p.metodo_pago == null ) throw new IllegalArgumentException("metodo_pago no puede ser vacio");
        if (p.tipo_pago == null) throw new IllegalArgumentException("tipo_pago no puede ser null");
        if (p.notas != null && p.notas.length() > 500) throw new IllegalArgumentException("notas maximo 500 caracteres");
    }
}
