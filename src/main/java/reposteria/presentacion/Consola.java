package reposteria.presentacion;

import reposteria.logica.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Consola {
    private final GestorReposteria gestor;

    public Consola(Connection conn) {
        this.gestor = GestorReposteria.getInstancia(conn);
    }

    public void iniciar() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Agregar Cliente");
            System.out.println("2. Listar Clientes");
            System.out.println("3. Agregar Producto");
            System.out.println("4. Listar Productos");
            System.out.println("5. Crear Pedido");
            System.out.println("6. Listar Pedidos");
            System.out.println("7. Registrar Egreso");
            System.out.println("8. Generar Reporte");
            System.out.println("9. Salir");
            System.out.print("Opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir salto de línea

            try {
                switch (opcion) {
                    case 1:
                        System.out.print("Nombre: ");
                        String nombre = scanner.nextLine();
                        System.out.print("Apellido: ");
                        String apellido = scanner.nextLine();
                        System.out.print("Teléfono: ");
                        String telefono = scanner.nextLine();
                        System.out.print("Dirección: ");
                        String direccion = scanner.nextLine();
                        gestor.agregarCliente(new Cliente(0, nombre, apellido, telefono, direccion));
                        break;
                    case 2:
                        List<Cliente> clientes = gestor.listarClientes();
                        for (Cliente c : clientes) {
                            System.out.println("Cliente: " + c.getNombreCompleto() + ", Tel: " + c.getTelefono());
                        }
                        break;
                    case 3:
                        System.out.print("Nombre: ");
                        String prodNombre = scanner.nextLine();
                        System.out.print("Precio: ");
                        double precio = scanner.nextDouble();
                        gestor.agregarProducto(new Producto(0, prodNombre, precio));
                        break;
                    case 4:
                        List<Producto> productos = gestor.listarProductos();
                        for (Producto p : productos) {
                            System.out.println("Producto: " + p.getNombre() + ", Precio: " + p.getPrecio());
                        }
                        break;
                    case 5:
                        System.out.print("ID Cliente: ");
                        int clienteId = scanner.nextInt();
                        List<DetallePedido> detalles = new ArrayList<>();
                        System.out.print("Cantidad de productos en el pedido: ");
                        int numDetalles = scanner.nextInt();
                        for (int i = 0; i < numDetalles; i++) {
                            System.out.print("ID Producto: ");
                            int prodId = scanner.nextInt();
                            System.out.print("Cantidad: ");
                            int cant = scanner.nextInt();
                            detalles.add(new DetallePedido(0, 0, prodId, cant, 0));
                        }
                        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        gestor.crearPedido(new Pedido(0, clienteId, fecha, 0, detalles));
                        break;
                    case 6:
                        List<Pedido> pedidos = gestor.listarPedidos();
                        for (Pedido p : pedidos) {
                            System.out.println("Pedido ID: " + p.getId() + ", Cliente ID: " + p.getClienteId() + ", Total: " + p.getTotal());
                        }
                        break;
                    case 7:
                        System.out.print("Monto: ");
                        double monto = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.print("Descripción: ");
                        String desc = scanner.nextLine();
                        String fechaEgreso = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        gestor.registrarEgreso(new Transaccion(0, "egreso", monto, desc, fechaEgreso));
                        break;
                    case 8:
                        System.out.print("Fecha inicio (YYYY-MM-DD HH:MM:SS) o enter para todos: ");
                        String desde = scanner.nextLine();
                        System.out.print("Fecha fin (YYYY-MM-DD HH:MM:SS) o enter para todos: ");
                        String hasta = scanner.nextLine();
                        double[] reporte = gestor.generarReporte(desde.isEmpty() ? null : desde, hasta.isEmpty() ? null : hasta);
                        System.out.printf("Reporte: Ingresos %.2f, Egresos %.2f, Balance %.2f%n",
                                reporte[0], reporte[1], reporte[2]);
                        break;
                    case 9:
                        scanner.close();
                        return;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (ValidationException e) {
                System.out.println("Error de validación: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
            }
        }
    }
}