# Comandos de Reportes por Email

Este documento describe cómo usar los comandos de reportes a través del sistema de emails de la barbería.

## 📊 Dashboard General

### REPORTEDASHBOARD
Genera un dashboard completo con todas las estadísticas del mes actual.

**Sintaxis:**
```
REPORTEDASHBOARD
```

**Descripción:**
- Muestra ingresos del mes actual
- Top barberos por rendimiento
- Servicios más populares
- Clientes frecuentes
- Distribución de estados de reservas
- Métodos de pago más utilizados
- Horas pico de actividad
- Días más ocupados

**Ejemplo:**
```
Asunto: REPORTEDASHBOARD
```

---

## 📈 Reportes Individuales

Todos los reportes individuales permiten personalizar el rango de fechas y otros parámetros.

### 1. REPORTEINGRESOS
Obtiene los ingresos totales de un mes específico.

**Sintaxis:**
```
REPORTEINGRESOS[año, mes]
```

**Parámetros:**
- `año`: Año en formato YYYY (ejemplo: 2025)
- `mes`: Mes en formato MM (ejemplo: 10 para octubre)

**Ejemplo:**
```
Asunto: REPORTEINGRESOS[2025, 10]
```

**Resultado:**
- Total de reservas
- Reservas completadas
- Ingresos totales
- Tasa de cancelación
- Tasa de no asistencia

---

### 2. REPORTERANKINGBARBEROS
Ranking de barberos por ingresos generados en un período.

**Sintaxis:**
```
REPORTERANKINGBARBEROS[fecha_inicio, fecha_fin]
```

**Parámetros:**
- `fecha_inicio`: Fecha de inicio en formato YYYY-MM-DD
- `fecha_fin`: Fecha de fin en formato YYYY-MM-DD

**Ejemplo:**
```
Asunto: REPORTERANKINGBARBEROS[2025-10-01, 2025-10-31]
```

**Resultado:**
- Lista de barberos ordenados por ingresos
- Total de reservas por barbero
- Ingresos generados por cada uno
- Gráfico de barras horizontales

---

### 3. REPORTESERVICIOSPOPULARES
Top de servicios más solicitados en un período.

**Sintaxis:**
```
REPORTESERVICIOSPOPULARES[fecha_inicio, fecha_fin, limite]
```

**Parámetros:**
- `fecha_inicio`: Fecha de inicio en formato YYYY-MM-DD
- `fecha_fin`: Fecha de fin en formato YYYY-MM-DD
- `limite`: Número de servicios a mostrar (ejemplo: 5 para top 5)

**Ejemplo:**
```
Asunto: REPORTESERVICIOSPOPULARES[2025-01-01, 2025-12-31, 5]
```

**Resultado:**
- Lista de servicios más populares
- Número de veces completado
- Ingresos totales por servicio
- Gráfico de barras horizontales

---

### 4. REPORTECLIENTESFRECUENTES
Top de clientes con más visitas en un período.

**Sintaxis:**
```
REPORTECLIENTESFRECUENTES[fecha_inicio, fecha_fin, limite]
```

**Parámetros:**
- `fecha_inicio`: Fecha de inicio en formato YYYY-MM-DD
- `fecha_fin`: Fecha de fin en formato YYYY-MM-DD
- `limite`: Número de clientes a mostrar (ejemplo: 10 para top 10)

**Ejemplo:**
```
Asunto: REPORTECLIENTESFRECUENTES[2025-01-01, 2025-12-31, 10]
```

**Resultado:**
- Lista de clientes más frecuentes
- Total de visitas por cliente
- Monto total gastado
- Última visita

---

### 5. REPORTEDISTRIBUCIONESTADOS
Distribución de reservas por estado en un período.

**Sintaxis:**
```
REPORTEDISTRIBUCIONESTADOS[fecha_inicio, fecha_fin]
```

**Parámetros:**
- `fecha_inicio`: Fecha de inicio en formato YYYY-MM-DD
- `fecha_fin`: Fecha de fin en formato YYYY-MM-DD

**Ejemplo:**
```
Asunto: REPORTEDISTRIBUCIONESTADOS[2025-07-01, 2025-09-30]
```

**Resultado:**
- Total de reservas
- Reservas completadas
- Reservas canceladas
- No asistió
- Confirmadas
- En proceso
- Gráfico circular (pie chart)

---

### 6. REPORTEHORASPICO
Horas del día con más actividad en un período.

**Sintaxis:**
```
REPORTEHORASPICO[fecha_inicio, fecha_fin]
```

**Parámetros:**
- `fecha_inicio`: Fecha de inicio en formato YYYY-MM-DD
- `fecha_fin`: Fecha de fin en formato YYYY-MM-DD

**Ejemplo:**
```
Asunto: REPORTEHORASPICO[2025-10-01, 2025-10-31]
```

**Resultado:**
- Horas del día ordenadas por actividad
- Total de reservas por hora
- Porcentaje de ocupación
- Histograma de actividad

---

### 7. REPORTEDIASOCUPADOS
Días de la semana con más reservas en un período.

**Sintaxis:**
```
REPORTEDIASOCUPADOS[fecha_inicio, fecha_fin]
```

**Parámetros:**
- `fecha_inicio`: Fecha de inicio en formato YYYY-MM-DD
- `fecha_fin`: Fecha de fin en formato YYYY-MM-DD

**Ejemplo:**
```
Asunto: REPORTEDIASOCUPADOS[2025-10-01, 2025-10-31]
```

**Resultado:**
- Días de la semana ordenados por actividad
- Total de reservas por día
- Ingresos promedio por día

---

### 8. REPORTEMETODOSPAGO
Distribución de pagos por método en un período.

**Sintaxis:**
```
REPORTEMETODOSPAGO[fecha_inicio, fecha_fin]
```

**Parámetros:**
- `fecha_inicio`: Fecha de inicio en formato YYYY-MM-DD
- `fecha_fin`: Fecha de fin en formato YYYY-MM-DD

**Ejemplo:**
```
Asunto: REPORTEMETODOSPAGO[2025-01-01, 2025-12-31]
```

**Resultado:**
- Métodos de pago utilizados
- Monto total por método
- Porcentaje de uso
- Gráfico circular (pie chart)

---

### 9. REPORTECONSUMOPRODUCTOS
Consumo de productos en servicios durante un período.

**Sintaxis:**
```
REPORTECONSUMOPRODUCTOS[fecha_inicio, fecha_fin]
```

**Parámetros:**
- `fecha_inicio`: Fecha de inicio en formato YYYY-MM-DD
- `fecha_fin`: Fecha de fin en formato YYYY-MM-DD

**Ejemplo:**
```
Asunto: REPORTECONSUMOPRODUCTOS[2025-10-01, 2025-10-31]
```

**Resultado:**
- Productos más utilizados
- Cantidad consumida
- Costo total de consumo
- Veces utilizado en servicios

---

### 10. REPORTEESTADISTICASBARBERO
Estadísticas detalladas de un barbero específico.

**Sintaxis:**
```
REPORTEESTADISTICASBARBERO[id_barbero, fecha_inicio, fecha_fin]
```

**Parámetros:**
- `id_barbero`: ID del barbero (número entero)
- `fecha_inicio`: Fecha de inicio en formato YYYY-MM-DD
- `fecha_fin`: Fecha de fin en formato YYYY-MM-DD

**Ejemplo:**
```
Asunto: REPORTEESTADISTICASBARBERO[1, 2025-10-01, 2025-10-31]
```

**Resultado:**
- Nombre del barbero
- Total de reservas atendidas
- Reservas completadas
- Ingresos totales generados
- Tasa de finalización
- Promedio de ingresos por reserva

---

## 📝 Formato de Fechas

Todos los comandos que requieren fechas deben usar el formato **YYYY-MM-DD**:
- ✅ `2025-10-01` (correcto)
- ✅ `2025-12-31` (correcto)
- ❌ `01-10-2025` (incorrecto)
- ❌ `2025/10/01` (incorrecto)

## 🎨 Gráficos Incluidos

Los reportes incluyen visualizaciones ASCII:
- **Barras horizontales**: Para rankings y comparaciones
- **Gráfico circular (pie chart)**: Para distribuciones porcentuales
- **Histograma**: Para datos de series temporales (horas)

## ⚠️ Manejo de Errores

Si un comando es inválido, recibirás un mensaje de error indicando:
- Parámetros faltantes
- Formato de fecha incorrecto
- Tipo de reporte no reconocido

**Ejemplo de error:**
```
Error: Se requieren fecha inicio y fecha fin. 
Ejemplo: REPORTEHORASPICO[2025-10-01, 2025-10-31]
```

## 📧 Cómo Usar

1. Componer un nuevo email
2. En el **asunto**, escribir el comando exacto (sin espacios extra)
3. El cuerpo del email puede estar vacío
4. Enviar el email
5. Recibirás la respuesta con el reporte generado

## 🔍 Comando de Ayuda

Para ver todos los comandos disponibles, envía:
```
Asunto: HELP
```

Esto te mostrará una lista completa de todos los comandos CRUD y de reportes disponibles.

---

## 💡 Consejos

1. **Dashboard rápido**: Usa `REPORTEDASHBOARD` para una vista general sin parámetros
2. **Análisis mensual**: Combina varios reportes del mismo período para análisis completo
3. **Fechas flexibles**: Puedes consultar cualquier rango de fechas (día, semana, mes, año)
4. **Top rankings**: Ajusta el parámetro `limite` según necesites (top 5, top 10, etc.)

---

## 📞 Soporte

Si tienes dudas sobre algún comando o necesitas ayuda, consulta:
- `HELP` - Lista completa de comandos
- `GRAFICOS_README.md` - Documentación sobre los gráficos
- `README.md` - Documentación general del sistema
