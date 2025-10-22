package reposteria.logica;

import javafx.beans.property.*;

public class Cliente {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nombre = new SimpleStringProperty();
    private final StringProperty apellido = new SimpleStringProperty();
    private final StringProperty telefono = new SimpleStringProperty();
    private final StringProperty direccion = new SimpleStringProperty();

    public Cliente(int id, String nombre, String apellido, String telefono, String direccion) {
        this.id.set(id);
        this.nombre.set(nombre);
        this.apellido.set(apellido);
        this.telefono.set(telefono);
        this.direccion.set(direccion);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }
    public String getNombre() { return nombre.get(); }
    public StringProperty nombreProperty() { return nombre; }
    public String getApellido() { return apellido.get(); }
    public StringProperty apellidoProperty() { return apellido; }
    public String getTelefono() { return telefono.get(); }
    public StringProperty telefonoProperty() { return telefono; }
    public String getDireccion() { return direccion.get(); }
    public StringProperty direccionProperty() { return direccion; }

    public String getNombreCompleto() {
        return nombre.get() + " " + apellido.get();
    }
}