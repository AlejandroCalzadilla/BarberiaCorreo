package org.barberia.usuarios.servicioemail;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.barberia.usuarios.domain.Categoria;
import org.barberia.usuarios.domain.Producto;
import org.barberia.usuarios.domain.Usuario;
import org.barberia.usuarios.repository.BarberoRepository;
import org.barberia.usuarios.repository.CategoriaRepository;
import org.barberia.usuarios.repository.ClienteRepository;
import org.barberia.usuarios.repository.HorarioRepository;
import org.barberia.usuarios.repository.PagoRepository;
import org.barberia.usuarios.repository.ProductoRepository;
import org.barberia.usuarios.repository.ServicioProductoRepository;
import org.barberia.usuarios.repository.ReservaRepository;
import org.barberia.usuarios.repository.ServicioRepository;
import org.barberia.usuarios.repository.UsuarioRepository;
import org.barberia.usuarios.repository.implentations.JdbcBarberoRepository;
import org.barberia.usuarios.repository.implentations.JdbcCategoriaRepository;
import org.barberia.usuarios.repository.implentations.JdbcClienteRepository;
import org.barberia.usuarios.repository.implentations.JdbcHorarioRepository;
import org.barberia.usuarios.repository.implentations.JdbcPagoRepository;
import org.barberia.usuarios.repository.implentations.JdbcProductoRepository;
import org.barberia.usuarios.repository.implentations.JdbServicioProductoRepository;
import org.barberia.usuarios.repository.implentations.JdbcReservaRepository;
import org.barberia.usuarios.repository.implentations.JdbcServicioRepository;
import org.barberia.usuarios.repository.implentations.JdbcUsuarioRepository;
import org.barberia.usuarios.service.BarberoService;
import org.barberia.usuarios.service.CategoriaService;
import org.barberia.usuarios.service.ClienteService;
import org.barberia.usuarios.service.HorarioService;
import org.barberia.usuarios.service.PagoService;
import org.barberia.usuarios.service.ProductoService;
import org.barberia.usuarios.service.ReservaService;
import org.barberia.usuarios.service.ServicioService;
import org.barberia.usuarios.service.UsuarioService;
import org.barberia.usuarios.validation.BarberoValidator;
import org.barberia.usuarios.validation.CategoriaValidator;
import org.barberia.usuarios.validation.ClienteValidator;
import org.barberia.usuarios.validation.HorarioValidator;
import org.barberia.usuarios.validation.PagoValidator;
import org.barberia.usuarios.validation.ProductoValidator;
import org.barberia.usuarios.validation.ReservaValidator;
import org.barberia.usuarios.validation.ServicioValidator;
import org.barberia.usuarios.validation.UsuarioValidator;

public class ComandoEmail {

    private CategoriaRepository categoriaRepository = new JdbcCategoriaRepository();
    private CategoriaValidator categoriaValidator = new CategoriaValidator();
    private CategoriaService categoriaService = new CategoriaService(categoriaRepository, categoriaValidator);

    private ProductoRepository productoRepository = new JdbcProductoRepository();

    private ProductoValidator productoValidator = new ProductoValidator();
    private ProductoService productoService = new ProductoService(productoRepository, categoriaRepository,
            productoValidator);

    private UsuarioRepository usuarioRepository = new JdbcUsuarioRepository();
    private UsuarioValidator usuarioValidator = new UsuarioValidator();
    private UsuarioService usuarioService = new UsuarioService(usuarioRepository, usuarioValidator);

    private BarberoRepository barberoRepository = new JdbcBarberoRepository();
    private BarberoValidator barberoValidator = new BarberoValidator();
    private BarberoService barberoService = new BarberoService(barberoRepository, usuarioRepository, barberoValidator);

    private ClienteRepository clienteRepository = new JdbcClienteRepository();
    private ClienteValidator clienteValidator = new ClienteValidator();
    private ClienteService clienteService = new ClienteService(clienteRepository, usuarioRepository, clienteValidator);

    private HorarioRepository horarioRepository = new JdbcHorarioRepository();
    private HorarioValidator horarioValidator = new HorarioValidator();
    private HorarioService horarioService = new HorarioService(horarioRepository, horarioValidator);

    private ServicioRepository servicioRepository = new JdbcServicioRepository();
    private ServicioValidator servicioValidator = new ServicioValidator();
    private ServicioService servicioService = new ServicioService(servicioRepository, servicioValidator);

    private ReservaRepository reservaRepository = new JdbcReservaRepository();
    private PagoRepository pagoRepository = new JdbcPagoRepository();
    private PagoValidator pagoValidator = new PagoValidator();
    private PagoService pagoService = new PagoService(pagoRepository, pagoValidator,reservaRepository);
    private ServicioProductoRepository reservaProductoRepository = new JdbServicioProductoRepository();
    private ReservaValidator reservaValidator = new ReservaValidator();
    private ReservaService reservaService = new ReservaService(reservaRepository, reservaValidator, pagoRepository,
            reservaProductoRepository,productoRepository,servicioRepository);

    // private CommandHelp commandHelp = new CommandHelp();
    public String evaluarYEjecutar(String subject) throws SQLException {
        if (Objects.equals(subject, "HELP")) {
            return CommandHelp.obtenerComandosDisponibles();
        }
        String respuestaConsulta;

        // Definir patrones para cada operación CRUD
        Pattern listarPatron = Pattern.compile("^LISTAR(\\w+)\\[\\*\\]$"); // Ej: LISTARCLIENTES[*]
        Pattern crearPatron = Pattern.compile("^CREATE(\\w+)\\[(.+)]$"); // Ej: CREATECLIENTES[nombre, apellido, otros]
        Pattern actualizarPatron = Pattern.compile("^UPDATE(\\w+)\\[(.+)]$"); // Ej: UPDATECLIENTES[param1, param2]
        Pattern eliminarPatron = Pattern.compile("^DELETE(\\w+)\\[(.+)]$"); // Ej: DELETECLIENTES[id]
        Pattern getPatron = Pattern.compile("^GET(\\w+)\\[(\\d+)]$"); // Ej: GETMEDICAMENTOS[2]

        Matcher matcher;

        // Evaluar cada patrón
        if ((matcher = listarPatron.matcher(subject)).matches()) {
            String entidad = matcher.group(1);
            respuestaConsulta = ejecutarConsultaListar(entidad);
        } else if ((matcher = crearPatron.matcher(subject)).matches()) {
            String entidad = matcher.group(1);
            String parametros = matcher.group(2);
            respuestaConsulta = ejecutarConsultaCrear(entidad, parametros);
        } else if ((matcher = actualizarPatron.matcher(subject)).matches()) {
            String entidad = matcher.group(1);
            String parametros = matcher.group(2);
           // respuestaConsulta = ejecutarConsultaActualizar(entidad, parametros);
            respuestaConsulta = "";
        } else if ((matcher = eliminarPatron.matcher(subject)).matches()) {
            String entidad = matcher.group(1);
            String id = matcher.group(2);
            respuestaConsulta = ejecutarConsultaEliminar(entidad, id);
        } else if ((matcher = getPatron.matcher(subject)).matches()) {
            String entidad = matcher.group(1);
            String id = matcher.group(2);
            respuestaConsulta = ejecutarConsultaGet(entidad, id);
        } else {
            respuestaConsulta = "Comando no reconocido.";
        }

        return respuestaConsulta;
    }

    // Métodos para ejecutar consultas CRUD simuladas
    private String ejecutarConsultaListar(String entidad) throws SQLException {
        String respuesta = "";
        try {
            switch (entidad) {
                case "PRODUCTOS" -> {
                    ProductoService productoService = new ProductoService(productoRepository, categoriaRepository,
                            productoValidator);
                    respuesta = productoService.getAllAsTable();
                }
                case "CATEGORIAS" -> {
                    respuesta = categoriaService.getAllAsTable();
                }
                case "USUARIOS" -> {
                    respuesta = usuarioService.getAllAsTable();
                }
                case "BARBEROS" -> {
                    respuesta = barberoService.getAllAsTable();
                }
                case "CLIENTES" -> {
                    respuesta = clienteService.getAllAsTable();
                }
                case "HORARIOS" -> {
                    respuesta = horarioService.getAllAsTable();
                }
                case "SERVICIOS" -> {
                    respuesta = servicioService.getAllAsTable();
                }
                case "RESERVAS" -> {
                    respuesta = reservaService.getAllAsTable();
                }
                case "PAGOS" -> {
                    respuesta = pagoService.getAllAsTable();
                }

                default -> respuesta = "Entidad no encontrada";
            }
            return respuesta;
        } catch (Exception e) {
            return "Error al obtener listado de " + entidad + ": " + e.getMessage();
        }
    }

    private String ejecutarConsultaCrear(String entidad, String parametros) throws SQLException {
        String respuesta = "";
        try {
            String[] params = parametros.split(",");
            for (int i = 0; i < params.length; i++) {
                params[i] = params[i].trim();
            }

            switch (entidad) {
                case "PRODUCTOS" -> {
                    if (params.length != 11) {
                        throw new IllegalArgumentException("Número de parámetros incorrecto");
                    }
                    Producto producto = new Producto();
                    producto.id_categoria = Integer.parseInt(params[0]);
                    producto.codigo = params[1];
                    producto.nombre = params[2];
                    producto.descripcion = params[3];
                    producto.precio_compra = new BigDecimal(params[4]);
                    producto.precio_venta = new BigDecimal(params[5]);
                    producto.stock_actual = Integer.parseInt(params[6]);
                    producto.stock_minimo = Integer.parseInt(params[7]);
                    producto.imagenurl = params[8];
                    producto.unidad_medida = params[8];
                    producto.unidad_medida = params[9];
                    respuesta = productoService.create(producto.id_categoria, producto.codigo, producto.nombre,
                            producto.descripcion, producto.precio_compra, producto.precio_venta,
                            producto.stock_actual, producto.stock_minimo, producto.imagenurl,
                            producto.unidad_medida).toString();

                }

                case "CATEGORIAS" -> {
                    if (params.length != 2) {
                        throw new IllegalArgumentException("Número de parámetros incorrecto");
                    }
                    Categoria categoria = new Categoria();
                    categoria.nombre = params[0];
                    categoria.descripcion = params[1];
                    respuesta = categoriaService.create(categoria.nombre, categoria.descripcion).toString();
                }

                case "USUARIOS" -> {
                    if (params.length != 7) {
                        throw new IllegalArgumentException("Número de parámetros incorrecto");
                    }
                    Usuario usuario = new Usuario();
                    usuario.nombre = params[0];
                    usuario.apellido = params[1];
                    usuario.email = params[2];
                    usuario.username = params[3];
                    usuario.telefono = params[4];
                    usuario.direccion = params[5];
                    usuario.password = params[6];

                    respuesta = usuarioService.create(
                            usuario.nombre, usuario.apellido, usuario.email, usuario.username, usuario.telefono,
                            usuario.direccion, usuario.password).toString();
                }
                case "BARBEROS" -> {
                    if (params.length != 3) {
                        throw new IllegalArgumentException("Número de parámetros incorrecto");
                    }

                    respuesta = barberoService.create(
                            Integer.parseInt(params[0]), 
                            params[1], 
                            params[2]).toString();
                }
                case "CLIENTES" -> {
                    if (params.length != 3) {
                        throw new IllegalArgumentException("Número de parámetros incorrecto");
                    }

                    respuesta = clienteService.create(
                            Integer.parseInt(params[0]), 
                            params[1], 
                            params[2]).toString();
                }
                case "HORARIOS" -> {
                    if (params.length != 4) {
                        throw new IllegalArgumentException("Número de parámetros incorrecto");
                    }

                    respuesta = horarioService.create(
                            params[0], 
                            params[1], 
                            params[2],
                            Integer.parseInt(params[3])
                            ).toString();
                }
                case "SERVICIOS" -> {
                    if (params.length != 3) {
                        throw new IllegalArgumentException("Número de parámetros incorrecto");
                    }

                    respuesta = servicioService.create(
                            params[0], 
                            params[1],
                            Integer.parseInt(params[2]),  
                            new BigDecimal(params[3]),
                            params[4]
                            ).toString();
                }
                /* case "RESERVAS" -> {
                    if (params.length != 5) {
                        throw new IllegalArgumentException("Número de parámetros incorrecto");
                    }

                    respuesta = reservaService.createConTransaccion(
                            Integer.parseInt(params[0]), 
                            Integer.parseInt(params[1]), 
                            params[2],
                            params[3],
                            new BigDecimal(params[4])
                            ).toString();
                } */

                default -> respuesta = "Entidad no encontrada";
            }

            return respuesta;
        } catch (Exception e) {
            return "Error al crear " + entidad + ": " + e.getMessage();
        }
    }

    /*
     * private String ejecutarConsultaActualizar(String entidad, String parametros)
     * {
     * String respuesta = "";
     * try {
     * String[] params = parametros.split(",");
     * for (int i = 0; i < params.length; i++) {
     * params[i] = params[i].trim();
     * }
     * switch (entidad) {
     * case "CLIENTES" -> {
     * if (params.length != 6) {
     * throw new IllegalArgumentException("Número de parámetros incorrecto");
     * }
     * ClientesN clientesN = new ClientesN();
     * respuesta = clientesN.actualizarCliente(
     * Integer.parseInt(params[0]), params[1], params[2], params[3], params[4],
     * params[5]
     * );
     * }
     * case "MEDICAMENTOS" -> {
     * if (params.length != 7) {
     * throw new IllegalArgumentException("Número de parámetros incorrecto");
     * }
     * MedicamentosN medicamentosN = new MedicamentosN();
     * Date fechaCaducidad = new Date(new
     * SimpleDateFormat("dd-MM-yyyy").parse(params[4]).getTime());
     * boolean esSustanciaControlada = Integer.parseInt(params[5]) != 0;
     * respuesta = medicamentosN.actualizarMedicamento(
     * Integer.parseInt(params[0]), params[1], params[2], params[3], fechaCaducidad,
     * esSustanciaControlada, Integer.parseInt(params[6])
     * );
     * }
     * case "ESPECIES" -> {
     * if (params.length != 2) {
     * throw new IllegalArgumentException("Número de parámetros incorrecto");
     * }
     * EspeciesN especiesN = new EspeciesN();
     * respuesta = especiesN.actualizarEspecie(Integer.parseInt(params[0]),
     * params[1]);
     * }
     * case "MASCOTAS" -> {
     * if (params.length != 8) {
     * throw new IllegalArgumentException("Número de parámetros incorrecto");
     * }
     * MascotasN mascotasN = new MascotasN();
     * respuesta = mascotasN.actualizarMascota(
     * Integer.parseInt(params[0]), params[1], Float.parseFloat(params[2]),
     * params[3], params[4], params[5], Integer.parseInt(params[6]),
     * Integer.parseInt(params[7])
     * );
     * }
     * case "RAZAS" -> {
     * if (params.length != 3) {
     * throw new IllegalArgumentException("Número de parámetros incorrecto");
     * }
     * RazasN razasN = new RazasN();
     * boolean actualizado = razasN.actualizarRaza(
     * Integer.parseInt(params[0]), params[1], Integer.parseInt(params[2])
     * );
     * respuesta = actualizado ? "Raza actualizada" : "Error al actualizar raza";
     * }
     * case "CATEGORIAS" -> {
     * if (params.length != 2) {
     * throw new IllegalArgumentException("Número de parámetros incorrecto");
     * }
     * CategoriasN categoriasN = new CategoriasN();
     * respuesta = categoriasN.actualizarCategoria(Integer.parseInt(params[0]),
     * params[1]);
     * }
     * case "ALMACENES" -> {
     * if (params.length != 4) {
     * throw new IllegalArgumentException("Número de parámetros incorrecto");
     * }
     * AlmacenesN almacenesN = new AlmacenesN();
     * respuesta = almacenesN.actualizarAlmacen(
     * Integer.parseInt(params[0]), params[1], params[2], params[3]
     * );
     * }
     * case "CONSULTAS" -> {
     * if (params.length < 9 || (params.length - 7) % 2 != 0) {
     * throw new IllegalArgumentException("Número de parámetros incorrecto");
     * }
     * ConsultasMedicasN consultas = new ConsultasMedicasN();
     * List<TratamientosN> tratamientos = new ArrayList<>();
     * for (int i = 7; i < params.length; i += 2) {
     * tratamientos.add(new TratamientosN(params[i], params[i + 1]));
     * }
     * respuesta = consultas.actualizarConsultaMedica(
     * Integer.parseInt(params[0]), params[1], params[2], params[3],
     * Float.parseFloat(params[4]),
     * Integer.parseInt(params[5]), Integer.parseInt(params[6]), tratamientos
     * );
     * ;
     * }
     * case "PROVEEDORES" -> {
     * if (params.length != 6) {
     * throw new IllegalArgumentException("Número de parámetros incorrecto");
     * }
     * ProveedoresN proveedoresN = new ProveedoresN();
     * respuesta = proveedoresN.actualizarProveedor(
     * Integer.parseInt(params[0]), params[1], params[2], params[3], params[4],
     * params[5]
     * );
     * }
     * case "USUARIOS" -> {
     * if (params.length != 4) {
     * throw new IllegalArgumentException("Número de parámetros incorrecto");
     * }
     * UsuariosN usuariosN = new UsuariosN();
     * respuesta = usuariosN.actualizarUsuario(
     * Integer.parseInt(params[0]), params[1], params[2], params[3]
     * );
     * }
     * case "VACUNAS" -> {
     * if (params.length != 4) {
     * throw new IllegalArgumentException("Número de parámetros incorrecto");
     * }
     * VacunasN vacunasN = new VacunasN();
     * respuesta = vacunasN.actualizarVacuna(
     * Integer.parseInt(params[0]), params[1], Integer.parseInt(params[2]),
     * params[3]
     * );
     * }
     * case "NOTASVENTAS" -> {
     * if (params.length < 9 && (params.length - 5) % 4 != 0) {
     * throw new IllegalArgumentException("Número de parámetros incorrecto");
     * }
     * NotaVentaN notaVentaN = new NotaVentaN();
     * List<DetalleNotaVentaN> detalles = new ArrayList<>();
     * for (int i = 5; i < params.length; i += 4) {
     * detalles.add(new DetalleNotaVentaN(
     * Integer.parseInt(params[i]), Float.parseFloat(params[i + 1]),
     * Float.parseFloat(params[i + 2]), Integer.parseInt(params[i + 3])
     * ));
     * }
     * respuesta = notaVentaN.actualizarNotaVenta(
     * Integer.parseInt(params[0]), params[1], Integer.parseInt(params[2]),
     * Integer.parseInt(params[3]),
     * Integer.parseInt(params[4]), detalles
     * );
     * }
     * case "NOTASCOMPRAS" -> {
     * if (params.length < 9 && (params.length - 5) % 5 != 0) {
     * throw new IllegalArgumentException("Número de parámetros incorrecto");
     * }
     * List<DetalleNotaCompraN> detalles = new ArrayList<>();
     * for (int i = 5; i < params.length; i += 5) {
     * detalles.add(new DetalleNotaCompraN(
     * Integer.parseInt(params[i]), Float.parseFloat(params[i + 1]),
     * Float.parseFloat(params[i + 2]), Float.parseFloat(params[i + 3]),
     * Integer.parseInt(params[i + 4])
     * ));
     * }
     * NotaCompraN notaCompraN = new NotaCompraN();
     * respuesta = notaCompraN.actualizarNotaCompra(
     * Integer.parseInt(params[0]), params[1], Integer.parseInt(params[2]),
     * Integer.parseInt(params[3]),
     * Integer.parseInt(params[4]), detalles
     * );
     * }
     * default -> respuesta = "Entidad no encontrada";
     * }
     * return respuesta;
     * } catch (Exception e) {
     * return "Error al actualizar " + entidad + ": " + e.getMessage();
     * }
     * }
     */

    private String ejecutarConsultaEliminar(String entidad, String id) {
        int entityId = Integer.parseInt(id);
        if (entityId <= 0) {
            return "ID inválido";
        }
        try {
            String respuesta = "";
            switch (entidad) {
                case "USUARIOS" -> {

                    respuesta = this.usuarioService.delete(entityId);
                }
                case "CLIENTES" -> {
                    respuesta = this.clienteService.delete(entityId);
                    ;
                }
                case "BARBEROS" -> {
                    respuesta = this.barberoService.delete(entityId);   
                }

                case "HORARIOS" -> {
                    respuesta = this.horarioService.delete(entityId);
                }
                case "CATEGORIAS" -> {
                    respuesta = this.categoriaService.deleteById(entityId);
                }
                case "PRODCUTOS" -> {
                    respuesta = this.productoService.delete(entityId);
                }
                case "SERVICIOS" -> {
                     respuesta = this.servicioService.delete(entityId);
                }


                default -> respuesta = "Entidad no encontrada";
            }
            return respuesta;
        } catch (Exception e) {
            return "Error al eliminar " + entidad + ": " + e.getMessage();
        }
    }

    private String ejecutarConsultaGet(String entidad, String stringId) {
        int id = Integer.parseInt(stringId);
        if (id <= 0) {
            return "ID inválido";
        }
        try {
            String respuesta = "";
            switch (entidad) {
                case "USUARIOS" -> {
                    respuesta = this.usuarioService.getByIdAsTable(id);
                }
                case "ClIENTES" -> {
                    respuesta = this.clienteService.getByIdAsTable(id);
                }
                case "BARBEROS" -> {
                    respuesta = this.barberoService.getByIdAsTable(id);
                }
                case "HORARIOS" -> {
                    respuesta = this.horarioService.getByIdAsTable(id);
                }
                case "CATEGORIA" -> {
                    respuesta = this.categoriaService.getByIdAsTable(id);
                }
                case "PRODUCTOS" -> {
                    respuesta = this.productoService.getByIdAsTable(id);
                }
                case "SERVICIOS" -> {
                      respuesta = this.servicioService.getByIdAsTable(id);
                }


                default -> respuesta = "Entidad no encontrada";
            }
            return respuesta;
        } catch (Exception e) {
            return "Error al obtener " + entidad + ": " + e.getMessage();
        }
    }
    /*
     * private String obtenerComandosDisponibles() {
     * StringBuilder sb = new StringBuilder();
     * sb.append("Comandos generales disponibles:\r\n")
     * .append("- LISTAR<entidad>[*]: Listar todos los registros de una entidad\r\n"
     * )
     * .append("- CREATE<entidad>[param1, param2, ...]: Crear un nuevo registro en una entidad\r\n"
     * )
     * .append("- UPDATE<entidad>[param1, param2, ...]: Actualizar un registro en una entidad\r\n"
     * )
     * .append("- DELETE<entidad>[id]: Eliminar un registro en una entidad\r\n")
     * .append("- GET<entidad>[id]: Obtener un registro por ID en una entidad\r\n");
     * sb.append("Entidades disponibles:\r\n");
     * sb.
     * append("CLIENTES, MEDICAMENTOS, ESPECIES, MASCOTAS, RAZAS, CATEGORIAS, ALMACENES, CONSULTAS, PROVEEDORES, "
     * +
     * "USUARIOS, VACUNAS, NOTASVENTAS, NOTASCOMPRAS, INVENTARIOS, COMPRASPORMES, COMPRASPORPROVEEDOR,"
     * +
     * "VENTASPORCLIENTE, VENTASPORMES\r\n");
     * sb.append("Comandos específicos disponibles:\r\n");
     * sb.append("CREAR CLIENTE:\r\n");
     * sb.
     * append("Parámetros: nombre, apellido, numero, fecha de nacimiento, correo y contraseña\r\n"
     * );
     * sb.append("Ejemplo:\r\n");
     * sb.
     * append("CREATECLIENTES[Gonzalo, Quispe Huanca, 12345678, masculino, 2000-11-01, ruddygonzqh@gmail.com, password]\r\n"
     * );
     * sb.append(
     * "-----------------------------------------------------------------------------------------------------\r\n"
     * );
     * sb.append("CREAR MASCOTA:\r\n");
     * sb.
     * append("Parámetros: nombre, peso, color, fecha nac, url_foto, id_cliente, id_raza\r\n"
     * );
     * sb.append("Ejemplo:\r\n");
     * sb.
     * append("CREATEMASCOTAS[Pacho 3, 10.5, Blanco, 2028-10-10, https://google.com, 2, 6]\r\n"
     * );
     * sb.append(
     * "-----------------------------------------------------------------------------------------------------\r\n"
     * );
     * sb.append("CREAR MEDICAMENTOS:\r\n");
     * sb.
     * append("Parámetros: nombre, cant_dosis, fabriacante, fecha caducidad, sustancia controlado (bool 0 o 1), id_categoria\r\n"
     * );
     * sb.append("Ejemplo:\r\n");
     * sb.
     * append("CREATEMEDICAMENTOS[Amoxicilina 500mg, 500mg, Labs ATI, 01-12-2028, 0, 1]\r\n"
     * );
     * sb.append(
     * "-----------------------------------------------------------------------------------------------------\r\n"
     * );
     * sb.append("CREAR ESPECIES:\r\n");
     * sb.append("Parámetros: nombre de la especie\r\n");
     * sb.append("Ejemplo:\r\n");
     * sb.append("CREATEESPECIES[Perro]\r\n");
     * sb.append(
     * "-----------------------------------------------------------------------------------------------------\r\n"
     * );
     * sb.append("CREAR RAZA:\r\n");
     * sb.append("Parámetros: nombre de la raza, id_especie\r\n");
     * sb.append("Ejemplo:\r\n");
     * sb.append("CREATERAZAS[Raza 6, 15]\r\n");
     * sb.append(
     * "-----------------------------------------------------------------------------------------------------\r\n"
     * );
     * sb.append("CREAR CATEGORÍA:\r\n");
     * sb.append("Parámetros: nombre de la categoría\r\n");
     * sb.append("Ejemplo:\r\n");
     * sb.append("CREATECATEGORIAS[Categoria Y]\r\n");
     * sb.append(
     * "-----------------------------------------------------------------------------------------------------\r\n"
     * );
     * sb.append("CREAR ALMACÉN:\r\n");
     * sb.
     * append("Parámetros: nombre del almacén, dirección, descipción adicional del almacén\r\n"
     * );
     * sb.append("Ejemplo:\r\n");
     * sb.
     * append("CREATEALMACENES[Almacen Y, Los pozos casco viejo, Descripcion del almacen]\r\n"
     * );
     * sb.append(
     * "-----------------------------------------------------------------------------------------------------\r\n"
     * );
     * sb.append("CREAR CONSULTA:\r\n");
     * sb.
     * append("Parámetros: fecha de consulta, motivo, diagnóstico, tarifa, id_mascota, id_cliente, detalles del tratamiento (receta y nota)\r\n"
     * );
     * sb.append("Ejemplo:\r\n");
     * sb.
     * append("CREATECONSULTAS[2024-10-10, vomitos de la mascota, la mascota está deshidratada, 10.5, 1, 2, Recetas de medicamentos, Nota adicional, Recetas de tratamientos 2, Nota adicional 2]\r\n"
     * );
     * sb.append(
     * "-----------------------------------------------------------------------------------------------------\r\n"
     * );
     * sb.append("CREAR PROVEEDOR:\r\n");
     * sb.
     * append("Parámetros: nombre proveedor, país, celular, correo, dirección\r\n");
     * sb.append("Ejemplo:\r\n");
     * sb.
     * append("CREATEPROVEEDORES[Proveedor Y, Bolivia, 12345678, prov02@gmail.com, Virgen de lujan]\r\n"
     * );
     * sb.append(
     * "-----------------------------------------------------------------------------------------------------\r\n"
     * );
     * sb.append("CREAR USUARIO:\r\n");
     * sb.append("Parámetros: correo, contraseña, nombre de usuario\r\n");
     * sb.append("Ejemplo:\r\n");
     * sb.
     * append("CREATEUSUARIOS[admin2@gmail.com, password, usuario admininistrador]\r\n"
     * );
     * sb.append(
     * "-----------------------------------------------------------------------------------------------------\r\n"
     * );
     * sb.append("CREAR VACUNA:\r\n");
     * sb.append("Parámetros: nombre vacuna, duración en días, descripcion\r\n");
     * sb.append("Ejemplo:\r\n");
     * sb.append("CREATEVACUNAS[vacuna Y, 10, Vacuna controlada para la rabia]\r\n"
     * );
     * sb.append(
     * "-----------------------------------------------------------------------------------------------------\r\n"
     * );
     * sb.append("CREAR NOTA DE VENTA:\r\n");
     * sb.
     * append("Parámetros: fecha, id_almacen, id_usuario (que registra la venta), id_cliente, cantidad_detalle_1,\r\n"
     * +
     * "precio_venta_detalle_1, subtotal_detalle_1, id_medicamento_detalle1,..., cantidad_detalle_n, precio_venta_detalle_n, subtotal_detalle_n, id_medicamento_detalle_n\r\n"
     * );
     * sb.
     * append("los detalles las notas son: cantida, precio de venta, subtotal, id_medicamento\r\n"
     * );
     * sb.append("Ejemplo:\r\n");
     * sb.
     * append("CREATENOTASVENTAS[2024-11-13, 2, 5, 2, 2, 200, 400, 2, 2, 220, 440, 3]\r\n"
     * );
     * sb.append(
     * "-----------------------------------------------------------------------------------------------------\r\n"
     * );
     * sb.append("CREAR NOTA DE COMPRA:\r\n");
     * sb.
     * append("Parámetros: fecha, id_almacen, id_usuario (que registra la venta), id_cliente, cantidad_detalle_1,\r\n"
     * +
     * "precio_compra_detalle_1, porcent_ganancia_detalle_1, id_medicamento_detalle1,..., cantidad_detalle_n, precio_venta_detalle_n, porcent_ganancia_detalle_n, id_medicamento_detalle_n\r\n"
     * );
     * sb.
     * append("los detalles las notas son: cantida, precio de venta, subtotal, id_medicamento\r\n"
     * );
     * sb.append("Ejemplo:\r\n");
     * sb.append("CREATENOTASCOMPRAS[2024-11-13, 2, 3, 2, 2, 200, 50, 400,2]\r\n");
     * sb.append(
     * "=====================================================================================================\r\n"
     * );
     * sb.append("ACTUALIZAR CLIENTE:\r\n");
     * sb.
     * append("Parámetros: id, nombre, apellido, numero, fecha de nacimiento, correo y contraseña\r\n"
     * );
     * sb.append("Ejemplo:\r\n");
     * sb.
     * append("UPDATECLIENTES[1, Gonzalo, Quispe Huanca, 12345678, masculino, 2000-11-01, ruddygonzqh@gmail.com, password]\r\n"
     * );
     * sb.append(
     * "-----------------------------------------------------------------------------------------------------\r\n"
     * );
     * sb.append("ACTUALIZAR MASCOTA:\r\n");
     * sb.
     * append("Parámetros: id, nombre, peso, color, fecha nac, url_foto, id_cliente, id_raza\r\n"
     * );
     * sb.append("Ejemplo:\r\n");
     * sb.
     * append("UPDATEMASCOTAS[2, Pacho 3, 10.5, Blanco, 2028-10-10, https://google.com, 2, 6]\r\n"
     * );
     * sb.append(
     * "-----------------------------------------------------------------------------------------------------\r\n"
     * );
     * sb.append("ACTUALIZAR MEDICAMENTOS:\r\n");
     * sb.
     * append("Parámetros: id, nombre, cant_dosis, fabriacante, fecha caducidad, sustancia controlado (bool 0 o 1), id_categoria\r\n"
     * );
     * sb.append("Ejemplo:\r\n");
     * sb.
     * append("UPDATEMEDICAMENTOS[3, Amoxicilina 500mg, 500mg, Labs ATI, 01-12-2028, 0, 1]\r\n"
     * );
     * sb.append(
     * "-----------------------------------------------------------------------------------------------------\r\n"
     * );
     * sb.append("ACTUALIZAR ESPECIES:\r\n");
     * sb.append("Parámetros: id, nombre de la especie\r\n");
     * sb.append("Ejemplo:\r\n");
     * sb.append("UPDATEESPECIES[4, Perro]\r\n");
     * sb.append(
     * "-----------------------------------------------------------------------------------------------------\r\n"
     * );
     * sb.append("ACTUALIZAR RAZA:\r\n");
     * sb.append("Parámetros: id, nombre de la raza, id_especie\r\n");
     * sb.append("Ejemplo:\r\n");
     * sb.append("UPDATERAZAS[5, Raza 6, 15]\r\n");
     * sb.append(
     * "-----------------------------------------------------------------------------------------------------\r\n"
     * );
     * sb.append("ACTUALIZAR CATEGORÍA:\r\n");
     * sb.append("Parámetros: id, nombre de la categoría\r\n");
     * sb.append("Ejemplo:\r\n");
     * sb.append("UPDATECATEGORIAS[6, Categoria Y]\r\n");
     * sb.append(
     * "-----------------------------------------------------------------------------------------------------\r\n"
     * );
     * sb.append("ACTUALIZAR ALMACÉN:\r\n");
     * sb.
     * append("Parámetros: id, nombre del almacén, dirección, descipción adicional del almacén\r\n"
     * );
     * sb.append("Ejemplo:\r\n");
     * sb.
     * append("UPDATEALMACENES[7, Almacen Y, Los pozos casco viejo, Descripcion del almacen]\r\n"
     * );
     * sb.append(
     * "-----------------------------------------------------------------------------------------------------\r\n"
     * );
     * sb.append("ACTUALIZAR CONSULTA:\r\n");
     * sb.
     * append("Parámetros: id, fecha de consulta, motivo, diagnóstico, tarifa, id_mascota, id_cliente, detalles del tratamiento (receta y nota)\r\n"
     * );
     * sb.append("Ejemplo:\r\n");
     * sb.
     * append("UPDATECONSULTAS[8, 2024-10-10, vomitos de la mascota, la mascota está deshidratada, 10.5, 1, 2, Recetas de medicamentos, Nota adicional, Recetas de tratamientos 2, Nota adicional 2]\r\n"
     * );
     * sb.append(
     * "-----------------------------------------------------------------------------------------------------\r\n"
     * );
     * sb.append("ACTUALIZAR PROVEEDOR:\r\n");
     * sb.
     * append("Parámetros: id, nombre proveedor, país, celular, correo, dirección\r\n"
     * );
     * sb.append("Ejemplo:\r\n");
     * sb.
     * append("UPDATEPROVEEDORES[9, Proveedor Y, Bolivia, 12345678, prov02@gmail.com, Virgen de lujan]\r\n"
     * );
     * sb.append(
     * "-----------------------------------------------------------------------------------------------------\r\n"
     * );
     * sb.append("ACTUALIZAR USUARIO:\r\n");
     * sb.append("Parámetros: id, correo, contraseña, nombre de usuario\r\n");
     * sb.append("Ejemplo:\r\n");
     * sb.
     * append("UPDATEUSUARIOS[10, admin2@gmail.com, password, usuario admininistrador]\r\n"
     * );
     * sb.append(
     * "-----------------------------------------------------------------------------------------------------\r\n"
     * );
     * sb.append("ACTUALIZAR VACUNA:\r\n");
     * sb.append("Parámetros: id, nombre vacuna, duración en días, descripcion\r\n"
     * );
     * sb.append("Ejemplo:\r\n");
     * sb.
     * append("UPDATEVACUNAS[11, vacuna Y, 10, Vacuna controlada para la rabia]\r\n"
     * );
     * sb.append(
     * "-----------------------------------------------------------------------------------------------------\r\n"
     * );
     * sb.append("ACTUALIZAR NOTA DE VENTA:\r\n");
     * sb.
     * append("Parámetros: id, fecha, id_almacen, id_usuario (que registra la venta), id_cliente, cantidad_detalle_1,\r\n"
     * +
     * "precio_venta_detalle_1, subtotal_detalle_1, id_medicamento_detalle1,..., cantidad_detalle_n, precio_venta_detalle_n, subtotal_detalle_n, id_medicamento_detalle_n\r\n"
     * );
     * sb.
     * append("los detalles las notas son: cantida, precio de venta, subtotal, id_medicamento\r\n"
     * );
     * sb.append("Ejemplo:\r\n");
     * sb.
     * append("UPDATENOTASVENTAS[2, 2024-11-13, 2, 5, 2, 2, 200, 400, 2, 2, 220, 440, 3]\r\n"
     * );
     * sb.append(
     * "-----------------------------------------------------------------------------------------------------\r\n"
     * );
     * sb.append("ACTUALIZAR NOTA DE COMPRA:\r\n");
     * sb.
     * append("Parámetros: id, fecha, id_almacen, id_usuario (que registra la venta), id_cliente, cantidad_detalle_1,\r\n"
     * +
     * "precio_venta_detalle_1, subtotal_detalle_1, id_medicamento_detalle1,..., cantidad_detalle_n, precio_venta_detalle_n, subtotal_detalle_n, id_medicamento_detalle_n\r\n"
     * );
     * sb.
     * append("los detalles las notas son: cantida, precio de venta, subtotal, id_medicamento\r\n"
     * );
     * sb.append("Ejemplo:\r\n");
     * sb.append("UPDATENOTASCOMPRAS[4, 2024-11-13, 2, 3, 2, 2, 200, 50, 400,2]\r\n"
     * );
     * sb.append(
     * "=====================================================================================================\r\n"
     * );
     * return sb.toString();
     * }
     */

}
