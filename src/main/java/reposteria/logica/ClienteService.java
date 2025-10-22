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

        if(validarNombreApellido(cliente.getNombre(), cliente.getApellido()) &&
           validarTelefono(cliente.getTelefono()) && validarDireccion(cliente.getDireccion()))
            clienteDAO.agregar(cliente);
    }

    public List<Cliente> listarClientes() throws SQLException {
        return clienteDAO.listar();
    }

    private boolean validarNombreApellido(String nombre, String apellido) throws ValidationException{
        if (nombre == null || nombre.trim().isEmpty()) 
            throw new ValidationException("El nombre del cliente no puede estar vacío.");
        
        if (nombre.length() < 3) 
            throw new ValidationException("El nombre del cliente debe tener al menos 3 caracteres.");
        
        if (!nombre.matches("^[a-zA-Z\\s]+$")) 
            throw new ValidationException("El nombre del cliente solo puede contener letras y espacios.");
        
        if (apellido == null || apellido.trim().isEmpty())
            throw new ValidationException("El apellido del cliente no puede estar vacío.");
        
        if (apellido.length() < 3)
            throw new ValidationException("El apellido del cliente debe tener al menos 3 caracteres.");
        
        if (!apellido.matches("^[a-zA-Z\\s]+$")) 
            throw new ValidationException("El apellido del cliente solo puede contener letras y espacios.");
        
        return true;
    }

    private boolean validarTelefono(String telefono) throws ValidationException{
        if (telefono == null || telefono.trim().isEmpty()) 
            throw new ValidationException("El teléfono no puede estar vacío.");
        
        if (!telefono.matches("^\\d{8,12}$")) 
            throw new ValidationException("El teléfono debe contener solo números y tener entre 8 y 12 dígitos.");

        return true;
    }

    private boolean validarDireccion(String direccion) throws ValidationException{
        if (direccion == null || direccion.trim().isEmpty()) 
            throw new ValidationException("La dirección no puede estar vacía.");

        if (direccion.length() < 5) 
            throw new ValidationException("La dirección debe tener al menos 5 caracteres.");
        
        return true;
    }
}