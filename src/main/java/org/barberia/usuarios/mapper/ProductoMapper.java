package org.barberia.usuarios.mapper;

import org.barberia.usuarios.domain.Producto;

import java.util.List;

public class ProductoMapper {
    public static String obtenerTodosTable(List<Producto> items) {
        StringBuilder sb = new StringBuilder();
        int idW=10,codW=12,nomW=15,precioW=12,stockW=8,estadoW=10,createdW=15,updatedW=14;
        int descW=25,uniW=10,imgW=15,catW=10;

        sb.append("┌").append("─".repeat(idW))
          .append("┬").append("─".repeat(catW))
          .append("┬").append("─".repeat(codW))
          .append("┬").append("─".repeat(nomW))
          .append("┬").append("─".repeat(descW))
          .append("┬").append("─".repeat(precioW))
          .append("┬").append("─".repeat(stockW))
          .append("┬").append("─".repeat(stockW))
          .append("┬").append("─".repeat(uniW))
          .append("┬").append("─".repeat(estadoW))
          .append("┬").append("─".repeat(imgW))
          .append("┬").append("─".repeat(createdW))
          .append("┬").append("─".repeat(updatedW))
          .append("┐\n");

        sb.append(String.format("│%-"+idW+"s│%-"+catW+"s│%-"+codW+"s│%-"+nomW+"s│%-"+descW+"s│%-"+precioW+"s│%-"+stockW+"s│%-"+stockW+"s│%-"+uniW+"s│%-"+estadoW+"s│%-"+imgW+"s│%-"+createdW+"s│%-"+updatedW+"s│\n",
                "ID", "ID_CAT", "Codigo", "Nombre", "Descripcion", "Precio", "Stock", "Min", "Unidad", "Estado", "Imagen", "Creado", "Actualizado"));

        sb.append("├").append("─".repeat(idW))
          .append("┼").append("─".repeat(catW))
          .append("┼").append("─".repeat(codW))
          .append("┼").append("─".repeat(nomW))
          .append("┼").append("─".repeat(descW))
          .append("┼").append("─".repeat(precioW))
          .append("┼").append("─".repeat(stockW))
          .append("┼").append("─".repeat(stockW))
          .append("┼").append("─".repeat(uniW))
          .append("┼").append("─".repeat(estadoW))
          .append("┼").append("─".repeat(imgW))
          .append("┼").append("─".repeat(createdW))
          .append("┼").append("─".repeat(updatedW))
          .append("┤\n");

        for (Producto p : items) {
            List<String> nombreLines = wrap(p.nombre, nomW);
            List<String> descLines = wrap(p.descripcion, descW);
            List<String> codLines = wrap(p.codigo, codW);
            List<String> uniLines = wrap(p.unidad_medida, uniW);
            List<String> imgLines = wrap(p.imagenurl, imgW);
            List<String> estLines = wrap(p.estado!=null?p.estado.name():"", estadoW);
            List<String> crLines = wrap(p.created_at!=null?p.created_at.toString():"", createdW);
            List<String> upLines = wrap(p.updated_at!=null?p.updated_at.toString():"", updatedW);
            
            int maxLines = Math.max(nombreLines.size(), Math.max(descLines.size(), 
                          Math.max(codLines.size(), Math.max(uniLines.size(), 
                          Math.max(imgLines.size(), Math.max(estLines.size(), 
                          Math.max(crLines.size(), upLines.size())))))));
            
            for (int i = 0; i < maxLines; i++) {
                String id = i == 0 ? String.valueOf(p.id_producto) : "";
                String cat = i == 0 ? String.valueOf(p.id_categoria) : "";
                String precio = i == 0 ? String.valueOf(p.precio_compra) : "";
                String stock = i == 0 ? String.valueOf(p.stock_actual) : "";
                String stockMin = i == 0 ? String.valueOf(p.stock_minimo) : "";
                
                sb.append(String.format("│%-"+idW+"s│%-"+catW+"s│%-"+codW+"s│%-"+nomW+"s│%-"+descW+"s│%-"+precioW+"s│%-"+stockW+"s│%-"+stockW+"s│%-"+uniW+"s│%-"+estadoW+"s│%-"+imgW+"s│%-"+createdW+"s│%-"+updatedW+"s│\n",
                        id, cat,
                        getLine(codLines, i, codW),
                        getLine(nombreLines, i, nomW),
                        getLine(descLines, i, descW),
                        precio, stock, stockMin,
                        getLine(uniLines, i, uniW),
                        getLine(estLines, i, estadoW),
                        getLine(imgLines, i, imgW),
                        getLine(crLines, i, createdW),
                        getLine(upLines, i, updatedW)));
            }
        }

        sb.append("└").append("─".repeat(idW))
          .append("┴").append("─".repeat(catW))
          .append("┴").append("─".repeat(codW))
          .append("┴").append("─".repeat(nomW))
          .append("┴").append("─".repeat(descW))
          .append("┴").append("─".repeat(precioW))
          .append("┴").append("─".repeat(stockW))
          .append("┴").append("─".repeat(stockW))
          .append("┴").append("─".repeat(uniW))
          .append("┴").append("─".repeat(estadoW))
          .append("┴").append("─".repeat(imgW))
          .append("┴").append("─".repeat(createdW))
          .append("┴").append("─".repeat(updatedW))
          .append("┘\n");
        return sb.toString();
    }

    public static String obtenerUnoTable(Producto p){ return obtenerTodosTable(java.util.List.of(p)); }

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
