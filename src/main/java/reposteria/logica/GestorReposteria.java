package reposteria.logica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import reposteria.logica.Excepciones.*;

public class GestorReposteria {
    private static GestorReposteria instancia;
    private final ClienteService clienteService;
    private final ProductoService productoService;
    private final PedidoService pedidoService;
    private final TransaccionService transaccionService;
    private final VarianteService varianteService;

    private GestorReposteria(Connection conn) {
        this.clienteService = new ClienteService(conn);
        this.productoService = new ProductoService(conn);
        this.pedidoService = new PedidoService(conn);
        this.transaccionService = new TransaccionService(conn);
        this.varianteService = new VarianteService(conn);
    }
    
    public static GestorReposteria getInstancia(Connection conn){
        if (instancia == null){
            instancia = new GestorReposteria(conn);
        }
        
        return instancia;
    }

    // Clientes
    public void agregarCliente(Cliente cliente) throws SQLException, ValidationException, TelefonoExistenteException {
        clienteService.agregarCliente(cliente);
    }

    public List<Cliente> listarClientes() throws SQLException {
        return clienteService.listarClientes();
    }
    public void eliminarCliente(int id) throws SQLException {
        clienteService.eliminarCliente(id);
    }

    public void modificarCliente(Cliente cliente) throws SQLException, ValidationException {
        clienteService.modificarCliente(cliente);
    }

    public void validarCliente(Cliente cliente) throws ValidationException {
        clienteService.clienteValido(cliente);
    }

    // Productos
    public void agregarProducto(Producto producto) throws SQLException, ValidationException {
        productoService.agregarProducto(producto);
    }

    public void modificarProducto(Producto producto) throws SQLException, ValidationException{
        productoService.modificarProducto(producto);
    }

    public void eliminarProducto(int id) throws SQLException, ValidationException {
        productoService.eliminarProducto(id);
    }

    public List<Producto> listarProductos() throws SQLException {
        return productoService.listarProductos();
    }

    public void validarProducto(Producto producto) throws ValidationException {
        productoService.productoValido(producto);
    }

    // Pedidos
    public void crearPedido(Pedido pedido) throws SQLException, ValidationException {
        pedidoService.crearPedido(pedido);
    }

    public List<Pedido> listarPedidos() throws SQLException {
        return pedidoService.listarPedidos();
    }

    public void registrarEgreso(Transaccion transaccion) throws SQLException, ValidationException {
        transaccionService.registrarEgreso(transaccion);
    }

    public double[] generarReporte(String desde, String hasta) throws SQLException {
        return transaccionService.generarReporte(desde, hasta);
    }

    // Variantes
    public void agregarVariante(Variante variante) throws SQLException, ValidationException {
        varianteService.agregarVariante(variante);
    }

    public List<Variante> listarVariantesPorProducto(int idProducto) throws SQLException {
        return varianteService.listarPorProducto(idProducto);
    }

    public void modificarVariante(Variante variante) throws SQLException, ValidationException {
        varianteService.modificarVariante(variante);
    }
    
    public void eliminarVariante(int idVariante) throws SQLException {
        varianteService.eliminarVariante(idVariante);
    }
}