package reposteria.persistencia.dao;

import java.sql.SQLException;
import java.util.List;

import reposteria.logica.Variante;

public interface VarianteDAO {
    void agregar(Variante variante) throws SQLException;
    List<Variante> listarPorProducto(int idProducto) throws SQLException;
    void modificar(Variante variante) throws SQLException;
    void eliminar(int idVariante) throws SQLException;
}
