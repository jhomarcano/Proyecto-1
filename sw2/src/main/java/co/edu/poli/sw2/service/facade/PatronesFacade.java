package co.edu.poli.sw2.service.facade;

import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Mision;
import co.edu.poli.sw2.modelo.Vigilancia;
import co.edu.poli.sw2.service.DroneService;
import co.edu.poli.sw2.service.Composite.Sensorcomposite;
import co.edu.poli.sw2.service.Composite.Sensorcompositedemo;
import co.edu.poli.sw2.service.adapter.AdaptadorMision;
import co.edu.poli.sw2.service.adapter.MisionJsonAdapter;
import co.edu.poli.sw2.service.prototype.DronePrototype;

/**
 * Fachada de los patrones de diseño usados desde la interfaz grafica.
 * <p>
 * La vista/controlador no necesita conocer como se coordinan Builder,
 * Prototype, Composite y Adapter. Cuando el usuario pulsa uno de los
 * botones de patrones, {@code DroneController} entra por esta fachada
 * y ella delega la operacion en el subsistema correspondiente.
 *
 * @author Alejandra Cano y Juan Rosero
 */
public class PatronesFacade {

    /** Servicio existente que concentra las operaciones de negocio de drones. */
    private final DroneService droneService;

    /** Implementacion del patron Prototype. */
    private final DronePrototype dronePrototype;

    /** Contrato Adapter usado para exportar misiones. */
    private final AdaptadorMision adaptadorMision;

    /**
     * Crea la fachada con las implementaciones reales del proyecto.
     */
    public PatronesFacade() {
        this(new DroneService(), new DronePrototype(), new MisionJsonAdapter());
    }

    /**
     * Constructor que permite inyectar los subsistemas.
     * <p>
     * Resulta util para pruebas unitarias y evita que la fachada quede
     * acoplada a una unica implementacion del Adapter.
     *
     * @param droneService servicio de drones
     * @param dronePrototype implementacion de Prototype
     * @param adaptadorMision implementacion de Adapter
     */
    public PatronesFacade(DroneService droneService,
                          DronePrototype dronePrototype,
                          AdaptadorMision adaptadorMision) {
        if (droneService == null || dronePrototype == null || adaptadorMision == null) {
            throw new IllegalArgumentException("Los componentes de la fachada no pueden ser nulos.");
        }
        this.droneService = droneService;
        this.dronePrototype = dronePrototype;
        this.adaptadorMision = adaptadorMision;
    }

    /**
     * Ejecuta la operacion correspondiente al patron Builder.
     * <p>
     * El generador existente usa internamente {@code VigilanciaBuilder}
     * para ensamblar el dron de vigilancia.
     *
     * @return un dron de vigilancia nuevo, aun no persistido
     */
    public Vigilancia generarVigilanciaAleatoria() {
        return droneService.generarVigilanciaAleatoria();
    }

    /**
     * Ejecuta la operacion correspondiente al patron Prototype.
     *
     * @param original dron que se desea clonar
     * @return una copia independiente del dron
     */
    public Drone clonar(Drone original) {
        return dronePrototype.clonar(original);
    }

    /**
     * Ejecuta la operacion correspondiente al patron Composite.
     *
     * @return raiz del arbol de sensores utilizado por la aplicacion
     */
    public Sensorcomposite construirArbolSensores() {
        return Sensorcompositedemo.construirArbolSensores();
    }

    /**
     * Ejecuta la operacion correspondiente al patron Adapter.
     *
     * @param mision mision que se desea exportar
     * @return ruta absoluta del archivo JSON generado
     */
    public String exportarMisionAJson(Mision mision) {
        return adaptadorMision.exportar(mision);
    }
}
