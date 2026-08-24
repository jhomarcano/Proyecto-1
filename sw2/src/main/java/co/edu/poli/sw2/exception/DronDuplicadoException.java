package co.edu.poli.sw2.exception;

/** Se lanza cuando se viola la restriccion UNIQUE(serial) de la tabla dron. */
public class DronDuplicadoException extends DronException {

    private static final long serialVersionUID = 1L;

    public DronDuplicadoException(String serial) {
        super("Ya existe un drone registrado con el serial '" + serial + "'.");
    }
}