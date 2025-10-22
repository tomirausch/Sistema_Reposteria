package reposteria.logica;

import javafx.beans.property.*;

public class Transaccion {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty tipo = new SimpleStringProperty();
    private final DoubleProperty monto = new SimpleDoubleProperty();
    private final StringProperty descripcion = new SimpleStringProperty();
    private final StringProperty fecha = new SimpleStringProperty();

    public Transaccion(int id, String tipo, double monto, String descripcion, String fecha) {
        this.id.set(id);
        this.tipo.set(tipo);
        this.monto.set(monto);
        this.descripcion.set(descripcion);
        this.fecha.set(fecha);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }
    public String getTipo() { return tipo.get(); }
    public StringProperty tipoProperty() { return tipo; }
    public double getMonto() { return monto.get(); }
    public DoubleProperty montoProperty() { return monto; }
    public String getDescripcion() { return descripcion.get(); }
    public StringProperty descripcionProperty() { return descripcion; }
    public String getFecha() { return fecha.get(); }
    public StringProperty fechaProperty() { return fecha; }
}