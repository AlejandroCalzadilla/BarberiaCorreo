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
                "ID_CATEG.", "Nombre", "Descripcion", "Estado", "Creado", "Actualizado"));

        sb.append("├").append("─".repeat(idW))
          .append("┼").append("─".repeat(nombreW))
          .append("┼").append("─".repeat(descW))
          .append("┼").append("─".repeat(estadoW))
          .append("┼").append("─".repeat(createdW))
          .append("┼").append("─".repeat(updatedW))
          .append("┤\n");

        for (int idx = 0; idx < categorias.size(); idx++) {
            Categoria c = categorias.get(idx);
            List<String> nombreLines = wrap(c.nombre, nombreW);
            List<String> descLines = wrap(c.descripcion, descW);
            List<String> estadoLines = wrap(c.estado != null ? c.estado.name() : "", estadoW);
            List<String> createdLines = wrap(c.created_at != null ? c.created_at.toString() : "", createdW);
            List<String> updatedLines = wrap(c.updated_at != null ? c.updated_at.toString() : "", updatedW);
            
            int maxLines = Math.max(nombreLines.size(), Math.max(descLines.size(), 
                          Math.max(estadoLines.size(), Math.max(createdLines.size(), updatedLines.size()))));
            
            for (int i = 0; i < maxLines; i++) {
                String id = i == 0 ? String.valueOf(c.id_categoria) : "";
                
                sb.append(String.format("│%-"+idW+"s│%-"+nombreW+"s│%-"+descW+"s│%-"+estadoW+"s│%-"+createdW+"s│%-"+updatedW+"s│\n",
                        id,
                        getLine(nombreLines, i, nombreW),
                        getLine(descLines, i, descW),
                        getLine(estadoLines, i, estadoW),
                        getLine(createdLines, i, createdW),
                        getLine(updatedLines, i, updatedW)));
            }
            
            // Línea divisoria entre registros (solo si no es el último)
            if (idx < categorias.size() - 1) {
                sb.append("├").append("─".repeat(idW))
                        .append("┼").append("─".repeat(nombreW))
                        .append("┼").append("─".repeat(descW))
                        .append("┼").append("─".repeat(estadoW))
                        .append("┼").append("─".repeat(createdW))
                        .append("┼").append("─".repeat(updatedW))
                        .append("┤\n");
            }
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

    private static List<String> wrap(String text, int width) {
        List<String> lines = new java.util.ArrayList<>();
        if (text == null || text.isEmpty()) {
            lines.add("");
            return lines;
        }
        
        int start = 0;
        while (start < text.length()) {
            int end = Math.min(start + width, text.length());
            lines.add(text.substring(start, end));
            start = end;
        }
        return lines;
    }
    
    private static String getLine(List<String> lines, int index, int width) {
        if (index < lines.size()) {
            String line = lines.get(index);
            return String.format("%-" + width + "s", line);
        }
        return " ".repeat(width);
    }
}
