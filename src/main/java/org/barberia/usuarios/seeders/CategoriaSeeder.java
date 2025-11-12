package org.barberia.usuarios.seeders;

import java.util.ArrayList;
import java.util.List;
import org.barberia.usuarios.domain.Categoria;
import org.barberia.usuarios.domain.enums.EstadoCategoria;
import org.barberia.usuarios.service.CategoriaService;

public class CategoriaSeeder {

    private CategoriaService categoriaService;
    private List<Integer> createdIds = new ArrayList<>();

    CategoriaSeeder(
            CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    public void seed() {
        System.out.println("  → Creando categorías...");
        try {
            Categoria categoria1 = new Categoria();
            categoria1.nombre = "Cuidado del Cabello";
            categoria1.descripcion = "Productos para el cuidado y estilo del cabello.";
            categoria1.estado = EstadoCategoria.activa;
            categoria1 = categoriaService.create(categoria1.nombre, categoria1.descripcion);
            createdIds.add(categoria1.id_categoria);
            System.out.println("  ✓ Categoría creada: " + categoria1.nombre + " (ID: " + categoria1.id_categoria + ")");

            Categoria categoria2 = new Categoria();
            categoria2.nombre = "Afeitado Clásico";
            categoria2.descripcion = "Productos para afeitado tradicional con navaja.";
            categoria2.estado = EstadoCategoria.activa;
            categoria2 = categoriaService.create(categoria2.nombre, categoria2.descripcion);
            createdIds.add(categoria2.id_categoria);
            System.out.println("  ✓ Categoría creada: " + categoria2.nombre + " (ID: " + categoria2.id_categoria + ")");

            Categoria categoria3 = new Categoria();
            categoria3.nombre = "Cuidado de la Barba";
            categoria3.descripcion = "Productos para el mantenimiento y estilo de la barba.";
            categoria3.estado = EstadoCategoria.activa;
            categoria3 = categoriaService.create(categoria3.nombre, categoria3.descripcion);
            createdIds.add(categoria3.id_categoria);
            System.out.println("  ✓ Categoría creada: " + categoria3.nombre + " (ID: " + categoria3.id_categoria + ")");

            Categoria categoria4 = new Categoria();
            categoria4.nombre = "Productos Desechables";
            categoria4.descripcion = "Artículos de un solo uso para higiene y protección.";
            categoria4.estado = EstadoCategoria.activa;
            categoria4 = categoriaService.create(categoria4.nombre, categoria4.descripcion);
            createdIds.add(categoria4.id_categoria);
            System.out.println("  ✓ Categoría creada: " + categoria4.nombre + " (ID: " + categoria4.id_categoria + ")");

            System.out.println("  → Total de categorías creadas: " + createdIds.size());
        } catch (Exception e) {
            System.err.println("  ✗ Error durante seed de categorías: " + e.getMessage());
            rollback();
            throw e;
        }
    }

    public void rollback() {
        List<Categoria> categorias = categoriaService.getAll();
        for (Categoria c : categorias) {
            if (c.id_categoria.equals(c.id_categoria)) {
                categoriaService.deleteById(c.id_categoria);
            }
        }
    }
}
