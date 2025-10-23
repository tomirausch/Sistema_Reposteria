package reposteria.presentacion;

import reposteria.logica.*;
import reposteria.logica.Excepciones.TelefonoExistenteException;
import reposteria.logica.Excepciones.ValidationException;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Consola {
    private final GestorReposteria gestor;
    private final Scanner scanner = new Scanner(System.in);

    public Consola(Connection conn) {
        this.gestor = GestorReposteria.getInstancia(conn);
    }

    public void iniciar() {
        while (true) {
            limpiarConsola();

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
                        limpiarConsola();
                        agregarCliente();
                        presionarEnterContinuar();
                        break;
                    case 2:
                        limpiarConsola();
                        listarClientes();
                        presionarEnterContinuar();
                        break;
                    case 3:
                        limpiarConsola();
                        agregarProducto();
                        presionarEnterContinuar();
                        break;
                    case 4:
                        limpiarConsola();
                        listarProducto();
                        presionarEnterContinuar();
                        break;
                    case 5:
                        limpiarConsola();
                        crearPedido();
                        presionarEnterContinuar();
                        break;
                    case 6:
                        limpiarConsola();
                        listarPedidos();
                        presionarEnterContinuar();
                        break;
                    case 7:
                        limpiarConsola();
                        registrarEgreso();
                        presionarEnterContinuar();
                        break;
                    case 8:
                        limpiarConsola();
                        generarReporte();
                        presionarEnterContinuar();
                        break;
                    case 9:
                        limpiarConsola();
                        System.out.println("||| H A S T A  P R O N T O |||");
                        presionarEnterContinuar();
                        scanner.close();
                        limpiarConsola();
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

    private void agregarCliente() throws SQLException, ValidationException, TelefonoExistenteException {
        System.out.println("///  NUEVO CLIENTE  ///");

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();

        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine();

        System.out.print("Dirección: ");
        String direccion = scanner.nextLine();

        gestor.agregarCliente(new Cliente(0, nombre, apellido, telefono, direccion));
    }

    private void listarClientes() throws SQLException, ValidationException {
        System.out.println("///  LISTA DE CLIENTES  ///");

        List<Cliente> clientes = gestor.listarClientes();
        System.out.println("");
        for (Cliente c : clientes) 
            System.out.println("Cliente: " + c.getNombreCompleto() + ", Tel: " + c.getTelefono());
    }

    private void agregarProducto() throws SQLException, ValidationException{
        System.out.println("///  NUEVO PRODUCTO  ///");

        System.out.print("Nombre: ");
        String prodNombre = scanner.nextLine();

        System.out.print("Precio: ");
        double precio = scanner.nextDouble();

        gestor.agregarProducto(new Producto(0, prodNombre, precio));
    }

    private void listarProducto() throws SQLException, ValidationException{
        System.out.println("///  LISTA DE PRODUCTOS  ///");

        List<Producto> productos = gestor.listarProductos();
        for (Producto p : productos) 
            System.out.println("Producto: " + p.getNombre() + ", Precio: " + p.getPrecio());
    }

    private void crearPedido() throws SQLException, ValidationException{
        System.out.println("///  NUEVO PEDIDO  ///");

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
    }

    private void listarPedidos() throws SQLException, ValidationException{
        System.out.println("///  LISTA DE PEDIDOS  ///");

        List<Pedido> pedidos = gestor.listarPedidos();
        for (Pedido p : pedidos) 
            System.out.println("Pedido ID: " + p.getId() + ", Cliente ID: " + p.getClienteId() + ", Total: " + p.getTotal());
    }

    private void registrarEgreso() throws SQLException, ValidationException{
        System.out.println("///  REGISTRAR EGRESO  ///");

        System.out.print("Monto: ");
        double monto = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Descripción: ");
        String desc = scanner.nextLine();

        String fechaEgreso = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        gestor.registrarEgreso(new Transaccion(0, "egreso", monto, desc, fechaEgreso));
    }

    private void generarReporte() throws SQLException, ValidationException{
        System.out.println("///  GENERAR REPORTE  ///");

        System.out.print("\nFecha inicio (YYYY-MM-DD HH:MM:SS) o enter para todos: ");
        String desde = scanner.nextLine();

        System.out.print("Fecha fin (YYYY-MM-DD HH:MM:SS) o enter para todos: ");
        String hasta = scanner.nextLine();

        double[] reporte = gestor.generarReporte(desde.isEmpty() ? null : desde, hasta.isEmpty() ? null : hasta);

        System.out.printf("\nReporte: Ingresos %.2f, Egresos %.2f, Balance %.2f%n",
                reporte[0], reporte[1], reporte[2]);
    }

    private void presionarEnterContinuar(){
        System.out.println("\nPresione Enter para continuar...");
        scanner.nextLine();
    }

    private void limpiarConsola(){
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Ignorar errores al limpiar la consola
        }
    }
}