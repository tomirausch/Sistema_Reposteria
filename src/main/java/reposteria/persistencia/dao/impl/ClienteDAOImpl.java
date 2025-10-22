package reposteria.persistencia.dao.impl;

import reposteria.logica.Cliente;
import reposteria.persistencia.dao.ClienteDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl implements ClienteDAO {
    private Connection conn;

    public ClienteDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void agregar(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (nombre, apellido, telefono, direccion) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getApellido());
            pstmt.setString(3, cliente.getTelefono());
            pstmt.setString(4, cliente.getDireccion());
            pstmt.executeUpdate();
            System.out.println("Cliente agregado.");
        }
    }

    @Override
    public List<Cliente> listar() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                clientes.add(new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("telefono"),
                        rs.getString("direccion")
                ));
            }
        }
        return clientes;
    }
}