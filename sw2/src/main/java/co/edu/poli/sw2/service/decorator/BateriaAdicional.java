package co.edu.poli.sw2.service.decorator;

/**
 * Decorador concreto del patron <b>Decorator</b>.
 * <p>
 * Agrega la descripcion de una bateria adicional a cualquier
 * {@link Componente}. Recibe un componente y no un
 * {@link co.edu.poli.sw2.modelo.Drone}: esa es la clave del patron, ya
 * que le permite envolver tanto a un {@link DroneWrapper} como a otro
 * decorador, y por tanto encadenar varios anadidos sobre el mismo dron.
 * <p>
 * El anadido existe solo en memoria. La tabla {@code dron} no tiene
 * ninguna columna para la bateria adicional y esta clase no conoce la
 * capa DAO, de modo que decorar un dron nunca modifica la base de datos.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see Componente
 * @see DroneWrapper
 */
public class BateriaAdicional implements Componente {

    /** Componente decorado. Puede ser el wrapper base u otro decorador. */
    private final Componente componente;

    /** Texto libre que describe la bateria que se esta anadiendo. */
    private final String descripcionBateria;

    /**
     * Envuelve un componente para anadirle una bateria adicional.
     *
     * @param componente         componente a decorar
     * @param descripcionBateria descripcion de la bateria que se agrega
     */
    public BateriaAdicional(Componente componente, String descripcionBateria) {
        this.componente = componente;
        this.descripcionBateria = descripcionBateria;
    }

    /**
     * Devuelve la descripcion de la bateria anadida por este decorador.
     *
     * @return el texto capturado en el formulario
     */
    public String getDescripcionBateria() {
        return descripcionBateria;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Primero delega en el componente envuelto y despues concatena su
     * propio aporte. Ese orden es lo que hace que la descripcion final
     * conserve todo lo que aportaron los niveles anteriores.
     *
     * @return la descripcion del componente envuelto mas la de la bateria
     */
    @Override
    public String descripcion() {
        String base = (componente == null) ? "" : componente.descripcion();
        return base + " + Bateria adicional: " + descripcionBateria;
    }
}