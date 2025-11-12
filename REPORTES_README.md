# Servicio de Reportes y Estadísticas

Este servicio proporciona reportes y estadísticas completas del negocio de la barbería, utilizando datos de múltiples tablas.

## 📊 Tablas Utilizadas

El servicio consulta las siguientes tablas:
- **reserva** - Información de reservas y servicios realizados
- **pago** - Información de pagos y métodos de pago
- **barbero** - Datos de los barberos
- **cliente** - Datos de los clientes
- **usuario** - Información de usuarios (nombres, contactos)
- **servicio** - Catálogo de servicios
- **producto** - Inventario de productos
- **categoria** - Categorías de productos
- **servicio_producto** - Relación entre servicios y productos
- **horario** - Horarios de trabajo

## 🚀 Ubicación

- **Servicio**: `src/main/java/org/barberia/usuarios/service/ReporteService.java`
- **Mapper**: `src/main/java/org/barberia/usuarios/mapper/ReporteMapper.java`
- **Ejemplos**: `src/main/java/org/barberia/usuarios/utils/ReporteEjemplos.java`

## 📈 Reportes Disponibles

### 1. Reportes de Ingresos

#### `getIngresosMensuales(int año, int mes)`
Obtiene ingresos totales de un mes específico.

**Retorna:**
```
- año
- mes
- total_reservas
- reservas_completadas
- ingresos_totales
- anticipos_recibidos
- pagos_finales
- pagos_pendientes
```

**Ejemplo:**
```java
ReporteService service = new ReporteService();
Map<String, Object> ingresos = service.getIngresosMensuales(2025, 10);
System.out.println(ReporteMapper.formatIngresosMensuales(ingresos));
```

#### `getIngresosPorRango(LocalDate fechaInicio, LocalDate fechaFin)`
Obtiene resumen de ingresos en un rango de fechas.

**Retorna:**
```
- fecha_inicio
- fecha_fin
- total_reservas
- ingresos_totales
- promedio_por_pago
- pago_maximo
- pago_minimo
```

### 2. Reportes de Barberos

#### `getRankingBarberos(LocalDate fechaInicio, LocalDate fechaFin)`
Obtiene ranking de barberos por desempeño.

**Retorna (Lista):**
```
- id_barbero
- nombre
- apellido
- especialidad
- total_servicios
- servicios_completados
- ingresos_generados
- promedio_por_servicio
```

**Ejemplo:**
```java
LocalDate inicio = LocalDate.of(2025, 1, 1);
LocalDate fin = LocalDate.of(2025, 12, 31);
List<Map<String, Object>> ranking = service.getRankingBarberos(inicio, fin);
System.out.println(ReporteMapper.formatRankingBarberos(ranking));
```

#### `getEstadisticasBarbero(int idBarbero, LocalDate fechaInicio, LocalDate fechaFin)`
Estadísticas detalladas de un barbero específico.

**Retorna:**
```
- id_barbero
- total_reservas
- completadas
- canceladas
- no_asistio
- ingresos_totales
- promedio_por_servicio
- clientes_unicos
- tasa_completadas (porcentaje)
```

### 3. Reportes de Servicios

#### `getServiciosMasPopulares(LocalDate fechaInicio, LocalDate fechaFin, int limit)`
Obtiene los servicios más solicitados.

**Parámetros:**
- `limit` - Cantidad de servicios a retornar (ej: top 5, top 10)

**Retorna (Lista):**
```
- id_servicio
- nombre
- precio
- duracion_minutos
- veces_solicitado
- veces_completado
- ingresos_generados
```

**Ejemplo:**
```java
// Top 5 servicios
List<Map<String, Object>> servicios = service.getServiciosMasPopulares(inicio, fin, 5);
System.out.println(ReporteMapper.formatServiciosPopulares(servicios));
```

### 4. Reportes de Clientes

#### `getClientesFrecuentes(LocalDate fechaInicio, LocalDate fechaFin, int limit)`
Obtiene los clientes más frecuentes.

**Retorna (Lista):**
```
- id_cliente
- nombre
- apellido
- email
- telefono
- total_reservas
- reservas_completadas
- gasto_total
- promedio_por_visita
- ultima_visita
```

**Ejemplo:**
```java
// Top 10 clientes
List<Map<String, Object>> clientes = service.getClientesFrecuentes(inicio, fin, 10);
System.out.println(ReporteMapper.formatClientesFrecuentes(clientes));
```

### 5. Reportes de Estados

#### `getDistribucionEstados(LocalDate fechaInicio, LocalDate fechaFin)`
Distribución de estados de reservas.

**Retorna:**
```
- total_reservas
- completadas
- canceladas
- no_asistio
- confirmadas
- en_proceso
- porcentaje_completadas
- porcentaje_canceladas
- porcentaje_no_asistio
```

### 6. Reportes de Productos

#### `getConsumoProductos(LocalDate fechaInicio, LocalDate fechaFin)`
Consumo de productos (especialmente desechables).

**Retorna (Lista):**
```
- id_producto
- codigo
- nombre
- stock_actual
- categoria
- cantidad_usada
- servicios_realizados
- alerta_stock (si stock < cantidad_usada * 2)
```

**Ejemplo:**
```java
List<Map<String, Object>> productos = service.getConsumoProductos(inicio, fin);
System.out.println(ReporteMapper.formatConsumoProductos(productos));
```

### 7. Reportes de Horarios

#### `getHorasPico(LocalDate fechaInicio, LocalDate fechaFin)`
Horas con más reservas.

**Retorna (Lista):**
```
- hora (formato "HH:00")
- total_reservas
- completadas
- ingresos
```

#### `getDiasMasOcupados(LocalDate fechaInicio, LocalDate fechaFin)`
Días de la semana más ocupados.

**Retorna (Lista):**
```
- dia_numero (0=Domingo, 1=Lunes, ..., 6=Sábado)
- dia_semana (nombre del día)
- total_reservas
- completadas
- ingresos
```

### 8. Reportes de Pagos

#### `getDistribucionMetodosPago(LocalDate fechaInicio, LocalDate fechaFin)`
Distribución por método de pago.

**Retorna (Lista):**
```
- metodo_pago
- cantidad_pagos
- monto_total
- promedio_por_pago
```

### 9. Dashboard General

#### `getDashboardGeneral(YearMonth mes)`
Dashboard completo con todas las métricas del mes.

**Retorna:**
```
- periodo
- ingresos (getIngresosMensuales)
- top_barberos (getRankingBarberos)
- servicios_populares (top 5)
- clientes_frecuentes (top 10)
- distribucion_estados
- metodos_pago
- horas_pico
- dias_ocupados
```

**Ejemplo:**
```java
YearMonth mes = YearMonth.of(2025, 10);
Map<String, Object> dashboard = service.getDashboardGeneral(mes);

// Acceder a cada sección
Map<String, Object> ingresos = (Map<String, Object>) dashboard.get("ingresos");
List<Map<String, Object>> barberos = (List<Map<String, Object>>) dashboard.get("top_barberos");
```

## 🎨 Formateo de Reportes

Todos los reportes tienen métodos de formateo en `ReporteMapper` que generan tablas ASCII profesionales:

```java
// Método estático para formatear
String reporteFormateado = ReporteMapper.formatIngresosMensuales(ingresos);
System.out.println(reporteFormateado);
```

Métodos disponibles:
- `formatIngresosMensuales(Map<String, Object>)`
- `formatRankingBarberos(List<Map<String, Object>>)`
- `formatServiciosPopulares(List<Map<String, Object>>)`
- `formatClientesFrecuentes(List<Map<String, Object>>)`
- `formatDistribucionEstados(Map<String, Object>)`
- `formatHorasPico(List<Map<String, Object>>)`
- `formatDiasMasOcupados(List<Map<String, Object>>)`
- `formatMetodosPago(List<Map<String, Object>>)`
- `formatConsumoProductos(List<Map<String, Object>>)`
- `formatEstadisticasBarbero(Map<String, Object>)`

## 📝 Ejemplo Completo

```java
import java.time.LocalDate;
import java.time.YearMonth;
import org.barberia.usuarios.service.ReporteService;
import org.barberia.usuarios.mapper.ReporteMapper;

public class Main {
    public static void main(String[] args) {
        ReporteService service = new ReporteService();
        
        // 1. Ingresos del mes actual
        Map<String, Object> ingresos = service.getIngresosMensuales(2025, 11);
        System.out.println(ReporteMapper.formatIngresosMensuales(ingresos));
        
        // 2. Ranking de barberos del último mes
        LocalDate inicio = LocalDate.of(2025, 10, 1);
        LocalDate fin = LocalDate.of(2025, 10, 31);
        List<Map<String, Object>> ranking = service.getRankingBarberos(inicio, fin);
        System.out.println(ReporteMapper.formatRankingBarberos(ranking));
        
        // 3. Dashboard completo
        YearMonth mes = YearMonth.of(2025, 10);
        Map<String, Object> dashboard = service.getDashboardGeneral(mes);
        
        // El dashboard contiene todos los reportes
        System.out.println("Dashboard generado para: " + dashboard.get("periodo"));
    }
}
```

## 🔧 Ejecución de Ejemplos

Para ejecutar los ejemplos completos:

```bash
# Compilar
mvn clean compile

# Ejecutar ejemplos
mvn exec:java -Dexec.mainClass="org.barberia.usuarios.utils.ReporteEjemplos"
```

## 💡 Casos de Uso

### Reporte Mensual para Administración
```java
YearMonth mesActual = YearMonth.now();
Map<String, Object> dashboard = service.getDashboardGeneral(mesActual);
// Generar reporte completo para enviar por correo
```

### Análisis de Rendimiento de Barbero
```java
int idBarbero = 1;
LocalDate inicio = LocalDate.now().minusMonths(3);
LocalDate fin = LocalDate.now();
Map<String, Object> stats = service.getEstadisticasBarbero(idBarbero, inicio, fin);
```

### Control de Inventario
```java
LocalDate inicio = LocalDate.now().minusMonths(1);
LocalDate fin = LocalDate.now();
List<Map<String, Object>> productos = service.getConsumoProductos(inicio, fin);
// Identificar productos con alerta_stock = "BAJO"
```

### Optimización de Horarios
```java
List<Map<String, Object>> horasPico = service.getHorasPico(inicio, fin);
List<Map<String, Object>> diasOcupados = service.getDiasMasOcupados(inicio, fin);
// Ajustar horarios según demanda
```

## 📊 Consultas SQL Incluidas

El servicio utiliza consultas SQL optimizadas con:
- **JOINs** para relacionar múltiples tablas
- **COALESCE** para manejar valores NULL
- **CASE** para lógica condicional
- **GROUP BY** para agregaciones
- **EXTRACT** para fechas
- **Subconsultas** para cálculos complejos

Todas las consultas incluyen manejo de errores y no requieren transacciones ya que son solo lecturas.

## ⚠️ Notas Importantes

1. **Rendimiento**: Para rangos de fechas muy amplios, las consultas pueden tardar. Considere usar límites apropiados.

2. **Datos Nulos**: Todos los reportes manejan correctamente datos NULL usando COALESCE.

3. **Formato de Fechas**: Use `LocalDate` para fechas y `YearMonth` para períodos mensuales.

4. **Estados de Reserva**: Los reportes consideran solo reservas con estado 'completada' para cálculos de ingresos.

5. **Productos Desechables**: El reporte de consumo calcula el uso basándose en servicios completados multiplicado por la cantidad en `servicio_producto`.
