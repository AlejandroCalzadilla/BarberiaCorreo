package org.barberia.usuarios.mapper;

import org.barberia.usuarios.domain.Servicio;

import java.util.List;

public class ServicioMapper {
    public static String obtenerTodosTable(List<Servicio> items) {
        StringBuilder sb = new StringBuilder();
        int idW=10,nomW=20,descW=30,durW=6,precioW=12,estadoW=10,imagenW=20,createdW=19,updatedW=19;

        sb.append("┌").append("─".repeat(idW))
          
          .append("┬").append("─".repeat(nomW))
          .append("┬").append("─".repeat(descW))
          .append("┬").append("─".repeat(durW))
          .append("┬").append("─".repeat(precioW))
          .append("┬").append("─".repeat(estadoW))
          .append("┬").append("─".repeat(imagenW))
          .append("┬").append("─".repeat(createdW))
          .append("┬").append("─".repeat(updatedW))
          .append("┐\n");

        sb.append(String.format("│%-"+idW+"s│%-"+nomW+"s│%-"+descW+"s│%-"+durW+"s│%-"+precioW+"s│%-"+estadoW+"s│%-"+imagenW+"s│%-"+createdW+"s│%-"+updatedW+"s│\n",
                "ID","Nombre","Descripcion","Min","Precio","Estado","Imagen","Creado","Actualizado"));

        sb.append("├").append("─".repeat(idW))
         
          .append("┼").append("─".repeat(nomW))
          .append("┼").append("─".repeat(descW))
          .append("┼").append("─".repeat(durW))
          .append("┼").append("─".repeat(precioW))
          .append("┼").append("─".repeat(estadoW))
          .append("┼").append("─".repeat(imagenW))
          .append("┼").append("─".repeat(createdW))
          .append("┼").append("─".repeat(updatedW))
          .append("┤\n");

        for (int idx = 0; idx < items.size(); idx++) {
            Servicio s = items.get(idx);
            List<String> nomLines = wrap(s.nombre, nomW);
            List<String> descLines = wrap(s.descripcion, descW);
            List<String> estLines = wrap(s.estado!=null?s.estado.name():"", estadoW);
            List<String> imgLines = wrap(s.imagen, imagenW);
            List<String> crLines = wrap(s.created_at!=null?s.created_at.toString():"", createdW);
            List<String> upLines = wrap(s.updated_at!=null?s.updated_at.toString():"", updatedW);
            
            int maxLines = Math.max(nomLines.size(), Math.max(descLines.size(), 
                          Math.max(estLines.size(), Math.max(imgLines.size(), 
                          Math.max(crLines.size(), upLines.size())))));
            
            for (int i = 0; i < maxLines; i++) {
                String id = i == 0 ? String.valueOf(s.id_servicio) : "";
                String dur = i == 0 ? String.valueOf(s.duracion_minutos_aprox) : "";
                String precio = i == 0 ? String.valueOf(s.precio) : "";
                
                sb.append(String.format("│%-"+idW+"s│%-"+nomW+"s│%-"+descW+"s│%-"+durW+"s│%-"+precioW+"s│%-"+estadoW+"s│%-"+imagenW+"s│%-"+createdW+"s│%-"+updatedW+"s│\n",
                        id,
                        getLine(nomLines, i, nomW),
                        getLine(descLines, i, descW),
                        dur, precio,
                        getLine(estLines, i, estadoW),
                        getLine(imgLines, i, imagenW),
                        getLine(crLines, i, createdW),
                        getLine(upLines, i, updatedW)));
            }
            
            // Línea divisoria entre registros (solo si no es el último)
            if (idx < items.size() - 1) {
                sb.append("├").append("─".repeat(idW))
                        .append("┼").append("─".repeat(nomW))
                        .append("┼").append("─".repeat(descW))
                        .append("┼").append("─".repeat(durW))
                        .append("┼").append("─".repeat(precioW))
                        .append("┼").append("─".repeat(estadoW))
                        .append("┼").append("─".repeat(imagenW))
                        .append("┼").append("─".repeat(createdW))
                        .append("┼").append("─".repeat(updatedW))
                        .append("┤\n");
            }
        }

        sb.append("└").append("─".repeat(idW))
         
          .append("┴").append("─".repeat(nomW))
          .append("┴").append("─".repeat(descW))
          .append("┴").append("─".repeat(durW))
          .append("┴").append("─".repeat(precioW))
          .append("┴").append("─".repeat(estadoW))
          .append("┴").append("─".repeat(imagenW))
          .append("┴").append("─".repeat(createdW))
          .append("┴").append("─".repeat(updatedW))
          .append("┘\n");
        return sb.toString();
    }

    public static String obtenerUnoTable(Servicio s){ return obtenerTodosTable(java.util.List.of(s)); }

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
