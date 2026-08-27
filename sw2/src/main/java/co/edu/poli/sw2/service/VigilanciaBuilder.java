package co.edu.poli.sw2.service;

import co.edu.poli.sw2.modelo.Vigilancia;

/**
 * Builder para construir instancias de {@link Vigilancia} paso a paso.
 * <p>
 * Complementa al patron Factory Method ({@link VigilanciaFactory}): mientras
 * la factoria decide que clase construir a partir de datos ya completos,
 * el builder se encarga de ensamblar el objeto cuando sus atributos se
 * conocen de forma incremental, por ejemplo al generar un dron aleatorio
 * a partir de estadisticas de la base de datos.
 * <p>
 * Cada metodo {@code con...} devuelve la misma instancia del builder para
 * permitir encadenamiento (fluent API).
 *
 * @author Alejandra Cano y Juan Rosero
 * @see Vigilancia
 * @see VigilanciaFactory
 */
public class VigilanciaBuilder {

    private int id = 0;
    private String serial;
    private String fabricante;
    private String modelo;
    private double peso;
    private boolean deteccionTermica;

    /**
     * Asigna el identificador del dron a construir.
     * <p>
     * Solo se usa cuando el builder se emplea para reconstruir un dron
     * existente (por ejemplo, al mapear una fila de la base de datos);
     * para drones nuevos se deja en 0.
     *
     * @param id identificador del dron
     * @return este mismo builder, para encadenar llamadas
     */
    public VigilanciaBuilder conId(int id) {
        this.id = id;
        return this;
    }

    /**
     * Asigna el serial del dron a construir.
     *
     * @param serial codigo unico del dron
     * @return este mismo builder, para encadenar llamadas
     */
    public VigilanciaBuilder conSerial(String serial) {
        this.serial = serial;
        return this;
    }

    /**
     * Asigna el fabricante del dron a construir.
     *
     * @param fabricante empresa fabricante
     * @return este mismo builder, para encadenar llamadas
     */
    public VigilanciaBuilder conFabricante(String fabricante) {
        this.fabricante = fabricante;
        return this;
    }

    /**
     * Asigna el modelo del dron a construir.
     *
     * @param modelo referencia comercial
     * @return este mismo builder, para encadenar llamadas
     */
    public VigilanciaBuilder conModelo(String modelo) {
        this.modelo = modelo;
        return this;
    }

    /**
     * Asigna el peso del dron a construir.
     *
     * @param peso peso en kilogramos
     * @return este mismo builder, para encadenar llamadas
     */
    public VigilanciaBuilder conPeso(double peso) {
        this.peso = peso;
        return this;
    }

    /**
     * Asigna si el dron a construir tiene deteccion termica.
     *
     * @param deteccionTermica {@code true} si tiene deteccion termica
     * @return este mismo builder, para encadenar llamadas
     */
    public VigilanciaBuilder conDeteccionTermica(boolean deteccionTermica) {
        this.deteccionTermica = deteccionTermica;
        return this;
    }

    /**
     * Ensambla la instancia final de {@link Vigilancia} con los datos acumulados.
     *
     * @return un nuevo dron de vigilancia
     */
    public Vigilancia build() {
        return new Vigilancia(id, serial, fabricante, modelo, peso, deteccionTermica);
    }
}