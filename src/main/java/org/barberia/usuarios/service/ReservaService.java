package org.barberia.usuarios.service;

import org.barberia.usuarios.domain.Barbero;
import org.barberia.usuarios.domain.Horario;
import org.barberia.usuarios.domain.Pago;
import org.barberia.usuarios.domain.Producto;
import org.barberia.usuarios.domain.Reserva;
import org.barberia.usuarios.domain.Servicio;
import org.barberia.usuarios.domain.ServicioProducto;
import org.barberia.usuarios.domain.Usuario;
import org.barberia.usuarios.domain.enums.DiaSemana;
import org.barberia.usuarios.domain.enums.EstadoReserva;
import org.barberia.usuarios.domain.enums.EstadoUsuario;
import org.barberia.usuarios.mapper.ReservaMapper;
import org.barberia.usuarios.repository.BarberoRepository;
import org.barberia.usuarios.repository.HorarioRepository;
import org.barberia.usuarios.repository.PagoRepository;
import org.barberia.usuarios.repository.ProductoRepository;
import org.barberia.usuarios.repository.ReservaRepository;
import org.barberia.usuarios.repository.ServicioProductoRepository;
import org.barberia.usuarios.repository.ServicioRepository;
import org.barberia.usuarios.repository.UsuarioRepository;
import org.barberia.usuarios.validation.ReservaValidator;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class ReservaService {
    private final ReservaRepository repo;
    private final ReservaValidator validator;
    private final PagoRepository pagoRepo;
    private final ServicioRepository servicioRepo;
    private final BarberoRepository barberoRepo;
    private final UsuarioRepository usuarioRepo;
    private final HorarioRepository horarioRepo;
    private final ServicioProductoRepository servicioProductoRepo;
    private final ProductoRepository productoRepo;

    public ReservaService(
            ReservaRepository repo,
            ReservaValidator validator,
            PagoRepository pagoRepo,
            ServicioRepository servicioRepo,
            BarberoRepository barberoRepo,
            UsuarioRepository usuarioRepo,
            HorarioRepository horarioRepo,
            ServicioProductoRepository servicioProductoRepo,
            ProductoRepository productoRepo) {
        this.repo = repo;
        this.validator = validator;
        this.pagoRepo = pagoRepo;
        this.servicioRepo = servicioRepo;
        this.barberoRepo = barberoRepo;
        this.usuarioRepo = usuarioRepo;
        this.horarioRepo = horarioRepo;
        this.servicioProductoRepo = servicioProductoRepo;
        this.productoRepo = productoRepo;
    }

    public String getAllAsTable() {
        return ReservaMapper.obtenerTodosTable(repo.findAll());
    }

    public String getByIdAsTable(Integer id) {
        return repo.findById(id).map(ReservaMapper::obtenerUnoTable).orElse("No se encontró reserva con id=" + id);
    }

    /**
     * Crea una reserva verificando que:
     * 1. El barbero esté activo
     * 2. La fecha y hora estén dentro del horario del barbero
     */
    public Reserva create(
            Integer id_cliente,
            Integer id_barbero,
            Integer id_servicio,
            LocalDate fecha_reserva,
            LocalTime hora_inicio,
            LocalTime hora_fin,
            String notas) {

        // Crear reserva
        Reserva r = new Reserva();
        r.id_cliente = id_cliente;
        r.id_barbero = id_barbero;
        r.id_servicio = id_servicio;
        r.fecha_reserva = fecha_reserva;
        r.hora_inicio = hora_inicio;
        r.hora_fin = hora_fin;
        r.notas = notas;
        r.estado = EstadoReserva.confirmada;
        // Validar estructura básica
        validator.validar(r);
        // 1. Verificar que el barbero existe
        boolean barberoActivo = barberoRepo.findById(r.id_barbero)
                .map(b -> usuarioRepo.findById(b.id_usuario)
                        .map(u -> u.estado == EstadoUsuario.activo)
                        .orElse(false))
                .orElse(false);

        if (!barberoActivo) {
            throw new IllegalArgumentException("El barbero no está activo  o no existe. No se pueden crear reservas.");
        }
        // 3. Verificar que la fecha/hora estén dentro del horario del barbero
        DayOfWeek dayOfWeek = fecha_reserva.getDayOfWeek();
        DiaSemana diaSemana = convertirDayOfWeekADiaSemana(dayOfWeek);
        List<Horario> horarios = horarioRepo.findAll();
        boolean horarioValido = false;
        for (Horario h : horarios) {
            if (h.id_barbero.equals(id_barbero) && h.dia_semana == diaSemana) {
                // Verificar que la hora de inicio y fin estén dentro del horario
                if (!hora_inicio.isBefore(h.hora_inicio) &&
                        !hora_fin.isAfter(h.hora_fin)) {
                    horarioValido = true;
                    break;
                }
            }
        }
        if (!horarioValido) {
            throw new IllegalArgumentException(
                    String.format("El barbero no tiene disponibilidad el día %s entre las %s y %s. " +
                            "Verifique los horarios del barbero.",
                            diaSemana, hora_inicio, hora_fin));
        }

        // 4. Obtener información del servicio para calcular totales
        Optional<Servicio> servicioOpt = servicioRepo.findById(r.id_servicio);
        if (servicioOpt.isEmpty()) {
            throw new IllegalArgumentException("El servicio con ID " + r.id_servicio + " no existe.");
        }
        Servicio servicio = servicioOpt.get();
        r.total = servicio.precio;
        r.monto_anticipo = r.total.multiply(BigDecimal.valueOf(0.5)); // 50% de anticipo
        r.precio_servicio = servicio.precio;

        // 5. Verificar y descontar stock de productos asociados al servicio
        List<ServicioProducto> serviciosProductos = servicioProductoRepo.findByServicioId(r.id_servicio);
        
        if (!serviciosProductos.isEmpty()) {
            // Primero verificar que hay suficiente stock de todos los productos
            for (ServicioProducto sp : serviciosProductos) {
                Optional<Producto> productoOpt = productoRepo.findById(sp.id_producto);
                if (productoOpt.isEmpty()) {
                    throw new IllegalArgumentException("El producto con ID " + sp.id_producto + " no existe.");
                }
                
                Producto producto = productoOpt.get();
                if (producto.stock_actual < sp.cantidad) {
                    throw new IllegalArgumentException(
                        String.format("Stock insuficiente para el producto '%s'. Disponible: %d, Requerido: %d",
                            producto.nombre, producto.stock_actual, sp.cantidad));
                }
            }
            
            // Si hay suficiente stock de todos, entonces descontar
            for (ServicioProducto sp : serviciosProductos) {
                Optional<Producto> productoOpt = productoRepo.findById(sp.id_producto);
                if (productoOpt.isPresent()) {
                    Producto producto = productoOpt.get();
                    producto.stock_actual -= sp.cantidad;
                    productoRepo.save(producto);
                    System.out.println(String.format("✓ Stock descontado: %s - Cantidad: %d (Stock restante: %d)",
                        producto.nombre, sp.cantidad, producto.stock_actual));
                }
            }
        }

        // 6. Guardar la reserva
        Reserva reservaCreada = repo.save(r);
        System.out.println("✓ Reserva creada exitosamente con ID: " + reservaCreada.id_reserva);
        return reservaCreada;
    }

    /**
     * Actualiza una reserva verificando barbero activo y horarios si cambian
     * fecha/hora
     */
    public Reserva update(Integer id, Reserva r) {
        // Verificar que existe la reserva
        Reserva reservaAnterior = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe reserva con id=" + id));

        validator.validar(r);
        r.id_reserva = id;

        // Si se cambian fecha, hora o barbero, validar horarios
        boolean cambioFechaHoraBarbero = !r.fecha_reserva.equals(reservaAnterior.fecha_reserva) ||
                !r.hora_inicio.equals(reservaAnterior.hora_inicio) ||
                !r.hora_fin.equals(reservaAnterior.hora_fin) ||
                !r.id_barbero.equals(reservaAnterior.id_barbero);

        if (cambioFechaHoraBarbero) {
            // 1. Verificar que el barbero existe
            Optional<Barbero> barberoOpt = barberoRepo.findById(r.id_barbero);
            if (barberoOpt.isEmpty()) {
                throw new IllegalArgumentException("El barbero con ID " + r.id_barbero + " no existe.");
            }
            Barbero barbero = barberoOpt.get();

            // 2. Verificar que el barbero esté activo
            Optional<Usuario> usuarioOpt = usuarioRepo.findById(barbero.id_usuario);
            if (usuarioOpt.isEmpty()) {
                throw new IllegalArgumentException("No se encontró el usuario asociado al barbero.");
            }
            Usuario usuario = usuarioOpt.get();

            if (usuario.estado != EstadoUsuario.activo) {
                throw new IllegalArgumentException("El barbero no está activo. No se puede actualizar la reserva.");
            }

            // 3. Verificar que la fecha/hora estén dentro del horario del barbero
            DayOfWeek dayOfWeek = r.fecha_reserva.getDayOfWeek();
            DiaSemana diaSemana = convertirDayOfWeekADiaSemana(dayOfWeek);

            List<Horario> horarios = horarioRepo.findAll();
            boolean horarioValido = false;

            for (Horario h : horarios) {
                if (h.id_barbero.equals(r.id_barbero) && h.dia_semana == diaSemana) {
                    // Verificar que la hora de inicio y fin estén dentro del horario
                    if (!r.hora_inicio.isBefore(h.hora_inicio) &&
                            !r.hora_fin.isAfter(h.hora_fin)) {
                        horarioValido = true;
                        break;
                    }
                }
            }

            if (!horarioValido) {
                throw new IllegalArgumentException(
                        String.format("El barbero no tiene disponibilidad el día %s entre las %s y %s. " +
                                "Verifique los horarios del barbero.",
                                diaSemana, r.hora_inicio, r.hora_fin));
            }
        }

        // Verificar si se está cancelando o marcando como no_asistio para devolver stock
        boolean devolverStock = (r.estado == EstadoReserva.cancelada || r.estado == EstadoReserva.no_asistio) &&
                                 (reservaAnterior.estado != EstadoReserva.cancelada && 
                                  reservaAnterior.estado != EstadoReserva.no_asistio);
        
        if (devolverStock) {
            System.out.println("✓ Procesando cancelación/no asistencia - devolviendo stock...");
            
            // Obtener productos del servicio y devolver al stock
            List<ServicioProducto> serviciosProductos = servicioProductoRepo.findByServicioId(reservaAnterior.id_servicio);
            for (ServicioProducto sp : serviciosProductos) {
                Optional<Producto> productoOpt = productoRepo.findById(sp.id_producto);
                if (productoOpt.isPresent()) {
                    Producto producto = productoOpt.get();
                    producto.stock_actual += sp.cantidad; // Devolver stock
                    productoRepo.save(producto);
                    System.out.println(String.format("  ✓ Stock devuelto: %s - Cantidad: %d (Stock actual: %d)",
                        producto.nombre, sp.cantidad, producto.stock_actual));
                }
            }
        }

        // Actualizar la reserva
        Reserva reservaActualizada = repo.save(r);
        System.out.println("✓ Reserva actualizada con ID: " + id);

        return reservaActualizada;
    }

    public String delete(Integer id) {
        // Eliminar pagos asociados primero
        for (Pago p : pagoRepo.findAll()) {
            if (p.id_reserva != null && p.id_reserva.equals(id)) {
                pagoRepo.deleteById(p.id_pago);
            }
        }
        repo.deleteById(id);

        return "Reserva con id=" + id + " eliminada.";
    }

    /**
     * Convierte DayOfWeek de Java a DiaSemana del dominio
     */
    private DiaSemana convertirDayOfWeekADiaSemana(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> DiaSemana.lunes;
            case TUESDAY -> DiaSemana.martes;
            case WEDNESDAY -> DiaSemana.miercoles;
            case THURSDAY -> DiaSemana.jueves;
            case FRIDAY -> DiaSemana.viernes;
            case SATURDAY -> DiaSemana.sabado;
            case SUNDAY -> DiaSemana.domingo;
        };
    }
}
