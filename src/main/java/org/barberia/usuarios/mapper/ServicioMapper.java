package org.barberia.usuarios.mapper;

import org.barberia.usuarios.domain.Servicio;

import java.util.List;

public class ServicioMapper {
    public static String obtenerTodosTable(List<Servicio> items) {
        StringBuilder sb = new StringBuilder();
        int idW=10,catW=10,nomW=20,descW=30,durW=6,precioW=12,estadoW=10,imagenW=20,createdW=19,updatedW=19;

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

        for (Servicio s : items) {
            String nom=t(s.nombre,nomW),desc=t(s.descripcion,descW),est=t(s.estado!=null?s.estado.name():"",estadoW),img=t(s.imagen,imagenW);
            String cr=t(s.created_at!=null?s.created_at.toString():"",createdW);
            String up=t(s.updated_at!=null?s.updated_at.toString():"",updatedW);
            sb.append(String.format("│%-"+idW+"s│%-"+nomW+"s│%-"+descW+"s│%-"+durW+"s│%-"+precioW+"s│%-"+estadoW+"s│%-"+imagenW+"s│%-"+createdW+"s│%-"+updatedW+"s│\n",
                    s.id_servicio, nom, desc, s.duracion_minutos_aprox, s.precio, est, img, cr, up));
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

    private static String t(String s,int w){ if(s==null)return""; if(s.length()<=w)return s; if(w<=3)return s.substring(0,Math.max(0,w)); return s.substring(0,w-3)+"..."; }
}
