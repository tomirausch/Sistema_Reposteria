package reposteria.logica;

import javafx.beans.property.*;

public class DetallePedido {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty pedidoId = new SimpleIntegerProperty();
    private final IntegerProperty productoId = new SimpleIntegerProperty();
    private final IntegerProperty cantidad = new SimpleIntegerProperty();
    private final DoubleProperty subtotal = new SimpleDoubleProperty();

    public DetallePedido(int id, int pedidoId, int productoId, int cantidad, double subtotal) {
        this.id.set(id);
        this.pedidoId.set(pedidoId);
        this.productoId.set(productoId);
        this.cantidad.set(cantidad);
        this.subtotal.set(subtotal);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }
    public int getPedidoId() { return pedidoId.get(); }
    public IntegerProperty pedidoIdProperty() { return pedidoId; }
    public int getProductoId() { return productoId.get(); }
    public IntegerProperty productoIdProperty() { return productoId; }
    public int getCantidad() { return cantidad.get(); }
    public IntegerProperty cantidadProperty() { return cantidad; }
    public double getSubtotal() { return subtotal.get(); }
    public DoubleProperty subtotalProperty() { return subtotal; }
}