package co.edu.poli.sw2.modelo;

/**
 * Dron especializado en tareas de vigilancia y monitoreo.
 * <p>
 * Extiende {@link Drone} indicando si cuenta con capacidad de deteccion
 * termica. Sus datos especificos se guardan en la tabla
 * {@code dron_vigilancia}, que referencia a {@code dron} mediante su
 * llave primaria.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see Drone
 */
public class Vigilancia extends Drone {

    private static final long serialVersionUID = 1L;

    /** Indica si el dron cuenta con camara de deteccion termica. */
    private boolean deteccionTermica;

    /**
     * Crea un dron de vigilancia con todos sus atributos.
     *
     * @param id               identificador del dron; se usa 0 para registros nuevos
     * @param serial           codigo unico del dron
     * @param fabricante       empresa fabricante
     * @param modelo           referencia comercial
     * @param peso             peso en kilogramos
     * @param deteccionTermica {@code true} si tiene deteccion termica
     */
    public Vigilancia(int id, String serial, String fabricante, String modelo,
                      double peso, boolean deteccionTermica) {
        super(id, serial, fabricante, modelo, peso);
        this.deteccionTermica = deteccionTermica;
    }

    /**
     * Indica si el dron tiene deteccion termica.
     *
     * @return {@code true} si cuenta con la capacidad, {@code false} en caso contrario
     */
    public boolean isDeteccionTermica() { return deteccionTermica; }

    /**
     * Activa o desactiva la deteccion termica del dron.
     *
     * @param deteccionTermica {@code true} si tiene deteccion termica
     */
    public void setDeteccionTermica(boolean deteccionTermica) { this.deteccionTermica = deteccionTermica; }

    /**
     * {@inheritDoc}
     *
     * @return siempre {@code "VIGILANCIA"}
     */
    @Override
    public String getTipo() { return "VIGILANCIA"; }
}