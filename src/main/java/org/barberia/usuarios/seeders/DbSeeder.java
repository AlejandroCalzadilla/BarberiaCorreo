package org.barberia.usuarios.seeders;


import org.barberia.usuarios.service.CategoriaService;
import org.barberia.usuarios.service.ClienteService;
import org.barberia.usuarios.service.HorarioService;
import org.barberia.usuarios.service.ProductoService;
import org.barberia.usuarios.service.ReservaService;


public class DbSeeder {
    

    private CategoriaSeeder categoriaSeeder;
    private ClienteSeeder clienteSeeder;
    private HorarioSeeder horarioSeeder;
    private ReservaSeeder reservaSeeder;
    private ProductoSeeder productoSeeder;


    private CategoriaService categoriaService;
    private ClienteService clienteService;
    private HorarioService horarioService;
    private ReservaService reservaService;
    private ProductoService productoService;

    public DbSeeder(
       CategoriaService categoriaService,
       ClienteService clienteService,
       HorarioService horarioService,
       ReservaService reservaService,
       ProductoService productoService
    ) {
        this.categoriaService = categoriaService;
        this.categoriaSeeder = new CategoriaSeeder(this.categoriaService);
        this.clienteSeeder = new ClienteSeeder(this.clienteService);
        this.horarioSeeder = new HorarioSeeder(this.horarioService);
        this.reservaSeeder = new ReservaSeeder(this.reservaService);
        this.productoSeeder = new ProductoSeeder(this.productoService);
    }


    public void seed() {
        categoriaSeeder.seed();
        //clienteSeeder.seed();
        //horarioSeeder.seed();
        //reservaSeeder.seed();
        //productoSeeder.seed();
    }

    public void rollback() {
        categoriaSeeder.rollback();
        //clienteSeeder.rollback();
        //horarioSeeder.rollback();
        //reservaSeeder.rollback();
        //productoSeeder.rollback();
    }
}
