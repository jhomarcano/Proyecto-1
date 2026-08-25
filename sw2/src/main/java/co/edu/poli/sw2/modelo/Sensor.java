package co.edu.poli.sw2.modelo;

import java.io.Serializable;

/**
 * Entidad que representa un sensor instalable en un dron.
 * <p>
 * Se lee desde la tabla {@code sensor} mediante
 * {@link co.edu.poli.sw2.dao.CatalogoRepositorio}. La asignacion de
 * sensores a un dron queda prevista para una fase posterior.
 *
 * @author Alejandra Cano y Juan Rosero
 */
public class Sensor implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Identificador asignado por la base de datos. */
    private int id;

    /** Clase de sensor, por ejemplo termico, multiespectral o LiDAR. */
    private String tipo;

    /** Empresa fabricante del sensor. */
    private String fabricante;

    /**
     * Crea un sensor con todos sus atributos.
     *
     * @param id         identificador del sensor
     * @param tipo       clase de sensor
     * @param fabricante empresa fabricante
     */
    public Sensor(int id, String tipo, String fabricante) {
        this.id = id;
        this.tipo = tipo;
        this.fabricante = fabricante;
    }

    /**
     * Devuelve el identificador del sensor.
     *
     * @return el id asignado por la base de datos
     */
    public int getId() { return id; }

    /**
     * Devuelve el tipo de sensor.
     *
     * @return la clase de sensor
     */
    public String getTipo() { return tipo; }

    /**
     * Devuelve el fabricante del sensor.
     *
     * @return el nombre de la empresa fabricante
     */
    public String getFabricante() { return fabricante; }

    /**
     * Devuelve una descripcion legible del sensor.
     *
     * @return el tipo seguido del fabricante
     */
    @Override
    public String toString() {
        return tipo + " - " + fabricante;
    }

    /**
     * Compara dos sensores por su identificador.
     * <p>
     * Dos sensores se consideran iguales si comparten el mismo id, ya que
     * este es unico en la base de datos.
     *
     * @param o el objeto a comparar
     * @return {@code true} si el objeto es un Sensor con el mismo id
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sensor)) return false;
        return id == ((Sensor) o).id;
    }

    /**
     * Calcula el codigo hash a partir del identificador.
     * <p>
     * Se mantiene coherente con {@link #equals(Object)}, como exige el
     * contrato de {@code Object}.
     *
     * @return el codigo hash derivado del id
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}