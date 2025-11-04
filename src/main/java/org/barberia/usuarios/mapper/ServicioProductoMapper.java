package org.barberia.usuarios.mapper;

import org.barberia.usuarios.domain.ServicioProducto;

import java.util.List;

public class ServicioProductoMapper {
    public static String obtenerTodosTable(List<ServicioProducto> items) {
        StringBuilder sb = new StringBuilder();
        int idW=10, srvW=10, prodW=10, cantW=8, subW=12;

        sb.append("┌").append("─".repeat(idW))
          .append("┬").append("─".repeat(srvW))
          .append("┬").append("─".repeat(prodW))
          .append("┬").append("─".repeat(cantW))
          .append("┬").append("─".repeat(subW))
          .append("┐\n");

        sb.append(String.format("│%-"+idW+"s│%-"+srvW+"s│%-"+prodW+"s│%-"+cantW+"s│%-"+subW+"s│\n",
                "ID","ID_SRV","ID_PROD","Cant","Subtotal"));

        sb.append("├").append("─".repeat(idW))
          .append("┼").append("─".repeat(srvW))
          .append("┼").append("─".repeat(prodW))
          .append("┼").append("─".repeat(cantW))
          .append("┼").append("─".repeat(subW))
          .append("┤\n");

        for (ServicioProducto sp : items) {
            sb.append(String.format("│%-"+idW+"s│%-"+srvW+"s│%-"+prodW+"s│%-"+cantW+"s│%-"+subW+"s│\n",
                    sp.id_servicio_producto, sp.id_servicio, sp.id_producto, sp.cantidad, sp.subtotal));
        }

        sb.append("└").append("─".repeat(idW))
          .append("┴").append("─".repeat(srvW))
          .append("┴").append("─".repeat(prodW))
          .append("┴").append("─".repeat(cantW))
          .append("┴").append("─".repeat(subW))
          .append("┘\n");
        return sb.toString();
    }

    public static String obtenerUnoTable(ServicioProducto sp){ return obtenerTodosTable(java.util.List.of(sp)); }
}
