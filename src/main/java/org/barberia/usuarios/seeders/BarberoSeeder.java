package org.barberia.usuarios.seeders;

import java.util.ArrayList;
import java.util.List;
import org.barberia.usuarios.domain.Barbero;
import org.barberia.usuarios.service.BarberoService;

public class BarberoSeeder {

    private BarberoService barberoService;

    BarberoSeeder(BarberoService barberoService) {
        this.barberoService = barberoService;

    }

    public List<Barbero> seed() {
        List<Barbero> barberos = new ArrayList<>();
        /* Barbero barbero1 = new Barbero();
        barbero1.especialidad = "Cortes de cabello y afeitados clásicos";
        barbero1.estado = barbero1.estado.disponible;
        barbero1.foto_perfil = "https://example.com/images/barbero1.jpg";
        barbero1.id_usuario = 6;
        barberos.add(barberoService.create(barbero1));

         Barbero barbero2 = new Barbero();
        barbero2.especialidad = "Estilos modernos y afilados";
        barbero2.estado = barbero2.estado.disponible;
        barbero2.foto_perfil = "https://example.com/images/barbero2.jpg";
        barbero2.id_usuario = 7;
        barberos.add(barberoService.create(barbero2)); 

        Barbero barbero3 = new Barbero();
        barbero3.especialidad = "Cortes de cabello para niños y afeitados suaves";
        barbero3.estado = barbero3.estado.disponible;
        barbero3.foto_perfil = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTEq7a5yy4t6p3tOBzQCf9wrpMcqwqH8S-JvA&s";
        barbero3.id_usuario = 8;
        barberos.add(barberoService.create(barbero3)); */
        return barberos;
    }

}
