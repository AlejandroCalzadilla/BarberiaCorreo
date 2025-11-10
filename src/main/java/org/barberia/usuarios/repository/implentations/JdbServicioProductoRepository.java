package org.barberia.usuarios.repository.implentations;

import org.barberia.usuarios.domain.ServicioProducto;
import org.barberia.usuarios.infra.Database;
import org.barberia.usuarios.repository.ServicioProductoRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbServicioProductoRepository implements ServicioProductoRepository {
    @Override
    public List<ServicioProducto> findAll() {
        String sql = "SELECT id_servicio, id_producto, cantidad FROM servicio_producto ORDER BY id_servicio";
        List<ServicioProducto> list = new ArrayList<>();
        try (Connection con = Database.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

     @Override
    public List<ServicioProducto> findByServicioId(Integer id) {
        String sql = "SELECT id_servicio, id_producto, cantidad FROM servicio_producto WHERE id_servicio = ?";
        List<ServicioProducto> list = new ArrayList<>();
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public ServicioProducto insert(ServicioProducto sp) {

        if (!productoExists(sp.id_producto)) {
            throw new RuntimeException("El producto con ID " + sp.id_producto + " no existe.");
        }
        String sql = "INSERT INTO servicio_producto (id_servicio, id_producto, cantidad) VALUES (?,?,?) ";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, sp.id_servicio);
            ps.setInt(2, sp.id_producto);
            ps.setInt(3, sp.cantidad);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sp;
    }

    @Override
    public ServicioProducto update(ServicioProducto sp) {
        String sql = "UPDATE servicio_producto SET id_servicio=?, id_producto=?, cantidad=? WHERE id_producto=? AND id_servicio=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, sp.id_servicio);
            ps.setInt(2, sp.id_producto);
            ps.setInt(3, sp.cantidad);
            ps.setInt(4, sp.id_producto); // WHERE id_producto=?
            ps.setInt(5, sp.id_servicio); // AND id_servicio=?
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sp;
    }

    @Override
    public void delete(Integer id_servicio, Integer id_producto) {
        String sql = "DELETE FROM servicio_producto WHERE id_servicio=? AND id_producto=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id_servicio);
            ps.setInt(2, id_producto);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ServicioProducto mapRow(ResultSet rs) throws SQLException {
        ServicioProducto sp = new ServicioProducto();
        sp.id_servicio = rs.getInt("id_servicio");
        sp.id_producto = rs.getInt("id_producto");
        sp.cantidad = rs.getInt("cantidad");

        return sp;
    }

    private boolean existsByProductoId(Integer id) {
        String sql = "SELECT 1 FROM servicio_producto WHERE id_producto=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean existsByServicioId(Integer id) {
        String sql = "SELECT 1 FROM servicio_producto WHERE id_servicio=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean productoExists(Integer id_producto) {
        String sql = "SELECT 1 FROM producto WHERE id_producto=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id_producto);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteByPago(int id) {
        String sql = "DELETE FROM pago_producto WHERE id_pago=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ServicioProducto> findByProductoId(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByProductoId'");
    }

}
