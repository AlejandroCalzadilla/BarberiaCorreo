package org.barberia.usuarios.seeders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.barberia.usuarios.domain.Producto;
import org.barberia.usuarios.domain.Servicio;
import org.barberia.usuarios.domain.ServicioProducto;
import org.barberia.usuarios.repository.ProductoRepository;
import org.barberia.usuarios.repository.ServicioProductoRepository;
import org.barberia.usuarios.repository.ServicioRepository;

public class ServicioProductoSeeder {
    
    private final ServicioProductoRepository servicioProductoRepository;
    private final ServicioRepository servicioRepository;
    private final ProductoRepository productoRepository;
    private final List<String> createdKeys = new ArrayList<>(); // Guardar "id_servicio:id_producto"
    
    public ServicioProductoSeeder(
        ServicioProductoRepository servicioProductoRepository,
        ServicioRepository servicioRepository,
        ProductoRepository productoRepository
    ) {
        this.servicioProductoRepository = servicioProductoRepository;
        this.servicioRepository = servicioRepository;
        this.productoRepository = productoRepository;
    }
    
    public void seed() {
        System.out.println("  → Creando relaciones Servicio-Producto...");
        
        // Obtener todos los servicios y productos
        List<Servicio> servicios = servicioRepository.findAll();
        List<Producto> productos = productoRepository.findAll();
        
        if (servicios.isEmpty() || productos.isEmpty()) {
            System.out.println("  ⚠ No hay servicios o productos disponibles");
            return;
        }
        
        // Mapear productos por código para fácil acceso
        Map<String, Integer> productoPorCodigo = new HashMap<>();
        for (Producto p : productos) {
            productoPorCodigo.put(p.codigo, p.id_producto);
        }
        
        // Mapear servicios por nombre
        Map<String, Integer> servicioPorNombre = new HashMap<>();
        for (Servicio s : servicios) {
            servicioPorNombre.put(s.nombre, s.id_servicio);
        }
        
        int asociacionesCreadas = 0;
        
        try {
            // Productos comunes para TODOS los servicios
            Integer toalla = productoPorCodigo.get("TOAL001");
            Integer capa = productoPorCodigo.get("CAPA001");
            Integer toallitaAlcohol = productoPorCodigo.get("ALCO001");
            
            // Asociar productos básicos a todos los servicios
            for (Servicio servicio : servicios) {
                // Toalla desechable (1 por servicio)
                if (toalla != null) {
                    asociacionesCreadas += crearAsociacion(servicio.id_servicio, toalla, 1, servicio.nombre);
                }
                
                // Capa desechable (1 por servicio)
                if (capa != null) {
                    asociacionesCreadas += crearAsociacion(servicio.id_servicio, capa, 1, servicio.nombre);
                }
                
                // Toallita con alcohol (1 por servicio para higiene)
                if (toallitaAlcohol != null) {
                    asociacionesCreadas += crearAsociacion(servicio.id_servicio, toallitaAlcohol, 1, servicio.nombre);
                }
            }
            
            // Productos específicos por tipo de servicio
            
            // CORTES DE CABELLO - necesitan peine
            Integer peine = productoPorCodigo.get("PEIN001");
            if (peine != null) {
                agregarProductoAServicio(servicioPorNombre, "Corte de Cabello Clásico", peine, 1);
                agregarProductoAServicio(servicioPorNombre, "Corte Fade/Degradado", peine, 1);
                agregarProductoAServicio(servicioPorNombre, "Corte Infantil", peine, 1);
                agregarProductoAServicio(servicioPorNombre, "Combo Corte + Barba", peine, 1);
                agregarProductoAServicio(servicioPorNombre, "Diseño de Cabello", peine, 1);
                agregarProductoAServicio(servicioPorNombre, "Tratamiento Capilar", peine, 1);
                asociacionesCreadas += 6;
            }
            
            // AFEITADO - necesita cuchilla y brocha
            Integer cuchilla = productoPorCodigo.get("CUCH001");
            Integer brocha = productoPorCodigo.get("BROW001");
            
            if (cuchilla != null && brocha != null) {
                // Afeitado Clásico con Navaja
                agregarProductoAServicio(servicioPorNombre, "Afeitado Clásico con Navaja", cuchilla, 1);
                agregarProductoAServicio(servicioPorNombre, "Afeitado Clásico con Navaja", brocha, 1);
                asociacionesCreadas += 2;
            }
            
            // ARREGLO DE BARBA - necesita guantes
            Integer guantes = productoPorCodigo.get("GUANT001");
            if (guantes != null) {
                agregarProductoAServicio(servicioPorNombre, "Arreglo de Barba", guantes, 1);
                agregarProductoAServicio(servicioPorNombre, "Combo Corte + Barba", guantes, 1);
                asociacionesCreadas += 2;
            }
            
            // COMBO - necesita más productos (cuchilla + brocha para afeitado)
            if (cuchilla != null && brocha != null) {
                agregarProductoAServicio(servicioPorNombre, "Combo Corte + Barba", cuchilla, 1);
                agregarProductoAServicio(servicioPorNombre, "Combo Corte + Barba", brocha, 1);
                asociacionesCreadas += 2;
            }
            
            System.out.println("  → Total de asociaciones Servicio-Producto creadas: " + asociacionesCreadas);
            
            // Mostrar resumen
            mostrarResumen();
            
        } catch (Exception e) {
            System.err.println("  ✗ Error durante seed de servicio-producto: " + e.getMessage());
            rollback();
            throw e;
        }
    }
    
    private int crearAsociacion(Integer idServicio, Integer idProducto, int cantidad, String nombreServicio) {
        try {
            ServicioProducto sp = new ServicioProducto();
            sp.id_servicio = idServicio;
            sp.id_producto = idProducto;
            sp.cantidad = cantidad;
            
            servicioProductoRepository.insert(sp);
            createdKeys.add(idServicio + ":" + idProducto);
            return 1;
        } catch (Exception e) {
            System.err.println("  ✗ Error creando asociación para servicio '" + nombreServicio + "': " + e.getMessage());
            return 0;
        }
    }
    
    private void agregarProductoAServicio(Map<String, Integer> servicioPorNombre, String nombreServicio, 
                                          Integer idProducto, int cantidad) {
        Integer idServicio = servicioPorNombre.get(nombreServicio);
        if (idServicio != null && idProducto != null) {
            ServicioProducto sp = new ServicioProducto();
            sp.id_servicio = idServicio;
            sp.id_producto = idProducto;
            sp.cantidad = cantidad;
            
            servicioProductoRepository.insert(sp);
            createdKeys.add(idServicio + ":" + idProducto);
        }
    }
    
    private void mostrarResumen() {
        System.out.println("\n  📦 Resumen de productos por servicio:");
        
        List<Servicio> servicios = servicioRepository.findAll();
        for (Servicio servicio : servicios) {
            List<ServicioProducto> productos = servicioProductoRepository.findByServicioId(servicio.id_servicio);
            if (!productos.isEmpty()) {
                System.out.println("    • " + servicio.nombre + ": " + productos.size() + " productos");
            }
        }
    }
    
    public void rollback() {
        System.out.println("  → Iniciando rollback de asociaciones servicio-producto...");
        
        for (String key : new ArrayList<>(createdKeys)) {
            try {
                String[] parts = key.split(":");
                Integer idServicio = Integer.parseInt(parts[0]);
                Integer idProducto = Integer.parseInt(parts[1]);
                
                servicioProductoRepository.delete(idServicio, idProducto);
            } catch (Exception ex) {
                System.err.println("  ✗ Error eliminando asociación " + key + " durante rollback: " + ex.getMessage());
            }
            createdKeys.remove(key);
        }
        
        System.out.println("  Rollback de servicio-producto completado");
    }
}
