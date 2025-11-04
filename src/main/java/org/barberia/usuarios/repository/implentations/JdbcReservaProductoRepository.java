package org.barberia.usuarios.repository.implentations;

import org.barberia.usuarios.domain.ServicioProducto;
import org.barberia.usuarios.infra.Database;
import org.barberia.usuarios.repository.ServicioProductoRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcReservaProductoRepository implements ServicioProductoRepository {
    @Override
    public List<ServicioProducto> findAll() {
        String sql = "SELECT id_reserva_producto, id_reserva, id_producto, cantidad, subtotal FROM servicio_producto ORDER BY id_reserva_producto";
        List<ServicioProducto> list = new ArrayList<>();
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public Optional<ServicioProducto> findById(Integer id) {
        String sql = "SELECT id_reserva_producto, id_reserva, id_producto, cantidad, subtotal FROM servicio_producto WHERE id_reserva_producto=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return Optional.of(mapRow(rs)); }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return Optional.empty();
    }

    @Override
    public ServicioProducto save(ServicioProducto sp) {
        if (sp.id_servicio_producto == null) return insert(sp);
        return update(sp);
    }

    private ServicioProducto insert(ServicioProducto sp) {
        String sql = "INSERT INTO reserva_producto (id_reserva, id_producto, cantidad, subtotal) VALUES (?,?,?,?) RETURNING id_reserva_producto";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, sp.id_servicio);
            ps.setInt(2, sp.id_producto);
            ps.setInt(3, sp.cantidad);
            ps.setBigDecimal(4, sp.subtotal);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) sp.id_servicio_producto = rs.getInt("id_reserva_producto"); }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return sp;
    }

    private ServicioProducto update(ServicioProducto sp) {
        String sql = "UPDATE reserva_producto SET id_reserva=?, id_producto=?, cantidad=?, subtotal=? WHERE id_servicio_producto=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, sp.id_servicio);
            ps.setInt(2, sp.id_producto);
            ps.setInt(3, sp.cantidad);
            ps.setBigDecimal(4, sp.subtotal);
            ps.setInt(5, sp.id_servicio_producto);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
        return sp;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM reserva_producto WHERE id_servicio_producto=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    private ServicioProducto mapRow(ResultSet rs) throws SQLException {
        ServicioProducto sp = new ServicioProducto();
        sp.id_servicio_producto = rs.getInt("id_reserva_producto");
        sp.id_servicio = rs.getInt("id_reserva");
        sp.id_producto = rs.getInt("id_producto");
        sp.cantidad = rs.getInt("cantidad");
        sp.subtotal = rs.getBigDecimal("subtotal");
        return sp;
    }
}
