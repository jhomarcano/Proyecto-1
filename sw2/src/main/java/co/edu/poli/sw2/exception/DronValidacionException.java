package co.edu.poli.sw2.exception;

/**
 * Se lanza cuando los datos de un dron no cumplen las reglas de negocio.
 * <p>
 * La origina {@link co.edu.poli.sw2.service.ValidadorDrone} antes de tocar
 * la base de datos, o el DAO al traducir una violacion de restriccion
 * CHECK o NOT NULL de PostgreSQL.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see DronException
 */
public class DronValidacionException extends DronException {

    private static final long serialVersionUID = 1L;

    /**
     * Crea la excepcion indicando que regla no se cumplio.
     *
     * @param mensaje descripcion de la validacion fallida
     */
    public DronValidacionException(String mensaje) {
        super(mensaje);
    }
}