package org.barberia.usuarios.repository.implentations;

import org.barberia.usuarios.domain.enums.*;
import org.barberia.usuarios.domain.Servicio;
import org.barberia.usuarios.infra.Database;
import org.barberia.usuarios.repository.ServicioRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcServicioRepository implements ServicioRepository {
    @Override
    public List<Servicio> findAll() {
        String sql = "SELECT id_servicio, nombre, descripcion, duracion_minutos_aprox, precio, estado, imagen, created_at, updated_at FROM servicio ORDER BY id_servicio";
        List<Servicio> list = new ArrayList<>();
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public Optional<Servicio> findById(Integer id) {
        String sql = "SELECT id_servicio, nombre, descripcion, duracion_minutos_aprox, precio, estado, imagen, created_at, updated_at FROM servicio WHERE id_servicio=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return Optional.of(mapRow(rs)); }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return Optional.empty();
    }

    @Override
    public Servicio save(Servicio s) {
        if (s.id_servicio == null) return insert(s);
        return update(s);
    }

    private Servicio insert(Servicio s) {
        String sql = "INSERT INTO servicio ( nombre, descripcion, duracion_minutos_aprox, precio, estado, imagen) VALUES (?,?,?,?,?::estado_item,?) RETURNING id_servicio, created_at, updated_at";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
          
            ps.setString(1, s.nombre);
            ps.setString(2, s.descripcion);
            ps.setInt(3, s.duracion_minutos_aprox);
            ps.setBigDecimal(4, s.precio);
            ps.setString(5, s.estado != null ? s.estado.name() : EstadoItem.activo.name());
            ps.setString(6, s.imagen);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    s.id_servicio = rs.getInt("id_servicio");
                    Timestamp cr = rs.getTimestamp("created_at");
                    Timestamp up = rs.getTimestamp("updated_at");
                    s.created_at = cr != null ? cr.toLocalDateTime() : null;
                    s.updated_at = up != null ? up.toLocalDateTime() : null;
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return s;
    }

    private Servicio update(Servicio s) {
        String sql = "UPDATE servicio SET nombre=?, descripcion=?, duracion_minutos_aprox=?, precio=?, estado=?::estado_item, imagen=?, updated_at=now() WHERE id_servicio=? RETURNING created_at, updated_at";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
           
            ps.setString(1, s.nombre);
            ps.setString(2, s.descripcion);
            ps.setInt(3, s.duracion_minutos_aprox);
            ps.setBigDecimal(4, s.precio);
            ps.setString(5, s.estado != null ? s.estado.name() : EstadoItem.activo.name());
            ps.setString(6, s.imagen);
            ps.setInt(7, s.id_servicio);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Timestamp cr = rs.getTimestamp("created_at");
                    Timestamp up = rs.getTimestamp("updated_at");
                    s.created_at = cr != null ? cr.toLocalDateTime() : null;
                    s.updated_at = up != null ? up.toLocalDateTime() : null;
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return s;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM servicio WHERE id_servicio=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void softDeleteById(Integer id) {
        String sql = "UPDATE servicio SET estado='inactivo'::estado_item, updated_at=now() WHERE id_servicio=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void activateById(Integer id) {
        String sql = "UPDATE servicio SET estado='activo'::estado_item, updated_at=now() WHERE id_servicio=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    private Servicio mapRow(ResultSet rs) throws SQLException {
        Servicio s = new Servicio();
        s.id_servicio = rs.getInt("id_servicio");
        s.nombre = rs.getString("nombre");
        s.descripcion = rs.getString("descripcion");
        s.duracion_minutos_aprox = rs.getInt("duracion_minutos_aprox");
        s.precio = rs.getBigDecimal("precio");
        String est = rs.getString("estado"); if (est != null) s.estado = EstadoItem.valueOf(est);
        s.imagen = rs.getString("imagen");
        Timestamp cr = rs.getTimestamp("created_at");
        Timestamp up = rs.getTimestamp("updated_at");
        s.created_at = cr != null ? cr.toLocalDateTime() : null;
        s.updated_at = up != null ? up.toLocalDateTime() : null;
        return s;
    }
}
