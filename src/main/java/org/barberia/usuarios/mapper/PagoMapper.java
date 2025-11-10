package org.barberia.usuarios.mapper;

import org.barberia.usuarios.domain.Pago;

import java.util.List;

public class PagoMapper {
    public static String obtenerTodosTable(List<Pago> items) {
        StringBuilder sb = new StringBuilder();
        int idW=10, resW=10, montoW=12, metW=12, tipW=12, estW=12, fpW=19, createdW=19, updatedW=19, notasW=25;

        sb.append("┌").append("─".repeat(idW))
          .append("┬").append("─".repeat(resW))
          .append("┬").append("─".repeat(montoW))
          .append("┬").append("─".repeat(metW))
          .append("┬").append("─".repeat(tipW))
          .append("┬").append("─".repeat(estW))
          .append("┬").append("─".repeat(fpW))
          .append("┬").append("─".repeat(notasW))
          .append("┬").append("─".repeat(createdW))
          .append("┬").append("─".repeat(updatedW))
          .append("┐\n");

        sb.append(String.format("│%-"+idW+"s│%-"+resW+"s│%-"+montoW+"s│%-"+metW+"s│%-"+tipW+"s│%-"+estW+"s│%-"+fpW+"s│%-"+notasW+"s│%-"+createdW+"s│%-"+updatedW+"s│\n",
                "ID","ID_RES","Monto","Metodo","Tipo","Estado","FechaPago","Notas","Creado","Actualizado"));

        sb.append("├").append("─".repeat(idW))
          .append("┼").append("─".repeat(resW))
          .append("┼").append("─".repeat(montoW))
          .append("┼").append("─".repeat(metW))
          .append("┼").append("─".repeat(tipW))
          .append("┼").append("─".repeat(estW))
          .append("┼").append("─".repeat(fpW))
          .append("┼").append("─".repeat(notasW))
          .append("┼").append("─".repeat(createdW))
          .append("┼").append("─".repeat(updatedW))
          .append("┤\n");

        for (int idx = 0; idx < items.size(); idx++) {
            Pago p = items.get(idx);
            List<String> metLines = wrap(p.metodo_pago!=null?p.metodo_pago.name():"", metW);
            List<String> tipLines = wrap(p.tipo_pago!=null?p.tipo_pago.name():"", tipW);
            List<String> estLines = wrap(p.estado!=null?p.estado.name():"", estW);
            List<String> fpLines = wrap(p.fecha_pago!=null?p.fecha_pago.toString():"", fpW);
            List<String> notasLines = wrap(p.notas, notasW);
            List<String> crLines = wrap(p.created_at!=null?p.created_at.toString():"", createdW);
            List<String> upLines = wrap(p.updated_at!=null?p.updated_at.toString():"", updatedW);
            
            int maxLines = Math.max(metLines.size(), Math.max(tipLines.size(), 
                          Math.max(estLines.size(), Math.max(fpLines.size(), 
                          Math.max(notasLines.size(), Math.max(crLines.size(), upLines.size()))))));
            
            for (int i = 0; i < maxLines; i++) {
                String id = i == 0 ? String.valueOf(p.id_pago) : "";
                String res = i == 0 ? String.valueOf(p.id_reserva) : "";
                String monto = i == 0 ? String.valueOf(p.monto_total) : "";
                
                sb.append(String.format("│%-"+idW+"s│%-"+resW+"s│%-"+montoW+"s│%-"+metW+"s│%-"+tipW+"s│%-"+estW+"s│%-"+fpW+"s│%-"+notasW+"s│%-"+createdW+"s│%-"+updatedW+"s│\n",
                        id, res, monto,
                        getLine(metLines, i, metW),
                        getLine(tipLines, i, tipW),
                        getLine(estLines, i, estW),
                        getLine(fpLines, i, fpW),
                        getLine(notasLines, i, notasW),
                        getLine(crLines, i, createdW),
                        getLine(upLines, i, updatedW)));
            }
            
            // Línea divisoria entre registros (solo si no es el último)
            if (idx < items.size() - 1) {
                sb.append("├").append("─".repeat(idW))
                        .append("┼").append("─".repeat(resW))
                        .append("┼").append("─".repeat(montoW))
                        .append("┼").append("─".repeat(metW))
                        .append("┼").append("─".repeat(tipW))
                        .append("┼").append("─".repeat(estW))
                        .append("┼").append("─".repeat(fpW))
                        .append("┼").append("─".repeat(notasW))
                        .append("┼").append("─".repeat(createdW))
                        .append("┼").append("─".repeat(updatedW))
                        .append("┤\n");
            }
        }

        sb.append("└").append("─".repeat(idW))
          .append("┴").append("─".repeat(resW))
          .append("┴").append("─".repeat(montoW))
          .append("┴").append("─".repeat(metW))
          .append("┴").append("─".repeat(tipW))
          .append("┴").append("─".repeat(estW))
          .append("┴").append("─".repeat(fpW))
          .append("┴").append("─".repeat(notasW))
          .append("┴").append("─".repeat(createdW))
          .append("┴").append("─".repeat(updatedW))
          .append("┘\n");
        return sb.toString();
    }

    public static String obtenerUnoTable(Pago p){ return obtenerTodosTable(java.util.List.of(p)); }

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
