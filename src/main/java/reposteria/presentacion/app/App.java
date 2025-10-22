package reposteria.presentacion.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Cargar el FXML de bienvenida (menú principal)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/reposteria/presentacion/welcome.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Batiendo con Amor - Sistema de Administración");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

