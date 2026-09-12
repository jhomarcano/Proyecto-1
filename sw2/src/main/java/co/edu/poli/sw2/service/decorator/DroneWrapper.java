package co.edu.poli.sw2.service.decorator;

import co.edu.poli.sw2.modelo.Agricultura;
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Vigilancia;

/**
 * Componente concreto del patron <b>Decorator</b>.
 * <p>
 * Adapta un {@link Drone} a la interfaz {@link Componente} para que
 * pueda ser decorado. Es la base de la cadena: no agrega funcionalidad
 * alguna, solo expone la descripcion del dron tal como esta registrado.
 * <p>
 * Este envoltorio es lo que permite aplicar el patron sin tocar la
 * entidad: {@code Drone} no implementa {@code Componente} ni conoce este
 * paquete. Si manana se quisiera decorar otra entidad, bastaria con
 * escribir otro wrapper.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see Componente
 * @see BateriaAdicional
 */
public class DroneWrapper implements Componente {

    /** Dron envuelto. Se guarda la referencia original, no una copia. */
    private final Drone drone;

    /**
     * Envuelve el dron indicado para poder decorarlo.
     *
     * @param drone dron a describir; no deberia ser {@code null}
     */
    public DroneWrapper(Drone drone) {
        this.drone = drone;
    }

    /**
     * Devuelve el dron envuelto.
     * <p>
     * Lo usa la capa de presentacion para mostrar que el objeto que
     * viaja dentro de la cadena de decoracion sigue siendo el mismo.
     *
     * @return el dron original recibido en el constructor
     */
    public Drone getDrone() {
        return drone;
    }

    /**
     * {@inheritDoc}
     *
     * @return la descripcion base del dron, sin ningun anadido
     */
    @Override
    public String descripcion() {
        if (drone == null) {
            return "Drone no disponible";
        }

        StringBuilder texto = new StringBuilder();
        texto.append("Drone ").append(drone.getTipo())
             .append(" [id=").append(drone.getId()).append("]")
             .append(" | serial: ").append(drone.getSerial())
             .append(" | fabricante: ").append(drone.getFabricante())
             .append(" | modelo: ").append(drone.getModelo() == null
                     ? "(sin dato)" : drone.getModelo())
             .append(" | peso: ").append(drone.getPeso()).append(" kg");

        if (drone instanceof Agricultura) {
            texto.append(" | capacidad tanque: ")
                 .append(((Agricultura) drone).getCapacidadTanque()).append(" L");
        } else if (drone instanceof Vigilancia) {
            texto.append(" | deteccion termica: ")
                 .append(((Vigilancia) drone).isDeteccionTermica() ? "Si" : "No");
        }

        return texto.toString();
    }
}