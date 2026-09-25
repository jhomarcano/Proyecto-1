package co.edu.poli.sw2.exception;

/**
 * Se lanza cuando el proxy de eliminacion recibe una contrasena invalida.
 * <p>
 * Hereda de {@link DronException} para que el manejador de errores de la
 * interfaz la muestre igual que cualquier otro error de dominio.
 *
 * @author Alejandra Cano y Juan Rosero
 */
public class ClaveIncorrectaException extends DronException {

    private static final long serialVersionUID = 1L;

    /** Crea la excepcion con el mensaje estandar de acceso denegado. */
    public ClaveIncorrectaException() {
        super("Contrasena incorrecta. La eliminacion fue cancelada.");
    }
}