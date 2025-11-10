package org.barberia.usuarios.mapper;

import org.barberia.usuarios.domain.ServicioProducto;

import java.util.List;

public class ServicioProductoMapper {
    public static String obtenerTodosTable(List<ServicioProducto> items) {
        StringBuilder sb = new StringBuilder();
        int idW=10, srvW=10, prodW=10, cantW=8, subW=12;

          sb.append("┬").append("─".repeat(srvW))
          .append("┬").append("─".repeat(prodW))
          .append("┬").append("─".repeat(cantW))
          .append("┐\n");

        sb.append(String.format("│%-"+srvW+"s│%-"+prodW+"s│%-"+cantW+"s│\n",
                "ID_SRV","ID_PROD","Cant"));

          sb.append("┼").append("─".repeat(srvW))
          .append("┼").append("─".repeat(prodW))
          .append("┼").append("─".repeat(cantW))
          .append("┤\n");

        for (ServicioProducto sp : items) {
            sb.append(String.format("│|%-"+srvW+"s│%-"+prodW+"s│%-"+cantW+"s│\n",
                    sp.id_servicio, sp.id_producto, sp.cantidad));
        }

        sb.append("┴").append("─".repeat(srvW))
          .append("┴").append("─".repeat(prodW))
          .append("┴").append("─".repeat(cantW))
          .append("┘\n");
        return sb.toString();
    }

    public static String obtenerUnoTable(ServicioProducto sp){ return obtenerTodosTable(java.util.List.of(sp)); }
}
