package co.edu.poli.sw2.controlador;

import co.edu.poli.sw2.exception.ConexionBDException;
import co.edu.poli.sw2.exception.DronDuplicadoException;
import co.edu.poli.sw2.exception.DronException;
import co.edu.poli.sw2.exception.DronNoEncontradoException;
import co.edu.poli.sw2.exception.DronValidacionException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public final class ManejadorErroresUI {

    private ManejadorErroresUI() {}

    public static void mostrar(DronException ex) {
        Alert alerta = new Alert(tipoPara(ex));
        alerta.setTitle(tituloPara(ex));
        alerta.setHeaderText(null);
        alerta.setContentText(ex.getMessage());
        alerta.showAndWait();
    }

    public static void mostrarInesperado(Throwable ex) {
        Alert alerta = new Alert(AlertType.ERROR);
        alerta.setTitle("Error inesperado");
        alerta.setHeaderText(null);
        alerta.setContentText("Ocurrio un error inesperado. Intente nuevamente.");
        alerta.showAndWait();
    }

        private static AlertType tipoPara(DronException ex) {
        return (ex instanceof ConexionBDException) ? AlertType.ERROR : AlertType.WARNING;
    }

    private static String tituloPara(DronException ex) {
        if (ex instanceof DronValidacionException) return "Datos invalidos";
        if (ex instanceof DronDuplicadoException) return "Registro duplicado";
        if (ex instanceof DronNoEncontradoException) return "No encontrado";
        if (ex instanceof ConexionBDException) return "Error de conexion";
        return "Error";
    }
}