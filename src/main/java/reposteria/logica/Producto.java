package reposteria.logica;

import javafx.beans.property.*;

public class Producto {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nombre = new SimpleStringProperty();
    private final DoubleProperty precio = new SimpleDoubleProperty();

    public Producto(int id, String nombre, double precio) {
        this.id.set(id);
        this.nombre.set(nombre);
        this.precio.set(precio);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }
    public String getNombre() { return nombre.get(); }
    public StringProperty nombreProperty() { return nombre; }
    public double getPrecio() { return precio.get(); }
    public DoubleProperty precioProperty() { return precio; }
}