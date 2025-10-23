package reposteria.logica;

import javafx.beans.property.*;

public class Producto {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nombre = new SimpleStringProperty();
    private final DoubleProperty precio = new SimpleDoubleProperty();
    private final BooleanProperty activo = new SimpleBooleanProperty();

    public Producto(int id, String nombre, double precio) {
        this.id.set(id);
        this.nombre.set(nombre);
        this.precio.set(precio);
        this.activo.set(true);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }
    public String getNombre() { return nombre.get(); }
    public StringProperty nombreProperty() { return nombre; }
    public double getPrecio() { return precio.get(); }
    public DoubleProperty precioProperty() { return precio; }
    public BooleanProperty activoProperty() { return activo; }
    public Boolean borrado() { return activo.get(); }
    public void setActivo(Boolean activo) { this.activo.setValue(activo); }
}