package org.barberia.usuarios.seeders;

import java.util.ArrayList;
import java.util.List;

import org.barberia.usuarios.domain.Producto;
import org.barberia.usuarios.domain.enums.EstadoItem;
import org.barberia.usuarios.repository.ProductoRepository;

/**
 * Seeder de productos que usa directamente el repository (no el service).
 * Guarda los ids creados para permitir rollback.
 */
public class ProductoSeeder {

    private final ProductoRepository productoRepository;
    private final List<Integer> createdIds = new ArrayList<>();

    public ProductoSeeder(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public void seed() {
        try {
            Producto p1 = new Producto();
            p1.id_categoria = 1; // ajustar según categorías existentes
            p1.codigo = "GEL001";
            p1.nombre = "Gel fijador";
            p1.descripcion = "Gel fijador de alta duración para peinados.";
            p1.precio_compra = new java.math.BigDecimal("3.50");
            p1.precio_venta = new java.math.BigDecimal("7.00");
            p1.stock_actual = 100;
            p1.stock_minimo = 5;
            p1.unidad_medida = "unidad";
            p1.estado = EstadoItem.activo;
            p1.imagenurl = "";
            productoRepository.save(p1);
            createdIds.add(p1.id_producto);

            Producto p2 = new Producto();
            p2.id_categoria = 1;
            p2.codigo = "SHM001";
            p2.nombre = "Shampoo nutritivo";
            p2.descripcion = "Shampoo nutritivo para todo tipo de cabello.";
            p2.precio_compra = new java.math.BigDecimal("4.00");
            p2.precio_venta = new java.math.BigDecimal("8.50");
            p2.stock_actual = 50;
            p2.stock_minimo = 5;
            p2.unidad_medida = "botella";
            p2.estado = EstadoItem.activo;
            p2.imagenurl = "";
            productoRepository.save(p2);
            createdIds.add(p2.id_producto);

            Producto p3 = new Producto();
            p3.id_categoria = 3;
            p3.codigo = "ACEB001";
            p3.nombre = "Aceite para barba";
            p3.descripcion = "Aceite hidratante para barba y piel.";
            p3.precio_compra = new java.math.BigDecimal("2.50");
            p3.precio_venta = new java.math.BigDecimal("6.00");
            p3.stock_actual = 30;
            p3.stock_minimo = 2;
            p3.unidad_medida = "frasco";
            p3.estado = EstadoItem.activo;
            p3.imagenurl = "";
            productoRepository.save(p3);
            createdIds.add(p3.id_producto);

        } catch (Exception e) {
            rollback();
            throw e;
        }
    }

    public void rollback() {
        for (Integer id : new ArrayList<>(createdIds)) {
            try {
                productoRepository.deleteById(id);
            } catch (Exception ex) {
                System.err.println("Error eliminando producto id=" + id + " durante rollback: " + ex.getMessage());
            }
            createdIds.remove(id);
        }
    }
}
