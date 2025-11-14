package org.barberia.usuarios.service;

import java.lang.StackWalker.Option;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.barberia.usuarios.domain.Pago;
import org.barberia.usuarios.domain.Reserva;
import org.barberia.usuarios.domain.enums.EstadoPago;
import org.barberia.usuarios.domain.enums.TipoPago;
import org.barberia.usuarios.mapper.PagoMapper;
import org.barberia.usuarios.repository.PagoRepository;
import org.barberia.usuarios.repository.ReservaRepository;
import org.barberia.usuarios.validation.PagoValidator;

public class PagoService {
    private final PagoRepository repo;
    private final PagoValidator validator;
    private final ReservaRepository reservaRepo;

    public PagoService(
            PagoRepository repo,
            PagoValidator validator,
            ReservaRepository reservaRepo)

    {
        this.repo = repo;
        this.validator = validator;
        this.reservaRepo = reservaRepo;
    }

    public String getAllAsTable() {
        return PagoMapper.obtenerTodosTable(repo.findAll());
    }

    public String getByIdAsTable(Integer id) {
        return repo.findById(id).map(PagoMapper::obtenerUnoTable).orElse("No se encontró pago con id=" + id);
    }

    public Pago create(Pago p, Integer reservaId) {
        validator.validar(p);
        p.fecha_pago = LocalDateTime.now();
        Reserva reserva = reservaRepo.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));

        // Obtener todos los pagos existentes de esta reserva
        java.util.List<Pago> pagosExistentes = repo.findAll().stream()
                .filter(pago -> pago.id_reserva.equals(reservaId))
                .toList();

        // Validar que no existan pagos duplicados según el tipo
        if (p.tipo_pago == TipoPago.pago_completo) {
            // Si ya existe algún pago, no se puede crear un pago completo
            if (!pagosExistentes.isEmpty()) {
                throw new IllegalArgumentException(
                        "No se puede crear un pago completo porque ya existen pagos para esta reserva");
            }
        }

        if (p.tipo_pago == TipoPago.anticipo) {
            // Verificar que no exista ya un anticipo
            boolean existeAnticipo = pagosExistentes.stream()
                    .anyMatch(pago -> pago.tipo_pago == TipoPago.anticipo);
            if (existeAnticipo) {
                throw new IllegalArgumentException("Ya existe un anticipo registrado para esta reserva");
            }
            
            // Verificar que no exista un pago completo
            boolean existePagoCompleto = pagosExistentes.stream()
                    .anyMatch(pago -> pago.tipo_pago == TipoPago.pago_completo);
            if (existePagoCompleto) {
                throw new IllegalArgumentException(
                        "No se puede crear un anticipo porque ya existe un pago completo para esta reserva");
            }
        }

        if (p.tipo_pago == TipoPago.pago_final) {
            // Verificar que exista un anticipo previo
            boolean existeAnticipo = pagosExistentes.stream()
                    .anyMatch(pago -> pago.tipo_pago == TipoPago.anticipo);
            if (!existeAnticipo) {
                throw new IllegalArgumentException(
                        "No se puede crear un pago final sin un anticipo previo");
            }
            
            // Verificar que no exista ya un pago final
            boolean existePagoFinal = pagosExistentes.stream()
                    .anyMatch(pago -> pago.tipo_pago == TipoPago.pago_final);
            if (existePagoFinal) {
                throw new IllegalArgumentException("Ya existe un pago final registrado para esta reserva");
            }
        }

        // Validar montos según el tipo de pago
        if (p.tipo_pago == TipoPago.anticipo) {
            if (p.monto_total.compareTo(reserva.monto_anticipo) == 0) {
                p.estado = EstadoPago.pagado;
            } else {
                throw new IllegalArgumentException("El monto del anticipo debe ser igual a " + reserva.monto_anticipo);
            }
        } else if (p.tipo_pago == TipoPago.pago_completo) {
            if (p.monto_total.compareTo(reserva.total) == 0) {
                p.estado = EstadoPago.pagado;
            } else {
                throw new IllegalArgumentException("El monto del pago completo debe ser igual a " + reserva.total);
            }
        } else { // pago_final
            BigDecimal monto_esperado = reserva.total.subtract(reserva.monto_anticipo);
            if (p.monto_total.compareTo(monto_esperado) == 0) {
                p.estado = EstadoPago.pagado;
            } else {
                throw new IllegalArgumentException("El monto del pago final debe ser igual a " + monto_esperado);
            }
        }

        p.id_reserva = reservaId;
        return repo.save(p);
    }

   

    public String update(Pago p, Integer reservaId) {
        Reserva reserva = reservaRepo.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));
        validator.validar(p);
        
        Pago pagoExistente = repo.findById(p.id_pago)
                .orElseThrow(() -> new IllegalArgumentException("Pago con id=" + p.id_pago + " no encontrado"));
        
         p.fecha_pago = pagoExistente.fecha_pago;       
         
        // Obtener todos los pagos existentes de esta reserva (excluyendo el pago actual)
        java.util.List<Pago> pagosExistentes = repo.findAll().stream()
                .filter(pago -> pago.id_reserva.equals(reservaId) && !pago.id_pago.equals(p.id_pago))
                .toList();

        // Validar que no existan pagos duplicados según el tipo
        if (p.tipo_pago == TipoPago.pago_completo) {
            // Si ya existe algún otro pago, no se puede actualizar a pago completo
            if (!pagosExistentes.isEmpty()) {
                throw new IllegalArgumentException(
                        "No se puede actualizar a pago completo porque ya existen otros pagos para esta reserva");
            }
        }

        if (p.tipo_pago == TipoPago.anticipo) {
            // Verificar que no exista ya otro anticipo
            boolean existeAnticipo = pagosExistentes.stream()
                    .anyMatch(pago -> pago.tipo_pago == TipoPago.anticipo);
            if (existeAnticipo) {
                throw new IllegalArgumentException("Ya existe otro anticipo registrado para esta reserva");
            }
            
            // Verificar que no exista un pago completo
            boolean existePagoCompleto = pagosExistentes.stream()
                    .anyMatch(pago -> pago.tipo_pago == TipoPago.pago_completo);
            if (existePagoCompleto) {
                throw new IllegalArgumentException(
                        "No se puede actualizar a anticipo porque ya existe un pago completo para esta reserva");
            }
        }

        if (p.tipo_pago == TipoPago.pago_final) {
            // Verificar que exista un anticipo previo
            boolean existeAnticipo = pagosExistentes.stream()
                    .anyMatch(pago -> pago.tipo_pago == TipoPago.anticipo);
            if (!existeAnticipo) {
                throw new IllegalArgumentException(
                        "No se puede actualizar a pago final sin un anticipo previo");
            }
            
            // Verificar que no exista ya otro pago final
            boolean existePagoFinal = pagosExistentes.stream()
                    .anyMatch(pago -> pago.tipo_pago == TipoPago.pago_final);
            if (existePagoFinal) {
                throw new IllegalArgumentException("Ya existe otro pago final registrado para esta reserva");
            }
        }

        // Validar montos según el tipo de pago
        if (p.tipo_pago == TipoPago.anticipo) {
            if (p.monto_total.compareTo(reserva.monto_anticipo) == 0) {
                p.estado = EstadoPago.pagado;
            } else {
                throw new IllegalArgumentException("El monto del anticipo debe ser igual a " + reserva.monto_anticipo);
            }
        } else if (p.tipo_pago == TipoPago.pago_completo) {
            if (p.monto_total.compareTo(reserva.total) == 0) {
                p.estado = EstadoPago.pagado;
            } else {
                throw new IllegalArgumentException("El monto del pago completo debe ser igual a " + reserva.total);
            }
        } else { // pago_final
            BigDecimal monto_esperado = reserva.total.subtract(reserva.monto_anticipo);
            if (p.monto_total.compareTo(monto_esperado) == 0) {
                p.estado = EstadoPago.pagado;
            } else {
                throw new IllegalArgumentException("El monto del pago final debe ser igual a " + monto_esperado);
            }
        }

        p.id_reserva = reservaId;
        return "Pago actualizado con id=" + repo.save(p).id_pago;
    }

    public String  delete(Integer id) {
        repo.deleteById(id);
        return "Pago con id=" + id + " eliminado.";
    }
}
