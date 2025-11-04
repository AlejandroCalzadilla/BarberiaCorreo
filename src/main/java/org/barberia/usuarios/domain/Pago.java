package org.barberia.usuarios.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.barberia.usuarios.domain.enums.EstadoPago;
import org.barberia.usuarios.domain.enums.MetodoPago;
import org.barberia.usuarios.domain.enums.TipoPago;

public class Pago {
    public Integer id_pago;
    public Integer id_reserva;
    public BigDecimal monto_total;
    public MetodoPago metodo_pago;
    public TipoPago tipo_pago;
    public EstadoPago estado;
    public LocalDateTime fecha_pago;
    public String notas;
    public LocalDateTime created_at;
    public LocalDateTime updated_at;
}
