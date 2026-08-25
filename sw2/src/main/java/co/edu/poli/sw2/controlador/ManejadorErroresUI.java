package co.edu.poli.sw2.controlador;

import co.edu.poli.sw2.exception.ConexionBDException;
import co.edu.poli.sw2.exception.DronDuplicadoException;
import co.edu.poli.sw2.exception.DronException;
import co.edu.poli.sw2.exception.DronNoEncontradoException;
import co.edu.poli.sw2.exception.DronValidacionException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Centraliza la presentacion de errores al usuario.
 * <p>
 * Convierte las excepciones de dominio en alertas de JavaFX, eligiendo el
 * titulo y el nivel de severidad segun el tipo de excepcion. Es la pieza
 * que garantiza el requisito de que ningun error se muestre por consola.
 * <p>
 * Es una clase de utilidad: constructor privado y metodos estaticos.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see DronException
 */
public final class ManejadorErroresUI {

    /** Constructor privado: la clase solo expone metodos estaticos. */
    private ManejadorErroresUI() {}

    /**
     * Muestra una alerta a partir de una excepcion de dominio.
     * <p>
     * Se usa el mensaje propio de la excepcion, redactado para el usuario
     * final, no la traza tecnica.
     *
     * @param ex excepcion de dominio a presentar
     */
    public static void mostrar(DronException ex) {
        Alert alerta = new Alert(tipoPara(ex));
        alerta.setTitle(tituloPara(ex));
        alerta.setHeaderText(null);
        alerta.setContentText(ex.getMessage());
        alerta.showAndWait();
    }

    /**
     * Muestra una alerta generica para errores no contemplados.
     * <p>
     * No expone detalles tecnicos al usuario; sirve como red de seguridad
     * para cualquier excepcion ajena al dominio.
     *
     * @param ex excepcion inesperada capturada
     */
    public static void mostrarInesperado(Throwable ex) {
        Alert alerta = new Alert(AlertType.ERROR);
        alerta.setTitle("Error inesperado");
        alerta.setHeaderText(null);
        alerta.setContentText("Ocurrio un error inesperado. Intente nuevamente.");
        alerta.showAndWait();
    }

    /**
     * Determina la severidad de la alerta segun el tipo de excepcion.
     * <p>
     * Los fallos de conexion se consideran errores; los demas, advertencias
     * que el usuario puede corregir.
     *
     * @param ex excepcion de dominio
     * @return el tipo de alerta correspondiente
     */
    private static AlertType tipoPara(DronException ex) {
        return (ex instanceof ConexionBDException) ? AlertType.ERROR : AlertType.WARNING;
    }

    /**
     * Determina el titulo de la alerta segun el tipo de excepcion.
     *
     * @param ex excepcion de dominio
     * @return un titulo descriptivo para la ventana de alerta
     */
    private static String tituloPara(DronException ex) {
        if (ex instanceof DronValidacionException) return "Datos invalidos";
        if (ex instanceof DronDuplicadoException) return "Registro duplicado";
        if (ex instanceof DronNoEncontradoException) return "No encontrado";
        if (ex instanceof ConexionBDException) return "Error de conexion";
        return "Error";
    }
}