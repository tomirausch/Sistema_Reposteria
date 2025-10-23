package reposteria.presentacion.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import reposteria.logica.Cliente;
import reposteria.logica.GestorReposteria;
import reposteria.logica.Excepciones.TelefonoExistenteException;
import reposteria.logica.Excepciones.ValidationException;
import reposteria.persistencia.BaseDatos;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class ClientesController {
    @FXML
    private TableView<Cliente> clientesTable;

    private ObservableList<Cliente> clientesData = FXCollections.observableArrayList();
    private GestorReposteria gestor;

    public void initialize() {
        gestor = GestorReposteria.getInstancia(BaseDatos.getInstance().getConnection());

        // Configurar columnas
        TableColumn<Cliente, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Cliente, String> nombreCol = new TableColumn<>("Nombre");
        nombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<Cliente, String> apellidoCol = new TableColumn<>("Apellido");
        apellidoCol.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        TableColumn<Cliente, Boolean> direccionCol = new TableColumn<>("Direccion");
        direccionCol.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        TableColumn<Cliente, String> telefonoCol = new TableColumn<>("Teléfono");
        telefonoCol.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        clientesTable.getColumns().setAll(idCol, nombreCol, apellidoCol, direccionCol, telefonoCol);
        cargarClientes();
    }

    public void setGestor(GestorReposteria gestor) {
        this.gestor = gestor;
        cargarClientes();
    }

    private void cargarClientes() {
        if (gestor != null){
            try {
                clientesData.setAll(gestor.listarClientes().stream().filter(Cliente::isActivo).toList());
                clientesTable.setItems(clientesData);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    void agregarCliente() {
        boolean clienteValido = false;

        // Variables para guardar los valores entre intentos
        String nombreValor = "";
        String apellidoValor = "";
        String direccionValor = "";
        String telefonoValor = "";

        while (!clienteValido) {
            // Crear diálogo personalizado
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Agregar Cliente");
            dialog.setHeaderText("Ingrese los datos del cliente");

            // Botones
            ButtonType aceptarButtonType = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(aceptarButtonType, ButtonType.CANCEL);

            // Layout
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

            // Campos de entrada (con valores previos)
            TextField nombre = new TextField(nombreValor);
            nombre.setPromptText("Nombre");

            TextField apellido = new TextField(apellidoValor);
            apellido.setPromptText("Apellido");

            TextField direccion = new TextField(direccionValor);
            direccion.setPromptText("Dirección");

            TextField telefono = new TextField(telefonoValor);
            telefono.setPromptText("Teléfono");

            grid.add(new Label("Nombre:"), 0, 0);
            grid.add(nombre, 1, 0);
            grid.add(new Label("Apellido:"), 0, 1);
            grid.add(apellido, 1, 1);
            grid.add(new Label("Dirección:"), 0, 2);
            grid.add(direccion, 1, 2);
            grid.add(new Label("Teléfono:"), 0, 3);
            grid.add(telefono, 1, 3);

            dialog.getDialogPane().setContent(grid);

            // Resultado
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == aceptarButtonType) {
                    return new Pair<>(nombre.getText(), apellido.getText());
                }
                return null;
            });

            Optional<Pair<String, String>> result = dialog.showAndWait();

            if (result.isEmpty()) {
                // Usuario canceló
                return;
            }

            // Guardar valores para persistirlos en caso de error
            nombreValor = nombre.getText();
            apellidoValor = apellido.getText();
            direccionValor = direccion.getText();
            telefonoValor = telefono.getText();

            try {
                Cliente nuevo = new Cliente(nombreValor, apellidoValor, direccionValor, telefonoValor);
                gestor.agregarCliente(nuevo); // Llama al ClienteService → puede lanzar ValidationException

                // Si llegó hasta acá, todo está bien
                cargarClientes();
                clienteValido = true;

                mostrarAlertaInfo("Cliente agregado correctamente.");
            } catch (ValidationException ve) {
                mostrarAlertaError("Error de validación", ve.getMessage());
            } catch (TelefonoExistenteException e) {
                mostrarAlertaError("Telefono existente", e.getMessage());
                clienteValido = true; // Salir del bucle ya que es un error específico
            }
            catch (Exception e) {
                e.printStackTrace();
                mostrarAlertaError("Error al agregar cliente", e.getMessage());
                return; // Sale del bucle si fue otro tipo de error
            }
        }
    }

    @FXML
    void modificarCliente() {
        boolean clienteValido = false;

        Cliente selected = clientesTable.getSelectionModel().getSelectedItem();

        if (selected != null) {
            // Variables para guardar los valores entre intentos
            String nombreValor = selected.getNombre();
            String apellidoValor = selected.getApellido();
            String direccionValor = selected.getDireccion();
            String telefonoValor = selected.getTelefono();

             while (!clienteValido) {
                // Crear un diálogo personalizado
                Dialog<Pair<String, String>> dialog = new Dialog<>();
                dialog.setTitle("Modificar Cliente");
                dialog.setHeaderText("Modifique los datos del cliente");

                // Configurar los botones
                ButtonType loginButtonType = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

                // Crear el layout con GridPane
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

                // Campos de entrada con valores iniciales
                TextField nombre = new TextField(nombreValor);
                nombre.setPromptText("Nombre");
                TextField apellido = new TextField(apellidoValor);
                apellido.setPromptText("Apellido");
                TextField direccion = new TextField(direccionValor);
                direccion.setPromptText("Dirección");
                TextField telefono = new TextField(telefonoValor);
                telefono.setPromptText("Teléfono");

                grid.add(new Label("Nombre:"), 0, 0);
                grid.add(nombre, 1, 0);
                grid.add(new Label("Apellido:"), 0, 1);
                grid.add(apellido, 1, 1);
                grid.add(new Label("Dirección:"), 0, 2);
                grid.add(direccion, 1, 2);
                grid.add(new Label("Teléfono:"), 0, 3);
                grid.add(telefono, 1, 3);

                dialog.getDialogPane().setContent(grid);

                // Convertir el resultado
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == loginButtonType) {
                        return new Pair<>(nombre.getText(), apellido.getText());
                    }
                    return null;
                });

                Optional<Pair<String, String>> result = dialog.showAndWait();

                if (result.isEmpty()) {
                    // Usuario canceló
                    return;
                }

                // Guardar valores para persistirlos en caso de error
                nombreValor = nombre.getText();
                apellidoValor = apellido.getText();
                direccionValor = direccion.getText();
                telefonoValor = telefono.getText();

                try {
                     // Crear un objeto temporal para validar
                    Cliente temporal = new Cliente(nombreValor, apellidoValor, direccionValor, telefonoValor);

                    // Validar sin modificar el objeto real aún
                    gestor.validarCliente(temporal); // <- llama al ClienteService

                    selected.setNombre(nombre.getText());
                    selected.setApellido(apellido.getText());
                    selected.setDireccion(direccion.getText());
                    selected.setTelefono(telefono.getText());
                    
                    gestor.modificarCliente(selected);
                    
                    cargarClientes();
                    clienteValido = true;

                    mostrarAlertaInfo("Cliente modificado correctamente.");
                } catch (Exception e) {
                    e.printStackTrace();
                    mostrarAlertaError("Error al modificar cliente", e.getMessage());
                }
                
            }
        } else {
            mostrarAlertaError("Advertencia", "Seleccione un cliente para modificar.");
        }
    }

    @FXML
    void borrarCliente() {
        Cliente selected = clientesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Eliminación");
            alert.setHeaderText("¿Está seguro eliminar a " + selected.getNombre() + "?");
            alert.setContentText("Esto es un borrado lógico y no elimina los datos permanentemente.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    selected.setActivo(false);
                    gestor.eliminarCliente(selected.getId());
                    cargarClientes();
                    mostrarAlertaInfo("Cliente eliminado correctamente.");
                } catch (Exception e) {
                    e.printStackTrace();
                    mostrarAlertaError("Error al borrar cliente", e.getMessage());
                }
            }
        } else {
            mostrarAlertaError("Advertencia", "Seleccione un cliente para borrar.");
        }
    }

    @FXML
    void volverMenu(ActionEvent event) throws IOException {
        SceneManager.switchScene((Node) event.getSource(), "/reposteria/presentacion/welcome.fxml");
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
