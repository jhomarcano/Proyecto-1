package co.edu.poli.sw2.exception;

public class ConexionBDException extends DronException {

    private static final long serialVersionUID = 1L;

    public ConexionBDException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public ConexionBDException(String mensaje) {
        super(mensaje);
    }
}