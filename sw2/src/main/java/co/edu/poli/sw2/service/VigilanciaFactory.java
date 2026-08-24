package co.edu.poli.sw2.service;

import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Vigilancia;

/** Factoria concreta que produce instancias de {@link Vigilancia}. */
public class VigilanciaFactory extends DroneFactory {

    private final String serial;
    private final String fabricante;
    private final String modelo;
    private final double peso;
    private final boolean deteccionTermica;

    public VigilanciaFactory(String serial, String fabricante, String modelo,
                             double peso, boolean deteccionTermica) {
        this.serial = serial;
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.peso = peso;
        this.deteccionTermica = deteccionTermica;
    }

    @Override
    public Drone crearDrone() {
        return new Vigilancia(0, serial, fabricante, modelo, peso, deteccionTermica);
    }
}