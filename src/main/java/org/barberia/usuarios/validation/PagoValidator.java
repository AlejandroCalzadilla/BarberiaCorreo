package org.barberia.usuarios.validation;

import org.barberia.usuarios.domain.Pago;

public class PagoValidator {
    public void validar(Pago p){
        if (p == null) throw new IllegalArgumentException("Pago no puede ser null");
        if (p.monto_total == null || p.monto_total.signum() < 0) throw new IllegalArgumentException("monto_total >= 0");
    }
}
