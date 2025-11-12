package org.barberia.usuarios.repository.implentations;

import org.barberia.usuarios.domain.enums.*;
import org.barberia.usuarios.domain.Reserva;
import org.barberia.usuarios.infra.Database;
import org.barberia.usuarios.repository.ReservaRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcReservaRepository implements ReservaRepository {
    @Override
    public List<Reserva> findAll() {
        String sql = "SELECT id_reserva, id_cliente, id_barbero, id_servicio, fecha_reserva, hora_inicio, hora_fin, estado, total, notas, monto_anticipo, created_at, updated_at FROM reserva ORDER BY id_reserva";
        List<Reserva> list = new ArrayList<>();
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public Optional<Reserva> findById(Integer id) {
        String sql = "SELECT id_reserva, id_cliente, id_barbero, id_servicio, fecha_reserva, hora_inicio, hora_fin, estado, total, notas, monto_anticipo, created_at, updated_at FROM reserva WHERE id_reserva=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return Optional.of(mapRow(rs)); }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return Optional.empty();
    }

    @Override
    public Reserva save(Reserva r) {
        if (r.id_reserva == null) return insert(r);
        return update(r);
    }

    private Reserva insert(Reserva r) {
        String sql = "INSERT INTO reserva (id_cliente, id_barbero, id_servicio, fecha_reserva, hora_inicio, hora_fin, estado, total, notas, monto_anticipo) VALUES (?,?,?,?,?,?,?::estado_reserva,?,?, ?) RETURNING id_reserva, created_at, updated_at";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, r.id_cliente);
            ps.setInt(2, r.id_barbero);
            ps.setInt(3, r.id_servicio);
            ps.setDate(4, r.fecha_reserva != null ? Date.valueOf(r.fecha_reserva) : null);
            ps.setTime(5, r.hora_inicio != null ? Time.valueOf(r.hora_inicio) : null);
            ps.setTime(6, r.hora_fin != null ? Time.valueOf(r.hora_fin) : null);
            ps.setString(7, r.estado != null ? r.estado.name() : EstadoReserva.confirmada.name());
            ps.setBigDecimal(8, r.total);
            ps.setString(9, r.notas);
            ps.setBigDecimal(10, r.monto_anticipo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    r.id_reserva = rs.getInt("id_reserva");
                    Timestamp cr = rs.getTimestamp("created_at");
                    Timestamp up = rs.getTimestamp("updated_at");
                    r.created_at = cr != null ? cr.toLocalDateTime() : null;
                    r.updated_at = up != null ? up.toLocalDateTime() : null;
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return r;
    }

    private Reserva update(Reserva r) {
        String sql = "UPDATE reserva SET id_cliente=?, id_barbero=?, id_servicio=?, fecha_reserva=?, hora_inicio=?, hora_fin=?, estado=?::estado_reserva, total=?, notas=?, monto_anticipo=?, updated_at=now() WHERE id_reserva=? RETURNING created_at, updated_at";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, r.id_cliente);
            ps.setInt(2, r.id_barbero);
            ps.setInt(3, r.id_servicio);
            ps.setDate(4, r.fecha_reserva != null ? Date.valueOf(r.fecha_reserva) : null);
            ps.setTime(5, r.hora_inicio != null ? Time.valueOf(r.hora_inicio) : null);
            ps.setTime(6, r.hora_fin != null ? Time.valueOf(r.hora_fin) : null);
            ps.setString(7, r.estado != null ? r.estado.name() : EstadoReserva.confirmada.name());
            ps.setBigDecimal(8, r.total);
            ps.setString(9, r.notas);
            ps.setBigDecimal(10, r.monto_anticipo);
            ps.setInt(11, r.id_reserva);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Timestamp cr = rs.getTimestamp("created_at");
                    Timestamp up = rs.getTimestamp("updated_at");
                    r.created_at = cr != null ? cr.toLocalDateTime() : null;
                    r.updated_at = up != null ? up.toLocalDateTime() : null;
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return r;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM reserva WHERE id_reserva=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    private Reserva mapRow(ResultSet rs) throws SQLException {
        Reserva r = new Reserva();
        r.id_reserva = rs.getInt("id_reserva");
        r.id_cliente = rs.getInt("id_cliente");
        r.id_barbero = rs.getInt("id_barbero");
        r.id_servicio = rs.getInt("id_servicio");
        Date fr = rs.getDate("fecha_reserva"); r.fecha_reserva = fr != null ? fr.toLocalDate() : null;
        Time hi = rs.getTime("hora_inicio"); r.hora_inicio = hi != null ? hi.toLocalTime() : null;
        Time hf = rs.getTime("hora_fin"); r.hora_fin = hf != null ? hf.toLocalTime() : null;
        String est = rs.getString("estado"); if (est != null) r.estado = EstadoReserva.valueOf(est);
        r.total = rs.getBigDecimal("total");
        r.notas = rs.getString("notas");
        r.monto_anticipo = rs.getBigDecimal("monto_anticipo");
        Timestamp cr = rs.getTimestamp("created_at"); r.created_at = cr != null ? cr.toLocalDateTime() : null;
        Timestamp up = rs.getTimestamp("updated_at"); r.updated_at = up != null ? up.toLocalDateTime() : null;
        return r;
    }
}
