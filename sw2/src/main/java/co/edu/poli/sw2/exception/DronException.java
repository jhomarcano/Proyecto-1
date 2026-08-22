package co.edu.poli.sw2.exception;

/** Excepcion base del dominio Dron. Todas las excepciones especificas heredan de esta. */
public class DronException extends RuntimeException {

    private static final long serialVersionUID = 1L;

   public DronException(String mensaje) {
        super(mensaje);
    }

    public DronException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}