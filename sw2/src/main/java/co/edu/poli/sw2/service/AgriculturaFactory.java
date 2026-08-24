package co.edu.poli.sw2.service;

import co.edu.poli.sw2.modelo.Agricultura;
import co.edu.poli.sw2.modelo.Drone;

/** Factoria concreta que produce instancias de {@link Agricultura}. */
public class AgriculturaFactory extends DroneFactory {

    private final String serial;
    private final String fabricante;
    private final String modelo;
    private final double peso;
    private final double capacidadTanque;

    public AgriculturaFactory(String serial, String fabricante, String modelo,
                              double peso, double capacidadTanque) {
        this.serial = serial;
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.peso = peso;
        this.capacidadTanque = capacidadTanque;
    }

    @Override
    public Drone crearDrone() {
        return new Agricultura(0, serial, fabricante, modelo, peso, capacidadTanque);
    }
}