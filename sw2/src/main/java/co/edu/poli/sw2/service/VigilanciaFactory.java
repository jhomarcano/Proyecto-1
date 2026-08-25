package co.edu.poli.sw2.service;

import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Vigilancia;

/**
 * Factoria concreta que produce instancias de {@link Vigilancia}.
 * <p>
 * Recibe en el constructor los datos capturados en el formulario y los
 * conserva hasta que se invoca {@link #crearDrone()}.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see DroneFactory
 */
public class VigilanciaFactory extends DroneFactory {

    /** Codigo unico del dron a construir. */
    private final String serial;

    /** Empresa fabricante del dron a construir. */
    private final String fabricante;

    /** Referencia comercial del dron a construir. */
    private final String modelo;

    /** Peso en kilogramos del dron a construir. */
    private final double peso;

    /** Indicador de deteccion termica del dron a construir. */
    private final boolean deteccionTermica;

    /**
     * Guarda los datos necesarios para construir un dron de vigilancia.
     *
     * @param serial           codigo unico del dron
     * @param fabricante       empresa fabricante
     * @param modelo           referencia comercial
     * @param peso             peso en kilogramos
     * @param deteccionTermica {@code true} si el dron tiene deteccion termica
     */
    public VigilanciaFactory(String serial, String fabricante, String modelo,
                             double peso, boolean deteccionTermica) {
        this.serial = serial;
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.peso = peso;
        this.deteccionTermica = deteccionTermica;
    }

    /**
     * {@inheritDoc}
     *
     * @return una instancia de {@link Vigilancia} con id en 0
     */
    @Override
    public Drone crearDrone() {
        return new Vigilancia(0, serial, fabricante, modelo, peso, deteccionTermica);
    }
}