package org.barberia.usuarios.service;

import org.barberia.usuarios.domain.Categoria;
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
        validator.validar(c);
        return repo.save(c);
    }

    public String  update(Integer id, String nombre, String descripcion) {
        Categoria c = new Categoria();
        c.id_categoria = id;
        c.nombre = nombre;
        c.descripcion = descripcion;
        validator.validar(c);
        return "categoria actualizada: " + repo.save(c).toString();
    }

    public String softDeleteById(Integer id) {
       return " categoria ahora es :  " + repo.softDeleteById(id);
    }

    public String  deleteById(Integer id) {
        repo.deleteById(id);
        return "Categoria con id=" + id + " eliminada (soft delete) " +
                "\n" + getByIdAsTable(id);
    }
}
