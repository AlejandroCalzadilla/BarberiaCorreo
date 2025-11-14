package org.barberia.usuarios.repository.implentations;

import org.barberia.usuarios.domain.Barbero;
import org.barberia.usuarios.domain.enums.*;
import org.barberia.usuarios.infra.Database;
import org.barberia.usuarios.repository.BarberoRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcBarberoRepository implements BarberoRepository {
    @Override
    public List<Barbero> findAll() {
        String sql = "SELECT id_barbero, id_usuario, especialidad, foto_perfil, estado, created_at, updated_at FROM barbero ORDER BY id_barbero";
        List<Barbero> list = new ArrayList<>();
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public Optional<Barbero> findById(Integer id) {
        String sql = "SELECT id_barbero, id_usuario, especialidad, foto_perfil, estado, created_at, updated_at FROM barbero WHERE id_barbero=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return Optional.of(mapRow(rs)); }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return Optional.empty();
    }

    @Override
    public Barbero save(Barbero b) {
        if (b.id_barbero == null) return insert(b);
        return update(b);
    }

    private Barbero insert(Barbero b) {
        String sql = "INSERT INTO barbero (id_usuario, especialidad, foto_perfil, estado) VALUES (?,?,?,?::estado_barbero) RETURNING id_barbero, created_at, updated_at";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, b.id_usuario);
            ps.setString(2, b.especialidad);
            ps.setString(3, b.foto_perfil);
            ps.setString(4, b.estado != null ? b.estado.name() : EstadoBarbero.disponible.name());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    b.id_barbero = rs.getInt("id_barbero");
                    Timestamp cr = rs.getTimestamp("created_at");
                    Timestamp up = rs.getTimestamp("updated_at");
                    b.created_at = cr != null ? cr.toLocalDateTime() : null;
                    b.updated_at = up != null ? up.toLocalDateTime() : null;
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return b;
    }

    private Barbero update(Barbero b) {
        String sql = "UPDATE barbero SET id_usuario=?, especialidad=?, foto_perfil=?, estado=?::estado_barbero, updated_at=now() WHERE id_barbero=? RETURNING created_at, updated_at";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, b.id_usuario);
            ps.setString(2, b.especialidad);
            ps.setString(3, b.foto_perfil);
            ps.setString(4, b.estado != null ? b.estado.name() : EstadoBarbero.disponible.name());
            ps.setInt(5, b.id_barbero);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Timestamp cr = rs.getTimestamp("created_at");
                    Timestamp up = rs.getTimestamp("updated_at");
                    b.created_at = cr != null ? cr.toLocalDateTime() : null;
                    b.updated_at = up != null ? up.toLocalDateTime() : null;
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return b;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM barbero WHERE id_barbero=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
    


    @Override
    public Optional<Barbero> findByUsuarioId(Integer usuarioId) {
        String sql = "SELECT id_barbero, id_usuario, especialidad, foto_perfil, estado, created_at, updated_at FROM barbero WHERE id_usuario=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return Optional.of(mapRow(rs)); }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return Optional.empty();
    }


  


    private Barbero mapRow(ResultSet rs) throws SQLException {
        Barbero b = new Barbero();
        b.id_barbero = rs.getInt("id_barbero");
        b.id_usuario = rs.getInt("id_usuario");
        b.especialidad = rs.getString("especialidad");
        b.foto_perfil = rs.getString("foto_perfil");
        String est = rs.getString("estado"); if (est != null) b.estado = EstadoBarbero.valueOf(est);
        Timestamp cr = rs.getTimestamp("created_at");
        Timestamp up = rs.getTimestamp("updated_at");
        b.created_at = cr != null ? cr.toLocalDateTime() : null;
        b.updated_at = up != null ? up.toLocalDateTime() : null;
        return b;
    }


    public boolean activo (int id_barbero) {
           String sql = "SELECT u.estado FROM usuario u JOIN barbero b ON u.id = b.id_usuario WHERE b.id_barbero=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id_barbero);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String estado = rs.getString("estado");
                    return EstadoUsuario.valueOf(estado) == EstadoUsuario.activo;
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return false;
    }
}
