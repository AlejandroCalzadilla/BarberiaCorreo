package org.barberia.usuarios.repository.implentations;

import org.barberia.usuarios.domain.enums.*;
import org.barberia.usuarios.domain.Producto;
import org.barberia.usuarios.infra.Database;
import org.barberia.usuarios.repository.ProductoRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcProductoRepository implements ProductoRepository {
    @Override
    public List<Producto> findAll() {
        String sql = "SELECT id_producto, id_categoria, codigo, nombre, descripcion, precio_compra, stock_actual, stock_minimo, unidad_medida, estado, imagenurl, created_at, updated_at ,precio_venta FROM producto ORDER BY id_producto";
        List<Producto> list = new ArrayList<>();
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
    public Optional<Producto> findById(Integer id) {
        String sql = "SELECT id_producto, id_categoria, codigo, nombre, descripcion, precio_compra, stock_actual, stock_minimo, unidad_medida, estado, imagenurl, created_at, updated_at, precio_venta FROM producto WHERE id_producto=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Producto save(Producto p) {
        if (p.id_producto == null)
            return insert(p);
        return update(p);
    }

    private Producto insert(Producto p) {
        String sql = "INSERT INTO producto (id_categoria, codigo, nombre, descripcion, precio_compra, stock_actual, stock_minimo, unidad_medida, estado, imagenurl, precio_venta) VALUES (?,?,?,?,?,?,?,?,?::estado_item,?,?) RETURNING id_producto, created_at, updated_at";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.id_categoria);
            ps.setString(2, p.codigo);
            ps.setString(3, p.nombre);
            ps.setString(4, p.descripcion);
            ps.setBigDecimal(5, p.precio_compra);
            ps.setInt(6, p.stock_actual);
            ps.setInt(7, p.stock_minimo);
            ps.setString(8, p.unidad_medida);
            ps.setString(9, p.estado != null ? p.estado.name() : EstadoItem.activo.name());
            ps.setString(10, p.imagenurl);
            ps.setBigDecimal(11, p.precio_venta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p.id_producto = rs.getInt("id_producto");
                    Timestamp cr = rs.getTimestamp("created_at");
                    Timestamp up = rs.getTimestamp("updated_at");
                    p.created_at = cr != null ? cr.toLocalDateTime() : null;
                    p.updated_at = up != null ? up.toLocalDateTime() : null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return p;
    }

    private Producto update(Producto p) {
        String sql = "UPDATE producto SET id_categoria=?, codigo=?, nombre=?, descripcion=?, precio_compra=?, stock_actual=?, stock_minimo=?, unidad_medida=?, estado=?::estado_item, imagenurl=?, precio_venta=?, updated_at=now() WHERE id_producto=? RETURNING created_at, updated_at, estado";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.id_categoria);
            ps.setString(2, p.codigo);
            ps.setString(3, p.nombre);
            ps.setString(4, p.descripcion);
            ps.setBigDecimal(5, p.precio_compra);
            ps.setInt(6, p.stock_actual);
            ps.setInt(7, p.stock_minimo);
            ps.setString(8, p.unidad_medida);
            ps.setString(9, p.estado != null ? p.estado.name() : EstadoItem.activo.name());
            ps.setString(10, p.imagenurl);
            ps.setBigDecimal(11, p.precio_venta);
            ps.setInt(12, p.id_producto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Timestamp cr = rs.getTimestamp("created_at");
                    Timestamp up = rs.getTimestamp("updated_at");
                    p.created_at = cr != null ? cr.toLocalDateTime() : null;
                    p.updated_at = up != null ? up.toLocalDateTime() : null;
                    String estado = rs.getString("estado");
                    if (estado != null)
                        p.estado = EstadoItem.valueOf(estado);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return p;
    }

    /*
     * @Override
     * public void deleteById(Integer id) {
     * String sql = "DELETE FROM producto WHERE id_producto=?";
     * try (Connection con = Database.getConnection(); PreparedStatement ps =
     * con.prepareStatement(sql)) {
     * ps.setInt(1, id);
     * ps.executeUpdate();
     * } catch (SQLException e) { throw new RuntimeException(e); }
     * }
     */
    @Override
    public void activateById(Integer id) {
        String sql = "UPDATE producto SET estado='activo'::estado_item, updated_at=now() WHERE id_producto=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { 
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "UPDATE producto SET estado='inactivo'::estado_item, updated_at=now() WHERE id_producto=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { 
            throw new RuntimeException(e);
        }
    }

    private Producto mapRow(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.id_producto = rs.getInt("id_producto");
        p.id_categoria = rs.getInt("id_categoria");
        p.codigo = rs.getString("codigo");
        p.nombre = rs.getString("nombre");
        p.descripcion = rs.getString("descripcion");
        p.precio_compra = rs.getBigDecimal("precio_compra");
        p.stock_actual = rs.getInt("stock_actual");
        p.stock_minimo = rs.getInt("stock_minimo");
        p.unidad_medida = rs.getString("unidad_medida");
        p.precio_venta = rs.getBigDecimal("precio_venta");
        String est = rs.getString("estado");
        if (est != null)
            p.estado = EstadoItem.valueOf(est);
        p.imagenurl = rs.getString("imagenurl");
        Timestamp cr = rs.getTimestamp("created_at");
        Timestamp up = rs.getTimestamp("updated_at");
        p.created_at = cr != null ? cr.toLocalDateTime() : null;
        p.updated_at = up != null ? up.toLocalDateTime() : null;
        return p;
    }
}
