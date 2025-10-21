package reposteria.persistencia.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ClienteDAO {
    void agregar(String nombre, String telefono, String direccion) throws SQLException;
    ResultSet listar() throws SQLException;
}