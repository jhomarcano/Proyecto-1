package co.edu.poli.sw2.service.decorator;

/**
 * Contrato comun del patron <b>Decorator</b>.
 * <p>
 * Lo implementan tanto el componente concreto ({@link DroneWrapper})
 * como los decoradores ({@link BateriaAdicional}). Gracias a que ambos
 * comparten esta interfaz, un decorador puede envolver indistintamente
 * a un componente base o a otro decorador, lo que permite apilar tantos
 * anadidos como se quiera sin modificar ninguna clase existente.
 * <p>
 * El patron se ubica en la capa de servicios y no en el modelo para que
 * la entidad {@link co.edu.poli.sw2.modelo.Drone} permanezca desacoplada:
 * el dron no sabe que puede ser decorado.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see DroneWrapper
 * @see BateriaAdicional
 */
public interface Componente {

    /**
     * Devuelve la descripcion acumulada del componente.
     * <p>
     * Cada decorador toma la descripcion del componente que envuelve y
     * le agrega la suya propia, de modo que el texto final refleja toda
     * la cadena de decoracion.
     *
     * @return el texto descriptivo del componente y de sus anadidos
     */
    String descripcion();
}