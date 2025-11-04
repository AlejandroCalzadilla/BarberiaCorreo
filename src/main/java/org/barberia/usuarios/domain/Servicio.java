package org.barberia.usuarios.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.barberia.usuarios.domain.enums.EstadoItem;

public class Servicio {
    public Integer id_servicio;
   
    public String nombre;
    public String descripcion;
    public Integer duracion_minutos_aprox;
    public BigDecimal precio;
    public EstadoItem estado;
    public String imagen;
    public LocalDateTime created_at;
    public LocalDateTime updated_at;
}
