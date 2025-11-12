package org.barberia.usuarios.seeders;

import java.util.ArrayList;
import java.util.List;
import org.barberia.usuarios.domain.Barbero;
import org.barberia.usuarios.domain.Usuario;
import org.barberia.usuarios.domain.enums.EstadoBarbero;
import org.barberia.usuarios.repository.BarberoRepository;
import org.barberia.usuarios.repository.UsuarioRepository;

public class BarberoSeeder {

    private BarberoRepository barberoRepository;
    private UsuarioRepository usuarioRepository;
    private final List<Integer> createdIds = new ArrayList<>();
    
    BarberoSeeder(BarberoRepository barberoRepository, UsuarioRepository usuarioRepository) {
        this.barberoRepository = barberoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void seed() {
        // Obtener usuarios disponibles (no asociados a barbero ni cliente)
        List<Usuario> usuariosDisponibles = usuarioRepository.findAvailableUsuarios();
        
        if (usuariosDisponibles.isEmpty()) {
            System.out.println("  ⚠ No hay usuarios disponibles para crear barberos");
            return;
        }
        
        // Datos de ejemplo para barberos
        String[][] datosBarberos = {
            {"Cortes clásicos y afeitados tradicionales", "https://example.com/images/barbero1.jpg"},
            {"Estilos modernos y diseños creativos", "https://example.com/images/barbero2.jpg"},
            {"Especialista en barba y bigote", "https://example.com/images/barbero3.jpg"},
            {"Cortes infantiles y familiares", "https://example.com/images/barbero4.jpg"},
            {"Experto en fade y degradados", "https://example.com/images/barbero5.jpg"}
        };
        
        int barberosCreados = 0;
        int maxBarberos = Math.min(usuariosDisponibles.size(), datosBarberos.length);
        
        for (int i = 0; i < maxBarberos; i++) {
            Usuario usuario = usuariosDisponibles.get(i);
            String[] datos = datosBarberos[i];
            
            try {
                Barbero barbero = new Barbero();
                barbero.id_usuario = usuario.id;
                barbero.especialidad = datos[0];
                barbero.foto_perfil = datos[1];
                barbero.estado = EstadoBarbero.disponible;
                
                barbero = barberoRepository.save(barbero);
                createdIds.add(barbero.id_barbero);
                barberosCreados++;
                
                System.out.println("  ✓ Barbero creado: " + usuario.nombre + " " + usuario.apellido + 
                                 " (Especialidad: " + barbero.especialidad + ")");
            } catch (Exception e) {
                System.err.println("  ✗ Error creando barbero para usuario ID " + usuario.id + 
                                 ": " + e.getMessage());
            }
        }
        
        System.out.println("  → Total de barberos creados: " + barberosCreados);
    }

    public void rollback() {
        for (Integer id : new ArrayList<>(createdIds)) {
            try {
                barberoRepository.deleteById(id);
            } catch (Exception ex) {
                System.err.println("  ✗ Error eliminando barbero id=" + id + " durante rollback: " + ex.getMessage());
            }
            createdIds.remove(id);
        }
    }

}
