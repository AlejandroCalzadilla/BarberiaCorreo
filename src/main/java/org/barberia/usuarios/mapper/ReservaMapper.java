package org.barberia.usuarios.mapper;

import org.barberia.usuarios.domain.Reserva;

import java.util.List;

public class ReservaMapper {
    public static String obtenerTodosTable(List<Reserva> items) {
        StringBuilder sb = new StringBuilder();
        int idW=10, cliW=10, barW=10, srvW=10, fechaW=10, hiW=8, hfW=8, estW=15, totalW=12, precioW=12, anticW=12, porcW=8, createdW=19, updatedW=19;
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
          .append("┬").append("─".repeat(porcW))
          .append("┬").append("─".repeat(createdW))
          .append("┬").append("─".repeat(updatedW))
          .append("┐\n");

        sb.append(String.format("│%-"+idW+"s│%-"+cliW+"s│%-"+barW+"s│%-"+srvW+"s│%-"+fechaW+"s│%-"+hiW+"s│%-"+hfW+"s│%-"+estW+"s│%-"+totalW+"s│%-"+notasW+"s│%-"+precioW+"s│%-"+anticW+"s│%-"+porcW+"s│%-"+createdW+"s│%-"+updatedW+"s│\n",
                "ID","ID_CLI","ID_BAR","ID_SRV","Fecha","Inicio","Fin","Estado","Total","Notas","PrecioSrv","Anticipo","%","Creado","Actualizado"));

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
          .append("┼").append("─".repeat(porcW))
          .append("┼").append("─".repeat(createdW))
          .append("┼").append("─".repeat(updatedW))
          .append("┤\n");

        for (Reserva r : items) {
            String fecha=r.fecha_reserva!=null?r.fecha_reserva.toString():"";
            String hi=r.hora_inicio!=null?r.hora_inicio.toString():"";
            String hf=r.hora_fin!=null?r.hora_fin.toString():"";
            String est=r.estado!=null?r.estado.name():"";
            String notas=t(r.notas,notasW);
            String cr=r.created_at!=null?r.created_at.toString():"";
            String up=r.updated_at!=null?r.updated_at.toString():"";
            sb.append(String.format("│%-"+idW+"s│%-"+cliW+"s│%-"+barW+"s│%-"+srvW+"s│%-"+fechaW+"s│%-"+hiW+"s│%-"+hfW+"s│%-"+estW+"s│%-"+totalW+"s│%-"+notasW+"s│%-"+precioW+"s│%-"+anticW+"s│%-"+porcW+"s│%-"+createdW+"s│%-"+updatedW+"s│\n",
                    r.id_reserva, r.id_cliente, r.id_barbero, r.id_servicio, fecha, hi, hf, est, r.total, notas, r.precio_servicio, r.monto_anticipo, r.porcentaje_anticipo, cr, up));
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
          .append("┴").append("─".repeat(porcW))
          .append("┴").append("─".repeat(createdW))
          .append("┴").append("─".repeat(updatedW))
          .append("" + "┘\n");
        return sb.toString();
    }

    public static String obtenerUnoTable(Reserva r){ return obtenerTodosTable(java.util.List.of(r)); }

    private static String t(String s,int w){ if(s==null)return""; if(s.length()<=w)return s; if(w<=3)return s.substring(0,Math.max(0,w)); return s.substring(0,w-3)+"..."; }
}
