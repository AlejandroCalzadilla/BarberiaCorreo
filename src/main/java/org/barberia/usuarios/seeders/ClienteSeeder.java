package org.barberia.usuarios.seeders;

import org.barberia.usuarios.domain.Cliente;
import org.barberia.usuarios.service.ClienteService;

public class ClienteSeeder {
    

    
    private  ClienteService clienteService;


    ClienteSeeder(
        ClienteService clienteService
    ) {
        this.clienteService = clienteService;
    }

    public void seed() {
        Cliente cliente1 = new Cliente();
       
        cliente1.fecha_nacimiento = (String) "1990-05-15";
        cliente1.ci = "1234567890";
        cliente1.id_usuario = 3;
        cliente1 = clienteService.create(cliente1);

        Cliente cliente2 = new Cliente();
        cliente2.fecha_nacimiento = (String) "1985-10-20";
        cliente2.ci = "0987654321";
        cliente2.id_usuario = 4;
        cliente2 = clienteService.create(cliente2);

        Cliente cliente3 = new Cliente();
        cliente3.fecha_nacimiento = (String) "1995-07-30";
        cliente3.ci = "1122334455";
        cliente3.id_usuario = 5;
        cliente3 = clienteService.create(cliente3);
    }
    

}
