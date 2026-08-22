package co.edu.poli.sw2;

import co.edu.poli.sw2.controlador.ManejadorErroresUI;
import co.edu.poli.sw2.exception.DronException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/co/edu/poli/sw2/vista/drone.fxml"));
            stage.setTitle("Gestion de Drones - CRUD (PostgreSQL)");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (DronException ex) {
            ManejadorErroresUI.mostrar(ex);
        } catch (Exception ex) {
            ManejadorErroresUI.mostrarInesperado(ex);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
