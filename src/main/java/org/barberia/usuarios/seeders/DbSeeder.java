package org.barberia.usuarios.seeders;


import org.barberia.usuarios.repository.BarberoRepository;
import org.barberia.usuarios.repository.CategoriaRepository;
import org.barberia.usuarios.repository.ClienteRepository;
import org.barberia.usuarios.repository.PagoRepository;
import org.barberia.usuarios.repository.ProductoRepository;
import org.barberia.usuarios.repository.ReservaRepository;
import org.barberia.usuarios.repository.ServicioProductoRepository;
import org.barberia.usuarios.repository.ServicioRepository;
import org.barberia.usuarios.repository.UsuarioRepository;
import org.barberia.usuarios.service.CategoriaService;
import org.barberia.usuarios.service.HorarioService;
import org.barberia.usuarios.service.UsuarioService;


public class DbSeeder {
    
    private UsuarioSeeder usuarioSeeder;
    private CategoriaSeeder categoriaSeeder;
    private ProductoSeeder productoSeeder;
    private ServicioSeeder servicioSeeder;
    private ServicioProductoSeeder servicioProductoSeeder;
    private BarberoSeeder barberoSeeder;
    private ClienteSeeder clienteSeeder;
    private HorarioSeeder horarioSeeder;
    private ReservaSeeder reservaSeeder;

    private UsuarioService usuarioService;
    private CategoriaService categoriaService;
    private HorarioService horarioService;
    private ProductoRepository productoRepository;
    private CategoriaRepository categoriaRepository;
    private ServicioRepository servicioRepository;
    private ServicioProductoRepository servicioProductoRepository;
    private ReservaRepository reservaRepository;
    private PagoRepository pagoRepository;
    private ClienteRepository clienteRepository;
    private BarberoRepository barberoRepository;
    private UsuarioRepository usuarioRepository;
    
    public DbSeeder(
       UsuarioService usuarioService,
       CategoriaService categoriaService,
       HorarioService horarioService,
       ProductoRepository productoRepo,
       CategoriaRepository categoriaRepo,
       ServicioRepository servicioRepo,
       ServicioProductoRepository servicioProductoRepo,
       ReservaRepository reservaRepo,
       PagoRepository pagoRepo,
       ClienteRepository clienteRepository,
       BarberoRepository barberoRepository,
       UsuarioRepository usuarioRepository
    ) {
        this.usuarioService = usuarioService;
        this.categoriaService = categoriaService;
        this.horarioService = horarioService;
        this.productoRepository = productoRepo;
        this.categoriaRepository = categoriaRepo;
        this.servicioRepository = servicioRepo;
        this.servicioProductoRepository = servicioProductoRepo;
        this.reservaRepository = reservaRepo;
        this.pagoRepository = pagoRepo;
        this.clienteRepository = clienteRepository;
        this.barberoRepository = barberoRepository;
        this.usuarioRepository = usuarioRepository;
        
        // Inicializar seeders
        this.usuarioSeeder = new UsuarioSeeder(this.usuarioService, this.usuarioRepository);
        this.categoriaSeeder = new CategoriaSeeder(this.categoriaService);
        this.productoSeeder = new ProductoSeeder(this.productoRepository, this.categoriaRepository);
        this.servicioSeeder = new ServicioSeeder(this.servicioRepository);
        this.servicioProductoSeeder = new ServicioProductoSeeder(
            this.servicioProductoRepository,
            this.servicioRepository,
            this.productoRepository
        );
        this.barberoSeeder = new BarberoSeeder(this.barberoRepository, this.usuarioRepository);
        this.clienteSeeder = new ClienteSeeder(this.clienteRepository, this.usuarioRepository);
        this.horarioSeeder = new HorarioSeeder(this.horarioService, this.barberoRepository);
        this.reservaSeeder = new ReservaSeeder(
            this.reservaRepository, 
            this.barberoRepository, 
            this.clienteRepository, 
            this.servicioRepository,
            this.pagoRepository
        );

    }


    public void seed() {
        System.out.println("=== Iniciando seed de base de datos ===");
        
        // 1. Usuarios (deben crearse primero porque Barberos y Clientes los referencian)
        /* System.out.println("→ Seeding Usuarios...");
        usuarioSeeder.seed(); */
        
        // 2. Categorías (necesarias para Productos)
       /*  System.out.println("→ Seeding Categorías...");
        categoriaSeeder.seed();
        
        // 3. Productos (dependen de Categorías)
        System.out.println("→ Seeding Productos...");
        productoSeeder.seed();
        
        // 4. Servicios (independientes)
        System.out.println("→ Seeding Servicios...");
        servicioSeeder.seed(); 
        
        // 5. ServicioProducto (dependen de Servicios y Productos)
        System.out.println("→ Seeding ServicioProducto...");
        servicioProductoSeeder.seed(); */
        
        // 6. Barberos (dependen de Usuarios)
       /*  System.out.println("→ Seeding Barberos...");
        barberoSeeder.seed();
        
        // 7. Clientes (dependen de Usuarios)
        System.out.println("→ Seeding Clientes...");
        clienteSeeder.seed();
        
        // 8. Horarios (dependen de Barberos)
        System.out.println("→ Seeding Horarios...");
        horarioSeeder.seed(); */
        
        // 9. Reservas (dependen de Clientes, Barberos, Servicios)
        System.out.println("→ Seeding Reservas...");
        reservaSeeder.seed();
        
        System.out.println("=== Seed completado exitosamente ===");
    }

    public void rollback() {
        System.out.println("=== Iniciando rollback de base de datos ===");
        
        // Rollback en orden inverso
        System.out.println("→ Rollback Reservas...");
        reservaSeeder.rollback();
        
        System.out.println("→ Rollback Horarios...");
        horarioSeeder.rollback();
        
        System.out.println("→ Rollback Clientes...");
        clienteSeeder.rollback();
        
        System.out.println("→ Rollback Barberos...");
        barberoSeeder.rollback();
        
        System.out.println("→ Rollback ServicioProducto...");
        servicioProductoSeeder.rollback();
        
        System.out.println("→ Rollback Servicios...");
        servicioSeeder.delete();
        
        System.out.println("→ Rollback Productos...");
        productoSeeder.rollback();
        
        System.out.println("→ Rollback Categorías...");
        categoriaSeeder.rollback();
        
        System.out.println("→ Rollback Usuarios...");
        usuarioSeeder.rollback();
        
        System.out.println("=== Rollback completado ===");
    }
}
