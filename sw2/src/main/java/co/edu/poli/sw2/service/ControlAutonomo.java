package co.edu.poli.sw2.service;

import co.edu.poli.sw2.modelo.Drone;

/**
 * Implementacion concreta del patron Bridge: control autonomo.
 * <p>
 * Representa el modo en que el drone ejecuta una ruta programada sin
 * intervencion humana directa.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see ControlDron
 */
public class ControlAutonomo implements ControlDron {

    @Override
    public String controlar(Drone drone) {
        return "Control AUTONOMO: el drone " + drone.getSerial()
                + " (" + drone.getTipo()
                + ") sigue una ruta programada por GPS, sin intervencion manual.";
    }
}