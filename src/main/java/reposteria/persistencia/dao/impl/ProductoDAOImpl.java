package reposteria.persistencia.dao.impl;

import reposteria.logica.Producto;
import reposteria.persistencia.dao.ProductoDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {
    private Connection conn;

    public ProductoDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void agregar(Producto producto) throws SQLException {
        String sql = "INSERT INTO productos (nombre, precio, unidad, medida) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, producto.getNombre());
            pstmt.setDouble(2, producto.getPrecio());
            pstmt.setString(3, producto.getUnidad());
            pstmt.setDouble(4, producto.getMedida());
            pstmt.executeUpdate();
            System.out.println("Producto agregado.");
        }
    }

    @Override
    public List<Producto> listar() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                productos.add(new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getString("unidad"),
                        rs.getDouble("medida")
                ));
            }
        }
        return productos;
    }

    @Override
    public double getPrecio(int idProducto) throws SQLException {
        String sql = "SELECT precio FROM productos WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idProducto);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getDouble("precio") : 0;
        }
    }
}