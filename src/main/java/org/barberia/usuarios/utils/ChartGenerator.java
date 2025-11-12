package org.barberia.usuarios.utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Generador de gráficos ASCII reutilizable para reportes estadísticos
 */
public class ChartGenerator {
    
    private static final String BLOCK_FULL = "█";
    private static final String BLOCK_LIGHT = "░";
    private static final int DEFAULT_MAX_WIDTH = 50;
    
    /**
     * Genera un gráfico de barras horizontal
     * 
     * @param data Lista de mapas con "label" y "value" (Number)
     * @param maxWidth Ancho máximo de las barras
     * @param showValues Si debe mostrar los valores numéricos
     * @return String con el gráfico formateado
     */
    public static String horizontalBarChart(List<Map<String, Object>> data, int maxWidth, boolean showValues) {
        if (data == null || data.isEmpty()) {
            return "No hay datos para mostrar";
        }
        
        StringBuilder sb = new StringBuilder();
        
        // Encontrar valor máximo para escalar
        double maxValue = data.stream()
            .mapToDouble(m -> getNumericValue(m.get("value")))
            .max()
            .orElse(1.0);
        
        if (maxValue == 0) maxValue = 1.0;
        
        // Encontrar label más largo para alineación
        int maxLabelLength = data.stream()
            .mapToInt(m -> m.get("label").toString().length())
            .max()
            .orElse(10);
        
        for (Map<String, Object> item : data) {
            String label = item.get("label").toString();
            double value = getNumericValue(item.get("value"));
            
            // Calcular longitud de la barra
            int barLength = (int) ((value / maxValue) * maxWidth);
            
            // Formatear label con padding
            String paddedLabel = String.format("%-" + maxLabelLength + "s", label);
            
            // Construir barra
            sb.append(paddedLabel).append(" │");
            for (int i = 0; i < barLength; i++) {
                sb.append(BLOCK_FULL);
            }
            
            // Agregar valor si se solicita
            if (showValues) {
                sb.append(" ").append(formatValue(value));
            }
            
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Genera un gráfico de barras horizontal con ancho por defecto
     */
    public static String horizontalBarChart(List<Map<String, Object>> data, boolean showValues) {
        return horizontalBarChart(data, DEFAULT_MAX_WIDTH, showValues);
    }
    
    /**
     * Genera un gráfico de líneas con tendencia
     * 
     * @param data Lista de mapas con "label" y "value" (Number)
     * @param height Altura del gráfico
     * @param width Ancho del gráfico
     * @return String con el gráfico formateado
     */
    public static String lineChart(List<Map<String, Object>> data, int height, int width) {
        if (data == null || data.isEmpty()) {
            return "No hay datos para mostrar";
        }
        
        StringBuilder sb = new StringBuilder();
        
        // Encontrar min y max para escalar
        double maxValue = data.stream()
            .mapToDouble(m -> getNumericValue(m.get("value")))
            .max()
            .orElse(1.0);
        
        double minValue = data.stream()
            .mapToDouble(m -> getNumericValue(m.get("value")))
            .min()
            .orElse(0.0);
        
        double range = maxValue - minValue;
        if (range == 0) range = 1.0;
        
        // Crear matriz para el gráfico
        char[][] grid = new char[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = ' ';
            }
        }
        
        // Plotear puntos
        for (int i = 0; i < data.size() && i < width; i++) {
            double value = getNumericValue(data.get(i).get("value"));
            int y = height - 1 - (int) (((value - minValue) / range) * (height - 1));
            
            if (y >= 0 && y < height) {
                grid[y][i] = '●';
                
                // Conectar con línea si no es el primero
                if (i > 0) {
                    double prevValue = getNumericValue(data.get(i - 1).get("value"));
                    int prevY = height - 1 - (int) (((prevValue - minValue) / range) * (height - 1));
                    
                    int startY = Math.min(y, prevY);
                    int endY = Math.max(y, prevY);
                    
                    for (int lineY = startY; lineY <= endY; lineY++) {
                        if (grid[lineY][i - 1] == ' ') {
                            grid[lineY][i - 1] = '│';
                        }
                    }
                }
            }
        }
        
        // Construir output
        sb.append(String.format("Max: %s\n", formatValue(maxValue)));
        
        for (int i = 0; i < height; i++) {
            sb.append("│");
            for (int j = 0; j < width; j++) {
                sb.append(grid[i][j]);
            }
            sb.append("│\n");
        }
        
        sb.append("└");
        for (int i = 0; i < width; i++) {
            sb.append("─");
        }
        sb.append("┘\n");
        sb.append(String.format("Min: %s\n", formatValue(minValue)));
        
        return sb.toString();
    }
    
    /**
     * Genera un gráfico circular (pie chart) simple
     * 
     * @param data Lista de mapas con "label" y "value" (Number)
     * @return String con el gráfico formateado
     */
    public static String pieChart(List<Map<String, Object>> data) {
        if (data == null || data.isEmpty()) {
            return "No hay datos para mostrar";
        }
        
        StringBuilder sb = new StringBuilder();
        
        // Calcular total
        double total = data.stream()
            .mapToDouble(m -> getNumericValue(m.get("value")))
            .sum();
        
        if (total == 0) {
            return "No hay datos para mostrar (total = 0)";
        }
        
        // Símbolos para diferentes segmentos
        String[] symbols = {"█", "▓", "▒", "░", "▪", "▫", "●", "○"};
        
        sb.append("\n");
        
        // Crear círculo ASCII (radio = 8)
        int radius = 8;
        int centerX = radius;
        int centerY = radius;
        
        // Calcular ángulos acumulados para cada segmento
        double[] angles = new double[data.size() + 1];
        angles[0] = 0;
        for (int i = 0; i < data.size(); i++) {
            double value = getNumericValue(data.get(i).get("value"));
            double percentage = value / total;
            angles[i + 1] = angles[i] + (percentage * 360);
        }
        
        // Dibujar círculo
        for (int y = 0; y <= radius * 2; y++) {
            sb.append("     ");
            for (int x = 0; x <= radius * 2; x++) {
                double dx = x - centerX;
                double dy = (y - centerY) * 2.0; // Factor de corrección para aspecto
                double distance = Math.sqrt(dx * dx + dy * dy);
                
                if (distance <= radius && distance >= radius - 1.5) {
                    // Calcular ángulo del punto actual
                    double angle = Math.toDegrees(Math.atan2(dy, dx));
                    if (angle < 0) angle += 360;
                    
                    // Determinar a qué segmento pertenece
                    int segmentIndex = 0;
                    for (int i = 0; i < data.size(); i++) {
                        if (angle >= angles[i] && angle < angles[i + 1]) {
                            segmentIndex = i;
                            break;
                        }
                    }
                    
                    sb.append(symbols[segmentIndex % symbols.length]);
                } else if (distance < radius - 1.5) {
                    // Interior del círculo - determinar segmento
                    double angle = Math.toDegrees(Math.atan2(dy, dx));
                    if (angle < 0) angle += 360;
                    
                    int segmentIndex = 0;
                    for (int i = 0; i < data.size(); i++) {
                        if (angle >= angles[i] && angle < angles[i + 1]) {
                            segmentIndex = i;
                            break;
                        }
                    }
                    
                    sb.append(symbols[segmentIndex % symbols.length]);
                } else {
                    sb.append(" ");
                }
            }
            sb.append("\n");
        }
        
        sb.append("\n");
        
        // Leyenda
        sb.append("Distribución:\n");
        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> item = data.get(i);
            String label = item.get("label").toString();
            double value = getNumericValue(item.get("value"));
            double percentage = (value / total) * 100;
            
            String symbol = symbols[i % symbols.length];
            sb.append(String.format("  %s %-20s: %8s (%5.2f%%)\n", 
                symbol, truncate(label, 20), formatValue(value), percentage));
        }
        
        return sb.toString();
    }
    
    /**
     * Genera un histograma de distribución
     * 
     * @param data Lista de mapas con "label" y "value" (Number)
     * @param maxHeight Altura máxima de las barras
     * @return String con el gráfico formateado
     */
    public static String histogram(List<Map<String, Object>> data, int maxHeight) {
        if (data == null || data.isEmpty()) {
            return "No hay datos para mostrar";
        }
        
        StringBuilder sb = new StringBuilder();
        
        // Encontrar valor máximo
        double maxValue = data.stream()
            .mapToDouble(m -> getNumericValue(m.get("value")))
            .max()
            .orElse(1.0);
        
        if (maxValue == 0) maxValue = 1.0;
        
        // Calcular altura de cada barra
        int[] heights = new int[data.size()];
        for (int i = 0; i < data.size(); i++) {
            double value = getNumericValue(data.get(i).get("value"));
            heights[i] = (int) ((value / maxValue) * maxHeight);
        }
        
        // Construir de arriba hacia abajo
        for (int row = maxHeight; row > 0; row--) {
            sb.append(String.format("%3d │", (int) ((row / (double) maxHeight) * maxValue)));
            for (int col = 0; col < data.size(); col++) {
                if (heights[col] >= row) {
                    sb.append(" ").append(BLOCK_FULL).append(BLOCK_FULL).append(" ");
                } else {
                    sb.append("    ");
                }
            }
            sb.append("\n");
        }
        
        // Línea base
        sb.append("    └");
        for (int i = 0; i < data.size(); i++) {
            sb.append("────");
        }
        sb.append("\n     ");
        
        // Labels
        for (Map<String, Object> item : data) {
            String label = item.get("label").toString();
            sb.append(String.format("%-4s", truncate(label, 4)));
        }
        sb.append("\n");
        
        return sb.toString();
    }
    
    /**
     * Genera un gráfico de comparación (antes/después)
     * 
     * @param label Etiqueta del ítem
     * @param value1 Valor 1
     * @param value2 Valor 2
     * @param label1 Etiqueta para valor 1
     * @param label2 Etiqueta para valor 2
     * @param maxWidth Ancho máximo
     * @return String con el gráfico formateado
     */
    public static String comparisonChart(String label, double value1, double value2, 
                                        String label1, String label2, int maxWidth) {
        StringBuilder sb = new StringBuilder();
        
        double maxValue = Math.max(value1, value2);
        if (maxValue == 0) maxValue = 1.0;
        
        int bar1Length = (int) ((value1 / maxValue) * maxWidth);
        int bar2Length = (int) ((value2 / maxValue) * maxWidth);
        
        sb.append(String.format("\n%s:\n", label));
        sb.append(String.format("%-10s │", label1));
        for (int i = 0; i < bar1Length; i++) sb.append(BLOCK_FULL);
        sb.append(String.format(" %s\n", formatValue(value1)));
        
        sb.append(String.format("%-10s │", label2));
        for (int i = 0; i < bar2Length; i++) sb.append(BLOCK_FULL);
        sb.append(String.format(" %s\n", formatValue(value2)));
        
        // Calcular diferencia
        double diff = value2 - value1;
        double percentChange = value1 != 0 ? (diff / value1) * 100 : 0;
        String arrow = diff > 0 ? "↑" : (diff < 0 ? "↓" : "→");
        
        sb.append(String.format("\nCambio: %s %s (%.2f%%)\n", arrow, formatValue(Math.abs(diff)), percentChange));
        
        return sb.toString();
    }
    
    /**
     * Genera un indicador de progreso (progress bar)
     * 
     * @param label Etiqueta
     * @param current Valor actual
     * @param target Valor objetivo
     * @param width Ancho de la barra
     * @return String con el indicador formateado
     */
    public static String progressBar(String label, double current, double target, int width) {
        StringBuilder sb = new StringBuilder();
        
        double percentage = target != 0 ? (current / target) * 100 : 0;
        int filledWidth = (int) ((current / target) * width);
        
        if (filledWidth > width) filledWidth = width;
        
        sb.append(String.format("%s: [", label));
        
        for (int i = 0; i < filledWidth; i++) {
            sb.append(BLOCK_FULL);
        }
        for (int i = filledWidth; i < width; i++) {
            sb.append(BLOCK_LIGHT);
        }
        
        sb.append(String.format("] %.1f%% (%s / %s)\n", 
            percentage, formatValue(current), formatValue(target)));
        
        return sb.toString();
    }
    
    /**
     * Genera un mini sparkline (gráfico de tendencia compacto)
     * 
     * @param values Lista de valores numéricos
     * @return String con el sparkline
     */
    public static String sparkline(List<Double> values) {
        if (values == null || values.isEmpty()) {
            return "";
        }
        
        String[] blocks = {"▁", "▂", "▃", "▄", "▅", "▆", "▇", "█"};
        
        double min = values.stream().mapToDouble(Double::doubleValue).min().orElse(0);
        double max = values.stream().mapToDouble(Double::doubleValue).max().orElse(1);
        double range = max - min;
        
        if (range == 0) range = 1.0;
        
        StringBuilder sb = new StringBuilder();
        for (Double value : values) {
            int index = (int) (((value - min) / range) * (blocks.length - 1));
            sb.append(blocks[index]);
        }
        
        return sb.toString();
    }
    
    // ==================== MÉTODOS AUXILIARES ====================
    
    private static double getNumericValue(Object value) {
        if (value == null) return 0.0;
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).doubleValue();
        }
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    private static String formatValue(double value) {
        if (value >= 1000000) {
            return String.format("%.2fM", value / 1000000);
        } else if (value >= 1000) {
            return String.format("%.2fK", value / 1000);
        } else if (value == (long) value) {
            return String.format("%d", (long) value);
        } else {
            return String.format("%.2f", value);
        }
    }
    
    private static String truncate(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 2) + "..";
    }
}
