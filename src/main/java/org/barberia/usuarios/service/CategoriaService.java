package org.barberia.usuarios.service;

import org.barberia.usuarios.domain.Categoria;
import org.barberia.usuarios.domain.enums.EstadoCategoria;
import org.barberia.usuarios.mapper.CategoriaMapper;
import org.barberia.usuarios.repository.CategoriaRepository;
import org.barberia.usuarios.validation.CategoriaValidator;

import java.util.List;

public class CategoriaService {
    private final CategoriaRepository repo;
    private final CategoriaValidator validator;

    public CategoriaService(CategoriaRepository repo, CategoriaValidator validator) {
        this.repo = repo;
        this.validator = validator;
    }

    public String getAllAsTable() {
        List<Categoria> list = repo.findAll();
        return CategoriaMapper.obtenerTodosTable(list);
    }

    public List<Categoria> getAll() {
        return repo.findAll();
    }

    public String getByIdAsTable(Integer id) {
        return repo.findById(id)
                .map(CategoriaMapper::obtenerUnoTable)
                .orElse("No se encontró categoria con id=" + id);
    }

    public Categoria create(String nombre, String descripcion) {
        Categoria c = new Categoria();
        c.nombre = nombre;
        c.descripcion = descripcion;
        c.estado = EstadoCategoria.activa;
        validator.validar(c);
        return repo.save(c);
    }

    public String  update(Integer id, String nombre, String descripcion) {
        Categoria c = new Categoria();
        c.id_categoria = id;
        c.nombre = nombre;
        c.descripcion = descripcion;
        validator.validar(c);
        Categoria cat = repo.save(c);
        System.out.println(cat);
        return  cat.toString();
    }

    public String  deleteById(Integer id) {
        repo.deleteById(id);
        return "Categoria con id=" + id + " eliminada (soft delete) " +
                "\n" + getByIdAsTable(id);
    }

    /**
     * Alterna el estado de una categoría: si está activa la desactiva (soft delete),
     * y si está inactiva la activa.
     * @param id id de la categoría
     * @return mensaje con el resultado y la representación en tabla
     */
    public String toggleActive(Integer id) {
        return repo.findById(id)
            .map(c -> {
                if (c.estado == EstadoCategoria.activa) {
                    repo.softDeleteById(id);
                    return "Categoria con id=" + id + " desactivada (soft delete)" + "\n" + getByIdAsTable(id);
                } else {
                    repo.activateById(id);
                    return "Categoria con id=" + id + " activada" + "\n" + getByIdAsTable(id);
                }
            })
            .orElse("No se encontró categoria con id=" + id);
    }
}
