package org.barberia.usuarios.seeders;

import java.util.ArrayList;
import java.util.List;

import org.barberia.usuarios.domain.Categoria;
import org.barberia.usuarios.service.CategoriaService;

public class CategoriaSeeder {

    private CategoriaService categoriaService;

    private List<Integer> createdIds = new ArrayList<>();

    CategoriaSeeder(
            CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    public void seed() {
        try {
            Categoria categoria1 = new Categoria();
            categoria1.nombre = "Cuidado del Cabello";
            categoria1.descripcion = "Productos para el cuidado y estilo del cabello.";
            categoria1.estado = categoria1.estado.activa;
            categoria1 = categoriaService.create(categoria1);
            createdIds.add(categoria1.id_categoria);

            Categoria categoria2 = new Categoria();
            categoria2.nombre = "Afeitado Clásico";
            categoria2.descripcion = "productos para afeitado tradicional con navaja.";
            categoria2.estado = categoria2.estado.activa;
            categoria2 = categoriaService.create(categoria2);
            createdIds.add(categoria2.id_categoria);

            Categoria categoria3 = new Categoria();
            categoria3.nombre = "Cuidado de la Barba";
            categoria3.descripcion = "Productos para el mantenimiento y estilo de la barba.";
            categoria3.estado = categoria3.estado.activa;
            categoria3 = categoriaService.create(categoria3);
            createdIds.add(categoria3.id_categoria);

        } catch (Exception e) {
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
