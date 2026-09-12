package co.edu.poli.sw2.service.bridge;

import co.edu.poli.sw2.modelo.Drone;

/**
 * Implementacion concreta del patron Bridge: control manual.
 * <p>
 * Representa el modo en que un piloto opera el drone en tiempo real,
 * por ejemplo con un mando remoto.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see ControlDron
 */
public class ControlBasico implements ControlDron {

    @Override
    public String controlar(Drone drone) {
        return "Control BASICO: el piloto opera manualmente el drone "
                + drone.getSerial() + " (" + drone.getTipo()
                + ") mediante mando remoto, en tiempo real.";
    }
}