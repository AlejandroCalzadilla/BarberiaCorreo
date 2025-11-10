package org.barberia.usuarios.servicioemail;

import java.util.ArrayList;
import java.util.List;

public class CommandHelp {

        private static class CommandExample {
                String operation;
                String entity;
                String description;
                String parameters;
                String example;

                public CommandExample(String operation, String entity, String description, String parameters,
                                String example) {
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
                                "id_categoria,codigo,nombre, descripcion, precio_compra,precio_venta, stock_actual, stock_minimo, imagenurl, unidad_medida",
                                "CREATEPRODUCTOS[1, SHP001, Shampoo, Shampoo para perros, 15.99, 25.99, 100, 20, https://example.com/shampoo.jpg, Litros]"));

                COMMANDS.add(new CommandExample(
                                "CREATE", "BARBEROS",
                                "Crear un nuevo barbero",
                                "id_usuario,especialidad,foto_perfil,",
                                "CREATEBARBEROS[1, Corte de cabello, Juan, Perez, 555-1234, juan.perez@example.com, Calle Falsa 123]"));

                COMMANDS.add(new CommandExample(
                                "CREATE", "SERVICIOS",
                                "Crear un nuevo servicio",
                                "id_usuario,especialidad,foto_perfil,",
                                "CREATESERVICIOS[1, Corte de cabello, Juan, Perez, 555-1234, juan.perez@example.com, Calle Falsa 123]"));

                COMMANDS.add(new CommandExample(
                                "CREATE",
                                "USUARIOS",
                                "Crear un nuevo Usuario",
                                "nombre, apellido, email , telefono , direccion , username, password",
                                "CREATEUSUARIOS[juan , perez, juanperez@gmail.com , 79845888, calle falsa 123, juanp, passowrd  ]"));

                COMMANDS.add(new CommandExample(
                                "CREATE",
                                "HORARIOS",
                                "Crear un nuevo hoario para un barbero",
                                "id_barbero, dia_semana, hora_inicio, hora_fin",
                                "CREATEHORARIOS[1, lunes , 10:30 , 11::30]"));
                
                COMMANDS.add(new CommandExample("CREATE",
                                "SERVICIOSPRODUCTOS",
                                "crear un nuevo servicio producto ",
                                "id_servicio ,id_producto ,cantidad ",
                                "CREATESERVICIOPRODUCTOS [1, 1, 2 ]"));

                COMMANDS.add(new CommandExample("CREATE",
                                "RESERVAS",
                                "crear una nueva reserva",
                                "id_cliente ,id_barbero ,id_servicio ,fecha_reserva ,hora_inicio, hora_fin,notas,  ",
                                "CREATERESERVAS[1, 1, 1, 2024-12-01, 10:00, 11:00 ,reserva para corte de cabello]"));

                // COMMANDS.add(null)

                // Agregar más UPDATE commands siguiendo el mismo patrón...
                COMMANDS.add(new CommandExample(
                                "UPDATE", "CATEGORIAS",
                                "Actualiza una  categoría",
                                "id_categoria , nombre, descripcion",
                                "UPDATETEGORIAS[id_categoria, Shampoo, Productos para el cuidado del cabello]"));

                COMMANDS.add(new CommandExample(
                                "UPDATE", "CLIENTES",
                                "Actualizar un cliente",
                                "id_cliente, id_usuario, fecha_nacimiento, ci",
                                "UPDATECLIENTES[ 1 ,1, 1990-01-01, 12345678]"));

                COMMANDS.add(new CommandExample(
                                "UPDATE", "PRODUCTOS",
                                "Actualizar un producto",
                                "id_producto, id_categoria,codigo,nombre, descripcion, precio_compra,precio_venta, stock_actual, stock_minimo, imagenurl, unidad_medida",
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
                                "UPDATE",
                                "USUARIOS",
                                "Actualizar un Usuario",
                                "id_usuario ,nombre, apellido, email , telefono , direccion , username, password",
                                "UPDATEUSUARIOS[1, juan , perez, juanperez@gmail.com , 79845888, calle falsa 123, juanp, passowrd  ]"));

                COMMANDS.add(new CommandExample(
                                "UPDATE",
                                "HORARIOS",
                                "Actualizar un horario para un barbero",
                                "id_horario, id_barbero, dia_semana, hora_inicio, hora_fin",
                                "UPDATEHORARIOS [1 ,1, lunes , 10:30 , 11::30] "));
                COMMANDS.add(new CommandExample(
                                "UPDATE",
                                "PAGOS",
                                "Actualizar un pago",
                                "id_reserva ,metodo_pago(efectivo,transferencia,otro), tipo_pago() ,notas ",
                                "UPDATEPAGOS[1, tarjeta, servicio, pago por corte de cabello]"));

                COMMANDS.add(new CommandExample(
                                "UPDATE",
                                "SERVICIOSPRODUCTOS",
                                "Actualizar un servicio producto ",
                                "id_servicio ,id_producto ,cantidad ",
                                "1, 1, 2"));
                COMMANDS.add(new CommandExample(
                                "UPDATE",
                                "RESERVAS",
                                "Actualizar una reserva",
                                "id_cliente ,id_barbero ,id_servicio ,fecha_reserva ,hora_inicio, hora_fin,notas ",
                                "UPDATERESERVAS[1, 1, 1, 2024-12-01, 10:00, 11:00 ,reserva para corte de cabello]"));
                COMMANDS.add(new CommandExample("CREATE",
                                "PAGOS",
                                "actualizar un  pago ",
                                "id_pago,id_reserva ,metodo_pago(efectivo,transferencia,otro), tipo_pago() ,notas ",
                                "CREATEPAGOS[1,1, tarjeta, servicio, pago por corte de cabello]"));

        }

        public static String obtenerComandosDisponibles() {
                StringBuilder sb = new StringBuilder();

                // Encabezado
                sb.append("═══════════════════════════════════════════════════════════════════════════════\n");
                sb.append("                    COMANDOS DISPONIBLES DEL SISTEMA                           \n");
                sb.append("═══════════════════════════════════════════════════════════════════════════════\n\n");

                // Comandos generales
                sb.append("COMANDOS GENERALES:\n");
                sb.append("  • LISTAR<entidad>[*]              - Listar todos los registros\n");
                sb.append("  • CREATE<entidad>[params...]      - Crear un nuevo registro\n");
                sb.append("  • UPDATE<entidad>[id, params...]  - Actualizar un registro\n");
                sb.append("  • DELETE<entidad>[id]             - Eliminar un registro\n");
                sb.append("  • GET<entidad>[id]                - Obtener un registro por ID\n");
                sb.append("  • HELP                            - Mostrar esta ayuda\n\n");

                // Entidades disponibles
                sb.append("ENTIDADES DISPONIBLES:\n");
                sb.append("  USUARIOS, CLIENTES, BARBEROS,CATEGORIAS, PRODUCTOS, SERVICIOS,\n");
                sb.append("  HORARIOS, SERVICIOPRODUCTOS, RESERVAS, PAGOS,\n");
                

                // Agrupar comandos por operación
                sb.append("═══════════════════════════════════════════════════════════════════════════════\n");
                sb.append("                           COMANDOS CREATE                                      \n");
                sb.append("═══════════════════════════════════════════════════════════════════════════════\n\n");

                for (CommandExample cmd : COMMANDS) {
                        if (cmd.operation.equals("CREATE")) {
                                sb.append(formatCommand(cmd));
                        }
                }

                sb.append("\n═══════════════════════════════════════════════════════════════════════════════\n");
                sb.append("                           COMANDOS UPDATE                                      \n");
                sb.append("═══════════════════════════════════════════════════════════════════════════════\n\n");

                for (CommandExample cmd : COMMANDS) {
                        if (cmd.operation.equals("UPDATE")) {
                                sb.append(formatCommand(cmd));
                        }
                }

                return sb.toString();
        }

        private static String formatCommand(CommandExample cmd) {
                StringBuilder sb = new StringBuilder();
                sb.append("┌─ ").append(cmd.operation).append(" ").append(cmd.entity).append("\n");
                sb.append("│  Descripción: ").append(cmd.description).append("\n");
                sb.append("│  Parámetros:  ").append(cmd.parameters).append("\n");
                sb.append("│  Ejemplo:     ").append(cmd.example).append("\n");
                sb.append("└").append("─".repeat(75)).append("\n\n");
                return sb.toString();
        }

        public static String buscarComando(String entidad, String operacion) {
                for (CommandExample cmd : COMMANDS) {
                        if (cmd.entity.equalsIgnoreCase(entidad) && cmd.operation.equalsIgnoreCase(operacion)) {
                                return formatCommand(cmd);
                        }
                }
                return "Comando no encontrado para " + operacion + " " + entidad;
        }
}