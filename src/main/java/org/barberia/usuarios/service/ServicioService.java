package org.barberia.usuarios.service;

import org.barberia.usuarios.domain.Servicio;
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

    public Servicio create(Servicio s) {
        validator.validar(s);
        return repo.save(s);
    }

    public Servicio update(Integer id, Servicio s) {
        validator.validar(s);
        s.id_servicio = id;
        return repo.save(s);
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
