package org.barberia.usuarios.mapper;

import org.barberia.usuarios.domain.Categoria;

import java.util.List;

public class CategoriaMapper {
    public static String obtenerTodosTable(List<Categoria> categorias) {
        StringBuilder sb = new StringBuilder();
        int idW = 10, nombreW = 20, estadoW = 10, createdW = 19, updatedW = 19;
        int descW = 30;

        sb.append("┌").append("─".repeat(idW))
          .append("┬").append("─".repeat(nombreW))
          .append("┬").append("─".repeat(descW))
          .append("┬").append("─".repeat(estadoW))
          .append("┬").append("─".repeat(createdW))
          .append("┬").append("─".repeat(updatedW))
          .append("┐\n");

        sb.append(String.format("│%-"+idW+"s│%-"+nombreW+"s│%-"+descW+"s│%-"+estadoW+"s│%-"+createdW+"s│%-"+updatedW+"s│\n",
                "ID_CATEGORIA", "Nombre", "Descripcion", "Estado", "Creado", "Actualizado"));

        sb.append("├").append("─".repeat(idW))
          .append("┼").append("─".repeat(nombreW))
          .append("┼").append("─".repeat(descW))
          .append("┼").append("─".repeat(estadoW))
          .append("┼").append("─".repeat(createdW))
          .append("┼").append("─".repeat(updatedW))
          .append("┤\n");

        for (Categoria c : categorias) {
            String nombre = t(c.nombre, nombreW);
            String desc = t(c.descripcion, descW);
            String estado = t(c.estado != null ? c.estado.name() : "", estadoW);
            String created = t(c.created_at != null ? c.created_at.toString() : "", createdW);
            String updated = t(c.updated_at != null ? c.updated_at.toString() : "", updatedW);
            sb.append(String.format("│%-"+idW+"s│%-"+nombreW+"s│%-"+descW+"s│%-"+estadoW+"s│%-"+createdW+"s│%-"+updatedW+"s│\n",
                    c.id_categoria, nombre, desc, estado, created, updated));
        }

        sb.append("└").append("─".repeat(idW))
          .append("┴").append("─".repeat(nombreW))
          .append("┴").append("─".repeat(descW))
          .append("┴").append("─".repeat(estadoW))
          .append("┴").append("─".repeat(createdW))
          .append("┴").append("─".repeat(updatedW))
          .append("┘\n");
        return sb.toString();
    }

    public static String obtenerUnoTable(Categoria c) {
        return obtenerTodosTable(java.util.List.of(c));
    }

    private static String t(String s, int w) {
        if (s == null) return "";
        if (s.length() <= w) return s;
        if (w <= 3) return s.substring(0, Math.max(0, w));
        return s.substring(0, w-3) + "...";
    }
}
