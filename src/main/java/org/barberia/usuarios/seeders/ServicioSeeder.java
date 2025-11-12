package org.barberia.usuarios.seeders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.barberia.usuarios.domain.Servicio;
import org.barberia.usuarios.domain.enums.EstadoItem;
import org.barberia.usuarios.repository.ServicioRepository;

public class ServicioSeeder {
    
    private final ServicioRepository servicioRepository;
    private final List<Integer> createdIds = new ArrayList<>();

    public ServicioSeeder(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    public void seed() {
        System.out.println("  → Creando servicios...");
        
        try {
            // Servicio 1: Corte de cabello clásico
            Servicio s1 = new Servicio();
            s1.nombre = "Corte de Cabello Clásico";
            s1.descripcion = "Corte de cabello tradicional con tijera y máquina, incluye lavado y secado.";
            s1.duracion_minutos_aprox = 30;
            s1.precio = new BigDecimal("15.00");
            s1.estado = EstadoItem.activo;
            s1.imagen = "https://example.com/images/corte-clasico.jpg";
            s1 = servicioRepository.save(s1);
            createdIds.add(s1.id_servicio);
            System.out.println("  ✓ Servicio creado: " + s1.nombre);

            // Servicio 2: Corte moderno/fade
            Servicio s2 = new Servicio();
            s2.nombre = "Corte Fade/Degradado";
            s2.descripcion = "Corte moderno con degradado (fade) bajo, medio o alto según preferencia del cliente.";
            s2.duracion_minutos_aprox = 45;
            s2.precio = new BigDecimal("20.00");
            s2.estado = EstadoItem.activo;
            s2.imagen = "https://example.com/images/fade.jpg";
            s2 = servicioRepository.save(s2);
            createdIds.add(s2.id_servicio);
            System.out.println("  ✓ Servicio creado: " + s2.nombre);

            // Servicio 3: Afeitado con navaja
            Servicio s3 = new Servicio();
            s3.nombre = "Afeitado Clásico con Navaja";
            s3.descripcion = "Afeitado tradicional con navaja, incluye toallas calientes y bálsamo aftershave.";
            s3.duracion_minutos_aprox = 30;
            s3.precio = new BigDecimal("12.00");
            s3.estado = EstadoItem.activo;
            s3.imagen = "https://example.com/images/afeitado.jpg";
            s3 = servicioRepository.save(s3);
            createdIds.add(s3.id_servicio);
            System.out.println("  ✓ Servicio creado: " + s3.nombre);

            // Servicio 4: Arreglo de barba
            Servicio s4 = new Servicio();
            s4.nombre = "Arreglo de Barba";
            s4.descripcion = "Perfilado y recorte de barba, incluye aceite hidratante para barba.";
            s4.duracion_minutos_aprox = 20;
            s4.precio = new BigDecimal("10.00");
            s4.estado = EstadoItem.activo;
            s4.imagen = "https://example.com/images/barba.jpg";
            s4 = servicioRepository.save(s4);
            createdIds.add(s4.id_servicio);
            System.out.println("  ✓ Servicio creado: " + s4.nombre);

            // Servicio 5: Corte infantil
            Servicio s5 = new Servicio();
            s5.nombre = "Corte Infantil";
            s5.descripcion = "Corte de cabello para niños (hasta 12 años), ambiente amigable y rápido.";
            s5.duracion_minutos_aprox = 25;
            s5.precio = new BigDecimal("10.00");
            s5.estado = EstadoItem.activo;
            s5.imagen = "https://example.com/images/corte-infantil.jpg";
            s5 = servicioRepository.save(s5);
            createdIds.add(s5.id_servicio);
            System.out.println("  ✓ Servicio creado: " + s5.nombre);

            // Servicio 6: Combo corte + barba
            Servicio s6 = new Servicio();
            s6.nombre = "Combo Corte + Barba";
            s6.descripcion = "Servicio completo: corte de cabello + arreglo de barba, ideal para un cambio total de look.";
            s6.duracion_minutos_aprox = 60;
            s6.precio = new BigDecimal("25.00");
            s6.estado = EstadoItem.activo;
            s6.imagen = "https://example.com/images/combo.jpg";
            s6 = servicioRepository.save(s6);
            createdIds.add(s6.id_servicio);
            System.out.println("  ✓ Servicio creado: " + s6.nombre);

            // Servicio 7: Diseño de cabello
            Servicio s7 = new Servicio();
            s7.nombre = "Diseño de Cabello";
            s7.descripcion = "Diseños artísticos con máquina en el cabello (líneas, figuras, patrones).";
            s7.duracion_minutos_aprox = 40;
            s7.precio = new BigDecimal("18.00");
            s7.estado = EstadoItem.activo;
            s7.imagen = "https://example.com/images/diseno.jpg";
            s7 = servicioRepository.save(s7);
            createdIds.add(s7.id_servicio);
            System.out.println("  ✓ Servicio creado: " + s7.nombre);

            // Servicio 8: Tratamiento capilar
            Servicio s8 = new Servicio();
            s8.nombre = "Tratamiento Capilar";
            s8.descripcion = "Tratamiento nutritivo para el cabello, incluye masaje capilar y productos premium.";
            s8.duracion_minutos_aprox = 35;
            s8.precio = new BigDecimal("22.00");
            s8.estado = EstadoItem.activo;
            s8.imagen = "https://example.com/images/tratamiento.jpg";
            s8 = servicioRepository.save(s8);
            createdIds.add(s8.id_servicio);
            System.out.println("  ✓ Servicio creado: " + s8.nombre);

            System.out.println("  → Total de servicios creados: " + createdIds.size());

        } catch (Exception e) {
            System.err.println("  ✗ Error durante seed de servicios: " + e.getMessage());
            delete();
            throw e;
        }
    }

    public void delete() {
        for (Integer id : new ArrayList<>(createdIds)) {
            try {
                servicioRepository.deleteById(id);
            } catch (Exception ex) {
                System.err.println("  ✗ Error eliminando servicio id=" + id + " durante rollback: " + ex.getMessage());
            }
            createdIds.remove(id);
        }
    }
}
