package org.barberia.usuarios.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.barberia.usuarios.domain.enums.EstadoItem;
import org.barberia.usuarios.mapper.ServicioMapper;

public class Servicio {


    public Servicio(){
        
    };
    public Servicio(String nombre,
            String descripcion,
            Integer duracion_minutos_aprox,
            BigDecimal precio,
            EstadoItem estado,
            String imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion_minutos_aprox = duracion_minutos_aprox;
        this.precio = precio;
        this.estado = estado;
        this.imagen = imagen;
    }
    public Integer id_servicio;
    public String nombre;
    public String descripcion;
    public Integer duracion_minutos_aprox;
    public BigDecimal precio;
    public EstadoItem estado;
    public String imagen;
    public LocalDateTime created_at;
    public LocalDateTime updated_at;

    @Override
    public String toString() {
        return ServicioMapper.obtenerUnoTable(this);
    }
}
