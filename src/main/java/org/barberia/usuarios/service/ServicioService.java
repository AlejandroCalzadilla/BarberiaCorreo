package org.barberia.usuarios.service;

import java.math.BigDecimal;

import org.barberia.usuarios.domain.Servicio;
import org.barberia.usuarios.domain.enums.EstadoItem;
import org.barberia.usuarios.mapper.ServicioMapper;
import org.barberia.usuarios.repository.ServicioRepository;
import org.barberia.usuarios.validation.ServicioValidator;

public class ServicioService {
    private final ServicioRepository repo;
    private final ServicioValidator validator;

    public ServicioService(
            ServicioRepository repo,
            ServicioValidator validator) {
        this.repo = repo;
        this.validator = validator;
    }

    public String getAllAsTable() {
        return ServicioMapper.obtenerTodosTable(repo.findAll());
    }

    public String getByIdAsTable(Integer id) {
        return repo.findById(id).map(ServicioMapper::obtenerUnoTable).orElse("No se encontró servicio con id=" + id);
    }

    public Servicio create(String nombre,
            String descripcion,
            Integer duracion_minutos_aprox,
            BigDecimal precio,
            String imagen) {
        Servicio s = new Servicio(
                nombre,
                descripcion,
                duracion_minutos_aprox,
                precio,
                EstadoItem.activo,
                imagen);
        validator.validar(s);
        return repo.save(s);
    }

    public String update(Integer id, String nombre, String descripcion,
            Integer duracion_minutos_aprox,
            BigDecimal precio,
            String imagen) {
        Servicio s = new Servicio(
                nombre,
                descripcion,
                duracion_minutos_aprox,
                precio,
                EstadoItem.activo,
                imagen);
        s.id_servicio = id;
        validator.validar(s);
        return "Servicio actualizado:  " + "\n" + ServicioMapper.obtenerUnoTable(repo.save(s));
    }

    public String delete(Integer id) {
        repo.deleteById(id);
        return "Servicio con id=" + id + " eliminado (soft delete) " +
                "\n" + getByIdAsTable(id);
    }

    /**
     * Alterna el estado de un servicio: si está activo lo desactiva (soft delete),
     * y si está inactivo lo activa.
     * @param id id del servicio
     * @return mensaje con el resultado y la representación en tabla
     */
    public String toggleActive(Integer id) {
        return repo.findById(id)
            .map(s -> {
                if (s.estado == EstadoItem.activo) {
                    repo.softDeleteById(id);
                    return "Servicio con id=" + id + " desactivado (soft delete)" + "\n" + getByIdAsTable(id);
                } else {
                    repo.activateById(id);
                    return "Servicio con id=" + id + " activado" + "\n" + getByIdAsTable(id);
                }
            })
            .orElse("No se encontró servicio con id=" + id);
    }
}
