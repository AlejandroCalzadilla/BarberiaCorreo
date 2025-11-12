package org.barberia.usuarios.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.barberia.usuarios.infra.Database;

public class ReporteService {
    
    public ReporteService() {
    }
    
    // ==================== REPORTES DE INGRESOS ====================
    
    /**
     * Obtiene los ingresos totales por mes
     */
    public Map<String, Object> getIngresosMensuales(int año, int mes) {
        String sql = """
            SELECT 
                COUNT(DISTINCT r.id_reserva) as total_reservas,
                COUNT(DISTINCT CASE WHEN r.estado = 'completada' THEN r.id_reserva END) as reservas_completadas,
                COALESCE(SUM(CASE WHEN p.estado = 'pagado' THEN p.monto_total ELSE 0 END), 0) as ingresos_totales,
                COALESCE(SUM(CASE WHEN p.tipo_pago = 'anticipo' AND p.estado = 'pagado' THEN p.monto_total ELSE 0 END), 0) as anticipos_recibidos,
                COALESCE(SUM(CASE WHEN p.tipo_pago = 'pago_final' AND p.estado = 'pagado' THEN p.monto_total ELSE 0 END), 0) as pagos_finales,
                COALESCE(SUM(CASE WHEN p.estado = 'pendiente' THEN p.monto_total ELSE 0 END), 0) as pagos_pendientes
            FROM reserva r
            LEFT JOIN pago p ON r.id_reserva = p.id_reserva
            WHERE EXTRACT(YEAR FROM r.fecha_reserva) = ? 
            AND EXTRACT(MONTH FROM r.fecha_reserva) = ?
        """;
        
        Map<String, Object> resultado = new HashMap<>();
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, año);
            stmt.setInt(2, mes);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    resultado.put("año", año);
                    resultado.put("mes", mes);
                    resultado.put("total_reservas", rs.getInt("total_reservas"));
                    resultado.put("reservas_completadas", rs.getInt("reservas_completadas"));
                    resultado.put("ingresos_totales", rs.getBigDecimal("ingresos_totales"));
                    resultado.put("anticipos_recibidos", rs.getBigDecimal("anticipos_recibidos"));
                    resultado.put("pagos_finales", rs.getBigDecimal("pagos_finales"));
                    resultado.put("pagos_pendientes", rs.getBigDecimal("pagos_pendientes"));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener ingresos mensuales: " + e.getMessage());
        }
        
        return resultado;
    }
    
    /**
     * Obtiene el resumen de ingresos de un rango de fechas
     */
    public Map<String, Object> getIngresosPorRango(LocalDate fechaInicio, LocalDate fechaFin) {
        String sql = """
            SELECT 
                COUNT(DISTINCT r.id_reserva) as total_reservas,
                COALESCE(SUM(CASE WHEN p.estado = 'pagado' THEN p.monto_total ELSE 0 END), 0) as ingresos_totales,
                COALESCE(AVG(CASE WHEN p.estado = 'pagado' THEN p.monto_total END), 0) as promedio_por_pago,
                COALESCE(MAX(CASE WHEN p.estado = 'pagado' THEN p.monto_total END), 0) as pago_maximo,
                COALESCE(MIN(CASE WHEN p.estado = 'pagado' THEN p.monto_total END), 0) as pago_minimo
            FROM reserva r
            LEFT JOIN pago p ON r.id_reserva = p.id_reserva
            WHERE r.fecha_reserva BETWEEN ? AND ?
        """;
        
        Map<String, Object> resultado = new HashMap<>();
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setObject(1, fechaInicio);
            stmt.setObject(2, fechaFin);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    resultado.put("fecha_inicio", fechaInicio);
                    resultado.put("fecha_fin", fechaFin);
                    resultado.put("total_reservas", rs.getInt("total_reservas"));
                    resultado.put("ingresos_totales", rs.getBigDecimal("ingresos_totales"));
                    resultado.put("promedio_por_pago", rs.getBigDecimal("promedio_por_pago"));
                    resultado.put("pago_maximo", rs.getBigDecimal("pago_maximo"));
                    resultado.put("pago_minimo", rs.getBigDecimal("pago_minimo"));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener ingresos por rango: " + e.getMessage());
        }
        
        return resultado;
    }
    
    // ==================== REPORTES DE BARBEROS ====================
    
    /**
     * Obtiene el ranking de barberos por cantidad de servicios realizados
     */
    public List<Map<String, Object>> getRankingBarberos(LocalDate fechaInicio, LocalDate fechaFin) {
        String sql = """
            SELECT 
                b.id_barbero,
                u.nombre,
                u.apellido,
                b.especialidad,
                COUNT(DISTINCT r.id_reserva) as total_servicios,
                COUNT(DISTINCT CASE WHEN r.estado = 'completada' THEN r.id_reserva END) as servicios_completados,
                COALESCE(SUM(CASE WHEN r.estado = 'completada' THEN r.total ELSE 0 END), 0) as ingresos_generados,
                COALESCE(AVG(CASE WHEN r.estado = 'completada' THEN r.total END), 0) as promedio_por_servicio
            FROM barbero b
            JOIN usuario u ON b.id_usuario = u.id_usuario
            LEFT JOIN reserva r ON b.id_barbero = r.id_barbero 
                AND r.fecha_reserva BETWEEN ? AND ?
            GROUP BY b.id_barbero, u.nombre, u.apellido, b.especialidad
            ORDER BY servicios_completados DESC, ingresos_generados DESC
        """;
        
        List<Map<String, Object>> ranking = new ArrayList<>();
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setObject(1, fechaInicio);
            stmt.setObject(2, fechaFin);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> barbero = new HashMap<>();
                    barbero.put("id_barbero", rs.getInt("id_barbero"));
                    barbero.put("nombre", rs.getString("nombre"));
                    barbero.put("apellido", rs.getString("apellido"));
                    barbero.put("especialidad", rs.getString("especialidad"));
                    barbero.put("total_servicios", rs.getInt("total_servicios"));
                    barbero.put("servicios_completados", rs.getInt("servicios_completados"));
                    barbero.put("ingresos_generados", rs.getBigDecimal("ingresos_generados"));
                    barbero.put("promedio_por_servicio", rs.getBigDecimal("promedio_por_servicio"));
                    ranking.add(barbero);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener ranking de barberos: " + e.getMessage());
        }
        
        return ranking;
    }
    
    /**
     * Obtiene estadísticas individuales de un barbero
     */
    public Map<String, Object> getEstadisticasBarbero(int idBarbero, LocalDate fechaInicio, LocalDate fechaFin) {
        String sql = """
            SELECT 
                COUNT(DISTINCT r.id_reserva) as total_reservas,
                COUNT(DISTINCT CASE WHEN r.estado = 'completada' THEN r.id_reserva END) as completadas,
                COUNT(DISTINCT CASE WHEN r.estado = 'cancelada' THEN r.id_reserva END) as canceladas,
                COUNT(DISTINCT CASE WHEN r.estado = 'no_asistio' THEN r.id_reserva END) as no_asistio,
                COALESCE(SUM(CASE WHEN r.estado = 'completada' THEN r.total ELSE 0 END), 0) as ingresos_totales,
                COALESCE(AVG(CASE WHEN r.estado = 'completada' THEN r.total END), 0) as promedio_por_servicio,
                COUNT(DISTINCT r.id_cliente) as clientes_unicos
            FROM reserva r
            WHERE r.id_barbero = ?
            AND r.fecha_reserva BETWEEN ? AND ?
        """;
        
        Map<String, Object> stats = new HashMap<>();
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idBarbero);
            stmt.setObject(2, fechaInicio);
            stmt.setObject(3, fechaFin);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("id_barbero", idBarbero);
                    stats.put("total_reservas", rs.getInt("total_reservas"));
                    stats.put("completadas", rs.getInt("completadas"));
                    stats.put("canceladas", rs.getInt("canceladas"));
                    stats.put("no_asistio", rs.getInt("no_asistio"));
                    stats.put("ingresos_totales", rs.getBigDecimal("ingresos_totales"));
                    stats.put("promedio_por_servicio", rs.getBigDecimal("promedio_por_servicio"));
                    stats.put("clientes_unicos", rs.getInt("clientes_unicos"));
                    
                    int total = rs.getInt("total_reservas");
                    if (total > 0) {
                        double tasaCompletadas = (rs.getInt("completadas") * 100.0) / total;
                        stats.put("tasa_completadas", String.format("%.2f%%", tasaCompletadas));
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener estadísticas de barbero: " + e.getMessage());
        }
        
        return stats;
    }
    
    // ==================== REPORTES DE SERVICIOS ====================
    
    /**
     * Obtiene los servicios más solicitados
     */
    public List<Map<String, Object>> getServiciosMasPopulares(LocalDate fechaInicio, LocalDate fechaFin, int limit) {
        
        String sql = """
            SELECT 
                s.id_servicio,
                s.nombre,
                s.precio,
                s.duracion_minutos_aprox,
                COUNT(r.id_reserva) as veces_solicitado,
                COUNT(CASE WHEN r.estado = 'completada' THEN 1 END) as veces_completado,
                COALESCE(SUM(CASE WHEN r.estado = 'completada' THEN r.total ELSE 0 END), 0) as ingresos_generados
            FROM servicio s
            LEFT JOIN reserva r ON s.id_servicio = r.id_servicio
                AND r.fecha_reserva BETWEEN ? AND ?
            WHERE s.estado = 'activo'
            GROUP BY s.id_servicio, s.nombre, s.precio, s.duracion_minutos_aprox
            ORDER BY veces_completado DESC, ingresos_generados DESC
            LIMIT ?
        """;
        
        List<Map<String, Object>> servicios = new ArrayList<>();
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setObject(1, fechaInicio);
            stmt.setObject(2, fechaFin);
            stmt.setInt(3, limit);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> servicio = new HashMap<>();
                    servicio.put("id_servicio", rs.getInt("id_servicio"));
                    servicio.put("nombre", rs.getString("nombre"));
                    servicio.put("precio", rs.getBigDecimal("precio"));
                    servicio.put("duracion_minutos_aprox", rs.getInt("duracion_minutos_aprox"));
                    servicio.put("veces_solicitado", rs.getInt("veces_solicitado"));
                    servicio.put("veces_completado", rs.getInt("veces_completado"));
                    servicio.put("ingresos_generados", rs.getBigDecimal("ingresos_generados"));
                    servicios.add(servicio);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener servicios populares: " + e.getMessage());
        }
        
        return servicios;
    }
    
    // ==================== REPORTES DE CLIENTES ====================
    
    /**
     * Obtiene los clientes más frecuentes
     */
    public List<Map<String, Object>> getClientesFrecuentes(LocalDate fechaInicio, LocalDate fechaFin, int limit) {
        String sql = """
            SELECT 
                c.id_cliente,
                u.nombre,
                u.apellido,
                u.email,
                u.telefono,
                COUNT(r.id_reserva) as total_reservas,
                COUNT(CASE WHEN r.estado = 'completada' THEN 1 END) as reservas_completadas,
                COALESCE(SUM(CASE WHEN r.estado = 'completada' THEN r.total ELSE 0 END), 0) as gasto_total,
                COALESCE(AVG(CASE WHEN r.estado = 'completada' THEN r.total END), 0) as promedio_por_visita,
                MAX(r.fecha_reserva) as ultima_visita
            FROM cliente c
            JOIN usuario u ON c.id_usuario = u.id_usuario
            LEFT JOIN reserva r ON c.id_cliente = r.id_cliente
                AND r.fecha_reserva BETWEEN ? AND ?
            GROUP BY c.id_cliente, u.nombre, u.apellido, u.email, u.telefono
            HAVING COUNT(r.id_reserva) > 0
            ORDER BY reservas_completadas DESC, gasto_total DESC
            LIMIT ?
        """;
        
        List<Map<String, Object>> clientes = new ArrayList<>();
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setObject(1, fechaInicio);
            stmt.setObject(2, fechaFin);
            stmt.setInt(3, limit);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> cliente = new HashMap<>();
                    cliente.put("id_cliente", rs.getInt("id_cliente"));
                    cliente.put("nombre", rs.getString("nombre"));
                    cliente.put("apellido", rs.getString("apellido"));
                    cliente.put("email", rs.getString("email"));
                    cliente.put("telefono", rs.getString("telefono"));
                    cliente.put("total_reservas", rs.getInt("total_reservas"));
                    cliente.put("reservas_completadas", rs.getInt("reservas_completadas"));
                    cliente.put("gasto_total", rs.getBigDecimal("gasto_total"));
                    cliente.put("promedio_por_visita", rs.getBigDecimal("promedio_por_visita"));
                    cliente.put("ultima_visita", rs.getDate("ultima_visita"));
                    clientes.add(cliente);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener clientes frecuentes: " + e.getMessage());
        }
        
        return clientes;
    }
    
    // ==================== REPORTES DE ESTADOS ====================
    
    /**
     * Obtiene distribución de estados de reservas
     */
    public Map<String, Object> getDistribucionEstados(LocalDate fechaInicio, LocalDate fechaFin) {
        String sql = """
            SELECT 
                COUNT(*) as total_reservas,
                COUNT(CASE WHEN estado = 'completada' THEN 1 END) as completadas,
                COUNT(CASE WHEN estado = 'cancelada' THEN 1 END) as canceladas,
                COUNT(CASE WHEN estado = 'no_asistio' THEN 1 END) as no_asistio,
                COUNT(CASE WHEN estado = 'confirmada' THEN 1 END) as confirmadas,
                COUNT(CASE WHEN estado = 'en_proceso' THEN 1 END) as en_proceso
            FROM reserva
            WHERE fecha_reserva BETWEEN ? AND ?
        """;
        
        Map<String, Object> distribucion = new HashMap<>();
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setObject(1, fechaInicio);
            stmt.setObject(2, fechaFin);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int total = rs.getInt("total_reservas");
                    distribucion.put("total_reservas", total);
                    distribucion.put("completadas", rs.getInt("completadas"));
                    distribucion.put("canceladas", rs.getInt("canceladas"));
                    distribucion.put("no_asistio", rs.getInt("no_asistio"));
                    distribucion.put("confirmadas", rs.getInt("confirmadas"));
                    distribucion.put("en_proceso", rs.getInt("en_proceso"));
                    
                    if (total > 0) {
                        distribucion.put("porcentaje_completadas", String.format("%.2f%%", (rs.getInt("completadas") * 100.0) / total));
                        distribucion.put("porcentaje_canceladas", String.format("%.2f%%", (rs.getInt("canceladas") * 100.0) / total));
                        distribucion.put("porcentaje_no_asistio", String.format("%.2f%%", (rs.getInt("no_asistio") * 100.0) / total));
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener distribución de estados: " + e.getMessage());
        }
        
        return distribucion;
    }
    
    // ==================== REPORTES DE PRODUCTOS ====================
    
    /**
     * Obtiene el consumo de productos desechables
     */
    public List<Map<String, Object>> getConsumoProductos(LocalDate fechaInicio, LocalDate fechaFin) {
        String sql = """
            SELECT 
                p.id_producto,
                p.codigo,
                p.nombre,
                p.stock_actual,
                c.nombre as categoria,
                COALESCE(SUM(sp.cantidad), 0) as cantidad_usada,
                COUNT(DISTINCT r.id_reserva) as servicios_realizados
            FROM producto p
            JOIN categoria c ON p.id_categoria = c.id_categoria
            LEFT JOIN servicio_producto sp ON p.id_producto = sp.id_producto
            LEFT JOIN servicio s ON sp.id_servicio = s.id_servicio
            LEFT JOIN reserva r ON s.id_servicio = r.id_servicio 
                AND r.fecha_reserva BETWEEN ? AND ?
                AND r.estado = 'completada'
            WHERE p.estado = 'activo'
            GROUP BY p.id_producto, p.codigo, p.nombre, p.stock_actual, c.nombre
            ORDER BY cantidad_usada DESC
        """;
        
        List<Map<String, Object>> productos = new ArrayList<>();
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setObject(1, fechaInicio);
            stmt.setObject(2, fechaFin);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> producto = new HashMap<>();
                    producto.put("id_producto", rs.getInt("id_producto"));
                    producto.put("codigo", rs.getString("codigo"));
                    producto.put("nombre", rs.getString("nombre"));
                    producto.put("stock_actual", rs.getInt("stock_actual"));
                    producto.put("categoria", rs.getString("categoria"));
                    producto.put("cantidad_usada", rs.getInt("cantidad_usada"));
                    producto.put("servicios_realizados", rs.getInt("servicios_realizados"));
                    
                    int stockActual = rs.getInt("stock_actual");
                    int cantidadUsada = rs.getInt("cantidad_usada");
                    if (cantidadUsada > 0 && stockActual < cantidadUsada * 2) {
                        producto.put("alerta_stock", "BAJO");
                    }
                    
                    productos.add(producto);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener consumo de productos: " + e.getMessage());
        }
        
        return productos;
    }
    
    // ==================== REPORTES DE HORARIOS ====================
    
    /**
     * Obtiene las horas pico (más reservas)
     */
    public List<Map<String, Object>> getHorasPico(LocalDate fechaInicio, LocalDate fechaFin) {
        String sql = """
            SELECT 
                EXTRACT(HOUR FROM hora_inicio) as hora,
                COUNT(*) as total_reservas,
                COUNT(CASE WHEN estado = 'completada' THEN 1 END) as completadas,
                COALESCE(SUM(CASE WHEN estado = 'completada' THEN total ELSE 0 END), 0) as ingresos
            FROM reserva
            WHERE fecha_reserva BETWEEN ? AND ?
            GROUP BY EXTRACT(HOUR FROM hora_inicio)
            ORDER BY total_reservas DESC
        """;
        
        List<Map<String, Object>> horas = new ArrayList<>();
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setObject(1, fechaInicio);
            stmt.setObject(2, fechaFin);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> hora = new HashMap<>();
                    int horaNum = rs.getInt("hora");
                    hora.put("hora", String.format("%02d:00", horaNum));
                    hora.put("total_reservas", rs.getInt("total_reservas"));
                    hora.put("completadas", rs.getInt("completadas"));
                    hora.put("ingresos", rs.getBigDecimal("ingresos"));
                    horas.add(hora);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener horas pico: " + e.getMessage());
        }
        
        return horas;
    }
    
    /**
     * Obtiene los días de la semana más ocupados
     */
    public List<Map<String, Object>> getDiasMasOcupados(LocalDate fechaInicio, LocalDate fechaFin) {
        String sql = """
            SELECT 
                EXTRACT(DOW FROM fecha_reserva) as dia_numero,
                CASE EXTRACT(DOW FROM fecha_reserva)
                    WHEN 0 THEN 'Domingo'
                    WHEN 1 THEN 'Lunes'
                    WHEN 2 THEN 'Martes'
                    WHEN 3 THEN 'Miércoles'
                    WHEN 4 THEN 'Jueves'
                    WHEN 5 THEN 'Viernes'
                    WHEN 6 THEN 'Sábado'
                END as dia_semana,
                COUNT(*) as total_reservas,
                COUNT(CASE WHEN estado = 'completada' THEN 1 END) as completadas,
                COALESCE(SUM(CASE WHEN estado = 'completada' THEN total ELSE 0 END), 0) as ingresos
            FROM reserva
            WHERE fecha_reserva BETWEEN ? AND ?
            GROUP BY EXTRACT(DOW FROM fecha_reserva)
            ORDER BY total_reservas DESC
        """;
        
        List<Map<String, Object>> dias = new ArrayList<>();
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setObject(1, fechaInicio);
            stmt.setObject(2, fechaFin);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> dia = new HashMap<>();
                    dia.put("dia_numero", rs.getInt("dia_numero"));
                    dia.put("dia_semana", rs.getString("dia_semana"));
                    dia.put("total_reservas", rs.getInt("total_reservas"));
                    dia.put("completadas", rs.getInt("completadas"));
                    dia.put("ingresos", rs.getBigDecimal("ingresos"));
                    dias.add(dia);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener días más ocupados: " + e.getMessage());
        }
        
        return dias;
    }
    
    // ==================== REPORTES DE PAGOS ====================
    
    /**
     * Obtiene distribución de métodos de pago
     */
    public List<Map<String, Object>> getDistribucionMetodosPago(LocalDate fechaInicio, LocalDate fechaFin) {
        String sql = """
            SELECT 
                p.metodo_pago,
                COUNT(*) as cantidad_pagos,
                COALESCE(SUM(p.monto_total), 0) as monto_total,
                COALESCE(AVG(p.monto_total), 0) as promedio_por_pago
            FROM pago p
            JOIN reserva r ON p.id_reserva = r.id_reserva
            WHERE r.fecha_reserva BETWEEN ? AND ?
            AND p.estado = 'pagado'
            GROUP BY p.metodo_pago
            ORDER BY monto_total DESC
        """;
        
        List<Map<String, Object>> metodos = new ArrayList<>();
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setObject(1, fechaInicio);
            stmt.setObject(2, fechaFin);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> metodo = new HashMap<>();
                    metodo.put("metodo_pago", rs.getString("metodo_pago"));
                    metodo.put("cantidad_pagos", rs.getInt("cantidad_pagos"));
                    metodo.put("monto_total", rs.getBigDecimal("monto_total"));
                    metodo.put("promedio_por_pago", rs.getBigDecimal("promedio_por_pago"));
                    metodos.add(metodo);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener distribución de métodos de pago: " + e.getMessage());
        }
        
        return metodos;
    }
    
    // ==================== DASHBOARD GENERAL ====================
    
    /**
     * Obtiene un dashboard completo con métricas generales
     */
    public Map<String, Object> getDashboardGeneral(YearMonth mes) {
        LocalDate inicio = mes.atDay(1);
        LocalDate fin = mes.atEndOfMonth();
        
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("periodo", mes.toString());
        dashboard.put("ingresos", getIngresosMensuales(mes.getYear(), mes.getMonthValue()));
        dashboard.put("top_barberos", getRankingBarberos(inicio, fin));
        dashboard.put("servicios_populares", getServiciosMasPopulares(inicio, fin, 5));
        dashboard.put("clientes_frecuentes", getClientesFrecuentes(inicio, fin, 10));
        dashboard.put("distribucion_estados", getDistribucionEstados(inicio, fin));
        dashboard.put("metodos_pago", getDistribucionMetodosPago(inicio, fin));
        dashboard.put("horas_pico", getHorasPico(inicio, fin));
        dashboard.put("dias_ocupados", getDiasMasOcupados(inicio, fin));
        
        return dashboard;
    }
}
