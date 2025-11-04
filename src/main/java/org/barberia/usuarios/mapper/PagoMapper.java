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

        for (Pago p : items) {
            String met=p.metodo_pago!=null?p.metodo_pago.name():"";
            String tip=p.tipo_pago!=null?p.tipo_pago.name():"";
            String est=p.estado!=null?p.estado.name():"";
            String fp=p.fecha_pago!=null?p.fecha_pago.toString():"";
            String cr=p.created_at!=null?p.created_at.toString():"";
            String up=p.updated_at!=null?p.updated_at.toString():"";
            String notas=t(p.notas, notasW);
            sb.append(String.format("│%-"+idW+"s│%-"+resW+"s│%-"+montoW+"s│%-"+metW+"s│%-"+tipW+"s│%-"+estW+"s│%-"+fpW+"s│%-"+notasW+"s│%-"+createdW+"s│%-"+updatedW+"s│\n",
                    p.id_pago, p.id_reserva, p.monto_total, met, tip, est, fp, notas, cr, up));
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

    private static String t(String s,int w){ if(s==null)return""; if(s.length()<=w)return s; if(w<=3)return s.substring(0,Math.max(0,w)); return s.substring(0,w-3)+"..."; }
}
