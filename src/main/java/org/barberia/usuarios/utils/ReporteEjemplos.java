package org.barberia.usuarios.utils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import org.barberia.usuarios.mapper.ReporteMapper;
import org.barberia.usuarios.service.ReporteService;

/**
 * Ejemplos de uso del servicio de reportes
 */
public class ReporteEjemplos {
    

    private  ReporteService reporteService;

     public ReporteEjemplos(
        ReporteService reporteService
     ) {
        this.reporteService = reporteService;
     }

    

  
   
    private String  ejemploIngresosMensuales(ReporteService service) {
        System.out.println("\n========== EJEMPLO 1: INGRESOS MENSUALES ==========");
        
        // Obtener ingresos del mes de octubre 2025
        Map<String, Object> ingresos = reporteService.getIngresosMensuales(2025, 10);
        
        // Formatear y mostrar
        String reporte = ReporteMapper.formatIngresosMensuales(ingresos);
        return reporte;
       // System.out.println(reporte);
    }
    
    private String ejemploRankingBarberos(ReporteService service) {
        System.out.println("\n========== EJEMPLO 2: RANKING DE BARBEROS ==========");
        
        // Obtener ranking del último mes
        LocalDate inicio = LocalDate.of(2025, 10, 1);
        LocalDate fin = LocalDate.of(2025, 10, 31);
        
        List<Map<String, Object>> ranking = service.getRankingBarberos(inicio, fin);
        
        // Formatear y mostrar
        String reporte = ReporteMapper.formatRankingBarberos(ranking);
        return reporte;
    }
    
    private String ejemploServiciosPopulares(ReporteService service) {
        System.out.println("\n========== EJEMPLO 3: SERVICIOS MÁS POPULARES ==========");
        
        // Top 5 servicios del año
        LocalDate inicio = LocalDate.of(2025, 1, 1);
        LocalDate fin = LocalDate.of(2025, 12, 31);
        
        List<Map<String, Object>> servicios = service.getServiciosMasPopulares(inicio, fin, 5);
        System.out.println("servicios"+servicios);
        // Formatear y mostrar
        String reporte = ReporteMapper.formatServiciosPopulares(servicios);
        return reporte;
    }
    
    private String ejemploClientesFrecuentes(ReporteService service) {
        System.out.println("\n========== EJEMPLO 4: CLIENTES MÁS FRECUENTES ==========");
        
        // Top 10 clientes del año
        LocalDate inicio = LocalDate.of(2025, 1, 1);
        LocalDate fin = LocalDate.of(2025, 12, 31);
        
        List<Map<String, Object>> clientes = service.getClientesFrecuentes(inicio, fin, 10);
        
        // Formatear y mostrar
        String reporte = ReporteMapper.formatClientesFrecuentes(clientes);
        return reporte;
    }
    
    private String ejemploDistribucionEstados(ReporteService service) {
        System.out.println("\n========== EJEMPLO 5: DISTRIBUCIÓN DE ESTADOS ==========");
        
        // Distribución del último trimestre
        LocalDate inicio = LocalDate.of(2025, 7, 1);
        LocalDate fin = LocalDate.of(2025, 9, 30);
        
        Map<String, Object> distribucion = service.getDistribucionEstados(inicio, fin);
        
        // Formatear y mostrar
        String reporte = ReporteMapper.formatDistribucionEstados(distribucion);
        return reporte;
    }
    
    private String ejemploHorasPico(ReporteService service) {
        System.out.println("\n========== EJEMPLO 6: HORAS PICO ==========");
        
        // Horas pico del mes actual
        LocalDate inicio = LocalDate.of(2025, 10, 1);
        LocalDate fin = LocalDate.of(2025, 10, 31);
        
        List<Map<String, Object>> horas = service.getHorasPico(inicio, fin);
        
        // Formatear y mostrar
        String reporte = ReporteMapper.formatHorasPico(horas);
        return reporte;
    }
    
    private String ejemploDiasMasOcupados(ReporteService service) {
        System.out.println("\n========== EJEMPLO 7: DÍAS MÁS OCUPADOS ==========");
        
        // Días ocupados del mes actual
        LocalDate inicio = LocalDate.of(2025, 10, 1);
        LocalDate fin = LocalDate.of(2025, 10, 31);
        
        List<Map<String, Object>> dias = service.getDiasMasOcupados(inicio, fin);
        
        // Formatear y mostrar
        String reporte = ReporteMapper.formatDiasMasOcupados(dias);
        return reporte;
    }
    
    private String ejemploMetodosPago(ReporteService service) {
        System.out.println("\n========== EJEMPLO 8: MÉTODOS DE PAGO ==========");
        
        // Métodos de pago del año
        LocalDate inicio = LocalDate.of(2025, 1, 1);
        LocalDate fin = LocalDate.of(2025, 12, 31);
        
        List<Map<String, Object>> metodos = service.getDistribucionMetodosPago(inicio, fin);
        
        // Formatear y mostrar
        String reporte = ReporteMapper.formatMetodosPago(metodos);
        return reporte;
    }
    
    private String ejemploConsumoProductos(ReporteService service) {
        System.out.println("\n========== EJEMPLO 9: CONSUMO DE PRODUCTOS ==========");
        
        // Consumo del último mes
        LocalDate inicio = LocalDate.of(2025, 10, 1);
        LocalDate fin = LocalDate.of(2025, 10, 31);
        
        List<Map<String, Object>> productos = service.getConsumoProductos(inicio, fin);
        
        // Formatear y mostrar
        String reporte = ReporteMapper.formatConsumoProductos(productos);
        return reporte;
    }
    
    private String ejemploEstadisticasBarbero() {
        System.out.println("\n========== EJEMPLO 10: ESTADÍSTICAS DE UN BARBERO ==========");
        
        // Estadísticas del barbero ID 1 en el último mes
        int idBarbero = 1;
        LocalDate inicio = LocalDate.of(2025, 10, 1);
        LocalDate fin = LocalDate.of(2025, 10, 31);
        
        Map<String, Object> stats = this.reporteService.getEstadisticasBarbero(idBarbero, inicio, fin);
        
        // Formatear y mostrar
        String reporte = ReporteMapper.formatEstadisticasBarbero(stats);
        return reporte;
    }
    
    public String ejemploDashboardGeneral() {
        System.out.println("\n========== EJEMPLO 11: DASHBOARD GENERAL ==========");
        
        // Dashboard del mes actual
        YearMonth mesActual = YearMonth.of(2025, 10);
        
        Map<String, Object> dashboard = this.reporteService.getDashboardGeneral(mesActual);
        
        // Construir y devolver TODO el dashboard como un String
        StringBuilder sb = new StringBuilder();
        sb.append("\n╔═══════════════════════════════════════════════════════════════════╗\n");
        sb.append("║              DASHBOARD GENERAL - ").append(dashboard.get("periodo")).append("                          ║\n");
        sb.append("╚═══════════════════════════════════════════════════════════════════╝\n\n");

        // Ingresos
        @SuppressWarnings("unchecked")
        Map<String, Object> ingresos = (Map<String, Object>) dashboard.get("ingresos");
        sb.append(ReporteMapper.formatIngresosMensuales(ingresos)).append("\n\n");

        // Top barberos
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> barberos = (List<Map<String, Object>>) dashboard.get("top_barberos");
        sb.append(ReporteMapper.formatRankingBarberos(barberos)).append("\n\n");

        // Servicios populares
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> servicios = (List<Map<String, Object>>) dashboard.get("servicios_populares");
        sb.append(ReporteMapper.formatServiciosPopulares(servicios)).append("\n\n");

        // Clientes frecuentes
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> clientes = (List<Map<String, Object>>) dashboard.get("clientes_frecuentes");
        sb.append(ReporteMapper.formatClientesFrecuentes(clientes)).append("\n\n");

        // Distribución de estados
        @SuppressWarnings("unchecked")
        Map<String, Object> estados = (Map<String, Object>) dashboard.get("distribucion_estados");
        sb.append(ReporteMapper.formatDistribucionEstados(estados)).append("\n\n");

        // Métodos de pago
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> metodos = (List<Map<String, Object>>) dashboard.get("metodos_pago");
        sb.append(ReporteMapper.formatMetodosPago(metodos)).append("\n\n");

        // Horas pico
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> horas = (List<Map<String, Object>>) dashboard.get("horas_pico");
        sb.append(ReporteMapper.formatHorasPico(horas)).append("\n\n");

        // Días más ocupados
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> dias = (List<Map<String, Object>>) dashboard.get("dias_ocupados");
        sb.append(ReporteMapper.formatDiasMasOcupados(dias)).append("\n");

        return sb.toString();
    }
}
