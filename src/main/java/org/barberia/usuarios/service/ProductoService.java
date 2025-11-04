package org.barberia.usuarios.service;

import org.barberia.usuarios.domain.Categoria;
import org.barberia.usuarios.domain.Producto;
import org.barberia.usuarios.mapper.ProductoMapper;
import org.barberia.usuarios.repository.CategoriaRepository;
import org.barberia.usuarios.repository.ProductoRepository;
import org.barberia.usuarios.validation.ProductoValidator;

import java.util.List;

public class ProductoService {
    private final ProductoRepository repo;
    private final CategoriaRepository categoriaRepo;
    private final ProductoValidator validator;

    public ProductoService(ProductoRepository repo, CategoriaRepository categoriaRepo, ProductoValidator validator) {
        this.repo = repo;
        this.categoriaRepo = categoriaRepo;
        this.validator = validator;
    }

    public String getAllAsTable() {
        List<Producto> list = repo.findAll();
        return ProductoMapper.obtenerTodosTable(list);
    }

    public String getByIdAsTable(Integer id) {
        return repo.findById(id).map(ProductoMapper::obtenerUnoTable)
                .orElse("No se encontró producto con id=" + id);
    }

    public Producto create(Producto p) {

        Categoria categoria = categoriaRepo.findById(p.id_categoria)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        if (categoria.estado != categoria.estado.activa) {
            throw new RuntimeException("No se puede asignar un producto a una categoria inactiva");
        }
        validator.validar(p);
        return repo.save(p);
    }

    public Producto update(Integer id, Producto p) {
        validator.validar(p);
        Categoria categoria = categoriaRepo.findById(p.id_categoria)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        if (categoria.estado != categoria.estado.activa) {
            throw new RuntimeException("No se puede asignar un producto a una categoria inactiva");
        }
        p.id_producto = id;
        return repo.save(p);
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
