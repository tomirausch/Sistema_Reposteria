package reposteria.logica;

import reposteria.persistencia.dao.ProductoDAO;
import reposteria.persistencia.dao.impl.ProductoDAOImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductoService {
    private final ProductoDAO productoDAO;

    public ProductoService(Connection conn) {
        this.productoDAO = new ProductoDAOImpl(conn);
    }

    public void agregarProducto(String nombre, double precio, int stock) throws SQLException {
        productoDAO.agregar(nombre, precio, stock);
    }

    public void listarProductos() throws SQLException {
        ResultSet rs = productoDAO.listar();
        while (rs.next()) {
            System.out.println("Producto: " + rs.getString("nombre") + ", Precio: " + rs.getDouble("precio") + ", Stock: " + rs.getInt("stock"));
        }
    }

    public void actualizarStock(int idProducto, int cantidad) throws SQLException {
        productoDAO.actualizarStock(idProducto, cantidad);
    }

    public double getPrecio(int idProducto) throws SQLException {
        return productoDAO.getPrecio(idProducto);
    }
}