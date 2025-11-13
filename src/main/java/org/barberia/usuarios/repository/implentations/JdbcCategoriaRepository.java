package org.barberia.usuarios.repository.implentations;

import org.barberia.usuarios.domain.Categoria;
import org.barberia.usuarios.domain.enums.*;
import org.barberia.usuarios.infra.Database;
import org.barberia.usuarios.repository.CategoriaRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JdbcCategoriaRepository implements CategoriaRepository {
    @Override
    public List<Categoria> findAll() {
        String sql = "SELECT id_categoria, nombre, descripcion, created_at, updated_at, estado FROM categoria ORDER BY id_categoria";
        List<Categoria> list = new ArrayList<>();
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
    public Optional<Categoria> findById(Integer id) {
        String sql = "SELECT id_categoria, nombre, descripcion, created_at, updated_at, estado FROM categoria WHERE id_categoria=?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Categoria save(Categoria c) {
        if (c.id_categoria == null) return insert(c);
        return update(c);
    }

    private Categoria insert(Categoria c) {
         String sql = "INSERT INTO categoria (nombre, descripcion, estado) VALUES (?,?,?::estado_categoria) RETURNING id_categoria, created_at, updated_at";   
         try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.nombre);
            ps.setString(2, c.descripcion);
            ps.setString(3, c.estado.name() != null ? c.estado.name() : EstadoCategoria.activa.name());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    c.id_categoria = rs.getInt("id_categoria");
                    Timestamp cr = rs.getTimestamp("created_at");
                    Timestamp up = rs.getTimestamp("updated_at");
                    c.created_at = cr != null ? cr.toLocalDateTime() : null;
                    c.updated_at = up != null ? up.toLocalDateTime() : null;
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate key value violates unique constraint")) {
                throw new IllegalArgumentException(extraerMensajeDuplicado(e.getMessage()));
            } else {
                throw new RuntimeException("Error al crear la categoria: " + e.getMessage(), e);
            }
        }
        return c;
    }

    private Categoria update(Categoria c) {
        String sql = "UPDATE categoria SET nombre=?, descripcion=?, estado=?::estado_categoria, updated_at=now() WHERE id_categoria=? RETURNING created_at, updated_at,estado";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.nombre);
            ps.setString(2, c.descripcion);
            ps.setString(3, c.estado != null ? c.estado.name() : EstadoCategoria.activa.name());
            ps.setInt(4, c.id_categoria);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Timestamp cr = rs.getTimestamp("created_at");
                    Timestamp up = rs.getTimestamp("updated_at");
                    c.created_at = cr != null ? cr.toLocalDateTime() : null;
                    c.updated_at = up != null ? up.toLocalDateTime() : null;
                    String estado = rs.getString("estado");
                    if (estado != null) c.estado = EstadoCategoria.valueOf(estado);
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate key value violates unique constraint")) {
                throw new IllegalArgumentException(extraerMensajeDuplicado(e.getMessage()));
            } else {
                throw new RuntimeException("Error al actualizar la categoria: " + e.getMessage(), e);
            }
        }
        return c;
    }

    @Override
    public void deleteById(Integer id) {
        this.findById(id);
        if (!this.findById(id).isPresent()) {
            throw new RuntimeException("Categoria con id " + id + " no existe.");
        }
        String sql = "DELETE FROM categoria WHERE id_categoria=?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String softDeleteById(Integer id) {
        this.findById(id);
        if (!this.findById(id).isPresent()) {
            throw new RuntimeException("Categoria con id " + id + " no existe.");
        }
        String sql = "UPDATE categoria SET estado='inactiva'::estado_categoria, updated_at=now() WHERE id_categoria=? RETURNING estado";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("estado");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void activateById(Integer id) {
        this.findById(id);
        if (!this.findById(id).isPresent()) {
            throw new RuntimeException("Categoria con id " + id + " no existe.");
        }
        String sql = "UPDATE categoria SET estado='activa'::estado_categoria, updated_at=now() WHERE id_categoria=?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Categoria mapRow(ResultSet rs) throws SQLException {
        Categoria c = new Categoria();
        c.id_categoria = rs.getInt("id_categoria");
        c.nombre = rs.getString("nombre");
        c.descripcion = rs.getString("descripcion");
        Timestamp cr = rs.getTimestamp("created_at");
        Timestamp up = rs.getTimestamp("updated_at");
        c.created_at = cr != null ? cr.toLocalDateTime() : null;
        c.updated_at = up != null ? up.toLocalDateTime() : null;
        String estado = rs.getString("estado");
        if (estado != null) c.estado = EstadoCategoria.valueOf(estado);
        return c;
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
