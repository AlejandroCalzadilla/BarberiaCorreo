package org.barberia.usuarios.service;

import org.barberia.usuarios.domain.Pago;
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
        reservaRepo.findById(reservaId).orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));
        validator.validar(p);
        p.id_reserva = reservaId;
        return repo.save(p);
    }

   

    public String update(Pago p, Integer reservaId) {
        reservaRepo.findById(reservaId).orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));
        validator.validar(p);
        p.id_reserva = reservaId;
        return "Pago actualizado con id=" + repo.save(p).id_pago;

    }

    public String  delete(Integer id) {
       
        repo.deleteById(id);
        return "Pago con id=" + id + " eliminado.";
    }
}
