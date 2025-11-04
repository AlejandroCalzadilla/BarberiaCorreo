package org.barberia.usuarios.service;

import org.barberia.usuarios.domain.Pago;
import org.barberia.usuarios.mapper.PagoMapper;
import org.barberia.usuarios.repository.PagoRepository;
import org.barberia.usuarios.validation.PagoValidator;

public class PagoService {
    private final PagoRepository repo;
    private final PagoValidator validator;

    public PagoService(PagoRepository repo, PagoValidator validator) {
        this.repo = repo;
        this.validator = validator;
    }

    public String getAllAsTable() {
        return PagoMapper.obtenerTodosTable(repo.findAll());
    }

    public String getByIdAsTable(Integer id) {
        return repo.findById(id).map(PagoMapper::obtenerUnoTable).orElse("No se encontró pago con id=" + id);
    }

    public Pago create(Pago p) {
        validator.validar(p);
        return repo.save(p);
    }

    public Pago update(Integer id, Pago p) {
        validator.validar(p);
        p.id_pago = id;
        return repo.save(p);
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
