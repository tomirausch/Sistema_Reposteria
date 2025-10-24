package reposteria.persistencia.dao;

import reposteria.logica.Producto;
import java.sql.SQLException;
import java.util.List;

public interface ProductoDAO {
    void agregar(Producto producto) throws SQLException;
    List<Producto> listar() throws SQLException;
    double getPrecio(int idProducto) throws SQLException;
    void modificar(Producto producto) throws SQLException;
    void reactivar(Producto producto) throws SQLException;
}