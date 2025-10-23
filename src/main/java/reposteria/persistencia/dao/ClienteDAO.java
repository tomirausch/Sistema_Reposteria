package reposteria.persistencia.dao;

import reposteria.logica.Cliente;
import java.sql.SQLException;
import java.util.List;

public interface ClienteDAO {
    void agregar(Cliente cliente) throws SQLException;
    List<Cliente> listar() throws SQLException;
    void eliminar(int id) throws SQLException;
    void modificar(Cliente cliente) throws SQLException;
    void reactivar(Cliente cliente) throws SQLException;
}