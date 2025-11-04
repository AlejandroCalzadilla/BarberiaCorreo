package org.barberia.usuarios.service;

import org.barberia.usuarios.domain.Reserva;
import org.barberia.usuarios.mapper.ReservaMapper;
import org.barberia.usuarios.repository.ReservaRepository;
import org.barberia.usuarios.validation.ReservaValidator;

public class ReservaService {
    private final ReservaRepository repo;
    private final ReservaValidator validator;

    public ReservaService(ReservaRepository repo, ReservaValidator validator) {
        this.repo = repo;
        this.validator = validator;
    }

    public String getAllAsTable() {
        return ReservaMapper.obtenerTodosTable(repo.findAll());
    }

    public String getByIdAsTable(Integer id) {
        return repo.findById(id).map(ReservaMapper::obtenerUnoTable).orElse("No se encontró reserva con id=" + id);
    }

    public Reserva create(Reserva r) {
        validator.validar(r);
        
        return repo.save(r);
    }

    public Reserva update(Integer id, Reserva r) {
        validator.validar(r);
        r.id_reserva = id;
        return repo.save(r);
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
