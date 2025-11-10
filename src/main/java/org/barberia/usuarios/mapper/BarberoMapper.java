package org.barberia.usuarios.mapper;

import org.barberia.usuarios.domain.Barbero;

import java.util.List;

public class BarberoMapper {
    public static String obtenerTodosTable(List<Barbero> items) {
        StringBuilder sb = new StringBuilder();
        int idW=10, usrW=10, espW=20, fotoW=25, estW=12, createdW=19, updatedW=19;

        sb.append("┌").append("─".repeat(idW))
          .append("┬").append("─".repeat(usrW))
          .append("┬").append("─".repeat(espW))
          .append("┬").append("─".repeat(fotoW))
          .append("┬").append("─".repeat(estW))
          .append("┬").append("─".repeat(createdW))
          .append("┬").append("─".repeat(updatedW))
          .append("┐\n");

        sb.append(String.format("│%-"+idW+"s│%-"+usrW+"s│%-"+espW+"s│%-"+fotoW+"s│%-"+estW+"s│%-"+createdW+"s│%-"+updatedW+"s│\n",
                "ID","ID_USR","Especialidad","Foto","Estado","Creado","Actualizado"));

        sb.append("├").append("─".repeat(idW))
          .append("┼").append("─".repeat(usrW))
          .append("┼").append("─".repeat(espW))
          .append("┼").append("─".repeat(fotoW))
          .append("┼").append("─".repeat(estW))
          .append("┼").append("─".repeat(createdW))
          .append("┼").append("─".repeat(updatedW))
          .append("┤\n");

        for (int idx = 0; idx < items.size(); idx++) {
            Barbero b = items.get(idx);
            List<String> espLines = wrap(b.especialidad, espW);
            List<String> fotoLines = wrap(b.foto_perfil, fotoW);
            List<String> estLines = wrap(b.estado!=null?b.estado.name():"", estW);
            List<String> crLines = wrap(b.created_at!=null?b.created_at.toString():"", createdW);
            List<String> upLines = wrap(b.updated_at!=null?b.updated_at.toString():"", updatedW);
            
            int maxLines = Math.max(espLines.size(), Math.max(fotoLines.size(), 
                          Math.max(estLines.size(), Math.max(crLines.size(), upLines.size()))));
            
            for (int i = 0; i < maxLines; i++) {
                String id = i == 0 ? String.valueOf(b.id_barbero) : "";
                String usr = i == 0 ? String.valueOf(b.id_usuario) : "";
                
                sb.append(String.format("│%-"+idW+"s│%-"+usrW+"s│%-"+espW+"s│%-"+fotoW+"s│%-"+estW+"s│%-"+createdW+"s│%-"+updatedW+"s│\n",
                        id, usr,
                        getLine(espLines, i, espW),
                        getLine(fotoLines, i, fotoW),
                        getLine(estLines, i, estW),
                        getLine(crLines, i, createdW),
                        getLine(upLines, i, updatedW)));
            }
            
            // Línea divisoria entre registros (solo si no es el último)
            if (idx < items.size() - 1) {
                sb.append("├").append("─".repeat(idW))
                        .append("┼").append("─".repeat(usrW))
                        .append("┼").append("─".repeat(espW))
                        .append("┼").append("─".repeat(fotoW))
                        .append("┼").append("─".repeat(estW))
                        .append("┼").append("─".repeat(createdW))
                        .append("┼").append("─".repeat(updatedW))
                        .append("┤\n");
            }
        }

        sb.append("└").append("─".repeat(idW))
          .append("┴").append("─".repeat(usrW))
          .append("┴").append("─".repeat(espW))
          .append("┴").append("─".repeat(fotoW))
          .append("┴").append("─".repeat(estW))
          .append("┴").append("─".repeat(createdW))
          .append("┴").append("─".repeat(updatedW))
          .append("┘\n");
        return sb.toString();
    }

    public static String obtenerUnoTable(Barbero b){ return obtenerTodosTable(java.util.List.of(b)); }

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
