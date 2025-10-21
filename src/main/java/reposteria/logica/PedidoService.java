package reposteria.logica;

import reposteria.persistencia.dao.PedidoDAO;
import reposteria.persistencia.dao.TransaccionDAO;
import reposteria.persistencia.dao.impl.PedidoDAOImpl;
import reposteria.persistencia.dao.impl.TransaccionDAOImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PedidoService {
    private final PedidoDAO pedidoDAO;
    private final ProductoService productoService;
    private final TransaccionDAO transaccionDAO;

    public PedidoService(Connection conn) {
        this.pedidoDAO = new PedidoDAOImpl(conn);
        this.productoService = new ProductoService(conn);
        this.transaccionDAO = new TransaccionDAOImpl(conn);
    }

    public void crearPedido(int clienteId, List<int[]> detalles) throws SQLException {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        double total = 0;

        int pedidoId = pedidoDAO.crear(clienteId, fecha, 0);

        for (int[] det : detalles) {
            int prodId = det[0];
            int cant = det[1];
            double precio = productoService.getPrecio(prodId);
            double subtotal = precio * cant;
            total += subtotal;

            pedidoDAO.agregarDetalle(pedidoId, prodId, cant, subtotal);
            productoService.actualizarStock(prodId, cant);
        }

        pedidoDAO.actualizarTotal(pedidoId, total);
        transaccionDAO.registrar("ingreso", total, "Pedido ID " + pedidoId, fecha);
        System.out.println("Pedido creado.");
    }

    public void listarPedidos() throws SQLException {
        ResultSet rs = pedidoDAO.listar();
        while (rs.next()) {
            System.out.println("Pedido ID: " + rs.getInt("id") + ", Cliente ID: " + rs.getInt("cliente_id") + ", Total: " + rs.getDouble("total"));
        }
    }
}