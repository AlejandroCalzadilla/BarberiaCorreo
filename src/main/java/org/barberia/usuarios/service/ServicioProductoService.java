package org.barberia.usuarios.service;

import org.barberia.usuarios.domain.ServicioProducto;
import org.barberia.usuarios.mapper.ServicioProductoMapper;
import org.barberia.usuarios.repository.ServicioProductoRepository;
import org.barberia.usuarios.validation.ServicioProductoValidator;


public class ServicioProductoService {
    private final ServicioProductoRepository repo;
    private final ServicioProductoValidator validator;

    public ServicioProductoService(ServicioProductoRepository repo, ServicioProductoValidator validator) {
        this.repo = repo;
        this.validator = validator;
    }

    public String getAllAsTable() {
        return ServicioProductoMapper.obtenerTodosTable(repo.findAll());
    }

    /* public String getByIdAsTable(Integer id) {
        return repo.findById(id).map(ServicioProductoMapper::obtenerUnoTable)
                .orElse("No se encontró servicio_producto con id=" + id);
    } */

    public ServicioProducto create(ServicioProducto sp) {
        validator.validar(sp);
        return repo.insert(sp);
    }

    public ServicioProducto update( Integer id_servicio, Integer id_producto, Integer cantidad) {
        ServicioProducto sp = new ServicioProducto();
        sp.id_servicio = id_servicio;
        sp.id_producto = id_producto;
        sp.cantidad = cantidad;
        validator.validar(sp);
        return repo.update(sp);
    }

    public void delete(Integer id_servicio , Integer id_producto) {
        repo.delete(id_servicio, id_producto);
    }
}
