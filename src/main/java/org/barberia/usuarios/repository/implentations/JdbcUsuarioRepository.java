package org.barberia.usuarios.repository.implentations;

import org.barberia.usuarios.domain.enums.*;
import org.barberia.usuarios.domain.Usuario;
import org.barberia.usuarios.infra.Database;
import org.barberia.usuarios.repository.UsuarioRepository;
import org.barberia.usuarios.utils.DuplicadoException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JdbcUsuarioRepository implements UsuarioRepository {

    @Override
    public List<Usuario> findAll() {
        String sql = "SELECT id_usuario, nombre, apellido, email, telefono, direccion, created_at, updated_at, estado, username, password FROM usuario ORDER BY id_usuario";
        List<Usuario> list = new ArrayList<>();
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Optional<Usuario> findById(Integer id) {
        String sql = "SELECT id_usuario, nombre, apellido, email, telefono, direccion, created_at, updated_at, estado, username, password FROM usuario WHERE id_usuario = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Usuario save(Usuario u) {
        if (u.id == null) {
            return insert(u);
        } else {
            return update(u);
        }
    }

    private Usuario insert(Usuario u) {
        String sql = "INSERT INTO usuario (nombre, apellido, email, telefono, direccion, estado, username, password) VALUES (?,?,?,?,?,?,?,?) RETURNING id_usuario, created_at, updated_at";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.nombre);
            ps.setString(2, u.apellido);
            ps.setString(3, u.email);
            ps.setString(4, u.telefono);
            ps.setString(5, u.direccion);
            ps.setObject(6, u.estado != null ? u.estado.name() : EstadoUsuario.activo.name(), Types.OTHER);
            ps.setString(7, u.username);
            ps.setString(8, u.password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    u.id = rs.getInt("id_usuario");
                    Timestamp c = rs.getTimestamp("created_at");
                    Timestamp up = rs.getTimestamp("updated_at");
                    u.created_at = c != null ? c.toLocalDateTime() : null;
                    u.updated_at = up != null ? up.toLocalDateTime() : null;
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate key value violates unique constraint")) {
                throw new IllegalArgumentException(extraerMensajeDuplicado(e.getMessage()));
            } else {
                throw new IllegalArgumentException("Error al crear el usuario: " + e.getMessage(), e);
            }
        }
        return u;
    }

    private Usuario update(Usuario u) {
        String sql = "UPDATE usuario SET nombre=?, apellido=?, email=?, telefono=?, direccion=?, username=?, password=?, updated_at = now() WHERE id_usuario=? RETURNING created_at, updated_at";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.nombre);
            ps.setString(2, u.apellido);
            ps.setString(3, u.email);
            ps.setString(4, u.telefono);
            ps.setString(5, u.direccion);
            ps.setString(6, u.username);
            ps.setString(7, u.password);
            ps.setInt(8, u.id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Timestamp c = rs.getTimestamp("created_at");
                    Timestamp up = rs.getTimestamp("updated_at");
                    u.created_at = c != null ? c.toLocalDateTime() : null;
                    u.updated_at = up != null ? up.toLocalDateTime() : null;
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate key value violates unique constraint")) {
                throw new IllegalArgumentException(extraerMensajeDuplicado(e.getMessage()));
            } else {
                throw new RuntimeException("Error al actualizar el usuario: " + e.getMessage(), e);
            }
        }
        return u;
    }

    
    public void softDeleteById(Integer id) {
        String sql = "UPDATE usuario SET estado = ?::estado_usuario, updated_at = now() WHERE id_usuario = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, EstadoUsuario.inactivo.name());
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void activateById(Integer id) {
        String sql = "UPDATE usuario SET estado = ?::estado_usuario, updated_at = now() WHERE id_usuario = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, EstadoUsuario.activo.name());
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Usuario> findAvailableUsuarios() {
        String sql = "SELECT u.id_usuario, u.nombre, u.apellido, u.email, u.telefono, u.direccion, " +
                     "u.created_at, u.updated_at, u.estado, u.username, u.password " +
                     "FROM usuario u " +
                     "WHERE u.estado = 'activo' " +
                     "AND u.id_usuario NOT IN (SELECT id_usuario FROM barbero) " +
                     "AND u.id_usuario NOT IN (SELECT id_usuario FROM cliente) " +
                     "ORDER BY u.id_usuario";
        List<Usuario> list = new ArrayList<>();
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

     

    private Usuario mapRow(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.id = rs.getInt("id_usuario");
        u.nombre = rs.getString("nombre");
        u.apellido = rs.getString("apellido");
        u.email = rs.getString("email");
        u.telefono = rs.getString("telefono");
        u.direccion = rs.getString("direccion");
        Timestamp c = rs.getTimestamp("created_at");
        Timestamp up = rs.getTimestamp("updated_at");
        u.created_at = c != null ? c.toLocalDateTime() : null;
        u.updated_at = up != null ? up.toLocalDateTime() : null;
        String estado = rs.getString("estado");
        if (estado != null) {
            u.estado = EstadoUsuario.valueOf(estado);
        }
        u.username = rs.getString("username");
        u.password = rs.getString("password");
        return u;
    }



      private String extraerMensajeDuplicado(String mensajeError) {
        // Extraer el campo duplicado: Key (username)=(alegonz)
        Pattern pattern = Pattern.compile("Key \\((.+?)\\)=\\((.+?)\\)");
        Matcher matcher = pattern.matcher(mensajeError);
        if (matcher.find()) {
            String campo = matcher.group(1);
            String valor = matcher.group(2);
            return String.format("El %s '%s' ya está registrado ", campo, valor);
        }
        return "Ya existe un registro con los datos proporcionados";
    }
}
