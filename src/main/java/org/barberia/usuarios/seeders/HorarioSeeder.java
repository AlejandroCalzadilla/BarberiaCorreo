
package org.barberia.usuarios.seeders;

import java.util.ArrayList;
import java.util.List;

import org.barberia.usuarios.domain.Barbero;
import org.barberia.usuarios.domain.enums.DiaSemana;
import org.barberia.usuarios.repository.BarberoRepository;
import org.barberia.usuarios.service.HorarioService;

public class HorarioSeeder {

    private HorarioService horarioService;
    private BarberoRepository barberoRepository;
    private final List<Integer> createdIds = new ArrayList<>();

    HorarioSeeder(HorarioService horarioService, BarberoRepository barberoRepository) {
        this.horarioService = horarioService;
        this.barberoRepository = barberoRepository;
    }

    public void seed() {
        System.out.println("  → Creando horarios para barberos...");
        
        // Obtener todos los barberos
        List<Barbero> barberos = barberoRepository.findAll();
        
        if (barberos.isEmpty()) {
            System.out.println("  ⚠ No hay barberos disponibles para crear horarios");
            return;
        }
        
        // Días de la semana (lunes a viernes)
        DiaSemana[] diasLaborales = {
            DiaSemana.lunes,
            DiaSemana.martes,
            DiaSemana.miercoles,
            DiaSemana.jueves,
            DiaSemana.viernes
        };
        
        int horariosCreados = 0;
        
        // Para cada barbero, crear horarios de lunes a viernes
        for (Barbero barbero : barberos) {
            for (DiaSemana dia : diasLaborales) {
                try {
                    // Crear horario usando el servicio
                    // El servicio espera strings, así que convertimos
                    var horario = horarioService.create(
                    barbero.id_barbero,    
                    dia.name(),           // día de la semana
                        "09:00",             // hora de inicio
                        "18:00"             // hora de fin
                           // id del barbero
                    );
                    
                    createdIds.add(horario.id_horario);
                    horariosCreados++;
                    
                } catch (Exception e) {
                    System.err.println("  ✗ Error creando horario " + dia + " para barbero ID " + 
                                     barbero.id_barbero + ": " + e.getMessage());
                }
            }
        }
        
        System.out.println("  → Total de horarios creados: " + horariosCreados + 
                         " (" + barberos.size() + " barberos × 5 días)");
    }

    public void rollback() {
        for (Integer id : new ArrayList<>(createdIds)) {
            try {
                horarioService.delete(id);
            } catch (Exception ex) {
                System.err.println("  ✗ Error eliminando horario id=" + id + " durante rollback: " + ex.getMessage());
            }
            createdIds.remove(id);
        }
        System.out.println("  Rollback de horarios completado");
    }

}