package org.barberia.usuarios;

import org.barberia.usuarios.service.BarberoService;
import org.barberia.usuarios.service.CategoriaService;
import org.barberia.usuarios.service.ClienteService;
import org.barberia.usuarios.service.HorarioService;
import org.barberia.usuarios.service.ProductoService;
import org.barberia.usuarios.service.ServicioService;
import org.barberia.usuarios.service.UsuarioService;
import org.barberia.usuarios.validation.BarberoValidator;
import org.barberia.usuarios.validation.CategoriaValidator;
import org.barberia.usuarios.validation.ClienteValidator;
import org.barberia.usuarios.validation.HorarioValidator;
import org.barberia.usuarios.validation.ProductoValidator;
import org.barberia.usuarios.validation.ServicioValidator;
import org.barberia.usuarios.validation.UsuarioValidator;
import org.barberia.usuarios.repository.implentations.*;
import org.barberia.usuarios.seeders.DbSeeder;

import java.math.BigDecimal;
import java.time.LocalTime;
import org.barberia.usuarios.domain.Barbero;
import org.barberia.usuarios.domain.Categoria;
import org.barberia.usuarios.domain.Cliente;
import org.barberia.usuarios.domain.Horario;
import org.barberia.usuarios.domain.Producto;
import org.barberia.usuarios.domain.Servicio;
import org.barberia.usuarios.domain.Usuario;
import org.barberia.usuarios.domain.enums.DiaSemana;
import org.barberia.usuarios.domain.enums.EstadoBarbero;
import org.barberia.usuarios.domain.enums.EstadoHorario;
import org.barberia.usuarios.domain.enums.EstadoItem;
import org.barberia.usuarios.repository.*;

public class Application {
  public static void main(String[] args) {

    CategoriaRepository categoriaRepository = new JdbcCategoriaRepository();
    CategoriaValidator categoriaValidator = new CategoriaValidator();
    CategoriaService categoriaService = new CategoriaService(categoriaRepository, categoriaValidator);

    ProductoRepository productoRepository = new JdbcProductoRepository();

    ProductoValidator productoValidator = new ProductoValidator();
    ProductoService productoService = new ProductoService(productoRepository, categoriaRepository, productoValidator);

     UsuarioRepository usuarioRepository = new JdbcUsuarioRepository();
    UsuarioValidator usuarioValidator = new UsuarioValidator();
    UsuarioService usuarioService = new UsuarioService(usuarioRepository, usuarioValidator);

    BarberoRepository barberoRepository = new JdbcBarberoRepository();
    BarberoValidator barberoValidator = new BarberoValidator();
    BarberoService barberoService = new BarberoService(barberoRepository, usuarioRepository, barberoValidator);

   
    ClienteRepository clienteRepository = new JdbcClienteRepository();
    ClienteValidator clienteValidator = new ClienteValidator();
    ClienteService clienteService = new ClienteService(clienteRepository, usuarioRepository, clienteValidator);



    HorarioRepository horarioRepository = new JdbcHorarioRepository();
    HorarioValidator horarioValidator = new HorarioValidator();
    HorarioService horarioService = new HorarioService(horarioRepository, horarioValidator);
    
    ServicioRepository servicioRepository = new JdbcServicioRepository();
    ServicioValidator servicioValidator = new ServicioValidator();
    ServicioService  servicioService = new ServicioService(servicioRepository,servicioValidator);


    DbSeeder dbSeeder = new DbSeeder(
        categoriaService,
        null,
        null,
        null,
        null);
    try {

      Categoria categoria = new Categoria();
      categoria.nombre = "Cuidado del Cabello";
      categoria.descripcion = "Actualizado Productos para el cuidado y estilo del cabello.";

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

      Usuario usuario = new Usuario();
      usuario.nombre = "Carlos  ";
      usuario.apellido = "Perez";
      usuario.telefono = "555-1234";
      usuario.direccion = "Calle Falsa 123";
      usuario.email = "carlos.perez@example.com";
      usuario.password = "password";
      usuario.username = "carlosperez";

      Usuario usuario2 = new Usuario();
      usuario2.nombre = "Ana Gomez";
      usuario2.apellido = "Martinez";
      usuario2.telefono = "555-5678";
      usuario2.direccion = "Avenida Siempre Viva 742";
      usuario2.email = "ana.gomez@example.com";
      usuario2.password = "securepassword";
      usuario2.username = "anagomez";


      System.out.println(barberoService.getAllAsTable());
     Horario horario = new Horario();
      horario.dia_semana = DiaSemana.martes;
      horario.estado = EstadoHorario.activo;
      horario.hora_inicio = LocalTime.of(22, 0);
      horario.hora_fin = LocalTime.of(23, 0);
      horario.id_barbero = 1;
     // Horario horario2 = horarioService.create(horario);
     // System.out.println("Horario creado: " + horario2.toString());
      System.out.println(horarioService.getAllAsTable());
 


      Servicio servicio = new Servicio();
      servicio.descripcion="Corte de cabello ";
      servicio.duracion_minutos_aprox= 90;
      servicio.estado =EstadoItem.activo;
      servicio.nombre = "corte de cabello";
      servicio.precio= BigDecimal.valueOf(50.00);
      servicio.imagen= "sdvashfd";
      

      Servicio servicio2 = servicioService.create(servicio);


 

     //System.out.println(productoService.getAllAsTable());
    

      /* String usuarioCreado = usuarioService.update(1,
          usuario.nombre,
          usuario.apellido,
          usuario.email,
          usuario.telefono,
          usuario.direccion,
          usuario.username,
          usuario.password); */
      // System.out.println(usuarioService.activate(2));
          //System.out.println(usuarioCreado);
     // System.out.println(usuarioService.getAllAsTable());
      Barbero barbero = new Barbero();
      barbero.especialidad = "Cortes Modernos";
      barbero.estado = EstadoBarbero.disponible;
      barbero.foto_perfil = "http://example.com/perfil.jpg";
      barbero.id_usuario = 1;
      // Barbero barbero2= barberoService.create(barbero);
      // System.out.println(barbero2.toString());

      Cliente cliente = new Cliente();
      cliente.fecha_nacimiento = "1990-05-15";
      cliente.ci = "8942008SC";
      cliente.id_usuario = 2;
      //Cliente cliente2= clienteService.create(cliente);
     //  System.out.println(cliente2.toString());
    //  System.out.println(cliente); 
     // System.out.println(usuarioService.getAllAsTable());
       
      // Producto productoCreado = productoService.create(producto);
      // System.out.println("Producto creado: " + productoCreado.toString());
      // System.out.println(productoService.getAllAsTable());
      // String message = categoriaService.update(1, categoria);
      // System.out.println(message);
      // String message= categoriaService.softDeleteById(4);
      // System.out.println(message);
      // dbSeeder.seed();
      // dbSeeder.rollback();
    } catch (Exception e) {
      System.out.println("Error : " + e.getMessage());
    }
    System.out.println("Seeding completed.");
    System.out.println(categoriaService.getAllAsTable());
  }
}
