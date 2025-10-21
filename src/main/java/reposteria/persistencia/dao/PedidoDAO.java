package reposteria.persistencia.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface PedidoDAO {
    int crear(int clienteId, String fecha, double total) throws SQLException;
    void agregarDetalle(int pedidoId, int productoId, int cantidad, double subtotal) throws SQLException;
    void actualizarTotal(int pedidoId, double total) throws SQLException;
    ResultSet listar() throws SQLException;
}