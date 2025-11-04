package org.barberia.usuarios.repository.implentations;

import org.barberia.usuarios.domain.enums.*;
import org.barberia.usuarios.domain.Pago;
import org.barberia.usuarios.infra.Database;
import org.barberia.usuarios.repository.PagoRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcPagoRepository implements PagoRepository {
    @Override
    public List<Pago> findAll() {
        String sql = "SELECT id_pago, id_reserva, monto_total, metodo_pago, tipo_pago, estado, fecha_pago, notas, created_at, updated_at FROM pago ORDER BY id_pago";
        List<Pago> list = new ArrayList<>();
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public Optional<Pago> findById(Integer id) {
        String sql = "SELECT id_pago, id_reserva, monto_total, metodo_pago, tipo_pago, estado, fecha_pago, notas, created_at, updated_at FROM pago WHERE id_pago=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return Optional.of(mapRow(rs)); }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return Optional.empty();
    }

    @Override
    public Pago save(Pago p) {
        if (p.id_pago == null) return insert(p);
        return update(p);
    }

    private Pago insert(Pago p) {
        String sql = "INSERT INTO pago (id_reserva, monto_total, metodo_pago, tipo_pago, estado, fecha_pago, notas) VALUES (?,?,?::metodo_pago,?::tipo_pago,?::estado_pago,?,?) RETURNING id_pago, created_at, updated_at";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.id_reserva);
            ps.setBigDecimal(2, p.monto_total);
            ps.setString(3, p.metodo_pago != null ? p.metodo_pago.name() : MetodoPago.efectivo.name());
            ps.setString(4, p.tipo_pago != null ? p.tipo_pago.name() : TipoPago.pago_completo.name());
            ps.setString(5, p.estado != null ? p.estado.name() : EstadoPago.pendiente.name());
            if (p.fecha_pago != null) ps.setTimestamp(6, Timestamp.valueOf(p.fecha_pago)); else ps.setTimestamp(6, null);
            ps.setString(7, p.notas);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p.id_pago = rs.getInt("id_pago");
                    Timestamp cr = rs.getTimestamp("created_at");
                    Timestamp up = rs.getTimestamp("updated_at");
                    p.created_at = cr != null ? cr.toLocalDateTime() : null;
                    p.updated_at = up != null ? up.toLocalDateTime() : null;
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return p;
    }

    private Pago update(Pago p) {
        String sql = "UPDATE pago SET id_reserva=?, monto_total=?, metodo_pago=?::metodo_pago, tipo_pago=?::tipo_pago, estado=?::estado_pago, fecha_pago=?, notas=?, updated_at=now() WHERE id_pago=? RETURNING created_at, updated_at";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.id_reserva);
            ps.setBigDecimal(2, p.monto_total);
            ps.setString(3, p.metodo_pago != null ? p.metodo_pago.name() : MetodoPago.efectivo.name());
            ps.setString(4, p.tipo_pago != null ? p.tipo_pago.name() : TipoPago.pago_completo.name());
            ps.setString(5, p.estado != null ? p.estado.name() : EstadoPago.pendiente.name());
            if (p.fecha_pago != null) ps.setTimestamp(6, Timestamp.valueOf(p.fecha_pago)); else ps.setTimestamp(6, null);
            ps.setString(7, p.notas);
            ps.setInt(8, p.id_pago);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Timestamp cr = rs.getTimestamp("created_at");
                    Timestamp up = rs.getTimestamp("updated_at");
                    p.created_at = cr != null ? cr.toLocalDateTime() : null;
                    p.updated_at = up != null ? up.toLocalDateTime() : null;
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return p;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM pago WHERE id_pago=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    private Pago mapRow(ResultSet rs) throws SQLException {
        Pago p = new Pago();
        p.id_pago = rs.getInt("id_pago");
        p.id_reserva = rs.getInt("id_reserva");
        p.monto_total = rs.getBigDecimal("monto_total");
        String met = rs.getString("metodo_pago"); if (met != null) p.metodo_pago = MetodoPago.valueOf(met);
        String tip = rs.getString("tipo_pago"); if (tip != null) p.tipo_pago = TipoPago.valueOf(tip);
        String est = rs.getString("estado"); if (est != null) p.estado = EstadoPago.valueOf(est);
        Timestamp fp = rs.getTimestamp("fecha_pago"); p.fecha_pago = fp != null ? fp.toLocalDateTime() : null;
        p.notas = rs.getString("notas");
        Timestamp cr = rs.getTimestamp("created_at"); p.created_at = cr != null ? cr.toLocalDateTime() : null;
        Timestamp up = rs.getTimestamp("updated_at"); p.updated_at = up != null ? up.toLocalDateTime() : null;
        return p;
    }
}
