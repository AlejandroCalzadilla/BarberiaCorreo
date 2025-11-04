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

    public Categoria create(Categoria c) {
        validator.validar(c);
        return repo.save(c);
    }

    public String  update(Integer id, Categoria c) {
        validator.validar(c);
        c.id_categoria = id;
        return "categoria actualizada: " + repo.save(c).toString();
    }

    public String softDeleteById(Integer id) {
       return " categoria ahora es :  " + repo.softDeleteById(id);
    }

    public void deleteById(Integer id) {
        repo.deleteById(id);
    }
}
