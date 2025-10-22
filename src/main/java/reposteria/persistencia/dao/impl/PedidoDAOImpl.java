package reposteria.persistencia.dao.impl;

import reposteria.logica.Pedido;
import reposteria.logica.DetallePedido;
import reposteria.persistencia.dao.PedidoDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {
    private Connection conn;

    public PedidoDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public int crear(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedidos (cliente_id, fecha, total) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, pedido.getClienteId());
            pstmt.setString(2, pedido.getFecha());
            pstmt.setDouble(3, pedido.getTotal());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    @Override
    public void agregarDetalle(DetallePedido detalle) throws SQLException {
        String sql = "INSERT INTO detalles_pedidos (pedido_id, producto_id, cantidad, subtotal) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, detalle.getPedidoId());
            pstmt.setInt(2, detalle.getProductoId());
            pstmt.setInt(3, detalle.getCantidad());
            pstmt.setDouble(4, detalle.getSubtotal());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void actualizarTotal(int pedidoId, double total) throws SQLException {
        String sql = "UPDATE pedidos SET total = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, total);
            pstmt.setInt(2, pedidoId);
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Pedido> listar() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                // Detalles no cargados aquí para simplicidad; cargar por separado si es necesario
                pedidos.add(new Pedido(
                        rs.getInt("id"),
                        rs.getInt("cliente_id"),
                        rs.getString("fecha"),
                        rs.getDouble("total"),
                        new ArrayList<>()
                ));
            }
        }
        return pedidos;
    }
}