package reposteria.logica;

import reposteria.logica.Excepciones.ValidationException;
import reposteria.persistencia.dao.*;
import reposteria.persistencia.dao.impl.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PedidoService {
    private final PedidoDAO pedidoDAO;
    private final ProductoService productoService;
    private final TransaccionDAO transaccionDAO;
    private final ClienteDAO clienteDAO;
    private final ProductoDAO productoDAO;

    public PedidoService(Connection conn) {
        this.pedidoDAO = new PedidoDAOImpl(conn);
        this.productoService = new ProductoService(conn);
        this.transaccionDAO = new TransaccionDAOImpl(conn);
        this.clienteDAO = new ClienteDAOImpl(conn);
        this.productoDAO = new ProductoDAOImpl(conn);
    }

    public void crearPedido(Pedido pedido) throws SQLException, ValidationException {
        // Validar cliente
        List<Cliente> clientes = clienteDAO.listar();
        boolean clienteExiste = clientes.stream().anyMatch(c -> c.getId() == pedido.getClienteId());
        if (!clienteExiste) 
            throw new ValidationException("El cliente con ID " + pedido.getClienteId() + " no existe.");
        
        // Validar detalles
        List<DetallePedido> detalles = pedido.getDetalles();
        if (detalles == null || detalles.isEmpty()) 
            throw new ValidationException("El pedido debe incluir al menos un producto.");
        
        List<Producto> productos = productoDAO.listar();
        for (DetallePedido det : detalles) {
            int prodId = det.getProductoId();
            int cant = det.getCantidad();
            if (cant <= 0) {
                throw new ValidationException("La cantidad del producto ID " + prodId + " debe ser mayor que 0.");
            }
            boolean productoExiste = productos.stream().anyMatch(p -> p.getId() == prodId);
            if (!productoExiste) {
                throw new ValidationException("El producto con ID " + prodId + " no existe.");
            }
        }

        int pedidoId = pedidoDAO.crear(pedido);
        double total = 0;

        for (DetallePedido det : detalles) {
            int prodId = det.getProductoId();
            int cant = det.getCantidad();
            double precio = productoService.getPrecio(prodId);
            double subtotal = precio * cant;
            det.pedidoIdProperty().set(pedidoId); // Usar propiedad JavaFX
            det.subtotalProperty().set(subtotal); // Usar propiedad JavaFX
            total += subtotal;
            pedidoDAO.agregarDetalle(det);
        }

        pedidoDAO.actualizarTotal(pedidoId, total);
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        transaccionDAO.registrar(new Transaccion(0, "ingreso", total, "Pedido ID " + pedidoId, fecha));
        System.out.println("Pedido creado.");
    }

    public List<Pedido> listarPedidos() throws SQLException {
        return pedidoDAO.listar();
    }
}