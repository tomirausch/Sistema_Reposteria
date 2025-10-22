package reposteria.logica;

import javafx.beans.property.*;
import java.util.List;

public class Pedido {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty clienteId = new SimpleIntegerProperty();
    private final StringProperty fecha = new SimpleStringProperty();
    private final DoubleProperty total = new SimpleDoubleProperty();
    private List<DetallePedido> detalles;

    public Pedido(int id, int clienteId, String fecha, double total, List<DetallePedido> detalles) {
        this.id.set(id);
        this.clienteId.set(clienteId);
        this.fecha.set(fecha);
        this.total.set(total);
        this.detalles = detalles;
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }
    public int getClienteId() { return clienteId.get(); }
    public IntegerProperty clienteIdProperty() { return clienteId; }
    public String getFecha() { return fecha.get(); }
    public StringProperty fechaProperty() { return fecha; }
    public double getTotal() { return total.get(); }
    public DoubleProperty totalProperty() { return total; }
    public List<DetallePedido> getDetalles() { return detalles; }
}