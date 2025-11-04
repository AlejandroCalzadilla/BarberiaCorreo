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

        for (Usuario usuario : usuarios) {
            String nombre = truncate(usuario.nombre, nombreWidth);
            String apellido = truncate(usuario.apellido, apellidoWidth);
            String email = truncate(usuario.email, emailWidth);
            String username = truncate(usuario.username, usernameWidth);
            String telefono = truncate(usuario.telefono, telefonoWidth);
            String direccion = truncate(usuario.direccion, direccionWidth);
            String estado = truncate(usuario.estado != null ? usuario.estado.name() : "", 10);
            String createdAt = truncate(usuario.created_at != null ? usuario.created_at.toString() : "", 19);
            String updatedAt = truncate(usuario.updated_at != null ? usuario.updated_at.toString() : "", 19);

            sb.append(String.format(
                    "│%-" + idWidth + "s│%-" + nombreWidth + "s│%-" + apellidoWidth + "s│%-" + emailWidth + "s│%-"
                            + usernameWidth + "s│%-" + telefonoWidth + "s│%-" + direccionWidth + "s│%-" + 10 + "s│%-" + 19 + "s│%-" + 19 + "s│\n",
                    usuario.id, nombre, apellido, email, username, telefono, direccion, estado, createdAt, updatedAt));
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

    private static String truncate(String str, int maxLength) {
        if (str == null)
            return "";
        if (str.length() <= maxLength)
            return str;
        if (maxLength <= 3) return str.substring(0, Math.max(0, maxLength));
        return str.substring(0, maxLength - 3) + "...";
    }
}
