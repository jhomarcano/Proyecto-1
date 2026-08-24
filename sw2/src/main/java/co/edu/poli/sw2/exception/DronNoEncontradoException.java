package co.edu.poli.sw2.exception;

/** Se lanza al intentar actualizar/eliminar un Drone que ya no existe en la BD. */
public class DronNoEncontradoException extends DronException {

    private static final long serialVersionUID = 1L;

   public DronNoEncontradoException(int id) {
        super("No se encontro un drone con id " + id + ".");
    }
}