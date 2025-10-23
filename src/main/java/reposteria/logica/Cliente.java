package reposteria.logica;

import javafx.beans.property.*;

public class Cliente {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nombre = new SimpleStringProperty();
    private final StringProperty apellido = new SimpleStringProperty();
    private final StringProperty telefono = new SimpleStringProperty();
    private final StringProperty direccion = new SimpleStringProperty();
    private final BooleanProperty activo = new SimpleBooleanProperty();

    public Cliente(int id, String nombre, String apellido, String direccion, String telefono) {
        this.id.set(id);
        this.nombre.set(nombre);
        this.apellido.set(apellido);
        this.telefono.set(telefono);
        this.direccion.set(direccion);
        this.activo.set(true);
    }

    public Cliente(String nombre, String apellido, String direccion, String telefono){
        this.nombre.set(nombre);
        this.apellido.set(apellido);
        this.telefono.set(telefono);
        this.direccion.set(direccion);
        this.activo.set(true);
    }

    public Cliente(int id, String nombre, String apellido, String direccion, String telefono, int activo){
        this.id.set(id);
        this.nombre.set(nombre);
        this.apellido.set(apellido);
        this.telefono.set(telefono);
        this.direccion.set(direccion);
        this.activo.set((activo == 1));
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
    public BooleanProperty activoProperty() { return activo; }
    public Boolean isActivo() { return activo.get(); }
    public void setActivo(Boolean activo) { this.activo.setValue(activo); }

    public String getNombreCompleto() {
        return nombre.get() + " " + apellido.get();
    }

    public void setNombre(String nombre) { this.nombre.set(nombre); }
    public void setApellido(String apellido) { this.apellido.set(apellido); }
    public void setDireccion(String direccion) { this.direccion.set(direccion); }
    public void setTelefono(String telefono) { this.telefono.set(telefono); }
}