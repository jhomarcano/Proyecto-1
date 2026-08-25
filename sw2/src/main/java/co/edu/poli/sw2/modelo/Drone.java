package co.edu.poli.sw2.modelo;

import java.io.Serializable;

/**
 * Entidad abstracta que representa un dron dentro del sistema de gestion.
 * <p>
 * Define los atributos comunes a todos los tipos de dron (identificador,
 * serial, fabricante, modelo y peso). Las subclases concretas
 * {@link Agricultura} y {@link Vigilancia} agregan los atributos propios
 * de su especialidad.
 * <p>
 * Se modelo como clase abstracta y no como interfaz porque la entidad
 * mantiene estado propio; una interfaz de Java no admite atributos de
 * instancia y obligaria a duplicarlos en cada subclase.
 *
 * @author Alejandra Cano y Juan Rosero
 */
public abstract class Drone implements Serializable {

    private static final long serialVersionUID = 3L;

    /** Identificador asignado por la base de datos. Vale 0 mientras no se persiste. */
    private int id;

    /** Codigo unico del dron. La base de datos lo protege con una restriccion UNIQUE. */
    private String serial;

    /** Empresa fabricante del dron. */
    private String fabricante;

    /** Referencia comercial del dron. */
    private String modelo;

    /** Peso del dron en kilogramos. Debe ser mayor que cero. */
    private double peso;

    /**
     * Inicializa los atributos comunes a todo dron.
     * <p>
     * El constructor es {@code protected} para impedir que se instancie
     * un {@code Drone} generico: solo las subclases pueden invocarlo.
     *
     * @param id         identificador del dron; se usa 0 para registros nuevos
     * @param serial     codigo unico del dron
     * @param fabricante empresa fabricante
     * @param modelo     referencia comercial
     * @param peso       peso en kilogramos
     */
    protected Drone(int id, String serial, String fabricante, String modelo, double peso) {
        this.id = id;
        this.serial = serial;
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.peso = peso;
    }

    /**
     * Devuelve el identificador del dron.
     *
     * @return el id asignado por la base de datos, o 0 si aun no se ha persistido
     */
    public int getId() { return id; }

    /**
     * Asigna el identificador del dron.
     * <p>
     * Lo utiliza la capa DAO despues de un INSERT, con el valor que
     * devuelve la secuencia de PostgreSQL.
     *
     * @param id identificador generado por la base de datos
     */
    public void setId(int id) { this.id = id; }

    /**
     * Devuelve el serial del dron.
     *
     * @return el codigo unico del dron
     */
    public String getSerial() { return serial; }

    /**
     * Asigna el serial del dron.
     *
     * @param serial codigo unico; no debe estar vacio
     */
    public void setSerial(String serial) { this.serial = serial; }

    /**
     * Devuelve el fabricante del dron.
     *
     * @return el nombre de la empresa fabricante
     */
    public String getFabricante() { return fabricante; }

    /**
     * Asigna el fabricante del dron.
     *
     * @param fabricante nombre de la empresa fabricante
     */
    public void setFabricante(String fabricante) { this.fabricante = fabricante; }

    /**
     * Devuelve el modelo del dron.
     *
     * @return la referencia comercial, o {@code null} si no se registro
     */
    public String getModelo() { return modelo; }

    /**
     * Asigna el modelo del dron.
     *
     * @param modelo referencia comercial del dron
     */
    public void setModelo(String modelo) { this.modelo = modelo; }

    /**
     * Devuelve el peso del dron.
     *
     * @return el peso en kilogramos
     */
    public double getPeso() { return peso; }

    /**
     * Asigna el peso del dron.
     *
     * @param peso peso en kilogramos; debe ser mayor que cero
     */
    public void setPeso(double peso) { this.peso = peso; }

    /**
     * Devuelve el discriminador del tipo de dron.
     * <p>
     * La capa DAO lo persiste en la columna {@code tipo} de la tabla
     * {@code dron} y lo usa al leer para saber que subclase instanciar.
     * La vista lo muestra en la columna "Tipo" de la tabla.
     *
     * @return {@code "AGRICULTURA"} o {@code "VIGILANCIA"} segun la subclase
     */
    public abstract String getTipo();
}