package org.barberia.usuarios.mapper;

import java.util.List;

import org.barberia.usuarios.domain.Usuario;

public class UsuarioMapper {

    public static String obtenerTodosTable(List<Usuario> usuarios) {
        StringBuilder sb = new StringBuilder();

        int idWidth = 5;
        int nombreWidth = 15;
        int apellidoWidth = 15;
        int emailWidth = 20;
        int usernameWidth = 15;
        int telefonoWidth = 12;
        int direccionWidth = 25;

        sb.append("┌").append("─".repeat(idWidth))
                .append("┬").append("─".repeat(nombreWidth))
                .append("┬").append("─".repeat(apellidoWidth))
                .append("┬").append("─".repeat(emailWidth))
                .append("┬").append("─".repeat(usernameWidth))
                .append("┬").append("─".repeat(telefonoWidth))
                .append("┬").append("─".repeat(direccionWidth))
                .append("┬").append("─".repeat(10))
                .append("┬").append("─".repeat(19))
                .append("┬").append("─".repeat(19))
                .append("┐\n");

        sb.append(String.format(
                "│%-" + idWidth + "s│%-" + nombreWidth + "s│%-" + apellidoWidth + "s│%-" + emailWidth + "s│%-"
                        + usernameWidth + "s│%-" + telefonoWidth + "s│%-" + direccionWidth + "s│%-" + 10 + "s│%-" + 19 + "s│%-" + 19 + "s│\n",
                "ID", "Nombre", "Apellido", "Email", "Username", "Teléfono", "Dirección", "Estado", "Creado", "Actualizado"));

        sb.append("├").append("─".repeat(idWidth))
                .append("┼").append("─".repeat(nombreWidth))
                .append("┼").append("─".repeat(apellidoWidth))
                .append("┼").append("─".repeat(emailWidth))
                .append("┼").append("─".repeat(usernameWidth))
                .append("┼").append("─".repeat(telefonoWidth))
                .append("┼").append("─".repeat(direccionWidth))
                .append("┼").append("─".repeat(10))
                .append("┼").append("─".repeat(19))
                .append("┼").append("─".repeat(19))
                .append("┤\n");

        for (int idx = 0; idx < usuarios.size(); idx++) {
            Usuario usuario = usuarios.get(idx);
            List<String> nombreLines = wrap(usuario.nombre, nombreWidth);
            List<String> apellidoLines = wrap(usuario.apellido, apellidoWidth);
            List<String> emailLines = wrap(usuario.email, emailWidth);
            List<String> usernameLines = wrap(usuario.username, usernameWidth);
            List<String> telefonoLines = wrap(usuario.telefono, telefonoWidth);
            List<String> direccionLines = wrap(usuario.direccion, direccionWidth);
            List<String> estadoLines = wrap(usuario.estado != null ? usuario.estado.name() : "", 10);
            List<String> createdAtLines = wrap(usuario.created_at != null ? usuario.created_at.toString() : "", 19);
            List<String> updatedAtLines = wrap(usuario.updated_at != null ? usuario.updated_at.toString() : "", 19);
            
            int maxLines = Math.max(nombreLines.size(), Math.max(apellidoLines.size(), 
                          Math.max(emailLines.size(), Math.max(usernameLines.size(), 
                          Math.max(telefonoLines.size(), Math.max(direccionLines.size(), 
                          Math.max(estadoLines.size(), Math.max(createdAtLines.size(), updatedAtLines.size()))))))));
            
            for (int i = 0; i < maxLines; i++) {
                String id = i == 0 ? String.valueOf(usuario.id) : "";
                
                sb.append(String.format(
                        "│%-" + idWidth + "s│%-" + nombreWidth + "s│%-" + apellidoWidth + "s│%-" + emailWidth + "s│%-"
                                + usernameWidth + "s│%-" + telefonoWidth + "s│%-" + direccionWidth + "s│%-" + 10 + "s│%-" + 19 + "s│%-" + 19 + "s│\n",
                        id,
                        getLine(nombreLines, i, nombreWidth),
                        getLine(apellidoLines, i, apellidoWidth),
                        getLine(emailLines, i, emailWidth),
                        getLine(usernameLines, i, usernameWidth),
                        getLine(telefonoLines, i, telefonoWidth),
                        getLine(direccionLines, i, direccionWidth),
                        getLine(estadoLines, i, 10),
                        getLine(createdAtLines, i, 19),
                        getLine(updatedAtLines, i, 19)));
            }
            
            // Línea divisoria entre registros (solo si no es el último)
            if (idx < usuarios.size() - 1) {
                sb.append("├").append("─".repeat(idWidth))
                        .append("┼").append("─".repeat(nombreWidth))
                        .append("┼").append("─".repeat(apellidoWidth))
                        .append("┼").append("─".repeat(emailWidth))
                        .append("┼").append("─".repeat(usernameWidth))
                        .append("┼").append("─".repeat(telefonoWidth))
                        .append("┼").append("─".repeat(direccionWidth))
                        .append("┼").append("─".repeat(10))
                        .append("┼").append("─".repeat(19))
                        .append("┼").append("─".repeat(19))
                        .append("┤\n");
            }
        }

        sb.append("└").append("─".repeat(idWidth))
                .append("┴").append("─".repeat(nombreWidth))
                .append("┴").append("─".repeat(apellidoWidth))
                .append("┴").append("─".repeat(emailWidth))
                .append("┴").append("─".repeat(usernameWidth))
                .append("┴").append("─".repeat(telefonoWidth))
                .append("┴").append("─".repeat(direccionWidth))
                .append("┴").append("─".repeat(10))
                .append("┴").append("─".repeat(19))
                .append("┴").append("─".repeat(19))
                .append("┘\n");

        return sb.toString();
    }

    public static String obtenerUnoTable(Usuario usuario) {
        return obtenerTodosTable(java.util.List.of(usuario));
    }

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
