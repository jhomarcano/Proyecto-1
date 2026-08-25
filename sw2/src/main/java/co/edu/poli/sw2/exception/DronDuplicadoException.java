package co.edu.poli.sw2.exception;

/**
 * Se lanza al intentar registrar un dron con un serial que ya existe.
 * <p>
 * Corresponde a la violacion de la restriccion {@code UNIQUE(serial)} de
 * la tabla {@code dron}, que el DAO detecta mediante el SQLState 23505.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see DronException
 */
public class DronDuplicadoException extends DronException {

    private static final long serialVersionUID = 1L;

    /**
     * Crea la excepcion componiendo un mensaje con el serial en conflicto.
     *
     * @param serial serial que ya se encuentra registrado
     */
    public DronDuplicadoException(String serial) {
        super("Ya existe un drone registrado con el serial '" + serial + "'.");
    }
}