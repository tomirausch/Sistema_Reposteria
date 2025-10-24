package reposteria.logica;

import reposteria.persistencia.dao.VarianteDAO;
import reposteria.persistencia.dao.impl.VarianteDAOImpl;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class VarianteService {
    private final VarianteDAO varianteDAO;

    public VarianteService(Connection conn) {
        this.varianteDAO = new VarianteDAOImpl(conn);
    }

    public void agregarVariante(Variante v) throws SQLException {
        varianteDAO.agregar(v);
    }

    public List<Variante> listarPorProducto(int idProducto) throws SQLException {
        return varianteDAO.listarPorProducto(idProducto);
    }

    public void modificarVariante(Variante v) throws SQLException {
        varianteDAO.modificar(v);
    }

    public void eliminarVariante(int idVariante) throws SQLException {
        varianteDAO.eliminar(idVariante);
    }
}
