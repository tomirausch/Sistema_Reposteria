package reposteria.presentacion.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import java.io.IOException;

public class ProductosController {

    @FXML
    void agregarProducto() {
        new Alert(Alert.AlertType.INFORMATION, "Agregar Cliente").showAndWait();
    }

    @FXML
    void modificarProducto() {
        new Alert(Alert.AlertType.INFORMATION, "Modificar Cliente").showAndWait();
    }

    @FXML
    void borrarProducto() {
        new Alert(Alert.AlertType.INFORMATION, "Borrar Cliente").showAndWait();
    }

    @FXML
    void volverMenu(ActionEvent event) throws IOException {
        SceneManager.switchScene((Node) event.getSource(), "/reposteria/presentacion/welcome.fxml");
    }

    // Navegación superior
    @FXML
    void switchToClientes(ActionEvent event) throws IOException {
        SceneManager.switchScene((Node) event.getSource(), "/reposteria/presentacion/clientes.fxml");
    }

    @FXML
    void switchToPedidos(ActionEvent event) throws IOException {
        SceneManager.switchScene((Node) event.getSource(), "/reposteria/presentacion/pedidos.fxml");
    }

    @FXML
    void switchToProductos(ActionEvent event) throws IOException {
        SceneManager.switchScene((Node) event.getSource(), "/reposteria/presentacion/productos.fxml");
    }

    @FXML
    void switchToEgresos(ActionEvent event) throws IOException {
        SceneManager.switchScene((Node) event.getSource(), "/reposteria/presentacion/egresos.fxml");
    }
}
