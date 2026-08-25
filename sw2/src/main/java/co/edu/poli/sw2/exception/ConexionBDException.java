package co.edu.poli.sw2.exception;

/**
 * Se lanza ante fallos de comunicacion con la base de datos.
 * <p>
 * Cubre la ausencia de credenciales, la falta del driver JDBC y los
 * errores de red o de servidor. Es la unica excepcion del dominio que el
 * controlador presenta con severidad de error, ya que el usuario no puede
 * corregirla desde el formulario.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see DronException
 */
public class ConexionBDException extends DronException {

    private static final long serialVersionUID = 1L;

    /**
     * Crea la excepcion con un mensaje y la causa tecnica original.
     *
     * @param mensaje descripcion del problema para el usuario
     * @param causa   excepcion original, normalmente una SQLException
     */
    public ConexionBDException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /**
     * Crea la excepcion solo con el mensaje para el usuario.
     *
     * @param mensaje descripcion del problema
     */
    public ConexionBDException(String mensaje) {
        super(mensaje);
    }
}