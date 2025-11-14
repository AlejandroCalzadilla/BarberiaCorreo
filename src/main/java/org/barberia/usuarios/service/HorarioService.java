package org.barberia.usuarios.service;

import java.util.List;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import org.barberia.usuarios.domain.Horario;
import org.barberia.usuarios.domain.enums.DiaSemana;
import org.barberia.usuarios.mapper.HorarioMapper;
import org.barberia.usuarios.repository.HorarioRepository;
import org.barberia.usuarios.validation.HorarioValidator;

public class HorarioService {
    private final HorarioRepository repo;
    private final HorarioValidator validator;

    public HorarioService(HorarioRepository repo, HorarioValidator validator) {
        this.repo = repo;
        this.validator = validator;
    }

    public String getAllAsTable() {
        return HorarioMapper.obtenerTodosTable(repo.findAll());
    }

    public String getByIdAsTable(Integer id) {
        return repo.findById(id).map(HorarioMapper::obtenerUnoTable).orElse("No se encontró horario con id=" + id);
    }

    public Horario create( Integer id_barbero,String dia_semana, String hora_inicio, String hora_fin) {
        // Normalizar y convertir la entrada al enum de forma tolerante
        DiaSemana dia = DiaSemana.parse(dia_semana);
        Horario h = new Horario();
        // asignar enum y parsear las horas a LocalTime
        h.dia_semana = dia;
        try {
            h.hora_inicio = LocalTime.parse(hora_inicio);
            h.hora_fin = LocalTime.parse(hora_fin);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Formato de hora inválido. Use HH:mm, por ejemplo '09:30'");
        }
        h.id_barbero = id_barbero;
        validator.validar(h);
        List<Horario> horarios = repo.findAll();
        for (Horario horarioExistente : horarios) {
            if (horarioExistente.id_barbero.equals(h.id_barbero) &&
                    horarioExistente.dia_semana.equals(h.dia_semana)) {
                // Verificar si hay solapamiento de horarios
                if (horariosSeSuperponen(horarioExistente, h)) {
                    throw new IllegalArgumentException(
                            String.format("El barbero ya tiene un horario que se solapa en %s de %s a %s. " +
                                    "No se puede crear el horario de %s a %s.",
                                    h.dia_semana, horarioExistente.hora_inicio, horarioExistente.hora_fin,
                                    h.hora_inicio, h.hora_fin));
                }
            }
        }
      return   repo.save(h);
    }

    public String update( 
    Integer id_horario,
    Integer id_barbero,
    String dia_semana,
    LocalTime hora_inicio,
     LocalTime hora_fin
    ) 
    
    {
        Horario h = new Horario();
        h.id_horario=id_horario;
        h.id_barbero= id_barbero;
        h.dia_semana = DiaSemana.parse(dia_semana);
        h.hora_inicio = hora_inicio;
        h.hora_fin = hora_fin;
        validator.validar(h);
        h.id_horario = id_horario;
        
        // Validar que no se solape con otros horarios (excepto consigo mismo)
        List<Horario> horarios = repo.findAll();
        for (Horario horarioExistente : horarios) {
            if (!horarioExistente.id_horario.equals(id_horario) &&
                horarioExistente.id_barbero.equals(h.id_barbero) &&
                horarioExistente.dia_semana.equals(h.dia_semana)) {
                if (horariosSeSuperponen(horarioExistente, h)) {
                    throw new IllegalArgumentException(
                        String.format("El horario actualizado se solapa con un horario existente en %s de %s a %s.",
                                     h.dia_semana, horarioExistente.hora_inicio, horarioExistente.hora_fin));
                }
            }
        }
        return HorarioMapper.obtenerUnoTable(repo.save(h));
    }

    public String delete(Integer id) {
        repo.deleteById(id);
        return "Horario con id=" + id + " eliminado " ;
    }

    /**
     * Verifica si dos horarios se superponen.
     * Dos horarios se superponen si:
     * - El inicio del horario1 está dentro del horario2
     * - El fin del horario1 está dentro del horario2
     * - El horario1 contiene completamente al horario2
     */
    private boolean horariosSeSuperponen(Horario h1, Horario h2) {
        // h1.inicio < h2.fin && h1.fin > h2.inicio
        return h1.hora_inicio.isBefore(h2.hora_fin) && h1.hora_fin.isAfter(h2.hora_inicio);
    }
}
