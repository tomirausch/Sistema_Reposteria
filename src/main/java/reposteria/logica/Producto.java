package reposteria.logica;

import javafx.beans.property.*;

public class Producto {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nombre = new SimpleStringProperty();
    private final DoubleProperty precioBase = new SimpleDoubleProperty();
    private final StringProperty unidad = new SimpleStringProperty();
    private final DoubleProperty medida = new SimpleDoubleProperty();
    private final BooleanProperty activo = new SimpleBooleanProperty();

    public Producto(int id, String nombre, double precio, String unidad, double medida){ 
        this.id.set(id);
        this.nombre.set(nombre);
        this.precioBase.set(precio);
        this.nombre.set(nombre);
        this.precioBase.set(precio);
        this.unidad.set(unidad);
        this.medida.set(medida);
        this.activo.set(true);
    }

    public Producto(String nombre, double precio, String unidad, double medida){
        this.nombre.set(nombre);
        this.precioBase.set(precio);
        this.unidad.set(unidad);
        this.medida.set(medida);
        this.activo.set(true);
    }

    public Producto(int id, String nombre, double precio, String unidad, double medida, int activo){
        this.id.set(id);
        this.nombre.set(nombre);
        this.precioBase.set(precio);
        this.nombre.set(nombre);
        this.precioBase.set(precio);
        this.unidad.set(unidad);
        this.medida.set(medida);
        this.activo.set(activo == 1);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }
    public String getNombre() { return nombre.get(); }
    public StringProperty nombreProperty() { return nombre; }
    public double getPrecio() { return precioBase.get(); }
    public DoubleProperty precioProperty() { return precioBase; }
    public BooleanProperty activoProperty() { return activo; }
    public Boolean isActivo() { return activo.get(); }
    public void setActivo(Boolean activo) { this.activo.setValue(activo); }
    public String getUnidad() { return unidad.get(); }
    public StringProperty unidadProperty() { return unidad; }
    public double getMedida() { return medida.get(); }
    public DoubleProperty medidaProperty() { return medida; }

    public void setNombre(String nombre) { this.nombre.set(nombre); }
    public void setPrecio(double precio) { this.precioBase.set(precio); }
    public void setUnidad(String unidad) { this.unidad.set(unidad); }
    public void setMedida(double medida) { this.medida.set(medida); }
}