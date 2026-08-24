package co.edu.poli.sw2.exception;

/** Se lanza cuando los datos de un Drone no cumplen las reglas de negocio. */
public class DronValidacionException extends DronException {

    private static final long serialVersionUID = 1L;
    public DronValidacionException(String mensaje) {
        super(mensaje);
   }
}