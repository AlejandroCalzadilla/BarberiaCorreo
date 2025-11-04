package org.barberia.usuarios.service;

import java.util.List;

import org.barberia.usuarios.domain.Horario;
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

    public Horario create(Horario h) {
        validator.validar(h);
        List<Horario> horarios = repo.findAll();
        for (Horario horarioExistente : horarios) {
            if (horarioExistente.id_barbero.equals(h.id_barbero) &&
                horarioExistente.dia_semana == h.dia_semana) {
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
        return repo.save(h);
    }

    public Horario update(Integer id, Horario h) {
        validator.validar(h);
        h.id_horario = id;
        
        // Validar que no se solape con otros horarios (excepto consigo mismo)
        List<Horario> horarios = repo.findAll();
        for (Horario horarioExistente : horarios) {
            if (!horarioExistente.id_horario.equals(id) &&
                horarioExistente.id_barbero.equals(h.id_barbero) &&
                horarioExistente.dia_semana == h.dia_semana) {
                if (horariosSeSuperponen(horarioExistente, h)) {
                    throw new IllegalArgumentException(
                        String.format("El horario actualizado se solapa con un horario existente en %s de %s a %s.",
                                     h.dia_semana, horarioExistente.hora_inicio, horarioExistente.hora_fin));
                }
            }
        }
        
        return repo.save(h);
    }

    public void delete(Integer id) {
        repo.deleteById(id);
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
