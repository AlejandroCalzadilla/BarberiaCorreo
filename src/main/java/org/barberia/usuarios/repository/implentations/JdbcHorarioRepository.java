package org.barberia.usuarios.repository.implentations;

import org.barberia.usuarios.domain.enums.*;
import org.barberia.usuarios.domain.Horario;
import org.barberia.usuarios.infra.Database;
import org.barberia.usuarios.repository.HorarioRepository;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JdbcHorarioRepository implements HorarioRepository {
    @Override
    public List<Horario> findAll() {
        String sql = "SELECT id_horario, id_barbero, dia_semana, hora_inicio, hora_fin, estado, created_at, updated_at FROM horario ORDER BY id_horario";
        List<Horario> list = new ArrayList<>();
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
    public Optional<Horario> findById(Integer id) {
        String sql = "SELECT id_horario, id_barbero, dia_semana, hora_inicio, hora_fin, estado, created_at, updated_at FROM horario WHERE id_horario=?";
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
    public Horario save(Horario h) {
        if (h.id_horario == null)
            return insert(h);
        return update(h);
    }

    private Horario insert(Horario h) {
        String sql = "INSERT INTO horario (id_barbero, dia_semana, hora_inicio, hora_fin, estado) VALUES (?,?::dia_semana,?, ?, ?::estado_horario) RETURNING id_horario, created_at, updated_at";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, h.id_barbero);
            ps.setString(2, h.dia_semana != null ? h.dia_semana.name() : DiaSemana.lunes.name());
            ps.setTime(3, h.hora_inicio != null ? Time.valueOf(h.hora_inicio) : null);
            ps.setTime(4, h.hora_fin != null ? Time.valueOf(h.hora_fin) : null);
            ps.setString(5, h.estado != null ? h.estado.name() : EstadoHorario.activo.name());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    h.id_horario = rs.getInt("id_horario");
                    Timestamp cr = rs.getTimestamp("created_at");
                    Timestamp up = rs.getTimestamp("updated_at");
                    h.created_at = cr != null ? cr.toLocalDateTime() : null;
                    h.updated_at = up != null ? up.toLocalDateTime() : null;
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate key value violates unique constraint")) {
                throw new IllegalArgumentException(extraerMensajeDuplicado(e.getMessage()));
            } else {
                throw new IllegalArgumentException("Error al crear el horario: " + e.getMessage(), e);
            }
        }
        return h;
    }

    private Horario update(Horario h) {
        String sql = "UPDATE horario SET id_barbero=?, dia_semana=?::dia_semana, hora_inicio=?, hora_fin=?, estado=?::estado_horario, updated_at=now() WHERE id_horario=? RETURNING created_at, updated_at, estado";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, h.id_barbero);
            ps.setString(2, h.dia_semana != null ? h.dia_semana.name() : DiaSemana.lunes.name());
            ps.setTime(3, h.hora_inicio != null ? Time.valueOf(h.hora_inicio) : null);
            ps.setTime(4, h.hora_fin != null ? Time.valueOf(h.hora_fin) : null);
            ps.setString(5, h.estado != null ? h.estado.name() : EstadoHorario.activo.name());
            ps.setInt(6, h.id_horario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Timestamp cr = rs.getTimestamp("created_at");
                    Timestamp up = rs.getTimestamp("updated_at");
                    h.created_at = cr != null ? cr.toLocalDateTime() : null;
                    h.updated_at = up != null ? up.toLocalDateTime() : null;
                    String estado = rs.getString("estado");
                    if (estado != null) h.estado = EstadoHorario.valueOf(estado);
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate key value violates unique constraint")) {
                throw new IllegalArgumentException(extraerMensajeDuplicado(e.getMessage()));
            } else {
                throw new IllegalArgumentException("Error al crear el horario: " + e.getMessage(), e);
            }

        }
        return h;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM horario WHERE id_horario=?";
        try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Horario mapRow(ResultSet rs) throws SQLException {
        Horario h = new Horario();
        h.id_horario = rs.getInt("id_horario");
        h.id_barbero = rs.getInt("id_barbero");
        String dia = rs.getString("dia_semana");
        if (dia != null)
            h.dia_semana = DiaSemana.valueOf(dia);
        Time hi = rs.getTime("hora_inicio");
        h.hora_inicio = hi != null ? hi.toLocalTime() : null;
        Time hf = rs.getTime("hora_fin");
        h.hora_fin = hf != null ? hf.toLocalTime() : null;
        String est = rs.getString("estado");
        if (est != null)
            h.estado = EstadoHorario.valueOf(est);
        Timestamp cr = rs.getTimestamp("created_at");
        Timestamp up = rs.getTimestamp("updated_at");
        h.created_at = cr != null ? cr.toLocalDateTime() : null;
        h.updated_at = up != null ? up.toLocalDateTime() : null;
        return h;
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
