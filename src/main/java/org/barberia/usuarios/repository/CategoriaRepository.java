package org.barberia.usuarios.repository;

import org.barberia.usuarios.domain.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository {
    List<Categoria> findAll();
    Optional<Categoria> findById(Integer id);
    Categoria save(Categoria c);
    void deleteById(Integer id);

    String softDeleteById(Integer id);
    void activateById(Integer id);
}
