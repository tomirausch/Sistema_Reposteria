package reposteria.logica;

import javafx.beans.property.*;

public class Variante {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty idProducto = new SimpleIntegerProperty();
    private final StringProperty descripcion = new SimpleStringProperty();
    private final DoubleProperty precioExtra = new SimpleDoubleProperty();
    private final BooleanProperty activo = new SimpleBooleanProperty();

    public Variante() {
    }

    // Constructor
    public Variante(int id, int idProducto, String descripcion, double precioExtra) {
        this.id.set(id);
        this.idProducto.set(idProducto);
        this.descripcion.set(descripcion);
        this.precioExtra.set(precioExtra);
        this.activo.set(true);
    }

    // Constructor sin ID para inserciones
    public Variante(int idProducto, String descripcion, double precioExtra) {
        this.idProducto.set(idProducto);
        this.descripcion.set(descripcion);
        this.precioExtra.set(precioExtra);
        this.activo.set(true);
    }

    // Getters y Setters
    public int getIdVariante() { return id.get(); }
    public IntegerProperty getIdProperty() { return id; }
    public int getIdProducto() { return idProducto.get(); }
    public IntegerProperty getIdProductoProperty() { return idProducto; }
    public String getDescripcion() { return descripcion.get(); }
    public StringProperty getDescripcionProperty() { return descripcion; }
    public double getPrecioExtra() { return precioExtra.get(); }
    public DoubleProperty getPrecioExtraProperty() { return precioExtra; }
    public boolean isActivo() { return activo.get(); }
    public BooleanProperty getActivoProperty() { return activo; }

    public void setIdVariante(int id) { this.id.set(id); }
    public void setIdProducto(int idProducto) { this.idProducto.set(idProducto); }
    public void setDescripcion(String descripcion) { this.descripcion.set(descripcion); }
    public void setPrecioExtra(double precioExtra) { this.precioExtra.set(precioExtra); }
    public void setActivo(boolean activo) { this.activo.set(activo); }

    @Override
    public String toString() {
        double val = precioExtra.get();
        return descripcion.get() + (val > 0 ? " (+$" + val + ")" : "");
    }
}
