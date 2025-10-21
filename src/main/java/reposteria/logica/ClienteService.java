package reposteria.logica;

import reposteria.persistencia.dao.ClienteDAO;
import reposteria.persistencia.dao.impl.ClienteDAOImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteService {
    private final ClienteDAO clienteDAO;

    public ClienteService(Connection conn) {
        this.clienteDAO = new ClienteDAOImpl(conn);
    }

    public void agregarCliente(String nombre, String telefono, String direccion) throws SQLException {
        clienteDAO.agregar(nombre, telefono, direccion);
    }

    public void listarClientes() throws SQLException {
        ResultSet rs = clienteDAO.listar();
        while (rs.next()) {
            System.out.println("Cliente: " + rs.getString("nombre") + ", Tel: " + rs.getString("telefono"));
        }
    }
}