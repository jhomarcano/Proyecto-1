package co.edu.poli.sw2.service.proxy;

/**
 * Subject del patron <b>Proxy</b>.
 * <p>
 * Define la unica operacion que el cliente conoce: eliminar un dron por
 * su identificador. Tanto el objeto real ({@link EliminadorDron}) como
 * el sustituto protector ({@link DronProxy}) implementan este contrato,
 * de modo que el cliente no puede distinguir uno del otro.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see DronProxy
 * @see EliminadorDron
 */
public interface EliminarDron {

    /**
     * Elimina el dron indicado.
     *
     * @param id identificador del dron a eliminar
     */
    void eliminarDron(int id);
}