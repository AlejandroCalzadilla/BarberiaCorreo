
package org.barberia.usuarios.seeders;

import org.barberia.usuarios.domain.Horario;
import org.barberia.usuarios.service.HorarioService;

public class HorarioSeeder {

    private HorarioService horarioService;

    HorarioSeeder(HorarioService horarioService) {
        this.horarioService = horarioService;

    }

    public void seed() {
        Horario horario1 = new Horario();
        horario1.dia_semana = horario1.dia_semana.lunes;
        horario1.estado = horario1.estado.activo;
        horario1.hora_inicio = horario1.hora_inicio.of(9, 0);
        horario1.hora_fin = horario1.hora_fin.of(18, 0);
        horario1.id_barbero = 3;
        horario1 = horarioService.create(horario1);

    }

}