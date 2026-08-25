package co.edu.poli.sw2.modelo;

/**
 * Entidad que representa una mision asignada a uno o varios drones.
 * <p>
 * Forma parte del modelo previsto en el diagrama de clases. El CRUD de
 * misiones no hace parte del alcance actual del proyecto.
 *
 * @author Alejandra Cano y Juan Rosero
 */
public class Mision {

    /** Identificador asignado por la base de datos. */
    private int id;

    /** Nombre descriptivo de la mision. */
    private String nombre;

    /** Lugar donde se ejecuta la mision. */
    private String ubicacion;

    /** Fecha programada para la mision. */
    private String fecha;

    /**
     * Crea una mision con todos sus atributos.
     *
     * @param id        identificador de la mision
     * @param nombre    nombre descriptivo
     * @param ubicacion lugar de ejecucion
     * @param fecha     fecha programada
     */
    public Mision(int id, String nombre, String ubicacion, String fecha) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
    }

    /**
     * Devuelve el identificador de la mision.
     *
     * @return el id asignado por la base de datos
     */
    public int getId() { return id; }

    /**
     * Devuelve el nombre de la mision.
     *
     * @return el nombre descriptivo
     */
    public String getNombre() { return nombre; }

    /**
     * Devuelve la ubicacion de la mision.
     *
     * @return el lugar de ejecucion
     */
    public String getUbicacion() { return ubicacion; }

    /**
     * Devuelve la fecha de la mision.
     *
     * @return la fecha programada
     */
    public String getFecha() { return fecha; }

    /**
     * Devuelve una descripcion legible de la mision.
     *
     * @return el nombre seguido de la ubicacion
     */
    @Override
    public String toString() {
        return nombre + " (" + ubicacion + ")";
    }
}