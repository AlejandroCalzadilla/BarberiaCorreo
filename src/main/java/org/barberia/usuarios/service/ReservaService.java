package org.barberia.usuarios.service;

import org.barberia.usuarios.domain.Pago;
import org.barberia.usuarios.domain.Producto;
import org.barberia.usuarios.domain.Reserva;
import org.barberia.usuarios.domain.Servicio;
import org.barberia.usuarios.domain.ServicioProducto;
import org.barberia.usuarios.domain.enums.EstadoPago;
import org.barberia.usuarios.domain.enums.EstadoReserva;
import org.barberia.usuarios.domain.enums.MetodoPago;
import org.barberia.usuarios.domain.enums.TipoPago;
import org.barberia.usuarios.mapper.ReservaMapper;
import org.barberia.usuarios.repository.PagoRepository;
import org.barberia.usuarios.repository.ProductoRepository;
import org.barberia.usuarios.repository.ReservaRepository;
import org.barberia.usuarios.repository.ServicioProductoRepository;
import org.barberia.usuarios.repository.ServicioRepository;
import org.barberia.usuarios.validation.ReservaValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class ReservaService {
    private final ReservaRepository repo;
    private final ReservaValidator validator;
    private final PagoRepository pagoRepo;
    private final ServicioProductoRepository servicioProductoRepo;
    private final ProductoRepository productoRepo;
    private final ServicioRepository servicioRepo;

    public ReservaService(ReservaRepository repo, ReservaValidator validator,
            PagoRepository pagoRepo, ServicioProductoRepository servicioProductoRepo, ProductoRepository productoRepo,
            ServicioRepository servicioRepo) {
        this.repo = repo;
        this.validator = validator;
        this.pagoRepo = pagoRepo;
        this.servicioProductoRepo = servicioProductoRepo;
        this.productoRepo = productoRepo;
        this.servicioRepo = servicioRepo;
    }

    public String getAllAsTable() {
        return ReservaMapper.obtenerTodosTable(repo.findAll());
    }

    public String getByIdAsTable(Integer id) {
        return repo.findById(id).map(ReservaMapper::obtenerUnoTable).orElse("No se encontró reserva con id=" + id);
    }

    public Reserva createConTransaccion(
            Integer id_cliente,
            Integer id_barbero,
            Integer id_servicio,
            LocalDate fecha_reserva,
            LocalTime hora_inicio,
            LocalTime hora_fin,
            BigDecimal total,
            String notas,
            BigDecimal precio_servicio,
            BigDecimal monto_anticipo,
            BigDecimal monto_total,
            MetodoPago metodo_pago,
            String tipo_pago,
            LocalDateTime fecha_pago,
            String notaspago) {

        Pago pagoAnticipo = new Pago();
        pagoAnticipo.metodo_pago = metodo_pago;
        pagoAnticipo.tipo_pago = TipoPago.parse(tipo_pago);
        pagoAnticipo.notas = notaspago;
        pagoAnticipo.estado = EstadoPago.pagado;
        pagoAnticipo.fecha_pago = fecha_pago;

            
        

        Reserva r = new Reserva();
        r.id_cliente = id_cliente;
        r.id_barbero = id_barbero;
        r.id_servicio = id_servicio;
        r.fecha_reserva = fecha_reserva;
        r.hora_inicio = hora_inicio;
        r.hora_fin = hora_fin;
        r.estado = EstadoReserva.confirmada;
        r.total = total;
        validator.validar(r);
        if (pagoAnticipo == null) {
            throw new IllegalArgumentException("El pago de anticipo es obligatorio para crear una reserva");
        }
        Integer reservaId = null;
        List<Integer> productosCreados = new java.util.ArrayList<>();
        Integer pagoId = null;
        try {
            // 1. Crear la reserva
            Optional<Servicio> servicio = servicioRepo.findById(r.id_servicio);
            if (servicio.isPresent()) {
                r.monto_anticipo = r.total.multiply(BigDecimal.valueOf(0.5));
            } else {
                throw new IllegalArgumentException("El servicio con ID " + r.id_servicio + " no existe.");
            }
            List<ServicioProducto> serviciosProductos = servicioProductoRepo.findByServicioId(r.id_servicio);
            for (ServicioProducto sp : serviciosProductos) {
                Optional<Producto> p = productoRepo.findById(sp.id_producto);
                if (p.isPresent()) {
                    Producto producto = p.get();
                    if (producto.stock_actual < sp.cantidad) {
                        throw new IllegalArgumentException(
                                "No hay suficiente stock para el producto ID " + producto.id_producto +
                                        ". Stock disponible: " + producto.stock_actual + ", cantidad requerida: "
                                        + sp.cantidad);
                    }
                    producto.stock_actual -= sp.cantidad;
                    productoRepo.save(producto);
                } else {
                    throw new IllegalArgumentException("El producto con ID " + sp.id_producto + " no existe.");
                }
                Reserva reservaCreada = repo.save(r);
                reservaId = reservaCreada.id_reserva;

                // 3. Crear el pago de anticipo (obligatorio)
                pagoAnticipo.id_reserva = reservaId;
                pagoAnticipo.fecha_pago = LocalDateTime.now();
                pagoAnticipo.estado = EstadoPago.pagado;
                pagoAnticipo.tipo_pago = TipoPago.anticipo;
                pagoAnticipo.monto_total = r.monto_anticipo;

                Pago pagoCreado = pagoRepo.save(pagoAnticipo);
                // crear el pago para el saldo restante
                Pago pagoSaldo = new Pago();
                pagoSaldo.id_reserva = reservaId;
                pagoSaldo.monto_total = r.total.subtract(pagoAnticipo.monto_total);
                pagoSaldo.metodo_pago = pagoAnticipo.metodo_pago;
                pagoSaldo.tipo_pago = TipoPago.pago_final;
                pagoSaldo.estado = EstadoPago.pendiente;
                pagoSaldo.fecha_pago = null; // Aún no se ha realizado el pago
                pagoSaldo.notas = "Pago final pendiente para la reserva ID " + reservaId;
                pagoRepo.save(pagoSaldo);

                pagoId = pagoCreado.id_pago;
                System.out.println("✓ Pago de anticipo creado con ID: " + pagoId);
                System.out.println("✓✓ Reserva completa creada exitosamente");

                return reservaCreada;
            }
        } catch (Exception e) {
            System.err.println("✗ Error al crear reserva, ejecutando rollback...");
            // Rollback manual: eliminar en orden inverso
            try {
                if (pagoId != null) {
                    pagoRepo.deleteById(pagoId);
                    System.out.println("  - Pago eliminado");
                }
                if (reservaId != null) {
                    repo.deleteById(reservaId);
                    System.out.println("  - Reserva eliminada");
                }
                System.out.println("✓ Rollback completado exitosamente");
            } catch (Exception rollbackEx) {
                System.err.println("✗✗ ERROR CRÍTICO: Fallo en rollback - " + rollbackEx.getMessage());
                throw new RuntimeException("Error crítico: fallo al crear reserva y fallo en rollback. " +
                        "Revisar datos manualmente. Error original: " + e.getMessage(), rollbackEx);
            }
            throw new RuntimeException("Error al crear la reserva: " + e.getMessage(), e);
        }
        throw new RuntimeException(
                "No se pudo crear la reserva: no se encontraron servicios asociados o no se realizó la creación.");
    }

    public Reserva update(Integer id, Reserva r) {
        validator.validar(r);
        r.id_reserva = id;
        return repo.save(r);
    }

    public void delete(Integer id) {
        for (Pago p : pagoRepo.findAll()) {
            if (p.id_reserva != null && p.id_reserva.equals(id)) {
                pagoRepo.deleteById(p.id_pago);
            }
        }
        repo.deleteById(id);

    }

    public Reserva updateConTransaccion(Integer id, Reserva r) {
        validator.validar(r);
        r.id_reserva = id;

        // Obtener reserva anterior
        Reserva reservaAnterior = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe reserva con id=" + id));

        try {
            // Caso 1: Cancelación o No asistió - Devolver stock de productos
            if (r.estado == EstadoReserva.cancelada || r.estado == EstadoReserva.no_asistio) {
                System.out.println("✓ Procesando cancelación/no asistencia - devolviendo stock...");

                // Obtener productos del servicio y devolver al stock
                List<ServicioProducto> serviciosProductos = servicioProductoRepo
                        .findByServicioId(reservaAnterior.id_servicio);
                for (ServicioProducto sp : serviciosProductos) {
                    Optional<Producto> productoOpt = productoRepo.findById(sp.id_producto);
                    if (productoOpt.isPresent()) {
                        Producto producto = productoOpt.get();
                        producto.stock_actual += sp.cantidad; // Devolver stock
                        productoRepo.save(producto);
                        System.out.println("  - Stock devuelto para producto ID " + producto.id_producto +
                                ": +" + sp.cantidad + " unidades");
                    }
                }
                // Actualizar estado de pagos a cancelado si aplica
                // Buscar pagos asociados y actualizarlos
                List<Pago> pagos = pagoRepo.findAll();
                for (Pago pago : pagos) {
                    if (pago.id_reserva != null && pago.id_reserva.equals(id)) {
                        pago.estado = EstadoPago.cancelado;
                        pagoRepo.save(pago);
                    }
                }

            }

            // Caso 2: Reprogramación - Solo actualizar fecha/hora/estado
            // Este caso no afecta el stock de productos

            // Actualizar la reserva
            Reserva reservaActualizada = repo.save(r);
            System.out.println("✓ Reserva actualizada con ID: " + id);

            System.out.println("✓✓ Reserva actualizada exitosamente");
            return reservaActualizada;

        } catch (Exception e) {
            System.err.println("✗ Error al actualizar reserva, ejecutando rollback...");
            try {
                // Rollback: restaurar reserva anterior
                repo.save(reservaAnterior);

                // Si se devolvió stock, revertirlo
                if (r.estado == EstadoReserva.cancelada || r.estado == EstadoReserva.no_asistio) {
                    List<ServicioProducto> serviciosProductos = servicioProductoRepo
                            .findByServicioId(reservaAnterior.id_servicio);
                    for (ServicioProducto sp : serviciosProductos) {
                        Optional<Producto> productoOpt = productoRepo.findById(sp.id_producto);
                        if (productoOpt.isPresent()) {
                            Producto producto = productoOpt.get();
                            producto.stock_actual -= sp.cantidad; // Revertir devolución
                            productoRepo.save(producto);
                        }
                    }
                }

                System.out.println("✓ Rollback completado");
            } catch (Exception rollbackEx) {
                System.err.println("✗✗ ERROR CRÍTICO: Fallo en rollback - " + rollbackEx.getMessage());
                throw new RuntimeException("Error crítico: fallo al actualizar reserva y fallo en rollback. " +
                        "Revisar datos manualmente. Error original: " + e.getMessage(), rollbackEx);
            }
            throw new RuntimeException("Error al actualizar la reserva: " + e.getMessage(), e);
        }
    }
}
