package co.edu.poli.sw2.modelo;

import java.io.Serializable;

/**
 * Entidad que representa un piloto habilitado para operar drones.
 * <p>
 * Se lee desde la tabla {@code piloto} mediante
 * {@link co.edu.poli.sw2.dao.CatalogoRepositorio}. Actualmente el sistema
 * solo consulta pilotos; la asignacion de un piloto a un dron queda
 * prevista para una fase posterior.
 *
 * @author Alejandra Cano y Juan Rosero
 */
public class Piloto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Identificador asignado por la base de datos. */
    private int id;

    /** Nombre completo del piloto. */
    private String nombre;

    /** Anios de experiencia acumulada del piloto. */
    private int experiencia;

    /** Numero de contacto del piloto. */
    private String telefono;

    /**
     * Crea un piloto con todos sus atributos.
     *
     * @param id          identificador del piloto
     * @param nombre      nombre completo
     * @param experiencia anios de experiencia
     * @param telefono    numero de contacto
     */
    public Piloto(int id, String nombre, int experiencia, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.experiencia = experiencia;
        this.telefono = telefono;
    }

    /**
     * Devuelve el identificador del piloto.
     *
     * @return el id asignado por la base de datos
     */
    public int getId() { return id; }

    /**
     * Devuelve el nombre del piloto.
     *
     * @return el nombre completo
     */
    public String getNombre() { return nombre; }

    /**
     * Devuelve los anios de experiencia del piloto.
     *
     * @return la experiencia en anios
     */
    public int getExperiencia() { return experiencia; }

    /**
     * Devuelve el telefono del piloto.
     *
     * @return el numero de contacto
     */
    public String getTelefono() { return telefono; }

    /**
     * Devuelve una descripcion legible del piloto.
     * <p>
     * Este formato es el que se muestra en los controles de seleccion
     * de la interfaz grafica.
     *
     * @return el nombre seguido de los anios de experiencia
     */
    @Override
    public String toString() {
        return nombre + " (Exp: " + experiencia + " anios)";
    }
}