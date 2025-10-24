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
        if (productoValido(producto)) {
            if(existeProducto(producto)){
                productoDAO.reactivar(producto);
            } else
                productoDAO.agregar(producto);
        }

    }

    public void modificarProducto(Producto producto) throws ValidationException, SQLException {
        if (productoValido(producto))
            productoDAO.modificar(producto);
    }

    public void eliminarProducto(int id) throws SQLException{
        productoDAO.eliminar(id);
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

    public boolean productoValido(Producto producto) throws ValidationException {
        String nombre = normalizarString(producto.getNombre());
        double precio = producto.getPrecio();
        String unidad = normalizarString(producto.getUnidad());
        double medida = producto.getMedida();

        if (nombre == null) 
            throw new ValidationException("El nombre del producto debe tener al menos 3 caracteres.");
        
        if (!nombre.matches("^[a-zA-Z\\s]+$")) 
            throw new ValidationException("El nombre del producto solo puede contener letras y espacios.");

        if (unidad == null){
            throw new ValidationException("La unidad del producto debe tener al menos 2 caracteres.");
        }

        if (precio < 1) {
            throw new ValidationException("El precio debe ser mayor a 0.");
        }

        if ("CM".equalsIgnoreCase(unidad) && medida < 1) {
            throw new ValidationException("La medida debe ser mayor a 0.");
        }
        if (!"CM".equalsIgnoreCase(unidad)) {
            producto.setMedida(0); // normaliza para persistir
    }
        
        producto.setNombre(nombre);
        producto.setUnidad(unidad);
        return true;
    }

    private boolean existeProducto(Producto producto) throws SQLException, ValidationException{
        List<Producto> productos = productoDAO.listar();
        for (Producto p : productos) {
            if (p.getNombre().equalsIgnoreCase(producto.getNombre()) &&
                p.getUnidad().equalsIgnoreCase(producto.getUnidad()) &&
                p.getMedida() == producto.getMedida()){
                if(p.isActivo())
                    throw new ValidationException("El producto ya está registrado en el sistema.");
                else return true;
            }
        }
        return false;
    }

    private String normalizarString(String cadena){
        if (cadena == null || cadena.isBlank()) return null;

        // Divide en palabras
        String[] palabras = cadena.trim().split("\\s+");

        // Verifica que cada palabra tenga al menos 3 letras
        for (String palabra : palabras) {
            if (palabra.length() < 2) {
                return null;
            }
        }

        // Convierte a mayúsculas
        return cadena.toUpperCase();
    }

}