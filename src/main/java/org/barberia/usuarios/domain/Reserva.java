package org.barberia.usuarios.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.barberia.usuarios.domain.enums.EstadoReserva;

public class Reserva {
    public Integer id_reserva;
    public Integer id_cliente;
    public Integer id_barbero;
    public Integer id_servicio;
    public LocalDate fecha_reserva;
    public LocalTime hora_inicio;
    public LocalTime hora_fin;
    public EstadoReserva estado;
    public BigDecimal total;
    public String notas;
    public BigDecimal precio_servicio;
    public BigDecimal monto_anticipo;
    public LocalDateTime created_at;
    public LocalDateTime updated_at;
}
