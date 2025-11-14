package org.barberia.usuarios.domain;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.barberia.usuarios.domain.enums.DiaSemana;
import org.barberia.usuarios.domain.enums.EstadoHorario;
import org.barberia.usuarios.mapper.HorarioMapper;
import org.barberia.usuarios.mapper.ProductoMapper;

public class Horario {
    public Integer id_horario;
    public Integer id_barbero;
    public DiaSemana dia_semana;
    public LocalTime hora_inicio;
    public LocalTime hora_fin;
    public EstadoHorario estado;
    public LocalDateTime created_at;
    public LocalDateTime updated_at;
    
    @Override
    public String toString() {
        return HorarioMapper.obtenerUnoTable(
                this);
    }

}
