package org.barberia.usuarios.repository.implentations;

import org.barberia.usuarios.domain.Cliente;
import org.barberia.usuarios.infra.Database;
import org.barberia.usuarios.repository.ClienteRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcClienteRepository implements ClienteRepository {
    @Override
    public List<Cliente> findAll() {
        String sql = "SELECT id_cliente, id_usuario, fecha_nacimiento, ci, created_at, updated_at FROM cliente ORDER BY id_cliente";
        List<Cliente> list = new ArrayList<>();
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public Optional<Cliente> findById(Integer id) {
        String sql = "SELECT id_cliente, id_usuario, fecha_nacimiento, ci, created_at, updated_at FROM cliente WHERE id_cliente=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return Optional.of(mapRow(rs)); }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return Optional.empty();
    }

    public Optional<Cliente> findByUsuarioId(Integer usuarioId) {
        String sql = "SELECT id_cliente, id_usuario, fecha_nacimiento, ci, created_at, updated_at FROM cliente WHERE id_usuario=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return Optional.of(mapRow(rs)); }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return Optional.empty();
    }

    @Override
    public Cliente save(Cliente c) {
        if (c.id_cliente == null) return insert(c);
        return update(c);
    }

    private Cliente insert(Cliente c) {
        String sql = "INSERT INTO cliente (id_usuario, fecha_nacimiento, ci) VALUES (?,?,?) RETURNING id_cliente, created_at, updated_at";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.id_usuario);
            if (c.fecha_nacimiento != null) ps.setDate(2, Date.valueOf(c.fecha_nacimiento)); else ps.setNull(2, Types.DATE);
            ps.setString(3, c.ci);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    c.id_cliente = rs.getInt("id_cliente");
                    Timestamp cr = rs.getTimestamp("created_at");
                    Timestamp up = rs.getTimestamp("updated_at");
                    c.created_at = cr != null ? cr.toLocalDateTime() : null;
                    c.updated_at = up != null ? up.toLocalDateTime() : null;
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return c;
    }

    private Cliente update(Cliente c) {
        String sql = "UPDATE cliente SET id_usuario=?, fecha_nacimiento=?, ci=?, updated_at=now() WHERE id_cliente=? RETURNING created_at, updated_at";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.id_usuario);
            if (c.fecha_nacimiento != null) ps.setDate(2, Date.valueOf(c.fecha_nacimiento)); else ps.setNull(2, Types.DATE);
            ps.setString(3, c.ci);
            ps.setInt(4, c.id_cliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Timestamp cr = rs.getTimestamp("created_at");
                    Timestamp up = rs.getTimestamp("updated_at");
                    c.created_at = cr != null ? cr.toLocalDateTime() : null;
                    c.updated_at = up != null ? up.toLocalDateTime() : null;
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return c;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM cliente WHERE id_cliente=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
    

    private Cliente mapRow(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.id_cliente = rs.getInt("id_cliente");
        c.id_usuario = rs.getInt("id_usuario");
        Date fn = rs.getDate("fecha_nacimiento");
        c.fecha_nacimiento = fn != null ? fn.toLocalDate().toString() : null;
        c.ci = rs.getString("ci");
        Timestamp cr = rs.getTimestamp("created_at");
        Timestamp up = rs.getTimestamp("updated_at");
        c.created_at = cr != null ? cr.toLocalDateTime() : null;
        c.updated_at = up != null ? up.toLocalDateTime() : null;
        return c;
    }
}
