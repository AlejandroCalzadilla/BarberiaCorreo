package org.barberia.usuarios.domain;

import java.time.LocalDateTime;

import org.barberia.usuarios.domain.enums.EstadoBarbero;
import org.barberia.usuarios.mapper.BarberoMapper;
import org.barberia.usuarios.validation.BarberoValidator;

public class Barbero {
    public Integer id_barbero;
    public Integer id_usuario;
    public String especialidad;
    public String foto_perfil;
    public EstadoBarbero estado;
    public LocalDateTime created_at;
    public LocalDateTime updated_at;



    @Override
    public String toString() {
        return BarberoMapper.obtenerUnoTable(this);
    }

}
