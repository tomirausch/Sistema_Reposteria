package reposteria.presentacion;

import reposteria.logica.ClienteService;
import reposteria.logica.PedidoService;
import reposteria.logica.ProductoService;
import reposteria.logica.TransaccionService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Consola {
    private ClienteService clienteService;
    private ProductoService productoService;
    private PedidoService pedidoService;
    private TransaccionService transaccionService;

    public Consola(Connection conn) {
        this.clienteService = new ClienteService(conn);
        this.productoService = new ProductoService(conn);
        this.pedidoService = new PedidoService(conn);
        this.transaccionService = new TransaccionService(conn);
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
                        System.out.print("Teléfono: ");
                        String telefono = scanner.nextLine();
                        System.out.print("Dirección: ");
                        String direccion = scanner.nextLine();
                        clienteService.agregarCliente(nombre, telefono, direccion);
                        break;
                    case 2:
                        clienteService.listarClientes();
                        break;
                    case 3:
                        System.out.print("Nombre: ");
                        String prodNombre = scanner.nextLine();
                        System.out.print("Precio: ");
                        double precio = scanner.nextDouble();
                        System.out.print("Stock: ");
                        int stock = scanner.nextInt();
                        productoService.agregarProducto(prodNombre, precio, stock);
                        break;
                    case 4:
                        productoService.listarProductos();
                        break;
                    case 5:
                        System.out.print("ID Cliente: ");
                        int clienteId = scanner.nextInt();
                        List<int[]> detalles = new ArrayList<>();
                        System.out.print("Cantidad de productos en el pedido: ");
                        int numDetalles = scanner.nextInt();
                        for (int i = 0; i < numDetalles; i++) {
                            System.out.print("ID Producto: ");
                            int prodId = scanner.nextInt();
                            System.out.print("Cantidad: ");
                            int cant = scanner.nextInt();
                            detalles.add(new int[]{prodId, cant});
                        }
                        pedidoService.crearPedido(clienteId, detalles);
                        break;
                    case 6:
                        pedidoService.listarPedidos();
                        break;
                    case 7:
                        System.out.print("Monto: ");
                        double monto = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.print("Descripción: ");
                        String desc = scanner.nextLine();
                        transaccionService.registrarEgreso(monto, desc);
                        break;
                    case 8:
                        System.out.print("Fecha inicio (YYYY-MM-DD HH:MM:SS) o enter para todos: ");
                        String desde = scanner.nextLine();
                        System.out.print("Fecha fin (YYYY-MM-DD HH:MM:SS) o enter para todos: ");
                        String hasta = scanner.nextLine();
                        transaccionService.generarReporte(desde.isEmpty() ? null : desde, hasta.isEmpty() ? null : hasta);
                        break;
                    case 9:
                        scanner.close();
                        return;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}