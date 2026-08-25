package co.edu.poli.sw2.modelo;

/**
 * Dron especializado en labores agricolas, como fumigacion y riego.
 * <p>
 * Extiende {@link Drone} agregando la capacidad del tanque. Sus datos
 * especificos se guardan en la tabla {@code dron_agricultura}, que
 * referencia a {@code dron} mediante su llave primaria.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see Drone
 */
public class Agricultura extends Drone {

    private static final long serialVersionUID = 1L;

    /** Capacidad del tanque de fumigacion en litros. Debe ser mayor que cero. */
    private double capacidadTanque;

    /**
     * Crea un dron de agricultura con todos sus atributos.
     *
     * @param id              identificador del dron; se usa 0 para registros nuevos
     * @param serial          codigo unico del dron
     * @param fabricante      empresa fabricante
     * @param modelo          referencia comercial
     * @param peso            peso en kilogramos
     * @param capacidadTanque capacidad del tanque en litros
     */
    public Agricultura(int id, String serial, String fabricante, String modelo,
                       double peso, double capacidadTanque) {
        super(id, serial, fabricante, modelo, peso);
        this.capacidadTanque = capacidadTanque;
    }

    /**
     * Devuelve la capacidad del tanque.
     *
     * @return la capacidad en litros
     */
    public double getCapacidadTanque() { return capacidadTanque; }

    /**
     * Asigna la capacidad del tanque.
     *
     * @param capacidadTanque capacidad en litros; debe ser mayor que cero
     */
    public void setCapacidadTanque(double capacidadTanque) { this.capacidadTanque = capacidadTanque; }

    /**
     * {@inheritDoc}
     *
     * @return siempre {@code "AGRICULTURA"}
     */
    @Override
    public String getTipo() { return "AGRICULTURA"; }
}