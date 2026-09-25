package co.edu.poli.sw2.service.proxy;

import co.edu.poli.sw2.exception.ClaveIncorrectaException;

/**
 * Proxy de proteccion del patron <b>Proxy</b>.
 * <p>
 * Se interpone entre el cliente y {@link EliminadorDron}: recibe la
 * misma peticion, pero solo la reenvia al objeto real si la contrasena
 * suministrada es valida. Si no lo es, la eliminacion nunca llega a la
 * base de datos.
 * <p>
 * Como implementa {@link EliminarDron} igual que el objeto real, el
 * cliente puede sustituir uno por otro sin cambiar una sola linea.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see EliminarDron
 * @see EliminadorDron
 */
public class DronProxy implements EliminarDron {

    /** Contrasena esperada para autorizar la operacion. */
    private static final String CLAVE_MAESTRA = "admin123";

    /** Objeto real al que se delega cuando la clave es correcta. */
    private final EliminarDron servicio;

    /** Contrasena capturada en la ventana emergente. */
    private final String contrasena;

    /**
     * Crea el proxy con su propio {@link EliminadorDron}.
     *
     * @param contrasena clave digitada por el usuario
     */
    public DronProxy(String contrasena) {
        this(new EliminadorDron(), contrasena);
    }

    /**
     * Crea el proxy reutilizando un objeto real ya existente.
     *
     * @param servicio   objeto real que ejecuta la eliminacion
     * @param contrasena clave digitada por el usuario
     */
    public DronProxy(EliminarDron servicio, String contrasena) {
        this.servicio = servicio;
        this.contrasena = contrasena;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Primero valida la contrasena; solo si es correcta delega en
     * {@link EliminadorDron}.
     *
     * @throws ClaveIncorrectaException si la contrasena no es valida
     */
    @Override
    public void eliminarDron(int id) {
        if (!validarContrasena()) {
            throw new ClaveIncorrectaException();
        }
        servicio.eliminarDron(id);
    }

    /**
     * Comprueba si la contrasena recibida autoriza la eliminacion.
     *
     * @return {@code true} si coincide con la clave maestra
     */
    private boolean validarContrasena() {
        return contrasena != null && contrasena.equals(CLAVE_MAESTRA);
    }
}