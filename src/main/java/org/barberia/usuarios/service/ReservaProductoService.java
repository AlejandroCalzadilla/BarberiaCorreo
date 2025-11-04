package org.barberia.usuarios.service;

import org.barberia.usuarios.domain.ServicioProducto;
import org.barberia.usuarios.mapper.ServicioProductoMapper;
import org.barberia.usuarios.repository.ServicioProductoRepository;
import org.barberia.usuarios.validation.ServicioProductoValidator;


public class ReservaProductoService {
    private final ServicioProductoRepository repo;
    private final ServicioProductoValidator validator;

    public ReservaProductoService(ServicioProductoRepository repo, ServicioProductoValidator validator) {
        this.repo = repo;
        this.validator = validator;
    }

    public String getAllAsTable() {
        return ServicioProductoMapper.obtenerTodosTable(repo.findAll());
    }

    public String getByIdAsTable(Integer id) {
        return repo.findById(id).map(ServicioProductoMapper::obtenerUnoTable)
                .orElse("No se encontró servicio_producto con id=" + id);
    }

    public ServicioProducto create(ServicioProducto sp) {
        validator.validar(sp);
        return repo.save(sp);
    }

    public ServicioProducto update(Integer id, ServicioProducto sp) {
        validator.validar(sp);
        sp.id_servicio_producto = id;
        return repo.save(sp);
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
