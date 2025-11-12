package org.barberia.usuarios.seeders;

import java.util.ArrayList;
import java.util.List;

import org.barberia.usuarios.domain.Categoria;
import org.barberia.usuarios.domain.Producto;
import org.barberia.usuarios.domain.enums.EstadoItem;
import org.barberia.usuarios.repository.CategoriaRepository;
import org.barberia.usuarios.repository.ProductoRepository;

/**
 * Seeder de productos que usa directamente el repository (no el service).
 * Guarda los ids creados para permitir rollback.
 */
public class ProductoSeeder {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final List<Integer> createdIds = new ArrayList<>();

    public ProductoSeeder(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public void seed() {
        System.out.println("  → Creando productos...");
        
        // Obtener categorías existentes por nombre
        List<Categoria> categorias = categoriaRepository.findAll();
        
        if (categorias.isEmpty()) {
            System.err.println("  ✗ No hay categorías disponibles. Ejecute CategoriaSeeder primero.");
            return;
        }
        
        // Buscar categorías por nombre
        Integer idCuidadoCabello = null;
        Integer idAfeitado = null;
        Integer idCuidadoBarba = null;
        Integer idDesechables = null;
        
        for (Categoria cat : categorias) {
            if (cat.nombre.equals("Cuidado del Cabello")) {
                idCuidadoCabello = cat.id_categoria;
            } else if (cat.nombre.equals("Afeitado Clásico")) {
                idAfeitado = cat.id_categoria;
            } else if (cat.nombre.equals("Cuidado de la Barba")) {
                idCuidadoBarba = cat.id_categoria;
            } else if (cat.nombre.equals("Productos Desechables")) {
                idDesechables = cat.id_categoria;
            }
        }
        
        try {
            // PRODUCTOS REUTILIZABLES
            
            // Producto 1: Gel fijador (Cuidado del Cabello)
            if (idCuidadoCabello != null) {
                Producto p1 = new Producto();
                p1.id_categoria = idCuidadoCabello;
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
                Producto creado = productoRepository.save(p1);
                createdIds.add(creado.id_producto);
                System.out.println("  ✓ Producto creado: " + p1.nombre);
            }

            // Producto 2: Shampoo nutritivo (Cuidado del Cabello)
            if (idCuidadoCabello != null) {
                Producto p2 = new Producto();
                p2.id_categoria = idCuidadoCabello;
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
                Producto creado = productoRepository.save(p2);
                createdIds.add(creado.id_producto);
                System.out.println("  ✓ Producto creado: " + p2.nombre);
            }

            // Producto 3: Aceite para barba (Cuidado de la Barba)
            if (idCuidadoBarba != null) {
                Producto p3 = new Producto();
                p3.id_categoria = idCuidadoBarba;
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
                Producto creado = productoRepository.save(p3);
                createdIds.add(creado.id_producto);
                System.out.println("  ✓ Producto creado: " + p3.nombre);
            }

            // Producto 4: Espuma de afeitar (Afeitado Clásico)
            if (idAfeitado != null) {
                Producto p4 = new Producto();
                p4.id_categoria = idAfeitado;
                p4.codigo = "ESPA001";
                p4.nombre = "Espuma de afeitar premium";
                p4.descripcion = "Espuma de afeitar para afeitado clásico con navaja.";
                p4.precio_compra = new java.math.BigDecimal("3.00");
                p4.precio_venta = new java.math.BigDecimal("6.50");
                p4.stock_actual = 40;
                p4.stock_minimo = 5;
                p4.unidad_medida = "lata";
                p4.estado = EstadoItem.activo;
                p4.imagenurl = "";
                Producto creado = productoRepository.save(p4);
                createdIds.add(creado.id_producto);
                System.out.println("  ✓ Producto creado: " + p4.nombre);
            }

            // PRODUCTOS DESECHABLES (UN SOLO USO)
            
            if (idDesechables != null) {
                Producto creado; // Variable reutilizable dentro del bloque
                
                // Producto 5: Toalla desechable
                Producto p5 = new Producto();
                p5.id_categoria = idDesechables;
                p5.codigo = "TOAL001";
                p5.nombre = "Toalla desechable";
                p5.descripcion = "Toalla desechable para uso individual por servicio.";
                p5.precio_compra = new java.math.BigDecimal("0.15");
                p5.precio_venta = new java.math.BigDecimal("0.50");
                p5.stock_actual = 500;
                p5.stock_minimo = 50;
                p5.unidad_medida = "unidad";
                p5.estado = EstadoItem.activo;
                p5.imagenurl = "";
                creado = productoRepository.save(p5);
                createdIds.add(creado.id_producto);
                System.out.println("  ✓ Producto creado: " + p5.nombre);

                // Producto 6: Capa desechable
                Producto p6 = new Producto();
                p6.id_categoria = idDesechables;
                p6.codigo = "CAPA001";
                p6.nombre = "Capa desechable";
                p6.descripcion = "Capa protectora desechable para cliente.";
                p6.precio_compra = new java.math.BigDecimal("0.20");
                p6.precio_venta = new java.math.BigDecimal("0.60");
                p6.stock_actual = 300;
                p6.stock_minimo = 30;
                p6.unidad_medida = "unidad";
                p6.estado = EstadoItem.activo;
                p6.imagenurl = "";
                creado = productoRepository.save(p6);
                createdIds.add(creado.id_producto);
                System.out.println("  ✓ Producto creado: " + p6.nombre);

                // Producto 7: Cuchilla desechable
                Producto p7 = new Producto();
                p7.id_categoria = idDesechables;
                p7.codigo = "CUCH001";
                p7.nombre = "Cuchilla desechable";
                p7.descripcion = "Cuchilla de afeitar desechable de un solo uso.";
                p7.precio_compra = new java.math.BigDecimal("0.25");
                p7.precio_venta = new java.math.BigDecimal("0.75");
                p7.stock_actual = 400;
                p7.stock_minimo = 40;
                p7.unidad_medida = "unidad";
                p7.estado = EstadoItem.activo;
                p7.imagenurl = "";
                creado = productoRepository.save(p7);
                createdIds.add(creado.id_producto);
                System.out.println("  ✓ Producto creado: " + p7.nombre);

                // Producto 8: Brocha aplicadora
                Producto p8 = new Producto();
                p8.id_categoria = idDesechables;
                p8.codigo = "BROW001";
                p8.nombre = "Brocha aplicadora desechable";
                p8.descripcion = "Brocha desechable para aplicación de productos.";
                p8.precio_compra = new java.math.BigDecimal("0.10");
                p8.precio_venta = new java.math.BigDecimal("0.35");
                p8.stock_actual = 600;
                p8.stock_minimo = 60;
                p8.unidad_medida = "unidad";
                p8.estado = EstadoItem.activo;
                p8.imagenurl = "";
                creado = productoRepository.save(p8);
                createdIds.add(creado.id_producto);
                System.out.println("  ✓ Producto creado: " + p8.nombre);

                // Producto 9: Guantes desechables
                Producto p9 = new Producto();
                p9.id_categoria = idDesechables;
                p9.codigo = "GUANT001";
                p9.nombre = "Guantes desechables";
                p9.descripcion = "Par de guantes desechables para barbero.";
                p9.precio_compra = new java.math.BigDecimal("0.08");
                p9.precio_venta = new java.math.BigDecimal("0.25");
                p9.stock_actual = 800;
                p9.stock_minimo = 100;
                p9.unidad_medida = "par";
                p9.estado = EstadoItem.activo;
                p9.imagenurl = "";
                creado = productoRepository.save(p9);
                createdIds.add(creado.id_producto);
                System.out.println("  ✓ Producto creado: " + p9.nombre);

                // Producto 10: Peine desechable
                Producto p10 = new Producto();
                p10.id_categoria = idDesechables;
                p10.codigo = "PEIN001";
                p10.nombre = "Peine desechable";
                p10.descripcion = "Peine desechable de un solo uso.";
                p10.precio_compra = new java.math.BigDecimal("0.12");
                p10.precio_venta = new java.math.BigDecimal("0.40");
                p10.stock_actual = 350;
                p10.stock_minimo = 35;
                p10.unidad_medida = "unidad";
                p10.estado = EstadoItem.activo;
                p10.imagenurl = "";
                creado = productoRepository.save(p10);
                createdIds.add(creado.id_producto);
                System.out.println("  ✓ Producto creado: " + p10.nombre);

                // Producto 11: Toallita con alcohol
                Producto p11 = new Producto();
                p11.id_categoria = idDesechables;
                p11.codigo = "ALCO001";
                p11.nombre = "Toallita con alcohol";
                p11.descripcion = "Toallita desinfectante con alcohol para higiene.";
                p11.precio_compra = new java.math.BigDecimal("0.05");
                p11.precio_venta = new java.math.BigDecimal("0.20");
                p11.stock_actual = 1000;
                p11.stock_minimo = 100;
                p11.unidad_medida = "unidad";
                p11.estado = EstadoItem.activo;
                p11.imagenurl = "";
                creado = productoRepository.save(p11);
                createdIds.add(creado.id_producto);
                System.out.println("  ✓ Producto creado: " + p11.nombre);
            }

            System.out.println("  → Total de productos creados: " + createdIds.size());

        } catch (Exception e) {
            System.err.println("  ✗ Error durante seed de productos: " + e.getMessage());
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
