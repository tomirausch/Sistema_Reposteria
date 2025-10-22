package reposteria.persistencia.dao.impl;

import reposteria.logica.Transaccion;
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
    public void registrar(Transaccion transaccion) throws SQLException {
        String sql = "INSERT INTO transacciones (tipo, monto, descripcion, fecha) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, transaccion.getTipo());
            pstmt.setDouble(2, transaccion.getMonto());
            pstmt.setString(3, transaccion.getDescripcion());
            pstmt.setString(4, transaccion.getFecha());
            pstmt.executeUpdate();
        }
    }

    @Override
    public double[] generarReporte(String desde, String hasta) throws SQLException {
        String sql = "SELECT tipo, SUM(monto) as total FROM transacciones";
        if (desde != null && hasta != null) {
            sql += " WHERE fecha BETWEEN ? AND ?";
        }
        sql += " GROUP BY tipo";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (desde != null && hasta != null) {
                pstmt.setString(1, desde);
                pstmt.setString(2, hasta);
            }
            ResultSet rs = pstmt.executeQuery();
            double ingresos = 0, egresos = 0;
            while (rs.next()) {
                if ("ingreso".equals(rs.getString("tipo"))) {
                    ingresos = rs.getDouble("total");
                } else if ("egreso".equals(rs.getString("tipo"))) {
                    egresos = rs.getDouble("total");
                }
            }
            return new double[]{ingresos, egresos, ingresos - egresos};
        }
    }
}