package reposteria.logica;

import reposteria.logica.Excepciones.TelefonoExistenteException;
import reposteria.logica.Excepciones.ValidationException;
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

    public void agregarCliente(Cliente cliente) throws SQLException, ValidationException, TelefonoExistenteException {
        if (clienteValido(cliente)) {
            if (telefonoExistente(cliente.getTelefono())) {
                throw new TelefonoExistenteException("El número de teléfono ya está registrado para otro cliente.");
            }
            if(!existeCliente(cliente))
                clienteDAO.agregar(cliente);
            else
                clienteDAO.reactivar(cliente);
        }
    }

    public List<Cliente> listarClientes() throws SQLException {
        return clienteDAO.listar();
    }

    public void eliminarCliente(int id) throws SQLException {
        clienteDAO.eliminar(id);
    }

    public void modificarCliente(Cliente cliente) throws SQLException, ValidationException {
        if (clienteValido(cliente))
            clienteDAO.modificar(cliente);
    }

    private boolean telefonoExistente(String telefono) {
        try {
            List<Cliente> clientes = clienteDAO.listar();
            for (Cliente c : clientes) {
                if (c.getTelefono().equals(telefono) && c.isActivo()) 
                    return true;
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean existeCliente(Cliente cliente) throws SQLException {
        List<Cliente> clientes = clienteDAO.listar();
        for (Cliente c : clientes) {
            if (c.getNombre().equalsIgnoreCase(cliente.getNombre()) &&
                c.getApellido().equalsIgnoreCase(cliente.getApellido()) &&
                c.getTelefono().equals(cliente.getTelefono()) &&
                c.getDireccion().equalsIgnoreCase(cliente.getDireccion())) 
                return true;
        }
        return false;
    }

    public boolean clienteValido(Cliente cliente) throws ValidationException {
        String nombre = normalizarNombre(cliente.getNombre());
        String apellido = normalizarNombre(cliente.getApellido());
        String telefono = cliente.getTelefono();
        String direccion = normalizarDireccion(cliente.getDireccion());

        if (nombre == null) 
            throw new ValidationException("El nombre del cliente debe tener al menos 3 caracteres.");
        
        if (!nombre.matches("^[a-zA-Z\\s]+$")) 
            throw new ValidationException("El nombre del cliente solo puede contener letras y espacios.");
        
        if (apellido == null)
            throw new ValidationException("El apellido del cliente debe tener al menos 3 caracteres.");
        
        if (!apellido.matches("^[a-zA-Z\\s]+$")) 
            throw new ValidationException("El apellido del cliente solo puede contener letras y espacios.");
        
        if (!validarTelefono(telefono))
            throw new ValidationException("El número de teléfono ingresado no es válido.");
        
        if (direccion == null)
            throw new ValidationException("La dirección del cliente debe tener al menos 3 caracteres.");
        
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setDireccion(direccion);
        return true;
    }

    private String normalizarNombre(String nombre){
        if (nombre == null || nombre.isBlank()) return null;

        // Divide en palabras
        String[] palabras = nombre.trim().split("\\s+");

        // Verifica que cada palabra tenga al menos 3 letras
        for (String palabra : palabras) {
            if (palabra.length() < 3) {
                return null;
            }
        }

        // Convierte a mayúsculas
        return nombre.toUpperCase();
    }

    private boolean validarTelefono(String telefono) throws ValidationException{
        // Elimina guiones y espacios para validar solo los dígitos
        String soloNumeros = telefono.replaceAll("[\\s-]", "");

        // Debe tener entre 8 y 15 dígitos (según formato)
        if (!soloNumeros.matches("\\d{8,15}")) {
            return false;
        }

        return true;
    }

    private String normalizarDireccion(String direccion) throws ValidationException{
        if (direccion == null || direccion.isBlank()) return null;


        // Divide en palabras
        String[] palabras = direccion.trim().split("\\s+");

        // Verifica que cada palabra tenga al menos 3 letras
        for (String palabra : palabras) {
            if (palabra.length() < 3) {
                return null;
            }
        }

        // Convierte a mayúsculas
        return direccion.toUpperCase();
    }

}