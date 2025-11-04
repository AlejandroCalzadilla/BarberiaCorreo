package org.barberia.usuarios.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Cliente {
    public Integer id_cliente;
    public Integer id_usuario;
    public String fecha_nacimiento;
    public String ci;
    public LocalDateTime created_at;
    public LocalDateTime updated_at;
}
