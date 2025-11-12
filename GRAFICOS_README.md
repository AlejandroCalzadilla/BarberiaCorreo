# 📊 Gráficos Estadísticos - Documentación

## Descripción General

El sistema de reportes ahora incluye visualizaciones gráficas en formato ASCII que facilitan la interpretación de datos estadísticos. La clase `ChartGenerator` proporciona múltiples tipos de gráficos reutilizables.

---

## Tipos de Gráficos Disponibles

### 1. **Gráfico de Barras Horizontal** 📊
Ideal para comparar valores entre diferentes categorías.

**Uso:**
```java
List<Map<String, Object>> data = new ArrayList<>();
Map<String, Object> item1 = new HashMap<>();
item1.put("label", "Producto A");
item1.put("value", 150);
data.add(item1);

String grafico = ChartGenerator.horizontalBarChart(data, 50, true);
System.out.println(grafico);
```

**Salida:**
```
Producto A │██████████████████████████████████ 150
Producto B │████████████████████ 80
Producto C │███████████████ 60
```

---

### 2. **Gráfico Circular (Pie Chart)** 🥧
Perfecto para mostrar distribuciones porcentuales.

**Uso:**
```java
String grafico = ChartGenerator.pieChart(data);
System.out.println(grafico);
```

**Salida:**
```
┌────────────────────────────────────────────────────────────┐
│████████████████████▓▓▓▓▓▓▓▒▒▒▒▒▒░░░░░                      │
└────────────────────────────────────────────────────────────┘

Distribución:
  █ Completadas        :   120.00 (60.00%)
  ▓ Canceladas         :    40.00 (20.00%)
  ▒ No Asistió         :    30.00 (15.00%)
  ░ Confirmadas        :    10.00 ( 5.00%)
```

---

### 3. **Histograma** 📈
Excelente para mostrar frecuencias o distribuciones.

**Uso:**
```java
String grafico = ChartGenerator.histogram(data, 10);
System.out.println(grafico);
```

**Salida:**
```
100 │ ██  ██      ██  
 80 │ ██  ██  ██  ██  
 60 │ ██  ██  ██  ██  
 40 │ ██  ██  ██  ██  
 20 │ ██  ██  ██  ██  
    └────────────────
     09h 10h 11h 12h
```

---

### 4. **Gráfico de Líneas** 📉
Ideal para mostrar tendencias a lo largo del tiempo.

**Uso:**
```java
String grafico = ChartGenerator.lineChart(data, 8, 40);
System.out.println(grafico);
```

**Salida:**
```
Max: 150.00
│        ●                               
│       │●                               
│      │ │●                              
│     │  │ ●                             
│    ●   │  ●                            
│   │    │   ●                           
│  │     │    ●──●                       
│ ●      │        ●                      
└────────────────────────────────────────┘
Min: 20.00
```

---

### 5. **Barra de Progreso** ⏳
Útil para metas y objetivos.

**Uso:**
```java
String barra = ChartGenerator.progressBar("Ventas del Mes", 7500, 10000, 30);
System.out.println(barra);
```

**Salida:**
```
Ventas del Mes: [██████████████████████░░░░░░░░] 75.0% (7.50K / 10.00K)
```

---

### 6. **Sparkline** ✨
Mini gráfico compacto para tendencias rápidas.

**Uso:**
```java
List<Double> valores = Arrays.asList(10.0, 15.0, 13.0, 18.0, 22.0, 20.0, 25.0);
String sparkline = ChartGenerator.sparkline(valores);
System.out.println("Tendencia: " + sparkline);
```

**Salida:**
```
Tendencia: ▁▃▂▅▇▆█
```

---

### 7. **Gráfico de Comparación** ⚖️
Compara dos valores con indicador de cambio.

**Uso:**
```java
String comparacion = ChartGenerator.comparisonChart(
    "Ingresos Mensuales", 
    8500, 
    12000, 
    "Octubre", 
    "Noviembre", 
    40
);
System.out.println(comparacion);
```

**Salida:**
```
Ingresos Mensuales:
Octubre    │████████████████████████████ 8.50K
Noviembre  │████████████████████████████████████████ 12.00K

Cambio: ↑ 3.50K (41.18%)
```

---

## Integración con Reportes

Los gráficos ya están integrados automáticamente en varios reportes:

### ✅ Reportes con Gráficos:

1. **Ranking de Barberos** - Gráfico de barras horizontal de ingresos
2. **Servicios Populares** - Gráfico de barras de popularidad
3. **Distribución de Estados** - Gráfico circular (pie chart)
4. **Horas Pico** - Histograma de actividad por hora
5. **Métodos de Pago** - Gráfico circular de distribución

### Ejemplo de Uso en Reportes:

```java
ReporteService reporteService = new ReporteService();

// Obtener ranking de barberos
LocalDate inicio = LocalDate.of(2025, 10, 1);
LocalDate fin = LocalDate.of(2025, 10, 31);
List<Map<String, Object>> ranking = reporteService.getRankingBarberos(inicio, fin);

// Formatear con gráfico incluido
String reporte = ReporteMapper.formatRankingBarberos(ranking);
System.out.println(reporte);
```

**Output:**
```
╔═════════════════════════════════════════════════════════════════════════════════════╗
║                          RANKING DE BARBEROS                                        ║
╠═══╦══════════════════════════╦════════════════╦══════════╦════════════╦═══════════╣
║ID ║ Nombre                   ║ Especialidad   ║ Servs.   ║ Completad. ║ Ingresos  ║
╠═══╬══════════════════════════╬════════════════╦══════════╦════════════╦═══════════╣
║1  ║ Juan Pérez              ║ Fade           ║      45  ║        42  ║$  1250.00║
║2  ║ María López             ║ Corte Clásico  ║      38  ║        36  ║$  1080.00║
╚═══╩══════════════════════════╩════════════════╩══════════╩════════════╩═══════════╝

📊 GRÁFICO DE INGRESOS POR BARBERO:

Juan           │████████████████████████████████████████ 1250.00
María          │█████████████████████████████████ 1080.00
```

---

## Personalización de Gráficos

### Ajustar Ancho de Barras:
```java
ChartGenerator.horizontalBarChart(data, 60, true); // 60 caracteres de ancho
```

### Ajustar Altura de Histogramas:
```java
ChartGenerator.histogram(data, 15); // 15 filas de altura
```

### Ajustar Dimensiones de Líneas:
```java
ChartGenerator.lineChart(data, 10, 50); // 10 alto x 50 ancho
```

---

## Tips de Uso

1. **Datos Vacíos**: Todos los métodos manejan listas vacías gracefully
2. **Valores Grandes**: Los valores se formatean automáticamente (K para miles, M para millones)
3. **Labels Largos**: Se truncan automáticamente para evitar desbordes
4. **Colores**: Los gráficos usan caracteres Unicode para diferentes intensidades

---

## Métodos Disponibles en ChartGenerator

| Método | Descripción | Parámetros Principales |
|--------|-------------|----------------------|
| `horizontalBarChart()` | Barras horizontales | data, maxWidth, showValues |
| `pieChart()` | Gráfico circular | data |
| `histogram()` | Histograma vertical | data, height |
| `lineChart()` | Gráfico de líneas | data, height, width |
| `progressBar()` | Barra de progreso | label, current, target, width |
| `sparkline()` | Mini gráfico | List<Double> values |
| `comparisonChart()` | Comparación de 2 valores | label, value1, value2, labels, width |

---

## Formato de Datos

Todos los gráficos (excepto sparkline) esperan una lista de mapas con esta estructura:

```java
Map<String, Object> item = new HashMap<>();
item.put("label", "Nombre del Item");  // String
item.put("value", 125.50);              // Number (int, double, BigDecimal)
```

---

## Ejemplos Avanzados

### Crear Dashboard Personalizado:
```java
public String crearDashboard() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("═══════════════════ DASHBOARD PERSONALIZADO ═══════════════════\n\n");
    
    // Progreso de meta mensual
    sb.append(ChartGenerator.progressBar("Meta Mensual", 8500, 10000, 40));
    sb.append("\n");
    
    // Tendencia semanal (sparkline)
    List<Double> tendencia = Arrays.asList(100.0, 120.0, 115.0, 140.0, 155.0, 150.0, 170.0);
    sb.append("Tendencia Semanal: ").append(ChartGenerator.sparkline(tendencia));
    sb.append("\n\n");
    
    // Top productos (barras)
    sb.append(ChartGenerator.horizontalBarChart(productosMasVendidos, 35, true));
    
    return sb.toString();
}
```

---

## Notas Importantes

- ✅ Todos los gráficos son **ASCII puro** - funcionan en cualquier terminal
- ✅ **Thread-safe** - todos los métodos son estáticos
- ✅ **Manejo de errores** - retornan mensajes informativos si hay problemas
- ✅ **Performance** - optimizados para grandes volúmenes de datos
- ⚠️ Los gráficos están diseñados para **terminales con fuente monoespaciada**

---

## Soporte

Para más información o reportar problemas, consulta el código fuente en:
`src/main/java/org/barberia/usuarios/utils/ChartGenerator.java`

---

**¡Disfruta visualizando tus datos! 📊✨**
