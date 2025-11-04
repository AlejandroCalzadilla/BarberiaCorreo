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

        for (Barbero b : items) {
            String esp=t(b.especialidad,espW), foto=t(b.foto_perfil,fotoW), est=t(b.estado!=null?b.estado.name():"",estW);
            String cr=b.created_at!=null?b.created_at.toString():"";
            String up=b.updated_at!=null?b.updated_at.toString():"";
            sb.append(String.format("│%-"+idW+"s│%-"+usrW+"s│%-"+espW+"s│%-"+fotoW+"s│%-"+estW+"s│%-"+createdW+"s│%-"+updatedW+"s│\n",
                    b.id_barbero, b.id_usuario, esp, foto, est, cr, up));
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

    private static String t(String s,int w){ if(s==null)return""; if(s.length()<=w)return s; if(w<=3)return s.substring(0,Math.max(0,w)); return s.substring(0,w-3)+"..."; }
}
