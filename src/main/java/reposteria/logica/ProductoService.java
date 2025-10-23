package reposteria.logica;

import reposteria.logica.Excepciones.ValidationException;
import reposteria.persistencia.dao.ProductoDAO;
import reposteria.persistencia.dao.impl.ProductoDAOImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProductoService {
    private final ProductoDAO productoDAO;

    public ProductoService(Connection conn) {
        this.productoDAO = new ProductoDAOImpl(conn);
    }

    public void agregarProducto(Producto producto) throws SQLException, ValidationException {
        if (esNombreValido(producto.getNombre())){
            if (!esNombreUnico(producto.getNombre())) 
                throw new ValidationException("El nombre del producto ya existe.");
            else if(esPrecioValido(producto.getPrecio())) {
                productoDAO.agregar(producto);
            }
        }

    }

    public List<Producto> listarProductos() throws SQLException {
        return productoDAO.listar();
    }

    public double getPrecio(int idProducto) throws SQLException, ValidationException {
        double precio = productoDAO.getPrecio(idProducto);
        if (precio == 0) {
            throw new ValidationException("El producto con ID " + idProducto + " no existe.");
        }
        return precio;
    }

    private boolean esNombreUnico(String nombre) throws SQLException {
        List<Producto> productos = productoDAO.listar();
        for (Producto p : productos) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return false;
            }
        }
        return true;
    }

    private boolean esPrecioValido(double precio) throws ValidationException {
        if (precio < 0) {
            throw new ValidationException("El precio del producto no puede ser negativo.");
        }

        return true;
    }

    private boolean esNombreValido(String nombre) throws ValidationException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidationException("El nombre del producto no puede estar vacío.");
        }
        if (nombre.length() < 3) {
            throw new ValidationException("El nombre del producto debe tener al menos 3 caracteres.");
        }
        if (!nombre.matches("^[a-zA-Z0-9\\s]+$")) {
            throw new ValidationException("El nombre del producto solo puede contener letras, números y espacios.");
        }

        return true;
    }
}