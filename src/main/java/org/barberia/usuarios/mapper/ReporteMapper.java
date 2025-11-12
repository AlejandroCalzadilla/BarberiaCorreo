package org.barberia.usuarios.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.barberia.usuarios.utils.ChartGenerator;

public class ReporteMapper {
    
    /**
     * Formatea el reporte de ingresos mensuales
     */
    public static String formatIngresosMensuales(Map<String, Object> ingresos) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n╔══════════════════════════════════════════════════════════════════╗\n");
        sb.append("║          REPORTE DE INGRESOS MENSUALES                           ║\n");
        sb.append("╠══════════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ Período: %s/%s                                                 ║\n", 
            ingresos.get("mes"), ingresos.get("año")));
        sb.append("╠══════════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ Total Reservas:           %5d                                  ║\n", 
            ingresos.get("total_reservas")));
        sb.append(String.format("║ Reservas Completadas:     %5d                                  ║\n", 
            ingresos.get("reservas_completadas")));
        sb.append("║                                                                  ║\n");
        sb.append(String.format("║ Ingresos Totales:       $%10.2f                              ║\n", 
            ingresos.get("ingresos_totales")));
        sb.append(String.format("║ Anticipos Recibidos:    $%10.2f                              ║\n", 
            ingresos.get("anticipos_recibidos")));
        sb.append(String.format("║ Pagos Finales:          $%10.2f                              ║\n", 
            ingresos.get("pagos_finales")));
        sb.append(String.format("║ Pagos Pendientes:       $%10.2f                              ║\n", 
            ingresos.get("pagos_pendientes")));
        sb.append("╚══════════════════════════════════════════════════════════════════╝\n");
        
        return sb.toString();
    }
    
    /**
     * Formatea el ranking de barberos
     */
    public static String formatRankingBarberos(List<Map<String, Object>> ranking) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n╔═══════════════════════════════════════════════════════════════════════════════════╗\n");
        sb.append("║                          RANKING DE BARBEROS                                      ║\n");
        sb.append("╠═══╦══════════════════════════╦════════════════╦══════════╦════════════╦═══════════╣\n");
        sb.append("║ID ║ Nombre                   ║ Especialidad   ║ Servs.   ║ Completad. ║ Ingresos  ║\n");
        sb.append("╠═══╬══════════════════════════╬════════════════╬══════════╬════════════╬═══════════╣\n");
        
        for (Map<String, Object> barbero : ranking) {
            String nombreCompleto = String.format("%s %s", 
                barbero.get("nombre"), 
                barbero.get("apellido"));
            
            sb.append(String.format("║%-3d║ %-24s ║ %-14s ║ %8d ║ %10d ║$%10.2f║\n",
                barbero.get("id_barbero"),
                truncate(nombreCompleto, 24),
                truncate(barbero.get("especialidad").toString(), 14),
                barbero.get("total_servicios"),
                barbero.get("servicios_completados"),
                barbero.get("ingresos_generados")));
        }
        
        sb.append("╚═══╩══════════════════════════╩════════════════╩══════════╩════════════╩═══════════╝\n");
        
        // Agregar gráfico de barras de ingresos
        if (!ranking.isEmpty()) {
            sb.append("\n📊 GRÁFICO DE INGRESOS POR BARBERO:\n\n");
            List<Map<String, Object>> chartData = new ArrayList<>();
            for (Map<String, Object> barbero : ranking) {
                Map<String, Object> item = new HashMap<>();
                String nombre = truncate(barbero.get("nombre").toString(), 15);
                item.put("label", nombre);
                item.put("value", barbero.get("ingresos_generados"));
                chartData.add(item);
            }
            sb.append(ChartGenerator.horizontalBarChart(chartData, 40, true));
        }
        
        return sb.toString();
    }
    
    /**
     * Formatea el reporte de servicios populares
     */
    public static String formatServiciosPopulares(List<Map<String, Object>> servicios) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n╔════════════════════════════════════════════════════════════════════════════════╗\n");
        sb.append("║                      SERVICIOS MÁS POPULARES                                   ║\n");
        sb.append("╠═══╦═══════════════════════════╦══════════╦══════════╦══════════╦══════════════╣\n");
        sb.append("║ID ║ Nombre                    ║ Precio   ║ Solicit. ║ Complet. ║ Ingresos     ║\n");
        sb.append("╠═══╬═══════════════════════════╬══════════╬══════════╬══════════╬══════════════╣\n");
        
        for (Map<String, Object> servicio : servicios) {
            sb.append(String.format("║%-3d║ %-25s ║ $%6.2f  ║ %8d ║ %8d ║ $%10.2f  ║\n",
                servicio.get("id_servicio"),
                truncate(servicio.get("nombre").toString(), 25),
                servicio.get("precio"),
                servicio.get("veces_solicitado"),
                servicio.get("veces_completado"),
                servicio.get("ingresos_generados")));
        }
        
        sb.append("╚═══╩═══════════════════════════╩══════════╩══════════╩══════════╩══════════════╝\n");
        
        // Agregar gráfico de popularidad
        if (!servicios.isEmpty()) {
            sb.append("\n📊 GRÁFICO DE POPULARIDAD (veces completado):\n\n");
            List<Map<String, Object>> chartData = new ArrayList<>();
            for (Map<String, Object> servicio : servicios) {
                Map<String, Object> item = new HashMap<>();
                item.put("label", truncate(servicio.get("nombre").toString(), 20));
                item.put("value", servicio.get("veces_completado"));
                chartData.add(item);
            }
            sb.append(ChartGenerator.horizontalBarChart(chartData, 35, true));
        }
        
        return sb.toString();
    }
    
    /**
     * Formatea el reporte de clientes frecuentes
     */
    public static String formatClientesFrecuentes(List<Map<String, Object>> clientes) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n╔══════════════════════════════════════════════════════════════════════════════════════╗\n");
        sb.append("║                         CLIENTES MÁS FRECUENTES                                      ║\n");
        sb.append("╠═══╦═══════════════════════════╦══════════╦════════════╦═══════════════╦══════════════╣\n");
        sb.append("║ID ║ Nombre                    ║ Total    ║ Completad. ║ Gasto Total   ║ Última Vis.  ║\n");
        sb.append("╠═══╬═══════════════════════════╬══════════╬════════════╬═══════════════╬══════════════╣\n");
        
        for (Map<String, Object> cliente : clientes) {
            String nombreCompleto = String.format("%s %s", 
                cliente.get("nombre"), 
                cliente.get("apellido"));
                
            sb.append(String.format("║%-3d║ %-25s ║ %8d ║ %10d ║ $%11.2f  ║ %-12s ║\n",
                cliente.get("id_cliente"),
                truncate(nombreCompleto, 25),
                cliente.get("total_reservas"),
                cliente.get("reservas_completadas"),
                cliente.get("gasto_total"),
                truncate(cliente.get("ultima_visita").toString(), 12)));
        }
        
        sb.append("╚═══╩═══════════════════════════╩══════════╩════════════╩═══════════════╩══════════════╝\n");
        
        return sb.toString();
    }
    
    /**
     * Formatea la distribución de estados
     */
    public static String formatDistribucionEstados(Map<String, Object> distribucion) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n╔══════════════════════════════════════════════════════════════╗\n");
        sb.append("║        DISTRIBUCIÓN DE ESTADOS DE RESERVAS                   ║\n");
        sb.append("╠══════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ Total de Reservas:        %6d                             ║\n", 
            distribucion.get("total_reservas")));
        sb.append("║                                                              ║\n");
        sb.append(String.format("║ Completadas:              %6d   (%5s)                  ║\n", 
            distribucion.get("completadas"),
            distribucion.getOrDefault("porcentaje_completadas", "0.00%")));
        sb.append(String.format("║ Canceladas:               %6d   (%5s)                  ║\n", 
            distribucion.get("canceladas"),
            distribucion.getOrDefault("porcentaje_canceladas", "0.00%")));
        sb.append(String.format("║ No Asistió:               %6d   (%5s)                  ║\n", 
            distribucion.get("no_asistio"),
            distribucion.getOrDefault("porcentaje_no_asistio", "0.00%")));
        sb.append(String.format("║ Confirmadas:              %6d                             ║\n", 
            distribucion.get("confirmadas"))); 
        sb.append(String.format("║ En Proceso:               %6d                             ║\n", 
            distribucion.get("en_proceso")));
        sb.append("╚══════════════════════════════════════════════════════════════╝\n");
        
        // Agregar gráfico circular
        sb.append("\n📊 GRÁFICO DE DISTRIBUCIÓN:\n");
        List<Map<String, Object>> chartData = new ArrayList<>();
        
        Map<String, Object> item1 = new HashMap<>();
        item1.put("label", "Completadas");
        item1.put("value", distribucion.get("completadas"));
        chartData.add(item1);
        
        Map<String, Object> item2 = new HashMap<>();
        item2.put("label", "Canceladas");
        item2.put("value", distribucion.get("canceladas"));
        chartData.add(item2);
        
        Map<String, Object> item3 = new HashMap<>();
        item3.put("label", "No Asistió");
        item3.put("value", distribucion.get("no_asistio"));
        chartData.add(item3);
        
        Map<String, Object> item4 = new HashMap<>();
        item4.put("label", "Confirmadas");
        item4.put("value", distribucion.get("confirmadas"));
        chartData.add(item4);
        
        Map<String, Object> item5 = new HashMap<>();
        item5.put("label", "En Proceso");
        item5.put("value", distribucion.get("en_proceso"));
        chartData.add(item5);
        
        sb.append(ChartGenerator.pieChart(chartData));
        
        return sb.toString();
    }
    
    /**
     * Formatea el reporte de horas pico
     */
    public static String formatHorasPico(List<Map<String, Object>> horas) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n╔═══════════════════════════════════════════════════════════╗\n");
        sb.append("║                    HORAS PICO                             ║\n");
        sb.append("╠══════════╦════════════╦════════════╦══════════════════════╣\n");
        sb.append("║ Hora     ║ Total Res. ║ Completad. ║ Ingresos             ║\n");
        sb.append("╠══════════╬════════════╬════════════╬══════════════════════╣\n");
        
        for (Map<String, Object> hora : horas) {
            sb.append(String.format("║ %-8s ║ %10d ║ %10d ║ $%19.2f ║\n",
                hora.get("hora"),
                hora.get("total_reservas"),
                hora.get("completadas"),
                hora.get("ingresos")));
        }
        
        sb.append("╚══════════╩════════════╩════════════╩══════════════════════╝\n");
        
        // Agregar histograma de actividad
        if (!horas.isEmpty()) {
            sb.append("\n📊 HISTOGRAMA DE ACTIVIDAD POR HORA:\n\n");
            List<Map<String, Object>> chartData = new ArrayList<>();
            for (Map<String, Object> hora : horas) {
                Map<String, Object> item = new HashMap<>();
                item.put("label", hora.get("hora").toString().substring(0, 2));
                item.put("value", hora.get("total_reservas"));
                chartData.add(item);
            }
            sb.append(ChartGenerator.histogram(chartData, 10));
        }
        
        return sb.toString();
    }
    
    /**
     * Formatea el reporte de días más ocupados
     */
    public static String formatDiasMasOcupados(List<Map<String, Object>> dias) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n╔═══════════════════════════════════════════════════════════════════╗\n");
        sb.append("║              DÍAS DE LA SEMANA MÁS OCUPADOS                       ║\n");
        sb.append("╠═══════════════╦════════════╦════════════╦═════════════════════════╣\n");
        sb.append("║ Día Semana    ║ Total Res. ║ Completad. ║ Ingresos                ║\n");
        sb.append("╠═══════════════╬════════════╬════════════╬═════════════════════════╣\n");
        
        for (Map<String, Object> dia : dias) {
            sb.append(String.format("║ %-13s ║ %10d ║ %10d ║ $%21.2f  ║\n",
                dia.get("dia_semana"),
                dia.get("total_reservas"),
                dia.get("completadas"),
                dia.get("ingresos")));
        }
        
        sb.append("╚═══════════════╩════════════╩════════════╩═════════════════════════╝\n");
        
        return sb.toString();
    }
    
    /**
     * Formatea el reporte de métodos de pago
     */
    public static String formatMetodosPago(List<Map<String, Object>> metodos) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n╔═════════════════════════════════════════════════════════════════╗\n");
        sb.append("║          DISTRIBUCIÓN DE MÉTODOS DE PAGO                        ║\n");
        sb.append("╠══════════════════╦══════════════╦═══════════════╦═══════════════╣\n");
        sb.append("║ Método Pago      ║ Cant. Pagos  ║ Monto Total   ║ Promedio      ║\n");
        sb.append("╠══════════════════╬══════════════╬═══════════════╬═══════════════╣\n");
        
        for (Map<String, Object> metodo : metodos) {
            sb.append(String.format("║ %-16s ║ %12d ║ $%11.2f ║ $%11.2f   ║\n",
                metodo.get("metodo_pago"),
                metodo.get("cantidad_pagos"),
                metodo.get("monto_total"),
                metodo.get("promedio_por_pago")));
        }
        
        sb.append("╚══════════════════╩══════════════╩═══════════════╩═══════════════╝\n");
        
        // Agregar gráfico circular de distribución
        if (!metodos.isEmpty()) {
            sb.append("\n📊 GRÁFICO DE DISTRIBUCIÓN POR MÉTODO:\n");
            List<Map<String, Object>> chartData = new ArrayList<>();
            for (Map<String, Object> metodo : metodos) {
                Map<String, Object> item = new HashMap<>();
                item.put("label", metodo.get("metodo_pago"));
                item.put("value", metodo.get("monto_total"));
                chartData.add(item);
            }
            sb.append(ChartGenerator.pieChart(chartData));
        }
        
        return sb.toString();
    }
    
    /**
     * Formatea el reporte de consumo de productos
     */
    public static String formatConsumoProductos(List<Map<String, Object>> productos) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n╔════════════════════════════════════════════════════════════════════════════════════╗\n");
        sb.append("║                         CONSUMO DE PRODUCTOS                                       ║\n");
        sb.append("╠═══╦════════════╦═══════════════════════╦══════════╦════════════╦══════════════════╣\n");
        sb.append("║ID ║ Código     ║ Nombre                ║ Stock    ║ Cant.Usada ║ Alerta           ║\n");
        sb.append("╠═══╬════════════╬═══════════════════════╬══════════╬════════════╬══════════════════╣\n");
        
        for (Map<String, Object> producto : productos) {
            String alerta = producto.containsKey("alerta_stock") ? 
                producto.get("alerta_stock").toString() : "OK";
                
            sb.append(String.format("║%-3d║ %-10s ║ %-21s ║ %8d ║ %10d ║ %-16s ║\n",
                producto.get("id_producto"),
                producto.get("codigo"),
                truncate(producto.get("nombre").toString(), 21),
                producto.get("stock_actual"),
                producto.get("cantidad_usada"),
                truncate(alerta, 16)));
        }
        
        sb.append("╚═══╩════════════╩═══════════════════════╩══════════╩════════════╩══════════════════╝\n");
        
        return sb.toString();
    }
    
    /**
     * Formatea estadísticas individuales de un barbero
     */
    public static String formatEstadisticasBarbero(Map<String, Object> stats) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n╔══════════════════════════════════════════════════════════════╗\n");
        sb.append(String.format("║      ESTADÍSTICAS DEL BARBERO #%-3d                        ║\n", 
            stats.get("id_barbero")));
        sb.append("╠══════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ Total Reservas:           %6d                          ║\n", 
            stats.get("total_reservas")));
        sb.append(String.format("║ Completadas:              %6d                          ║\n", 
            stats.get("completadas")));
        sb.append(String.format("║ Canceladas:               %6d                          ║\n", 
            stats.get("canceladas")));
        sb.append(String.format("║ No Asistió:               %6d                          ║\n", 
            stats.get("no_asistio")));
        sb.append("║                                                              ║\n");
        sb.append(String.format("║ Ingresos Totales:        $%10.2f                   ║\n", 
            stats.get("ingresos_totales")));
        sb.append(String.format("║ Promedio por Servicio:   $%10.2f                   ║\n", 
            stats.get("promedio_por_servicio")));
        sb.append("║                                                              ║\n");
        sb.append(String.format("║ Clientes Únicos:          %6d                          ║\n", 
            stats.get("clientes_unicos")));
        
        if (stats.containsKey("tasa_completadas")) {
            sb.append(String.format("║ Tasa de Completadas:      %6s                         ║\n", 
                stats.get("tasa_completadas")));
        }
        
        sb.append("╚══════════════════════════════════════════════════════════════╝\n");
        
        return sb.toString();
    }
    
    /**
     * Trunca un string a una longitud máxima
     */
    private static String truncate(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
}
