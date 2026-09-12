package co.edu.poli.sw2.service.bridge;

import co.edu.poli.sw2.modelo.Drone;

/**
 * Implementor del patron <b>Bridge</b>.
 * <p>
 * Define el contrato del "algoritmo" de control que se le puede aplicar
 * a un drone. No depende de la jerarquia {@link co.edu.poli.sw2.modelo.Drone}
 * y no tiene relacion con la capa DAO: el control nunca se persiste.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see ControlBasico
 * @see ControlAutonomo
 * @see ModoControlDron
 */
public interface ControlDron {

    /**
     * Ejecuta el algoritmo de control sobre el drone indicado.
     *
     * @param drone drone al que se le aplica el control
     * @return una descripcion legible de como quedo controlado el drone
     */
    String controlar(Drone drone);
}