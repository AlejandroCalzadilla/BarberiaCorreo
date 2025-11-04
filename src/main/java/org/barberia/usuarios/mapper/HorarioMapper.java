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

        for (Horario h : items) {
            String dia=h.dia_semana!=null?h.dia_semana.name():"";
            String hi=h.hora_inicio!=null?h.hora_inicio.toString():"";
            String hf=h.hora_fin!=null?h.hora_fin.toString():"";
            String est=h.estado!=null?h.estado.name():"";
            String cr=h.created_at!=null?h.created_at.toString():"";
            String up=h.updated_at!=null?h.updated_at.toString():"";
            sb.append(String.format("│%-"+idW+"s│%-"+barW+"s│%-"+diaW+"s│%-"+hiW+"s│%-"+hfW+"s│%-"+estW+"s│%-"+createdW+"s│%-"+updatedW+"s│\n",
                    h.id_horario, h.id_barbero, dia, hi, hf, est, cr, up));
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
}
