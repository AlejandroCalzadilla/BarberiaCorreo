package org.barberia.usuarios.service;

import org.barberia.usuarios.domain.Categoria;
import org.barberia.usuarios.domain.Producto;
import org.barberia.usuarios.mapper.ProductoMapper;
import org.barberia.usuarios.repository.CategoriaRepository;
import org.barberia.usuarios.repository.ProductoRepository;
import org.barberia.usuarios.validation.ProductoValidator;

import java.math.BigDecimal;
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

    public List<Producto> getAllAsTable() {
        List<Producto> list = repo.findAll();
        return list ;
    }

    public String getByIdAsTable(Integer id) {
        return repo.findById(id).map(ProductoMapper::obtenerUnoTable)
                .orElse("No se encontró producto con id=" + id);
    }

    public Producto create(int id_categoria, String codigo, String nombre, String descripcion,
            BigDecimal precio_compra, BigDecimal precio_venta,
            int stock_actual, int stock_minimo, String unidad_medida, String imagenurl) {

        Producto nuevoProducto = new Producto();
        nuevoProducto.codigo = codigo;
        nuevoProducto.nombre = nombre;
        nuevoProducto.descripcion = descripcion;
        nuevoProducto.precio_compra = precio_compra;
        nuevoProducto.precio_venta = precio_venta;
        nuevoProducto.stock_actual = stock_actual;
        nuevoProducto.stock_minimo = stock_minimo;
        nuevoProducto.imagenurl = imagenurl;

        Categoria categoria = categoriaRepo.findById(id_categoria)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        if (categoria.estado != categoria.estado.activa) {
            throw new RuntimeException("No se puede asignar un producto a una categoria inactiva");
        }
        validator.validar(nuevoProducto);
        return repo.save(nuevoProducto);
    }

    public String update(Integer id,Integer idCategoria, String codigo, String nombre, String descripcion,
            BigDecimal precio_compra, BigDecimal precio_venta,
            int stock_actual, int stock_minimo, String unidad_medida, String imagenurl) {

        Producto p = new Producto();
        p.codigo = codigo;
        p.nombre = nombre;
        p.descripcion = descripcion;
        p.precio_compra = precio_compra;
        p.precio_venta = precio_venta;
        p.stock_actual = stock_actual;
        p.stock_minimo = stock_minimo;
        p.unidad_medida = unidad_medida;
        p.imagenurl = imagenurl;
        p.id_categoria= idCategoria;

        validator.validar(p);
        Categoria categoria = categoriaRepo.findById(p.id_categoria)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        if (categoria.estado != categoria.estado.activa) {
            throw new RuntimeException("No se puede asignar un producto a una categoria inactiva");
        }
        p.id_producto = id;
         repo.save(p);
         return "Producto con id=" + id + " actualizado " +
                "\n" + getByIdAsTable(id);
    }

    public String  delete(Integer id) {
        repo.deleteById(id);
        return "Producto con id=" + id + " eliminado (soft delete) " +
                "\n" + getByIdAsTable(id);
    }
    
}
