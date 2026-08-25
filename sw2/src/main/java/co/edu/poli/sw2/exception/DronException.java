package co.edu.poli.sw2.exception;

/**
 * Excepcion base del dominio de drones.
 * <p>
 * Todas las excepciones especificas del sistema heredan de esta, lo que
 * permite capturarlas de forma uniforme en el controlador con un solo
 * bloque {@code catch (DronException ex)}.
 * <p>
 * Extiende {@link RuntimeException} porque se trata de condiciones que el
 * usuario puede corregir desde la interfaz, no de fallos que cada metodo
 * intermedio deba declarar y propagar explicitamente.
 *
 * @author Alejandra Cano y Juan Rosero
 */
public class DronException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Crea la excepcion con un mensaje dirigido al usuario.
     *
     * @param mensaje descripcion del problema
     */
    public DronException(String mensaje) {
        super(mensaje);
    }

    /**
     * Crea la excepcion con un mensaje y la causa tecnica que la origino.
     *
     * @param mensaje descripcion del problema
     * @param causa   excepcion original, normalmente una SQLException
     */
    public DronException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}