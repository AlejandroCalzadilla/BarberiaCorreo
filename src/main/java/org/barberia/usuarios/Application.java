package org.barberia.usuarios;

import org.barberia.usuarios.service.*;
import org.barberia.usuarios.servicioemail.CommandHelp;
import org.barberia.usuarios.utils.ReporteEjemplos;
import org.barberia.usuarios.validation.*;
import org.barberia.usuarios.repository.implentations.*;
import org.barberia.usuarios.seeders.DbSeeder;
import java.math.BigDecimal;
import java.time.LocalTime;
import org.barberia.usuarios.domain.Categoria;
import org.barberia.usuarios.domain.Horario;
import org.barberia.usuarios.domain.Pago;
import org.barberia.usuarios.domain.Producto;
import org.barberia.usuarios.domain.Reserva;
import org.barberia.usuarios.domain.Servicio;
import org.barberia.usuarios.domain.ServicioProducto;
import org.barberia.usuarios.domain.Usuario;
import org.barberia.usuarios.domain.enums.DiaSemana;
import org.barberia.usuarios.domain.enums.EstadoHorario;
import org.barberia.usuarios.domain.enums.EstadoItem;
import org.barberia.usuarios.domain.enums.EstadoReserva;
import org.barberia.usuarios.domain.enums.MetodoPago;
import org.barberia.usuarios.domain.enums.TipoPago;
import org.barberia.usuarios.repository.*;

public class Application {
  public static void main(String[] args) {

    CategoriaRepository categoriaRepository = new JdbcCategoriaRepository();
    ProductoRepository productoRepository = new JdbcProductoRepository();
    UsuarioRepository usuarioRepository = new JdbcUsuarioRepository();
    BarberoRepository barberoRepository = new JdbcBarberoRepository();
    HorarioRepository horarioRepository = new JdbcHorarioRepository();
    ServicioRepository servicioRepository = new JdbcServicioRepository();
    ReservaRepository reservaRepository = new JdbcReservaRepository();
    ClienteRepository clienteRepository = new JdbcClienteRepository();
    PagoRepository pagoRepository = new JdbcPagoRepository();
    ServicioProductoRepository servicioProductoRepository = new JdbServicioProductoRepository();

    ClienteValidator clienteValidator = new ClienteValidator();
    CategoriaValidator categoriaValidator = new CategoriaValidator();
    ProductoValidator productoValidator = new ProductoValidator();
    UsuarioValidator usuarioValidator = new UsuarioValidator();
    BarberoValidator barberoValidator = new BarberoValidator();
    ServicioValidator servicioValidator = new ServicioValidator();
    HorarioValidator horarioValidator = new HorarioValidator();
    ReservaValidator reservaValidator = new ReservaValidator();
    PagoValidator pagoValidator = new PagoValidator();
    ServicioProductoValidator servicioProductoValidator = new ServicioProductoValidator();

    CategoriaService categoriaService = new CategoriaService(categoriaRepository, categoriaValidator);
    ProductoService productoService = new ProductoService(productoRepository, categoriaRepository, productoValidator);
    UsuarioService usuarioService = new UsuarioService(usuarioRepository, usuarioValidator);
    BarberoService barberoService = new BarberoService(barberoRepository, usuarioRepository, barberoValidator);
    ClienteService clienteService = new ClienteService(clienteRepository, usuarioRepository, clienteValidator);
    HorarioService horarioService = new HorarioService(horarioRepository, horarioValidator);
    ServicioService servicioService = new ServicioService(servicioRepository, servicioValidator);
    ServicioProductoService servicioProductoService = new ServicioProductoService(servicioProductoRepository,
        servicioProductoValidator);
    PagoService pagoService = new PagoService(pagoRepository, pagoValidator, reservaRepository);
    ReservaService reservaService = new ReservaService(reservaRepository, reservaValidator, pagoRepository,
          servicioRepository,barberoRepository ,usuarioRepository, horarioRepository,servicioProductoRepository, productoRepository);
    CommandHelp commandHelp = new CommandHelp();

    System.out.println(commandHelp.obtenerComandosDisponibles());

     
     DbSeeder dbSeeder = new DbSeeder(
        usuarioService,
        categoriaService,
        horarioService,
        productoRepository,
        categoriaRepository,
        servicioRepository,
        servicioProductoRepository,
        reservaRepository,
        pagoRepository,
        clienteRepository,
        barberoRepository,
        usuarioRepository
         
        );
 
       ReporteService reporteService = new ReporteService();  
       // dbSeeder.seed();
       ReporteEjemplos reporteEjemplos = new ReporteEjemplos(reporteService);

       //System.out.println(reporteEjemplos.ejemploDashboardGeneral());
       try {

      /* CATEGORIA CRUD */
      Categoria categoria = new Categoria();
      categoria.nombre = "Cuidado del Cabello";
      categoria.descripcion = "Actualizado Productos para el cuidado y estilo del cabello.";
     /*  categoriaService.create(categoria.nombre, categoria.descripcion);
      categoriaService.update(1, "Cuidado del Cabello2", "Actualizado cuidado del cabello");
      categoriaService.deleteById(1); */

      /* PRODUCTO CRUD */
      Producto producto = new Producto();
      producto.nombre = "Gel para el cabello";
      producto.codigo = "GEL123";
      producto.descripcion = "Gel fijador de alta duracion";
      producto.precio_compra = BigDecimal.valueOf(5.00);
      producto.precio_venta = BigDecimal.valueOf(10.00);
      producto.stock_actual = 100;
      producto.stock_minimo = 10;
      producto.unidad_medida = "unidad";
      producto.estado = producto.estado.activo;
      producto.id_categoria = 4;
      /* productoService.create(
          1,
          "P0005",
          "gel para cabello",
          "dabsdjagsjhdvgasd",
          producto.precio_compra,
          producto.precio_venta,
          10,
          2,
          "frasco",
          "dasdsd"); */

      /* productoService.update(
          1,
          "dsadasds",
          "dasdsadsa",
          "adasdas",
          producto.precio_compra,
          producto.precio_venta,
          10,
          9,
          "frasco",
          "dasdsadsa"); */
      //productoService.delete(1);



      /* USUARIO CRUD */
    /*   usuarioService.getAllAsTable();
      usuarioService.getByIdAsTable(1); */
      Usuario usuario = new Usuario();
      usuario.nombre = "Carlos  ";
      usuario.apellido = "Perez";
      usuario.telefono = "555-1234";
      usuario.direccion = "Calle Falsa 123";
      usuario.email = "carlos.perez@example.com";
      usuario.password = "password";
      usuario.username = "carlosperez";
    /*   usuarioService.create(
          "alejandro",
          "calzadilla",
          "ale@gmail.com",
          "79803692",
          "calle falsa 123",
          "alecalsre",
          "password"); */

     /*  usuarioService.update(
        1,
        "alejandro",
        "calzadilla nogales",
        "ale@gamil.com",
        "79803692",
        "calle falsa 123",
        "juaniss",
        "password");   */  




      /* HORARIO CRUD */
      
    /*   System.out.println(barberoService.getAllAsTable());
      System.out.println(horarioService.getAllAsTable());   
      System.out.println(horarioService.getByIdAsTable(1)); */
      Horario horario = new Horario();
      horario.dia_semana = DiaSemana.martes;
      horario.estado = EstadoHorario.activo;
      horario.hora_inicio = LocalTime.of(22, 0);
      horario.hora_fin = LocalTime.of(23, 0);
      horario.id_barbero = 1;
     /*  Horario horario2 = horarioService.create(horario);
      System.out.println("Horario creado: " + horario2.toString());
      System.out.println(horarioService.getAllAsTable()); */

      /* SERVICIO CRUD */
      
        Servicio servicio = new Servicio();
        servicio.descripcion="Corte de cabello ";
        servicio.duracion_minutos_aprox= 90;
        servicio.estado =EstadoItem.activo;
        servicio.nombre = "corte de cabello";
        servicio.precio= BigDecimal.valueOf(50.00);
        servicio.imagen= "sdvashfd";
        /* servicioService.create(
            servicio.nombre,
            servicio.descripcion,
            servicio.duracion_minutos_aprox,
            servicio.precio,
            servicio.imagen); */
      /* servicioService.update(
        1, 
        "dasdsadsads",
        "ddasdsad",
        5,
        servicio.precio, "dadsdsa"); 
       servicioService.delete(1); */



     /**RESERVA CRUD  */
      Reserva reserva = new Reserva();
      reserva.fecha_reserva = java.time.LocalDate.parse("2024-10-15");
      reserva.hora_inicio = java.time.LocalTime.parse("10:00:00");
      reserva.hora_fin = java.time.LocalTime.parse("11:30:00");
      reserva.id_barbero = 1;
      reserva.id_cliente = 1;
      reserva.id_servicio = 1;
      reserva.estado = EstadoReserva.confirmada;
      reserva.total = BigDecimal.valueOf(50.00);
      reserva.notas = "Por favor, ser puntual.";
      reserva.precio_servicio = BigDecimal.valueOf(50.00);
      reserva.monto_anticipo = BigDecimal.valueOf(10.00);
     // reserva.porcentaje_anticipo = BigDecimal.valueOf(20.00);


      /* PAG CRUD */
      Pago pago = new Pago();
      pago.monto_total = BigDecimal.valueOf(10.00);
      pago.metodo_pago = MetodoPago.efectivo;
      pago.tipo_pago = TipoPago.anticipo;
      pago.notas = "Anticipo para reserva de corte de cabello.";

      ServicioProducto servicioProducto = new ServicioProducto();
      servicioProducto.id_servicio = 1;
      servicioProducto.id_producto = 1;
      servicioProducto.cantidad = 2;
      // servicioProducto.subtotal = BigDecimal.valueOf(100.00);

      // servicioProductoService.delete(1,1);
      // System.out.println(servicioProductoService.getAllAsTable());

      // System.out.println(usuarioService.getAllAsTable());
      // System.out.println(clienteService.getAllAsTable());
      // System.out.println(barberoService.getAllAsTable());
      // System.out.println(productoService.getAllAsTable());
      // System.out.println(categoriaService.getAllAsTable());
      // System.out.println(servicioService.getAllAsTable());
      // System.out.println(horarioService.getAllAsTable());
      // System.out.println(pagoService.getAllAsTable());
      // System.out.println(horarioService.getAllAsTable());
      //reservaService.getAllAsTable();
     // System.out.println(reservaService.getAllAsTable());
    } catch (Exception e) {
      System.out.println("Error : " + e.getMessage());
    }
    System.out.println("Seeding completed.");
    // System.out.println(categoriaService.getAllAsTable());
  }
}
