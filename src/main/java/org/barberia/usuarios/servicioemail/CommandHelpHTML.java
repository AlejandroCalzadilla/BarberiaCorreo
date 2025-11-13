package org.barberia.usuarios.servicioemail;

import java.util.ArrayList;
import java.util.List;

public class CommandHelpHTML {

    private static class CommandExample {
        String operation;
        String entity;
        String description;
        String parameters;
        String example;

        public CommandExample(String operation, String entity, String description, String parameters, String example) {
            this.operation = operation;
            this.entity = entity;
            this.description = description;
            this.parameters = parameters;
            this.example = example;
        }
    }

    private static final List<CommandExample> COMMANDS = new ArrayList<>();

    static {
        // CREATE commands
        COMMANDS.add(new CommandExample(
                "CREATE", "CATEGORIAS",
                "Crear una nueva categoría",
                "nombre, descripcion",
                "CREATECATEGORIAS[Shampoo, Productos para el cuidado del cabello]"));

        COMMANDS.add(new CommandExample(
                "CREATE", "CLIENTES",
                "Crear un nuevo cliente",
                "id_usuario, fecha_nacimiento, ci",
                "CREATECLIENTES[1, 1990-01-01, 12345678]"));

        COMMANDS.add(new CommandExample(
                "CREATE", "PRODUCTOS",
                "Crear un nuevo producto",
                "id_categoria, codigo, nombre, descripcion, precio_compra, precio_venta, stock_actual, stock_minimo, imagenurl, unidad_medida",
                "CREATEPRODUCTOS[1, SHP001, Shampoo, Shampoo para perros, 15.99, 25.99, 100, 20, https://example.com/shampoo.jpg, Litros]"));

        COMMANDS.add(new CommandExample(
                "CREATE", "BARBEROS",
                "Crear un nuevo barbero",
                "id_usuario, especialidad, foto_perfil",
                "CREATEBARBEROS[1, Corte de cabello, Juan, Perez, 555-1234, juan.perez@example.com, Calle Falsa 123]"));

        COMMANDS.add(new CommandExample(
                "CREATE", "SERVICIOS",
                "Crear un nuevo servicio",
                "nombre, descripcion, precio, duracion",
                "CREATESERVICIOS[Corte de cabello, Servicio profesional, 50.00, 45]"));

        COMMANDS.add(new CommandExample(
                "CREATE", "USUARIOS",
                "Crear un nuevo Usuario",
                "nombre, apellido, email, telefono, direccion, username, password",
                "CREATEUSUARIOS[juan, perez, juanperez@gmail.com, 79845888, calle falsa 123, juanp, password]"));

        COMMANDS.add(new CommandExample(
                "CREATE", "HORARIOS",
                "Crear un nuevo horario para un barbero",
                "id_barbero, dia_semana, hora_inicio, hora_fin",
                "CREATEHORARIOS[1, lunes, 10:30, 11:30]"));

        COMMANDS.add(new CommandExample(
                "CREATE", "SERVICIOSPRODUCTOS",
                "Crear un nuevo servicio producto",
                "id_servicio, id_producto, cantidad",
                "CREATESERVICIOPRODUCTOS[1, 1, 2]"));

        COMMANDS.add(new CommandExample(
                "CREATE", "RESERVAS",
                "Crear una nueva reserva (valida barbero activo y horario)",
                "id_cliente, id_barbero, id_servicio, fecha_reserva(YYYY-MM-DD), hora_inicio(HH:MM), hora_fin(HH:MM), notas",
                "CREATERESERVAS[1, 1, 1, 2024-12-01, 10:00, 11:00, reserva para corte de cabello]"));

        // UPDATE commands
        COMMANDS.add(new CommandExample(
                "UPDATE", "CATEGORIAS",
                "Actualiza una categoría",
                "id_categoria, nombre, descripcion",
                "UPDATECATEGORIAS[1, Shampoo, Productos para el cuidado del cabello]"));

        COMMANDS.add(new CommandExample(
                "UPDATE", "CLIENTES",
                "Actualizar un cliente",
                "id_cliente, id_usuario, fecha_nacimiento, ci",
                "UPDATECLIENTES[1, 1, 1990-01-01, 12345678]"));

        COMMANDS.add(new CommandExample(
                "UPDATE", "PRODUCTOS",
                "Actualizar un producto",
                "id_producto, id_categoria, codigo, nombre, descripcion, precio_compra, precio_venta, stock_actual, stock_minimo, imagenurl, unidad_medida",
                "UPDATEPRODUCTOS[1, 1, SHP001, Shampoo, Shampoo para perros, 15.99, 25.99, 100, 20, https://example.com/shampoo.jpg, Litros]"));

        COMMANDS.add(new CommandExample(
                "UPDATE", "BARBEROS",
                "Actualizar un barbero",
                "id_barbero, id_usuario, especialidad, foto_perfil",
                "UPDATEBARBEROS[1, 1, Corte de cabello, juan.perez@example.com, Calle Falsa 123]"));

        COMMANDS.add(new CommandExample(
                "UPDATE", "SERVICIOS",
                "Actualizar un servicio",
                "id_servicio, nombre, descripcion, precio",
                "UPDATESERVICIOS[1, Corte de cabello, Servicio de corte de cabello, 20.00]"));

        COMMANDS.add(new CommandExample(
                "UPDATE", "USUARIOS",
                "Actualizar un Usuario",
                "id_usuario, nombre, apellido, email, telefono, direccion, username, password",
                "UPDATEUSUARIOS[1, juan, perez, juanperez@gmail.com, 79845888, calle falsa 123, juanp, password]"));

        COMMANDS.add(new CommandExample(
                "UPDATE", "HORARIOS",
                "Actualizar un horario para un barbero",
                "id_horario, id_barbero, dia_semana, hora_inicio, hora_fin",
                "UPDATEHORARIOS[1, 1, lunes, 10:30, 11:30]"));

        COMMANDS.add(new CommandExample(
                "UPDATE", "SERVICIOSPRODUCTOS",
                "Actualizar un servicio producto",
                "id_servicio, id_producto, cantidad",
                "UPDATESERVICIOPRODUCTOS[1, 1, 2]"));

        COMMANDS.add(new CommandExample(
                "UPDATE", "RESERVAS",
                "Actualizar una reserva (valida barbero activo y horario si cambian)",
                "id_reserva, id_cliente, id_barbero, id_servicio, fecha_reserva(YYYY-MM-DD), hora_inicio(HH:MM), hora_fin(HH:MM), notas, estado",
                "UPDATERESERVAS[1, 1, 1, 1, 2024-12-01, 10:00, 11:00, reserva para corte de cabello, completada]"));

        COMMANDS.add(new CommandExample(
                "UPDATE", "PAGOS",
                "Actualizar un pago",
                "id_pago, id_reserva, metodo_pago, tipo_pago, notas",
                "UPDATEPAGOS[1, 1, tarjeta, servicio, pago por corte de cabello]"));
    }

    public static String obtenerComandosDisponibles() {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n<head>\n");
        html.append("<meta charset='UTF-8'>\n");
        html.append("<style>\n");
        html.append("body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }\n");
        html.append(".container { max-width: 900px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }\n");
        html.append("h1 { color: #2c3e50; text-align: center; border-bottom: 3px solid #3498db; padding-bottom: 15px; }\n");
        html.append("h2 { color: #34495e; margin-top: 30px; border-left: 4px solid #3498db; padding-left: 15px; }\n");
        html.append("h3 { color: #16a085; margin-top: 25px; }\n");
        html.append(".command-box { background: #ecf0f1; border-left: 4px solid #3498db; padding: 15px; margin: 15px 0; border-radius: 5px; }\n");
        html.append(".command-title { font-weight: bold; color: #2980b9; font-size: 1.1em; margin-bottom: 8px; }\n");
        html.append(".command-desc { color: #555; margin: 5px 0; }\n");
        html.append(".command-params { color: #7f8c8d; font-size: 0.9em; margin: 5px 0; }\n");
        html.append(".command-example { background: #fff; padding: 10px; border-radius: 3px; font-family: monospace; color: #c0392b; margin-top: 8px; border: 1px solid #ddd; }\n");
        html.append("ul { list-style-type: none; padding-left: 0; }\n");
        html.append("li { background: #fff; margin: 8px 0; padding: 10px; border-radius: 5px; border-left: 3px solid #95a5a6; }\n");
        html.append(".entity-list { background: #e8f4f8; padding: 15px; border-radius: 5px; margin: 10px 0; }\n");
        html.append(".report-box { background: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 10px 0; border-radius: 5px; }\n");
        html.append("</style>\n");
        html.append("</head>\n<body>\n");
        html.append("<div class='container'>\n");

        // Encabezado
        html.append("<h1>📋 COMANDOS DISPONIBLES DEL SISTEMA</h1>\n");

        // Comandos generales
        html.append("<h2>⚙️ COMANDOS GENERALES</h2>\n");
        html.append("<ul>\n");
        html.append("<li><strong>LISTAR&lt;entidad&gt;[*]</strong> - Listar todos los registros</li>\n");
        html.append("<li><strong>CREATE&lt;entidad&gt;[params...]</strong> - Crear un nuevo registro</li>\n");
        html.append("<li><strong>UPDATE&lt;entidad&gt;[id, params...]</strong> - Actualizar un registro</li>\n");
        html.append("<li><strong>DELETE&lt;entidad&gt;[id]</strong> - Eliminar un registro</li>\n");
        html.append("<li><strong>GET&lt;entidad&gt;[id]</strong> - Obtener un registro por ID</li>\n");
        html.append("<li><strong>HELP</strong> - Mostrar esta ayuda</li>\n");
        html.append("</ul>\n");

        // Entidades disponibles
        html.append("<h2>🗂️ ENTIDADES DISPONIBLES</h2>\n");
        html.append("<div class='entity-list'>\n");
        html.append("<strong>USUARIOS</strong>, <strong>CLIENTES</strong>, <strong>BARBEROS</strong>, <strong>CATEGORIAS</strong>, <strong>PRODUCTOS</strong>, <strong>SERVICIOS</strong>, <strong>HORARIOS</strong>, <strong>SERVICIOPRODUCTOS</strong>, <strong>RESERVAS</strong>, <strong>PAGOS</strong>\n");
        html.append("</div>\n");

        // Comandos de reportes
        html.append("<h2>📊 COMANDOS DE REPORTES</h2>\n");
        html.append(obtenerComandosReportesHTML());

        // Comandos CREATE
        html.append("<h2>➕ COMANDOS CREATE</h2>\n");
        for (CommandExample cmd : COMMANDS) {
            if (cmd.operation.equals("CREATE")) {
                html.append(formatCommandHTML(cmd));
            }
        }

        // Comandos UPDATE
        html.append("<h2>✏️ COMANDOS UPDATE</h2>\n");
        for (CommandExample cmd : COMMANDS) {
            if (cmd.operation.equals("UPDATE")) {
                html.append(formatCommandHTML(cmd));
            }
        }

        html.append("</div>\n</body>\n</html>");

        return html.toString();
    }

    private static String obtenerComandosReportesHTML() {
        StringBuilder html = new StringBuilder();

        // Dashboard
        html.append("<div class='report-box'>\n");
        html.append("<div class='command-title'>📈 REPORTEDASHBOARD</div>\n");
        html.append("<div class='command-desc'>Dashboard general del mes actual con todas las estadísticas</div>\n");
        html.append("<div class='command-params'>Parámetros: Ninguno</div>\n");
        html.append("<div class='command-example'>Ejemplo: REPORTEDASHBOARD</div>\n");
        html.append("</div>\n");

        // Ingresos
        html.append("<div class='report-box'>\n");
        html.append("<div class='command-title'>💰 REPORTEINGRESOS</div>\n");
        html.append("<div class='command-desc'>Obtiene los ingresos totales de un mes específico</div>\n");
        html.append("<div class='command-params'>Parámetros: año, mes</div>\n");
        html.append("<div class='command-example'>Ejemplo: REPORTEINGRESOS[2025, 10]</div>\n");
        html.append("</div>\n");

        // Ranking Barberos
        html.append("<div class='report-box'>\n");
        html.append("<div class='command-title'>🏆 REPORTERANKINGBARBEROS</div>\n");
        html.append("<div class='command-desc'>Ranking de barberos por ingresos en un período</div>\n");
        html.append("<div class='command-params'>Parámetros: fecha_inicio (YYYY-MM-DD), fecha_fin (YYYY-MM-DD)</div>\n");
        html.append("<div class='command-example'>Ejemplo: REPORTERANKINGBARBEROS[2025-10-01, 2025-10-31]</div>\n");
        html.append("</div>\n");

        // Servicios Populares
        html.append("<div class='report-box'>\n");
        html.append("<div class='command-title'>⭐ REPORTESERVICIOSPOPULARES</div>\n");
        html.append("<div class='command-desc'>Top de servicios más solicitados en un período</div>\n");
        html.append("<div class='command-params'>Parámetros: fecha_inicio, fecha_fin, limite</div>\n");
        html.append("<div class='command-example'>Ejemplo: REPORTESERVICIOSPOPULARES[2025-01-01, 2025-12-31, 5]</div>\n");
        html.append("</div>\n");

        // Clientes Frecuentes
        html.append("<div class='report-box'>\n");
        html.append("<div class='command-title'>👥 REPORTECLIENTESFRECUENTES</div>\n");
        html.append("<div class='command-desc'>Top de clientes con más visitas en un período</div>\n");
        html.append("<div class='command-params'>Parámetros: fecha_inicio, fecha_fin, limite</div>\n");
        html.append("<div class='command-example'>Ejemplo: REPORTECLIENTESFRECUENTES[2025-01-01, 2025-12-31, 10]</div>\n");
        html.append("</div>\n");

        // Distribución Estados
        html.append("<div class='report-box'>\n");
        html.append("<div class='command-title'>📊 REPORTEDISTRIBUCIONESTADOS</div>\n");
        html.append("<div class='command-desc'>Distribución de reservas por estado en un período</div>\n");
        html.append("<div class='command-params'>Parámetros: fecha_inicio, fecha_fin</div>\n");
        html.append("<div class='command-example'>Ejemplo: REPORTEDISTRIBUCIONESTADOS[2025-07-01, 2025-09-30]</div>\n");
        html.append("</div>\n");

        // Horas Pico
        html.append("<div class='report-box'>\n");
        html.append("<div class='command-title'>⏰ REPORTEHORASPICO</div>\n");
        html.append("<div class='command-desc'>Horas del día con más actividad en un período</div>\n");
        html.append("<div class='command-params'>Parámetros: fecha_inicio, fecha_fin</div>\n");
        html.append("<div class='command-example'>Ejemplo: REPORTEHORASPICO[2025-10-01, 2025-10-31]</div>\n");
        html.append("</div>\n");

        // Días Ocupados
        html.append("<div class='report-box'>\n");
        html.append("<div class='command-title'>📅 REPORTEDIASOCUPADOS</div>\n");
        html.append("<div class='command-desc'>Días de la semana con más reservas en un período</div>\n");
        html.append("<div class='command-params'>Parámetros: fecha_inicio, fecha_fin</div>\n");
        html.append("<div class='command-example'>Ejemplo: REPORTEDIASOCUPADOS[2025-10-01, 2025-10-31]</div>\n");
        html.append("</div>\n");

        // Métodos de Pago
        html.append("<div class='report-box'>\n");
        html.append("<div class='command-title'>💳 REPORTEMETODOSPAGO</div>\n");
        html.append("<div class='command-desc'>Distribución de pagos por método en un período</div>\n");
        html.append("<div class='command-params'>Parámetros: fecha_inicio, fecha_fin</div>\n");
        html.append("<div class='command-example'>Ejemplo: REPORTEMETODOSPAGO[2025-01-01, 2025-12-31]</div>\n");
        html.append("</div>\n");

        // Consumo Productos
        html.append("<div class='report-box'>\n");
        html.append("<div class='command-title'>📦 REPORTECONSUMOPRODUCTOS</div>\n");
        html.append("<div class='command-desc'>Consumo de productos en servicios durante un período</div>\n");
        html.append("<div class='command-params'>Parámetros: fecha_inicio, fecha_fin</div>\n");
        html.append("<div class='command-example'>Ejemplo: REPORTECONSUMOPRODUCTOS[2025-10-01, 2025-10-31]</div>\n");
        html.append("</div>\n");

        // Estadísticas Barbero
        html.append("<div class='report-box'>\n");
        html.append("<div class='command-title'>👨‍💼 REPORTEESTADISTICASBARBERO</div>\n");
        html.append("<div class='command-desc'>Estadísticas detalladas de un barbero específico</div>\n");
        html.append("<div class='command-params'>Parámetros: id_barbero, fecha_inicio, fecha_fin</div>\n");
        html.append("<div class='command-example'>Ejemplo: REPORTEESTADISTICASBARBERO[1, 2025-10-01, 2025-10-31]</div>\n");
        html.append("</div>\n");

        return html.toString();
    }

    private static String formatCommandHTML(CommandExample cmd) {
        StringBuilder html = new StringBuilder();
        
        html.append("<div class='command-box'>\n");
        html.append("<div class='command-title'>").append(cmd.operation).append(" ").append(cmd.entity).append("</div>\n");
        html.append("<div class='command-desc'>").append(cmd.description).append("</div>\n");
        html.append("<div class='command-params'>Parámetros: ").append(cmd.parameters).append("</div>\n");
        html.append("<div class='command-example'>").append(cmd.example).append("</div>\n");
        html.append("</div>\n");
        
        return html.toString();
    }

    public static String buscarComando(String entidad, String operacion) {
        for (CommandExample cmd : COMMANDS) {
            if (cmd.entity.equalsIgnoreCase(entidad) && cmd.operation.equalsIgnoreCase(operacion)) {
                return formatCommandHTML(cmd);
            }
        }
        return "<p style='color: red;'>Comando no encontrado para " + operacion + " " + entidad + "</p>";
    }
}
