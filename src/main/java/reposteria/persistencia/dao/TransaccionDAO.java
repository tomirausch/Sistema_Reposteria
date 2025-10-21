package reposteria.persistencia.dao;

import java.sql.SQLException;

public interface TransaccionDAO {
    void registrar(String tipo, double monto, String descripcion, String fecha) throws SQLException;
    double[] generarReporte(String desde, String hasta) throws SQLException;
}