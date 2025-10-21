package reposteria.logica;

import reposteria.persistencia.dao.TransaccionDAO;
import reposteria.persistencia.dao.impl.TransaccionDAOImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransaccionService {
    private final TransaccionDAO transaccionDAO;

    public TransaccionService(Connection conn) {
        this.transaccionDAO = new TransaccionDAOImpl(conn);
    }

    public void registrarEgreso(double monto, String descripcion) throws SQLException {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        transaccionDAO.registrar("egreso", monto, descripcion, fecha);
    }

    public void generarReporte(String desde, String hasta) throws SQLException {
        double[] reporte = transaccionDAO.generarReporte(desde, hasta);
        System.out.printf("Reporte: Ingresos %.2f, Egresos %.2f, Balance %.2f%n",
                reporte[0], reporte[1], reporte[2]);
    }
}