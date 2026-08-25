package co.edu.poli.sw2.exception;

/**
 * Se lanza al intentar actualizar o eliminar un dron que ya no existe.
 * <p>
 * El DAO la lanza cuando una sentencia UPDATE o DELETE afecta cero filas,
 * lo que indica que el registro fue eliminado por otra via.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see DronException
 */
public class DronNoEncontradoException extends DronException {

    private static final long serialVersionUID = 1L;

    /**
     * Crea la excepcion componiendo un mensaje con el id buscado.
     *
     * @param id identificador del dron que no se encontro
     */
    public DronNoEncontradoException(int id) {
        super("No se encontro un drone con id " + id + ".");
    }
}