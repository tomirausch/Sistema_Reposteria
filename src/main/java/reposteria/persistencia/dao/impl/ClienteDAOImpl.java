package reposteria.persistencia.dao.impl;

import reposteria.persistencia.dao.ClienteDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteDAOImpl implements ClienteDAO {
    private Connection conn;

    public ClienteDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void agregar(String nombre, String telefono, String direccion) throws SQLException {
        String sql = "INSERT INTO clientes (nombre, telefono, direccion) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, telefono);
            pstmt.setString(3, direccion);
            pstmt.executeUpdate();
            System.out.println("Cliente agregado.");
        }
    }

    @Override
    public ResultSet listar() throws SQLException {
        String sql = "SELECT * FROM clientes";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        return pstmt.executeQuery();
    }
}