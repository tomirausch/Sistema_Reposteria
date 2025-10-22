package reposteria.persistencia.dao;

import reposteria.logica.Pedido;
import reposteria.logica.DetallePedido;
import java.sql.SQLException;
import java.util.List;

public interface PedidoDAO {
    int crear(Pedido pedido) throws SQLException;
    void agregarDetalle(DetallePedido detalle) throws SQLException;
    void actualizarTotal(int pedidoId, double total) throws SQLException;
    List<Pedido> listar() throws SQLException;
}