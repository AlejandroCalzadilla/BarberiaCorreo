package org.barberia.usuarios.domain;

import java.time.LocalDateTime;
import org.barberia.usuarios.domain.enums.*;
import org.barberia.usuarios.mapper.CategoriaMapper;

public class Categoria {
   
    public Integer id_categoria;
    public String nombre;
    public String descripcion;
    public LocalDateTime created_at;
    public LocalDateTime updated_at;
    public EstadoCategoria estado;

    @Override
    public String toString() {
        return CategoriaMapper.obtenerUnoTable(
            this
        );
    }
}
