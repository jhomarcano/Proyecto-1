package co.edu.poli.sw2.service;

import co.edu.poli.sw2.modelo.Drone;

/**
 * Factoria abstracta del patron <b>Factory Method</b>.
 * <p>
 * Define el contrato que toda factoria concreta debe cumplir. Cada
 * subclase decide que tipo de {@link Drone} instanciar, de modo que las
 * capas superiores puedan crear drones sin depender de las clases
 * concretas {@code Agricultura} o {@code Vigilancia}.
 * <p>
 * Los datos necesarios para construir el dron se reciben en el
 * constructor de la factoria concreta, no como parametros del metodo;
 * asi la firma de {@code crearDrone()} se mantiene identica en todas las
 * subclases y el polimorfismo funciona correctamente.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see AgriculturaFactory
 * @see VigilanciaFactory
 */
public abstract class DroneFactory {

    /**
     * Construye la instancia concreta de Drone que corresponda.
     *
     * @return un dron nuevo, con id en 0 por no haberse persistido aun
     */
    public abstract Drone crearDrone();
}