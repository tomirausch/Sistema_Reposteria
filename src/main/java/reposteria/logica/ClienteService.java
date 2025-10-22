package reposteria.logica;

import reposteria.persistencia.dao.ClienteDAO;
import reposteria.persistencia.dao.impl.ClienteDAOImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ClienteService {
    private final ClienteDAO clienteDAO;

    public ClienteService(Connection conn) {
        this.clienteDAO = new ClienteDAOImpl(conn);
    }

    public void agregarCliente(Cliente cliente) throws SQLException, ValidationException {
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            throw new ValidationException("El nombre del cliente no puede estar vacío.");
        }
        if (cliente.getNombre().length() < 3) {
            throw new ValidationException("El nombre del cliente debe tener al menos 3 caracteres.");
        }
        if (!cliente.getNombre().matches("^[a-zA-Z\\s]+$")) {
            throw new ValidationException("El nombre del cliente solo puede contener letras y espacios.");
        }
        if (cliente.getApellido() == null || cliente.getApellido().trim().isEmpty()) {
            throw new ValidationException("El apellido del cliente no puede estar vacío.");
        }
        if (cliente.getApellido().length() < 3) {
            throw new ValidationException("El apellido del cliente debe tener al menos 3 caracteres.");
        }
        if (!cliente.getApellido().matches("^[a-zA-Z\\s]+$")) {
            throw new ValidationException("El apellido del cliente solo puede contener letras y espacios.");
        }
        if (cliente.getTelefono() == null || cliente.getTelefono().trim().isEmpty()) {
            throw new ValidationException("El teléfono no puede estar vacío.");
        }
        if (!cliente.getTelefono().matches("^\\d{8,12}$")) {
            throw new ValidationException("El teléfono debe contener solo números y tener entre 8 y 12 dígitos.");
        }
        if (cliente.getDireccion() == null || cliente.getDireccion().trim().isEmpty()) {
            throw new ValidationException("La dirección no puede estar vacía.");
        }
        if (cliente.getDireccion().length() < 5) {
            throw new ValidationException("La dirección debe tener al menos 5 caracteres.");
        }

        clienteDAO.agregar(cliente);
    }

    public List<Cliente> listarClientes() throws SQLException {
        return clienteDAO.listar();
    }
}