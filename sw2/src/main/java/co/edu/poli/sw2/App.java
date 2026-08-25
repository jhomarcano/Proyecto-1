package co.edu.poli.sw2;

import co.edu.poli.sw2.controlador.ManejadorErroresUI;
import co.edu.poli.sw2.exception.DronException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Punto de entrada de la aplicacion de gestion de drones.
 * <p>
 * Carga la vista principal desde {@code drone.fxml} y muestra la ventana.
 * Cualquier fallo durante el arranque, incluido el de conexion a la base
 * de datos, se presenta como alerta en lugar de imprimirse en consola.
 *
 * @author Alejandra Cano y Juan Rosero
 */
public class App extends Application {

    /**
     * Construye y muestra la ventana principal de la aplicacion.
     *
     * @param stage escenario principal que aporta JavaFX
     */
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

    /**
     * Arranca la aplicacion JavaFX.
     *
     * @param args argumentos de linea de comandos; no se utilizan
     */
    public static void main(String[] args) {
        launch(args);
    }
}