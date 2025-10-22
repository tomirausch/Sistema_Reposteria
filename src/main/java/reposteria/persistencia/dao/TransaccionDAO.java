package reposteria.persistencia.dao;

import reposteria.logica.Transaccion;
import java.sql.SQLException;
import java.util.List;

public interface TransaccionDAO {
    void registrar(Transaccion transaccion) throws SQLException;
    double[] generarReporte(String desde, String hasta) throws SQLException;
}