package reposteria.persistencia.dao.impl;

import reposteria.persistencia.dao.ProductoDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductoDAOImpl implements ProductoDAO {
    private Connection conn;

    public ProductoDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void agregar(String nombre, double precio, int stock) throws SQLException {
        String sql = "INSERT INTO productos (nombre, precio, stock) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setDouble(2, precio);
            pstmt.setInt(3, stock);
            pstmt.executeUpdate();
            System.out.println("Producto agregado.");
        }
    }

    @Override
    public ResultSet listar() throws SQLException {
        String sql = "SELECT * FROM productos";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        return pstmt.executeQuery();
    }

    @Override
    public void actualizarStock(int idProducto, int cantidad) throws SQLException {
        String sql = "UPDATE productos SET stock = stock - ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cantidad);
            pstmt.setInt(2, idProducto);
            pstmt.executeUpdate();
        }
    }

    @Override
    public double getPrecio(int idProducto) throws SQLException {
        String sql = "SELECT precio FROM productos WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idProducto);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getDouble(1) : 0;
        }
    }
}