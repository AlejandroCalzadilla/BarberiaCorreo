package org.barberia.usuarios.seeders;

import java.util.ArrayList;
import java.util.List;

import org.barberia.usuarios.domain.Cliente;
import org.barberia.usuarios.domain.Usuario;
import org.barberia.usuarios.repository.ClienteRepository;
import org.barberia.usuarios.repository.UsuarioRepository;

public class ClienteSeeder {
    
    private  UsuarioRepository usuarioRepository;
    private  ClienteRepository clienteRepository;
    private final List<Integer> createdIds = new ArrayList<>();

    ClienteSeeder(
        ClienteRepository clienteRepository,
        UsuarioRepository usuarioRepository
    ) {
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void seed() {
        // Obtener usuarios disponibles (no asociados a barbero ni cliente)
        List<Usuario> usuariosDisponibles = usuarioRepository.findAvailableUsuarios();
        
        if (usuariosDisponibles.isEmpty()) {
            System.out.println("  ⚠ No hay usuarios disponibles para crear clientes");
            return;
        }
        
        // Datos de ejemplo para clientes
        String[][] datosClientes = {
            {"1990-05-15", "1234567890"},
            {"1985-10-20", "0987654321"},
            {"1995-07-30", "1122334455"},
            {"1992-03-12", "5566778899"},
            {"1988-11-25", "9988776655"}
        };
        
        int clientesCreados = 0;
        int maxClientes = Math.min(usuariosDisponibles.size(), datosClientes.length);
        
        for (int i = 0; i < maxClientes; i++) {
            Usuario usuario = usuariosDisponibles.get(i);
            String[] datos = datosClientes[i];
            
            try {
                Cliente cliente = new Cliente();
                cliente.id_usuario = usuario.id;
                cliente.fecha_nacimiento = datos[0];
                cliente.ci = datos[1];
                
                cliente = clienteRepository.save(cliente);
                createdIds.add(cliente.id_cliente);
                clientesCreados++;
                
                System.out.println("  ✓ Cliente creado: " + usuario.nombre + " " + usuario.apellido + 
                                 " (CI: " + cliente.ci + ")");
            } catch (Exception e) {
                System.err.println("  ✗ Error creando cliente para usuario ID " + usuario.id + 
                                 ": " + e.getMessage());
            }
        }
        
        System.out.println("  → Total de clientes creados: " + clientesCreados);
    }
    
    public void rollback() {
        for (Integer id : new ArrayList<>(createdIds)) {
            try {
              clienteRepository.deleteById(id);
            } catch (Exception ex) {
                System.err.println("Error eliminando producto id=" + id + " durante rollback: " + ex.getMessage());
            }
            createdIds.remove(id);
        }
    }

}
