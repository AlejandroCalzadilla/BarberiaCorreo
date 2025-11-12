package org.barberia.usuarios.mapper;

import org.barberia.usuarios.domain.Reserva;

import java.util.List;

public class ReservaMapper {
    public static String obtenerTodosTable(List<Reserva> items) {
        StringBuilder sb = new StringBuilder();
        int idW=4, cliW=7, barW=7, srvW=7, fechaW=10, hiW=8, hfW=8, estW=12, totalW=8, precioW=9, anticW=8, createdW=19, updatedW=19;
        int notasW=20;

        sb.append("┌").append("─".repeat(idW))
          .append("┬").append("─".repeat(cliW))
          .append("┬").append("─".repeat(barW))
          .append("┬").append("─".repeat(srvW))
          .append("┬").append("─".repeat(fechaW))
          .append("┬").append("─".repeat(hiW))
          .append("┬").append("─".repeat(hfW))
          .append("┬").append("─".repeat(estW))
          .append("┬").append("─".repeat(totalW))
          .append("┬").append("─".repeat(notasW))
          .append("┬").append("─".repeat(precioW))
          .append("┬").append("─".repeat(anticW))
          .append("┬").append("─".repeat(createdW))
          .append("┬").append("─".repeat(updatedW))
          .append("┐\n");

        sb.append(String.format("│%-"+idW+"s│%-"+cliW+"s│%-"+barW+"s│%-"+srvW+"s│%-"+fechaW+"s│%-"+hiW+"s│%-"+hfW+"s│%-"+estW+"s│%-"+totalW+"s│%-"+notasW+"s│%-"+precioW+"s│%-"+anticW+"s│%-"+createdW+"s│%-"+updatedW+"s│\n",
                "ID","ID_CLI","ID_BAR","ID_SRV","Fecha","Inicio","Fin","Estado","Total","Notas","PrecioSrv","Anticipo","Creado","Actualizado"));

        sb.append("├").append("─".repeat(idW))
          .append("┼").append("─".repeat(cliW))
          .append("┼").append("─".repeat(barW))
          .append("┼").append("─".repeat(srvW))
          .append("┼").append("─".repeat(fechaW))
          .append("┼").append("─".repeat(hiW))
          .append("┼").append("─".repeat(hfW))
          .append("┼").append("─".repeat(estW))
          .append("┼").append("─".repeat(totalW))
          .append("┼").append("─".repeat(notasW))
          .append("┼").append("─".repeat(precioW))
          .append("┼").append("─".repeat(anticW))
          .append("┼").append("─".repeat(createdW))
          .append("┼").append("─".repeat(updatedW))
          .append("┤\n");

        for (int idx = 0; idx < items.size(); idx++) {
            Reserva r = items.get(idx);
            List<String> fechaLines = wrap(r.fecha_reserva!=null?r.fecha_reserva.toString():"", fechaW);
            List<String> hiLines = wrap(r.hora_inicio!=null?r.hora_inicio.toString():"", hiW);
            List<String> hfLines = wrap(r.hora_fin!=null?r.hora_fin.toString():"", hfW);
            List<String> estLines = wrap(r.estado!=null?r.estado.name():"", estW);
            List<String> notasLines = wrap(r.notas, notasW);
            List<String> crLines = wrap(r.created_at!=null?r.created_at.toString():"", createdW);
            List<String> upLines = wrap(r.updated_at!=null?r.updated_at.toString():"", updatedW);
            
            int maxLines = Math.max(fechaLines.size(), Math.max(hiLines.size(), 
                          Math.max(hfLines.size(), Math.max(estLines.size(), 
                          Math.max(notasLines.size(), Math.max(crLines.size(), upLines.size()))))));
            
            for (int i = 0; i < maxLines; i++) {
                String id = i == 0 ? String.valueOf(r.id_reserva) : "";
                String cli = i == 0 ? String.valueOf(r.id_cliente) : "";
                String bar = i == 0 ? String.valueOf(r.id_barbero) : "";
                String srv = i == 0 ? String.valueOf(r.id_servicio) : "";
                String total = i == 0 ? String.valueOf(r.total) : "";
                String precio = i == 0 ? String.valueOf(r.precio_servicio) : "";
                String antic = i == 0 ? String.valueOf(r.monto_anticipo) : "";
               
                
                sb.append(String.format("│%-"+idW+"s│%-"+cliW+"s│%-"+barW+"s│%-"+srvW+"s│%-"+fechaW+"s│%-"+hiW+"s│%-"+hfW+"s│%-"+estW+"s│%-"+totalW+"s│%-"+notasW+"s│%-"+precioW+"s│%-"+anticW+"s│%-"+createdW+"s│%-"+updatedW+"s│\n",
                        id, cli, bar, srv,
                        getLine(fechaLines, i, fechaW),
                        getLine(hiLines, i, hiW),
                        getLine(hfLines, i, hfW),
                        getLine(estLines, i, estW),
                        total,
                        getLine(notasLines, i, notasW),
                        precio, antic,
                        getLine(crLines, i, createdW),
                        getLine(upLines, i, updatedW)));
            }
            
            // Línea divisoria entre registros (solo si no es el último)
            if (idx < items.size() - 1) {
                sb.append("├").append("─".repeat(idW))
                        .append("┼").append("─".repeat(cliW))
                        .append("┼").append("─".repeat(barW))
                        .append("┼").append("─".repeat(srvW))
                        .append("┼").append("─".repeat(fechaW))
                        .append("┼").append("─".repeat(hiW))
                        .append("┼").append("─".repeat(hfW))
                        .append("┼").append("─".repeat(estW))
                        .append("┼").append("─".repeat(totalW))
                        .append("┼").append("─".repeat(notasW))
                        .append("┼").append("─".repeat(precioW))
                        .append("┼").append("─".repeat(anticW))
                       
                        .append("┼").append("─".repeat(createdW))
                        .append("┼").append("─".repeat(updatedW))
                        .append("┤\n");
            }
        }

        sb.append("└").append("─".repeat(idW))
          .append("┴").append("─".repeat(cliW))
          .append("┴").append("─".repeat(barW))
          .append("┴").append("─".repeat(srvW))
          .append("┴").append("─".repeat(fechaW))
          .append("┴").append("─".repeat(hiW))
          .append("┴").append("─".repeat(hfW))
          .append("┴").append("─".repeat(estW))
          .append("┴").append("─".repeat(totalW))
          .append("┴").append("─".repeat(notasW))
          .append("┴").append("─".repeat(precioW))
          .append("┴").append("─".repeat(anticW))
          
          .append("┴").append("─".repeat(createdW))
          .append("┴").append("─".repeat(updatedW))
          .append("" + "┘\n");
        return sb.toString();
    }

    public static String obtenerUnoTable(Reserva r){ return obtenerTodosTable(java.util.List.of(r)); }

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
