package org.barberia.usuarios.mapper;

import org.barberia.usuarios.domain.Cliente;

import java.util.List;

public class ClienteMapper {
    public static String obtenerTodosTable(List<Cliente> items) {
        StringBuilder sb = new StringBuilder();
        int idW=10, usrW=10, fnW=12, ciW=12, createdW=19, updatedW=19;

        sb.append("┌").append("─".repeat(idW))
          .append("┬").append("─".repeat(usrW))
          .append("┬").append("─".repeat(fnW))
          .append("┬").append("─".repeat(ciW))
          .append("┬").append("─".repeat(createdW))
          .append("┬").append("─".repeat(updatedW))
          .append("┐\n");

        sb.append(String.format("│%-"+idW+"s│%-"+usrW+"s│%-"+fnW+"s│%-"+ciW+"s│%-"+createdW+"s│%-"+updatedW+"s│\n",
                "ID","ID_USR","Nacimiento","CI","Creado","Actualizado"));

        sb.append("├").append("─".repeat(idW))
          .append("┼").append("─".repeat(usrW))
          .append("┼").append("─".repeat(fnW))
          .append("┼").append("─".repeat(ciW))
          .append("┼").append("─".repeat(createdW))
          .append("┼").append("─".repeat(updatedW))
          .append("┤\n");

        for (Cliente c : items) {
            String fn=c.fecha_nacimiento!=null?c.fecha_nacimiento.toString():"";
            String cr=c.created_at!=null?c.created_at.toString():"";
            String up=c.updated_at!=null?c.updated_at.toString():"";
            sb.append(String.format("│%-"+idW+"s│%-"+usrW+"s│%-"+fnW+"s│%-"+ciW+"s│%-"+createdW+"s│%-"+updatedW+"s│\n",
                    c.id_cliente, c.id_usuario, fn, c.ci, cr, up));
        }

        sb.append("└").append("─".repeat(idW))
          .append("┴").append("─".repeat(usrW))
          .append("┴").append("─".repeat(fnW))
          .append("┴").append("─".repeat(ciW))
          .append("┴").append("─".repeat(createdW))
          .append("┴").append("─".repeat(updatedW))
          .append("┘\n");
        return sb.toString();
    }

    public static String obtenerUnoTable(Cliente c){ return obtenerTodosTable(java.util.List.of(c)); }
}
