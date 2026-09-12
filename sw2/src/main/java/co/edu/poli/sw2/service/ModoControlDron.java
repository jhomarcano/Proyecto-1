package co.edu.poli.sw2.service;

import co.edu.poli.sw2.modelo.Drone;

/**
 * Abstraccion del patron <b>Bridge</b>.
 * <p>
 * No hereda de {@link ControlDron}: lo <b>contiene</b>. Esa es la esencia
 * del patron: la forma de asignar el control (esta clase) queda
 * desacoplada de como se ejecuta el control ({@link ControlBasico} o
 * {@link ControlAutonomo}), de modo que ambas jerarquias pueden crecer
 * por separado sin que una obligue a modificar la otra.
 * <p>
 * El resultado se guarda unicamente en {@link RegistroControlDron}
 * (memoria), nunca en la base de datos: la tabla {@code dron} no tiene
 * ninguna columna para el modo de control.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see ControlDron
 * @see RegistroControlDron
 */
public class ModoControlDron {

    /** Implementacion concreta del algoritmo de control (el "puente"). */
    private final ControlDron controlDron;

    /**
     * Crea la abstraccion enlazada a una implementacion concreta.
     *
     * @param controlDron algoritmo de control a delegar; no puede ser {@code null}
     */
    public ModoControlDron(ControlDron controlDron) {
        this.controlDron = controlDron;
    }

    /**
     * Aplica el control al drone y lo registra en memoria.
     *
     * @param drone drone sobre el que se asigna el control
     * @return la descripcion generada por la implementacion concreta
     */
    public String asignar(Drone drone) {
        String descripcion = controlDron.controlar(drone);
        RegistroControlDron.guardar(drone.getSerial(), descripcion);
        return descripcion;
    }
}