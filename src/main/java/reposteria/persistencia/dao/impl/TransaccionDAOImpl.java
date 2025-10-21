package reposteria.persistencia.dao.impl;

import reposteria.persistencia.dao.TransaccionDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransaccionDAOImpl implements TransaccionDAO {
    private Connection conn;

    public TransaccionDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void registrar(String tipo, double monto, String descripcion, String fecha) throws SQLException {
        String sql = "INSERT INTO transacciones (tipo, monto, descripcion, fecha) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tipo);
            pstmt.setDouble(2, monto);
            pstmt.setString(3, descripcion);
            pstmt.setString(4, fecha);
            pstmt.executeUpdate();
            System.out.println("Transacción " + tipo + " registrada.");
        }
    }

    @Override
    public double[] generarReporte(String desde, String hasta) throws SQLException {
        double ingresos = 0, egresos = 0;

        String sqlIngresos = "SELECT SUM(monto) FROM transacciones WHERE tipo = 'ingreso'";
        if (desde != null && hasta != null) {
            sqlIngresos += " AND fecha BETWEEN ? AND ?";
        }
        try (PreparedStatement pstmtIngresos = conn.prepareStatement(sqlIngresos)) {
            if (desde != null && hasta != null) {
                pstmtIngresos.setString(1, desde);
                pstmtIngresos.setString(2, hasta);
            }
            ResultSet rsIngresos = pstmtIngresos.executeQuery();
            ingresos = rsIngresos.next() ? rsIngresos.getDouble(1) : 0;
        }

        String sqlEgresos = "SELECT SUM(monto) FROM transacciones WHERE tipo = 'egreso'";
        if (desde != null && hasta != null) {
            sqlEgresos += " AND fecha BETWEEN ? AND ?";
        }
        try (PreparedStatement pstmtEgresos = conn.prepareStatement(sqlEgresos)) {
            if (desde != null && hasta != null) {
                pstmtEgresos.setString(1, desde);
                pstmtEgresos.setString(2, hasta);
            }
            ResultSet rsEgresos = pstmtEgresos.executeQuery();
            egresos = rsEgresos.next() ? rsEgresos.getDouble(1) : 0;
        }

        double balance = ingresos - egresos;
        return new double[]{ingresos, egresos, balance};
    }
}