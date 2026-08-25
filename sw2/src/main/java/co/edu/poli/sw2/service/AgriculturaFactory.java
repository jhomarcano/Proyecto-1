package co.edu.poli.sw2.service;

import co.edu.poli.sw2.modelo.Agricultura;
import co.edu.poli.sw2.modelo.Drone;

/**
 * Factoria concreta que produce instancias de {@link Agricultura}.
 * <p>
 * Recibe en el constructor los datos capturados en el formulario y los
 * conserva hasta que se invoca {@link #crearDrone()}.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see DroneFactory
 */
public class AgriculturaFactory extends DroneFactory {

    /** Codigo unico del dron a construir. */
    private final String serial;

    /** Empresa fabricante del dron a construir. */
    private final String fabricante;

    /** Referencia comercial del dron a construir. */
    private final String modelo;

    /** Peso en kilogramos del dron a construir. */
    private final double peso;

    /** Capacidad del tanque en litros del dron a construir. */
    private final double capacidadTanque;

    /**
     * Guarda los datos necesarios para construir un dron de agricultura.
     *
     * @param serial          codigo unico del dron
     * @param fabricante      empresa fabricante
     * @param modelo          referencia comercial
     * @param peso            peso en kilogramos
     * @param capacidadTanque capacidad del tanque en litros
     */
    public AgriculturaFactory(String serial, String fabricante, String modelo,
                              double peso, double capacidadTanque) {
        this.serial = serial;
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.peso = peso;
        this.capacidadTanque = capacidadTanque;
    }

    /**
     * {@inheritDoc}
     *
     * @return una instancia de {@link Agricultura} con id en 0
     */
    @Override
    public Drone crearDrone() {
        return new Agricultura(0, serial, fabricante, modelo, peso, capacidadTanque);
    }
}