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
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new ValidationException("El nombre del producto no puede estar vacío.");
        }
        if (producto.getNombre().length() < 3) {
            throw new ValidationException("El nombre del producto debe tener al menos 3 caracteres.");
        }
        if (!producto.getNombre().matches("^[a-zA-Z0-9\\s]+$")) {
            throw new ValidationException("El nombre del producto solo puede contener letras, números y espacios.");
        }
        if (producto.getPrecio() <= 0) {
            throw new ValidationException("El precio debe ser mayor que 0.");
        }

        productoDAO.agregar(producto);
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
}