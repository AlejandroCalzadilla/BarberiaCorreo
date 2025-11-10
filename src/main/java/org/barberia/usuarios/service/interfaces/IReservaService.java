package org.barberia.usuarios.service.interfaces;

import java.util.List;

import org.barberia.usuarios.domain.Pago;
import org.barberia.usuarios.domain.Reserva;
import org.barberia.usuarios.domain.ServicioProducto;

public interface IReservaService {

    String getAllAsTable();
    String getByIdAsTable(Integer id);

    /**
     * Crea una reserva con transacción que incluye:
     * 1. Crear la reserva
     * 2. Crear productos asociados (opcional)
     * 3. Crear pago de anticipo (obligatorio)
     * 
     * Si algo falla, se revierte toda la operación.
     * 
     * @param r Reserva a crear
     * @param productos Lista de productos asociados (puede ser null o vacía)
     * @param pagoAnticipo Pago de anticipo (obligatorio)
     * @return La reserva creada con su ID
     */
    String createConTransaccion(Reserva r, List<ServicioProducto> productos, Pago pagoAnticipo);
   
   


    /**
     * Actualiza una reserva con transacción que incluye:
     * 1. Actualizar la reserva
     * 2. Eliminar productos antiguos y crear los nuevos (opcional)
     * 3. Actualizar el pago asociado (opcional)
     * 
     * Si algo falla, se revierte toda la operación.
     * 
     * @param id ID de la reserva a actualizar
     * @param r Reserva con datos actualizados
     * @param nuevosProductos Nueva lista de productos (puede ser null si no se actualizan)
     * @param pagoActualizado Pago actualizado (puede ser null si no se actualiza)
     * @return La reserva actualizada
     */
    String updateConTransaccion(Integer id, Reserva r, List<ServicioProducto> nuevosProductos, Pago pagoActualizado);
    String deleteConTransaccion(Integer id);







     /**
     * Método simple para actualizar solo la reserva (sin transacción).
     * Usar updateConTransaccion() cuando se necesite actualizar también productos y pago.
     */
    String update(Integer id, Reserva r);
    String delete(Integer id);
    
}
