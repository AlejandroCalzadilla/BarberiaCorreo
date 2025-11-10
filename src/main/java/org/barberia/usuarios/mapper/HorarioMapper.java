package org.barberia.usuarios.mapper;

import org.barberia.usuarios.domain.Horario;

import java.util.List;

public class HorarioMapper {
    public static String obtenerTodosTable(List<Horario> items) {
        StringBuilder sb = new StringBuilder();
        int idW=10, barW=10, diaW=10, hiW=8, hfW=8, estW=10, createdW=19, updatedW=19;

        sb.append("┌").append("─".repeat(idW))
          .append("┬").append("─".repeat(barW))
          .append("┬").append("─".repeat(diaW))
          .append("┬").append("─".repeat(hiW))
          .append("┬").append("─".repeat(hfW))
          .append("┬").append("─".repeat(estW))
          .append("┬").append("─".repeat(createdW))
          .append("┬").append("─".repeat(updatedW))
          .append("┐\n");

        sb.append(String.format("│%-"+idW+"s│%-"+barW+"s│%-"+diaW+"s│%-"+hiW+"s│%-"+hfW+"s│%-"+estW+"s│%-"+createdW+"s│%-"+updatedW+"s│\n",
                "ID","ID_BAR","Dia","Inicio","Fin","Estado","Creado","Actualizado"));

        sb.append("├").append("─".repeat(idW))
          .append("┼").append("─".repeat(barW))
          .append("┼").append("─".repeat(diaW))
          .append("┼").append("─".repeat(hiW))
          .append("┼").append("─".repeat(hfW))
          .append("┼").append("─".repeat(estW))
          .append("┼").append("─".repeat(createdW))
          .append("┼").append("─".repeat(updatedW))
          .append("┤\n");

        for (int idx = 0; idx < items.size(); idx++) {
            Horario h = items.get(idx);
            List<String> diaLines = wrap(h.dia_semana!=null?h.dia_semana.name():"", diaW);
            List<String> hiLines = wrap(h.hora_inicio!=null?h.hora_inicio.toString():"", hiW);
            List<String> hfLines = wrap(h.hora_fin!=null?h.hora_fin.toString():"", hfW);
            List<String> estLines = wrap(h.estado!=null?h.estado.name():"", estW);
            List<String> crLines = wrap(h.created_at!=null?h.created_at.toString():"", createdW);
            List<String> upLines = wrap(h.updated_at!=null?h.updated_at.toString():"", updatedW);
            
            int maxLines = Math.max(diaLines.size(), Math.max(hiLines.size(), 
                          Math.max(hfLines.size(), Math.max(estLines.size(), 
                          Math.max(crLines.size(), upLines.size())))));
            
            for (int i = 0; i < maxLines; i++) {
                String id = i == 0 ? String.valueOf(h.id_horario) : "";
                String bar = i == 0 ? String.valueOf(h.id_barbero) : "";
                
                sb.append(String.format("│%-"+idW+"s│%-"+barW+"s│%-"+diaW+"s│%-"+hiW+"s│%-"+hfW+"s│%-"+estW+"s│%-"+createdW+"s│%-"+updatedW+"s│\n",
                        id, bar,
                        getLine(diaLines, i, diaW),
                        getLine(hiLines, i, hiW),
                        getLine(hfLines, i, hfW),
                        getLine(estLines, i, estW),
                        getLine(crLines, i, createdW),
                        getLine(upLines, i, updatedW)));
            }
            
            // Línea divisoria entre registros (solo si no es el último)
            if (idx < items.size() - 1) {
                sb.append("├").append("─".repeat(idW))
                        .append("┼").append("─".repeat(barW))
                        .append("┼").append("─".repeat(diaW))
                        .append("┼").append("─".repeat(hiW))
                        .append("┼").append("─".repeat(hfW))
                        .append("┼").append("─".repeat(estW))
                        .append("┼").append("─".repeat(createdW))
                        .append("┼").append("─".repeat(updatedW))
                        .append("┤\n");
            }
        }

        sb.append("└").append("─".repeat(idW))
          .append("┴").append("─".repeat(barW))
          .append("┴").append("─".repeat(diaW))
          .append("┴").append("─".repeat(hiW))
          .append("┴").append("─".repeat(hfW))
          .append("┴").append("─".repeat(estW))
          .append("┴").append("─".repeat(createdW))
          .append("┴").append("─".repeat(updatedW))
          .append("┘\n");
        return sb.toString();
    }

    public static String obtenerUnoTable(Horario h){ return obtenerTodosTable(java.util.List.of(h)); }

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
