package org.barberia.usuarios.seeders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.barberia.usuarios.domain.Barbero;
import org.barberia.usuarios.domain.Cliente;
import org.barberia.usuarios.domain.Pago;
import org.barberia.usuarios.domain.Reserva;
import org.barberia.usuarios.domain.Servicio;
import org.barberia.usuarios.domain.enums.EstadoPago;
import org.barberia.usuarios.domain.enums.EstadoReserva;
import org.barberia.usuarios.domain.enums.MetodoPago;
import org.barberia.usuarios.domain.enums.TipoPago;
import org.barberia.usuarios.repository.BarberoRepository;
import org.barberia.usuarios.repository.ClienteRepository;
import org.barberia.usuarios.repository.PagoRepository;
import org.barberia.usuarios.repository.ReservaRepository;
import org.barberia.usuarios.repository.ServicioRepository;

public class ReservaSeeder {

    private ReservaRepository reservaRepository;
    private BarberoRepository barberoRepository;
    private ClienteRepository clienteRepository;
    private ServicioRepository servicioRepository;
    private PagoRepository pagoRepository;
    
    private final List<Integer> createdReservaIds = new ArrayList<>();
    private final List<Integer> createdPagoIds = new ArrayList<>();
    private final Random random = new Random();

    ReservaSeeder(
        ReservaRepository reservaRepository,
        BarberoRepository barberoRepository,
        ClienteRepository clienteRepository,
        ServicioRepository servicioRepository,
        PagoRepository pagoRepository
    ) {
        this.reservaRepository = reservaRepository;
        this.barberoRepository = barberoRepository;
        this.clienteRepository = clienteRepository;
        this.servicioRepository = servicioRepository;
        this.pagoRepository = pagoRepository;
    }

    public void seed() {
        System.out.println("  → Creando reservas para el año 2025...");
        
        // Obtener datos necesarios
        List<Barbero> barberos = barberoRepository.findAll();
        List<Cliente> clientes = clienteRepository.findAll();
        List<Servicio> servicios = servicioRepository.findAll();
        
        if (barberos.isEmpty() || clientes.isEmpty() || servicios.isEmpty()) {
            System.out.println("  ⚠ No hay suficientes datos (barberos, clientes o servicios) para crear reservas");
            return;
        }
        
        int reservasCreadas = 0;
        int pagosCreados = 0;
        
        // Generar reservas distribuidas a lo largo del 2025
        // Enero a Noviembre (mes actual) - todas fechas pasadas
        LocalDate fechaInicio = LocalDate.of(2025, 1, 1);
        LocalDate fechaFin = LocalDate.of(2025, 11, 10); // Fecha actual aproximada
        
        try {
            // Crear aproximadamente 2-3 reservas por semana = ~100-150 reservas en el año
            for (LocalDate fecha = fechaInicio; fecha.isBefore(fechaFin); fecha = fecha.plusDays(random.nextInt(3) + 1)) {
                
                // Saltear algunos días (domingos por ejemplo)
                if (fecha.getDayOfWeek().getValue() == 7) {
                    continue;
                }
                
                // Crear 1-3 reservas por día
                int reservasPorDia = random.nextInt(3) + 1;
                
                for (int i = 0; i < reservasPorDia; i++) {
                    // Seleccionar datos aleatorios
                    Barbero barbero = barberos.get(random.nextInt(barberos.size()));
                    Cliente cliente = clientes.get(random.nextInt(clientes.size()));
                    Servicio servicio = servicios.get(random.nextInt(servicios.size()));
                    
                    // Hora de inicio entre 9:00 y 16:00
                    int horaInicio = 9 + random.nextInt(8);
                    LocalTime hora_inicio = LocalTime.of(horaInicio, random.nextBoolean() ? 0 : 30);
                    
                    // Calcular hora fin basada en duración del servicio
                    LocalTime hora_fin = hora_inicio.plusMinutes(servicio.duracion_minutos_aprox);
                    
                    // Crear la reserva
                    Reserva reserva = new Reserva();
                    reserva.id_cliente = cliente.id_cliente;
                    reserva.id_barbero = barbero.id_barbero;
                    reserva.id_servicio = servicio.id_servicio;
                    reserva.fecha_reserva = fecha;
                    reserva.hora_inicio = hora_inicio;
                    reserva.hora_fin = hora_fin;
                    reserva.precio_servicio = servicio.precio;
                    reserva.total = servicio.precio;
                    reserva.monto_anticipo = servicio.precio.multiply(new BigDecimal("0.5"));
                    
                    // Determinar estado de la reserva (pasadas = mayormente completadas)
                    EstadoReserva[] estadosPosibles = determinarEstado(fecha);
                    reserva.estado = estadosPosibles[random.nextInt(estadosPosibles.length)];
                    
                    // Notas ocasionales
                    if (random.nextInt(4) == 0) {
                        String[] notasPosibles = {
                            "Cliente prefiere barbero específico",
                            "Primera vez del cliente",
                            "Cliente habitual",
                            "Solicitud especial de estilo",
                            "Reserva para evento especial"
                        };
                        reserva.notas = notasPosibles[random.nextInt(notasPosibles.length)];
                    }
                    
                    // Guardar reserva
                    Reserva reservaGuardada = reservaRepository.save(reserva);
                    createdReservaIds.add(reservaGuardada.id_reserva);
                    reservasCreadas++;
                    
                    // Crear pagos según el estado de la reserva
                    pagosCreados += crearPagosParaReserva(reservaGuardada, fecha);
                    
                    // Feedback ocasional para no saturar consola
                    if (reservasCreadas % 50 == 0) {
                        System.out.println("  ... " + reservasCreadas + " reservas creadas hasta " + fecha);
                    }
                }
            }
            
            System.out.println("  → Total de reservas creadas: " + reservasCreadas);
            System.out.println("  → Total de pagos creados: " + pagosCreados);
            
            // Mostrar resumen por estado
            mostrarResumenEstados();
            
        } catch (Exception e) {
            System.err.println("  ✗ Error durante seed de reservas: " + e.getMessage());
            rollback();
            throw e;
        }
    }
    
    private EstadoReserva[] determinarEstado(LocalDate fechaReserva) {
        LocalDate hoy = LocalDate.now();
        
        // Si la fecha ya pasó
        if (fechaReserva.isBefore(hoy)) {
            // 70% completadas, 15% canceladas, 10% no_asistio, 5% en_proceso (quedaron colgadas)
            int rand = random.nextInt(100);
            if (rand < 70) {
                return new EstadoReserva[]{EstadoReserva.completada};
            } else if (rand < 85) {
                return new EstadoReserva[]{EstadoReserva.cancelada};
            } else if (rand < 95) {
                return new EstadoReserva[]{EstadoReserva.no_asistio};
            } else {
                return new EstadoReserva[]{EstadoReserva.en_proceso}; // Error del sistema, quedó en proceso
            }
        } else {
            // Reservas futuras (no debería haber en este seeder, pero por si acaso)
            return new EstadoReserva[]{EstadoReserva.confirmada, EstadoReserva.en_proceso};
        }
    }
    
    private int crearPagosParaReserva(Reserva reserva, LocalDate fechaReserva) {
        int pagosCreados = 0;
        MetodoPago[] metodosPago = MetodoPago.values();
        MetodoPago metodoPago = metodosPago[random.nextInt(metodosPago.length)];
        
        try {
            switch (reserva.estado) {
                case completada:
                    // Pago de anticipo (pagado)
                    Pago anticipo = crearPago(
                        reserva.id_reserva,
                        reserva.monto_anticipo,
                        TipoPago.anticipo,
                        metodoPago,
                        EstadoPago.pagado,
                        fechaReserva.minusDays(random.nextInt(5) + 1).atTime(random.nextInt(20) + 3, random.nextInt(60)),
                        "Anticipo pagado"
                    );
                    createdPagoIds.add(anticipo.id_pago);
                    pagosCreados++;
                    
                    // Pago final (pagado porque está completada)
                    BigDecimal saldoRestante = reserva.total.subtract(reserva.monto_anticipo);
                    Pago pagoFinal = crearPago(
                        reserva.id_reserva,
                        saldoRestante,
                        TipoPago.pago_final,
                        metodoPago,
                        EstadoPago.pagado,
                        fechaReserva.atTime(reserva.hora_fin.plusMinutes(5)),
                        "Pago final completado"
                    );
                    createdPagoIds.add(pagoFinal.id_pago);
                    pagosCreados++;
                    break;
                    
                case cancelada:
                    // Solo anticipo, puede estar pagado o cancelado
                    EstadoPago estadoAnticipo = random.nextBoolean() ? EstadoPago.cancelado : EstadoPago.pagado;
                    Pago anticipoCancelado = crearPago(
                        reserva.id_reserva,
                        reserva.monto_anticipo,
                        TipoPago.anticipo,
                        metodoPago,
                        estadoAnticipo,
                        fechaReserva.minusDays(random.nextInt(10) + 1).atTime(random.nextInt(20) + 3, random.nextInt(60)),
                        estadoAnticipo == EstadoPago.pagado ? "Anticipo pagado (reserva luego cancelada)" : "Anticipo cancelado"
                    );
                    createdPagoIds.add(anticipoCancelado.id_pago);
                    pagosCreados++;
                    
                    // Pago final cancelado
                    Pago finalCancelado = crearPago(
                        reserva.id_reserva,
                        reserva.total.subtract(reserva.monto_anticipo),
                        TipoPago.pago_final,
                        metodoPago,
                        EstadoPago.cancelado,
                        null,
                        "Pago cancelado por cancelación de reserva"
                    );
                    createdPagoIds.add(finalCancelado.id_pago);
                    pagosCreados++;
                    break;
                    
                case no_asistio:
                    // Anticipo pagado (no se devuelve)
                    Pago anticipoNoAsistio = crearPago(
                        reserva.id_reserva,
                        reserva.monto_anticipo,
                        TipoPago.anticipo,
                        metodoPago,
                        EstadoPago.pagado,
                        fechaReserva.minusDays(random.nextInt(5) + 1).atTime(random.nextInt(20) + 3, random.nextInt(60)),
                        "Anticipo pagado (cliente no asistió)"
                    );
                    createdPagoIds.add(anticipoNoAsistio.id_pago);
                    pagosCreados++;
                    
                    // Pago final no realizado
                    Pago finalNoAsistio = crearPago(
                        reserva.id_reserva,
                        reserva.total.subtract(reserva.monto_anticipo),
                        TipoPago.pago_final,
                        metodoPago,
                        EstadoPago.cancelado,
                        null,
                        "No realizado - cliente no asistió"
                    );
                    createdPagoIds.add(finalNoAsistio.id_pago);
                    pagosCreados++;
                    break;
                    
                case en_proceso:
                case confirmada:
                    // Solo anticipo pagado, pago final pendiente
                    Pago anticipoConfirmado = crearPago(
                        reserva.id_reserva,
                        reserva.monto_anticipo,
                        TipoPago.anticipo,
                        metodoPago,
                        EstadoPago.pagado,
                        fechaReserva.minusDays(random.nextInt(5) + 1).atTime(random.nextInt(20) + 3, random.nextInt(60)),
                        "Anticipo pagado"
                    );
                    createdPagoIds.add(anticipoConfirmado.id_pago);
                    pagosCreados++;
                    
                    Pago finalPendiente = crearPago(
                        reserva.id_reserva,
                        reserva.total.subtract(reserva.monto_anticipo),
                        TipoPago.pago_final,
                        metodoPago,
                        EstadoPago.pendiente,
                        null,
                        "Pago final pendiente"
                    );
                    createdPagoIds.add(finalPendiente.id_pago);
                    pagosCreados++;
                    break;
                    
                    
                    
            }
            
        } catch (Exception e) {
            System.err.println("  ✗ Error creando pagos para reserva " + reserva.id_reserva + ": " + e.getMessage());
        }
        
        return pagosCreados;
    }
    
    private Pago crearPago(Integer idReserva, BigDecimal monto, TipoPago tipo, 
                          MetodoPago metodo, EstadoPago estado, LocalDateTime fechaPago, String notas) {
        Pago pago = new Pago();
        pago.id_reserva = idReserva;
        pago.monto_total = monto;
        pago.tipo_pago = tipo;
        pago.metodo_pago = metodo;
        pago.estado = estado;
        pago.fecha_pago = fechaPago;
        pago.notas = notas;
        
        return pagoRepository.save(pago);
    }
    
    private void mostrarResumenEstados() {
        List<Reserva> todasReservas = reservaRepository.findAll();
        int completadas = 0, canceladas = 0, noAsistio = 0, enProceso = 0, confirmadas = 0, pendientes = 0;
        
        for (Reserva r : todasReservas) {
            if (createdReservaIds.contains(r.id_reserva)) {
                switch (r.estado) {
                    case completada: completadas++; break;
                    case cancelada: canceladas++; break;
                    case no_asistio: noAsistio++; break;
                    case en_proceso: enProceso++; break;
                    case confirmada: confirmadas++; break;
                   
                }
            }
        }
        
        System.out.println("\n  📊 Resumen de estados de reservas:");
        System.out.println("    ✓ Completadas: " + completadas);
        System.out.println("    ✗ Canceladas: " + canceladas);
        System.out.println("    ⚠ No asistió: " + noAsistio);
        System.out.println("    ⏳ En proceso: " + enProceso);
        System.out.println("    ✔ Confirmadas: " + confirmadas);
        System.out.println("    💰 Pendiente pago: " + pendientes);
    }

    public void rollback() {
        System.out.println("  → Iniciando rollback de reservas y pagos...");
        
        // Primero eliminar pagos
        for (Integer id : new ArrayList<>(createdPagoIds)) {
            try {
                pagoRepository.deleteById(id);
            } catch (Exception ex) {
                System.err.println("  ✗ Error eliminando pago id=" + id + " durante rollback: " + ex.getMessage());
            }
            createdPagoIds.remove(id);
        }
        
        // Luego eliminar reservas
        for (Integer id : new ArrayList<>(createdReservaIds)) {
            try {
                reservaRepository.deleteById(id);
            } catch (Exception ex) {
                System.err.println("  ✗ Error eliminando reserva id=" + id + " durante rollback: " + ex.getMessage());
            }
            createdReservaIds.remove(id);
        }
        
        System.out.println("  Rollback de reservas completado");
    }
    
}
