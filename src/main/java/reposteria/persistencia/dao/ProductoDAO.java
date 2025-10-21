package reposteria.persistencia.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ProductoDAO {
    void agregar(String nombre, double precio, int stock) throws SQLException;
    ResultSet listar() throws SQLException;
    void actualizarStock(int idProducto, int cantidad) throws SQLException;
    double getPrecio(int idProducto) throws SQLException;
}