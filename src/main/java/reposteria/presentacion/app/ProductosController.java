package reposteria.presentacion.app;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import reposteria.logica.GestorReposteria;
import reposteria.logica.Producto;
import reposteria.logica.Excepciones.ValidationException;
import reposteria.persistencia.BaseDatos;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class ProductosController {
    @FXML
    private TableView<Producto> productosTable;

    private ObservableList<Producto> productosData = FXCollections.observableArrayList();
    private GestorReposteria gestor;

    public void initialize() {
        gestor = GestorReposteria.getInstancia(BaseDatos.getInstance().getConnection());

        TableColumn<Producto, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Producto, String> nombreCol = new TableColumn<>("Nombre");
        nombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Producto, String> precioCol = new TableColumn<>("Precio");
        precioCol.setCellValueFactory(new PropertyValueFactory<>("precio"));

        // 🧩 Columna personalizada de Unidad
        TableColumn<Producto, String> unidadCol = new TableColumn<>("Unidad");
        unidadCol.setCellValueFactory(cellData -> {
            Producto p = cellData.getValue();
            String unidad = p.getUnidad();
            if (unidad.equalsIgnoreCase("CM") && p.getMedida() != 0) {
                return new SimpleStringProperty(p.getMedida() + " " + unidad);
            } else {
                return new SimpleStringProperty(unidad);
            }
        });
        productosTable.getColumns().setAll(idCol, nombreCol, precioCol, unidadCol);
        cargarProductos();
    }

     public void setGestor(GestorReposteria gestor) {
        this.gestor = gestor;
        cargarProductos();
    }

    private void cargarProductos() {
        if (gestor != null){
            try {
                productosData.setAll(gestor.listarProductos().stream().filter(Producto::isActivo).toList());
                productosTable.setItems(productosData);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void agregarProducto() {
        boolean productoValido = false;

        // Variables para guardar los valores entre intentos
        String nombreValor = "";
        String precioValor = "";
        String unidadValor = "";
        String medidaValor = "";

        while(!productoValido){
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Agregar Producto");
            dialog.setHeaderText("Ingrese los datos del producto");

            ButtonType btnAceptar = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(btnAceptar, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField nombre = new TextField(nombreValor);
            nombre.setPromptText("Nombre del producto");

            TextField precio = new TextField(precioValor);
            precio.setPromptText("Precio unitario");

            ComboBox<String> unidad = new ComboBox<>();
            unidad.getItems().addAll("Kg", "Docena", "Cm");
            unidad.setValue(unidadValor);
            unidad.setPromptText("Unidad de medida");

            TextField medidaCm = new TextField();
            medidaCm.setPromptText("Tamaño en cm");
            medidaCm.setDisable(!"cm".equalsIgnoreCase(unidadValor)); // deshabilitado por defecto

            // Mostrar el campo medida solo si se selecciona “Cm”
            unidad.setOnAction(e -> {
                boolean esCm = "Cm".equalsIgnoreCase(unidad.getValue());
                medidaCm.setDisable(!esCm);
                medidaCm.setText(medidaValor);
            });

            grid.add(new Label("Nombre:"), 0, 0);
            grid.add(nombre, 1, 0);
            grid.add(new Label("Precio:"), 0, 1);
            grid.add(precio, 1, 1);
            grid.add(new Label("Unidad:"), 0, 2);
            grid.add(unidad, 1, 2);
            grid.add(new Label("Medida (solo si es cm):"), 0, 3);
            grid.add(medidaCm, 1, 3);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == btnAceptar) {
                    return ButtonType.OK;
                }
                return null;
            });

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isEmpty()) {
                // Usuario canceló
                return;
            }

            // Guardar valores para persistirlos en caso de error
            nombreValor = nombre.getText();
            precioValor = precio.getText();
            unidadValor = unidad.getValue();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    double precioVal = Double.parseDouble(precio.getText());
                    String unidadSeleccionada = unidad.getValue();
                    String nombreProd = nombre.getText().trim().toUpperCase();

                    double medidaVal = 0;
                    if ("Cm".equalsIgnoreCase(unidadSeleccionada)) {
                        medidaVal = Double.parseDouble(medidaCm.getText());
                    }

                    Producto nuevo = new Producto(nombreProd, precioVal, unidadSeleccionada, medidaVal);
                    gestor.agregarProducto(nuevo);
                    cargarProductos();
                    productoValido = true;

                    mostrarAlertaInfo("Producto agregado correctamente.");
                } catch (NumberFormatException e) {
                    mostrarAlertaError("Error", "El precio o la medida deben ser valores numéricos.");
                } catch (Exception e) {
                    mostrarAlertaError("Error al agregar producto", e.getMessage());
                }
            }
        }
    }


    @FXML
    void modificarProducto() {
        boolean productoValido = false;

        Producto selected = productosTable.getSelectionModel().getSelectedItem();

        if (selected != null) {
            // Variables persistentes para mantener los valores ingresados
            String nombreValor = selected.getNombre();
            String unidadValor = selected.getUnidad();
            String medidaValor = selected.getMedida() != 0 ? String.valueOf(selected.getMedida()) : "";
            String precioValor = String.valueOf(selected.getPrecio());

            while (!productoValido) {
                // Crear diálogo personalizado
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle("Modificar Producto");
                dialog.setHeaderText("Modifique los datos del producto");

                ButtonType aceptarButtonType = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(aceptarButtonType, ButtonType.CANCEL);

                // Layout
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                // Campos
                TextField nombre = new TextField(nombreValor);
                nombre.setPromptText("Nombre");

                TextField precio = new TextField(precioValor);
                precio.setPromptText("Precio");

                ComboBox<String> unidad = new ComboBox<>();
                unidad.getItems().addAll("kg", "docena", "cm");
                unidad.setValue(unidadValor);

                TextField medida = new TextField(medidaValor);
                medida.setPromptText("Medida (solo si unidad = cm)");
                medida.setDisable(!"cm".equalsIgnoreCase(unidadValor));

                // Habilitar o deshabilitar campo medida según unidad seleccionada
                unidad.setOnAction(e -> {
                    boolean esCm = "cm".equals(unidad.getValue());
                    medida.setDisable(!esCm);
                    if (!esCm) medida.clear();
                });

                // Agregar al grid
                grid.add(new Label("Nombre:"), 0, 0);
                grid.add(nombre, 1, 0);
                grid.add(new Label("Precio:"), 0, 1);
                grid.add(precio, 1, 1);
                grid.add(new Label("Unidad:"), 0, 2);
                grid.add(unidad, 1, 2);
                grid.add(new Label("Medida:"), 0, 3);
                grid.add(medida, 1, 3);

                dialog.getDialogPane().setContent(grid);

                // Mostrar diálogo
                Optional<ButtonType> result = dialog.showAndWait();

                if (result.isEmpty() || result.get() == ButtonType.CANCEL) {
                    return; // Usuario canceló
                }

                // Guardar valores
                nombreValor = nombre.getText().trim();
                unidadValor = (unidad.getValue() == null) ? "" : unidad.getValue().trim();
                medidaValor = medida.getText().trim();
                precioValor = precio.getText().trim();

                try {
                    double precioNum = 0.0;
                    double medidaNum = 0.0;

                    // Convierte precio si hay texto
                    if (!precioValor.isBlank()) {
                        precioNum = Double.parseDouble(precioValor);
                    }

                    // Solo intenta convertir medida si la unidad es "cm"
                    if ("cm".equalsIgnoreCase(unidadValor) && !medidaValor.isBlank()) {
                        medidaNum = Double.parseDouble(medidaValor);
                    }

                    Producto temporal = new Producto(nombreValor, precioNum, unidadValor, medidaNum);
                    gestor.validarProducto(temporal); 

                    selected.setNombre(nombreValor);
                    selected.setUnidad(unidadValor);
                    selected.setMedida(medidaNum);
                    selected.setPrecio(precioNum);

                    gestor.modificarProducto(selected);
                    cargarProductos();
                    productoValido = true;

                    mostrarAlertaInfo("Producto modificado correctamente.");

                } catch (ValidationException ve) {
                    mostrarAlertaError("Error de validación", ve.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                    mostrarAlertaError("Error al modificar producto", e.getMessage());
                }
            }
        } else {
            mostrarAlertaError("Advertencia", "Seleccione un producto para modificar.");
        }
    }

    @FXML
    void borrarProducto() {
        new Alert(Alert.AlertType.INFORMATION, "Borrar Producto").showAndWait();
    }

    private void mostrarAlertaInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
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
