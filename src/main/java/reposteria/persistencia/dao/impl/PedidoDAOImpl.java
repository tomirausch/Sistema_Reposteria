package reposteria.persistencia.dao.impl;

import reposteria.persistencia.dao.PedidoDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PedidoDAOImpl implements PedidoDAO {
    private Connection conn;

    public PedidoDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public int crear(int clienteId, String fecha, double total) throws SQLException {
        String sql = "INSERT INTO pedidos (cliente_id, fecha, total) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, clienteId);
            pstmt.setString(2, fecha);
            pstmt.setDouble(3, total);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    @Override
    public void agregarDetalle(int pedidoId, int productoId, int cantidad, double subtotal) throws SQLException {
        String sql = "INSERT INTO detalles_pedidos (pedido_id, producto_id, cantidad, subtotal) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pedidoId);
            pstmt.setInt(2, productoId);
            pstmt.setInt(3, cantidad);
            pstmt.setDouble(4, subtotal);
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
    public ResultSet listar() throws SQLException {
        String sql = "SELECT * FROM pedidos";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        return pstmt.executeQuery();
    }
}