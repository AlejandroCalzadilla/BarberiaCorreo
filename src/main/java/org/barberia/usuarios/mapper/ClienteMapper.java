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

        for (int idx = 0; idx < items.size(); idx++) {
            Cliente c = items.get(idx);
            List<String> fnLines = wrap(c.fecha_nacimiento!=null?c.fecha_nacimiento.toString():"", fnW);
            List<String> ciLines = wrap(c.ci, ciW);
            List<String> crLines = wrap(c.created_at!=null?c.created_at.toString():"", createdW);
            List<String> upLines = wrap(c.updated_at!=null?c.updated_at.toString():"", updatedW);
            
            int maxLines = Math.max(fnLines.size(), Math.max(ciLines.size(), 
                          Math.max(crLines.size(), upLines.size())));
            
            for (int i = 0; i < maxLines; i++) {
                String id = i == 0 ? String.valueOf(c.id_cliente) : "";
                String usr = i == 0 ? String.valueOf(c.id_usuario) : "";
                
                sb.append(String.format("│%-"+idW+"s│%-"+usrW+"s│%-"+fnW+"s│%-"+ciW+"s│%-"+createdW+"s│%-"+updatedW+"s│\n",
                        id, usr,
                        getLine(fnLines, i, fnW),
                        getLine(ciLines, i, ciW),
                        getLine(crLines, i, createdW),
                        getLine(upLines, i, updatedW)));
            }
            
            // Línea divisoria entre registros (solo si no es el último)
            if (idx < items.size() - 1) {
                sb.append("├").append("─".repeat(idW))
                        .append("┼").append("─".repeat(usrW))
                        .append("┼").append("─".repeat(fnW))
                        .append("┼").append("─".repeat(ciW))
                        .append("┼").append("─".repeat(createdW))
                        .append("┼").append("─".repeat(updatedW))
                        .append("┤\n");
            }
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
