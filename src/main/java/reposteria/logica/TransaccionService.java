package reposteria.logica;

import reposteria.persistencia.dao.TransaccionDAO;
import reposteria.persistencia.dao.impl.TransaccionDAOImpl;

import java.sql.Connection;
import java.sql.SQLException;

public class TransaccionService {
    private final TransaccionDAO transaccionDAO;

    public TransaccionService(Connection conn) {
        this.transaccionDAO = new TransaccionDAOImpl(conn);
    }

    public void registrarEgreso(Transaccion transaccion) throws SQLException, ValidationException {
        if (transaccion.getMonto() <= 0) {
            throw new ValidationException("El monto debe ser mayor que 0.");
        }
        if (transaccion.getDescripcion() == null || transaccion.getDescripcion().trim().isEmpty()) {
            throw new ValidationException("La descripción no puede estar vacía.");
        }
        if (transaccion.getDescripcion().length() < 3) {
            throw new ValidationException("La descripción debe tener al menos 3 caracteres.");
        }
        if (!"egreso".equals(transaccion.getTipo())) {
            throw new ValidationException("El tipo debe ser 'egreso'.");
        }

        transaccionDAO.registrar(transaccion);
    }

    public double[] generarReporte(String desde, String hasta) throws SQLException {
        return transaccionDAO.generarReporte(desde, hasta);
    }
}