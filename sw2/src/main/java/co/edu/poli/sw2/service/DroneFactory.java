package co.edu.poli.sw2.service;

import co.edu.poli.sw2.modelo.Drone;

/**
 * Factoria abstracta del patron Factory Method. Cada factoria concreta
 * decide que subclase de {@link Drone} se instancia, de modo que las capas
 * superiores no dependan de las clases concretas.
 */
public abstract class DroneFactory {

    /** Construye la instancia concreta de Drone que corresponda. */
    public abstract Drone crearDrone();
}