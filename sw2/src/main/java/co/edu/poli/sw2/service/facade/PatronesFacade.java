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
 *
 * La fachada permite:
 *
 * 1. Ejecutar Builder, Prototype, Composite y Adapter
 *    de manera individual.
 *
 * 2. Ejecutar los cuatro patrones mediante una sola operacion:
 *    ejecutarTodos().
 *
 * Los cuatro patrones NO dependen de esta clase.
 * La fachada solamente los coordina.
 *
 * @author Alejandra Cano y Juan Rosero
 */
public class PatronesFacade {

    private final DroneService droneService;
    private final DronePrototype dronePrototype;
    private final AdaptadorMision adaptadorMision;

    /**
     * Constructor por defecto.
     */
    public PatronesFacade() {
        this(
            new DroneService(),
            new DronePrototype(),
            new MisionJsonAdapter()
        );
    }

    /**
     * Constructor con inyeccion de dependencias.
     */
    public PatronesFacade(
            DroneService droneService,
            DronePrototype dronePrototype,
            AdaptadorMision adaptadorMision) {

        if (droneService == null ||
            dronePrototype == null ||
            adaptadorMision == null) {

            throw new IllegalArgumentException(
                "Los componentes de la fachada no pueden ser nulos."
            );
        }

        this.droneService = droneService;
        this.dronePrototype = dronePrototype;
        this.adaptadorMision = adaptadorMision;
    }

    // =========================================================
    // BUILDER - funcionamiento independiente
    // =========================================================

    /**
     * Ejecuta Builder de manera independiente.
     */
    public Vigilancia generarVigilanciaAleatoria() {
        return droneService.generarVigilanciaAleatoria();
    }

    // =========================================================
    // PROTOTYPE - funcionamiento independiente
    // =========================================================

    /**
     * Ejecuta Prototype de manera independiente.
     */
    public Drone clonar(Drone original) {
        return dronePrototype.clonar(original);
    }

    // =========================================================
    // COMPOSITE - funcionamiento independiente
    // =========================================================

    /**
     * Ejecuta Composite de manera independiente.
     */
    public Sensorcomposite construirArbolSensores() {
        return Sensorcompositedemo.construirArbolSensores();
    }

    // =========================================================
    // ADAPTER - funcionamiento independiente
    // =========================================================

    /**
     * Ejecuta Adapter de manera independiente.
     */
    public String exportarMisionAJson(Mision mision) {
        return adaptadorMision.exportar(mision);
    }

    // =========================================================
    // FACADE - EJECUTA LOS CUATRO PATRONES
    // =========================================================

    /**
     * Ejecuta Builder, Prototype, Composite y Adapter
     * mediante una sola llamada.
     *
     * Flujo:
     *
     * 1. Builder genera una Vigilancia.
     * 2. Prototype clona la Vigilancia (como subclase de Drone).
     * 3. Composite construye el arbol de sensores.
     * 4. Adapter exporta una Mision a JSON.
     *
     * Ninguno de los patrones conoce esta fachada.
     */
    public EjecucionCompleta ejecutarTodos(Mision mision) {

        if (mision == null) {
            throw new IllegalArgumentException(
                "La mision no puede ser nula."
            );
        }

        // 1. BUILDER
        Vigilancia vigilanciaConstruida = generarVigilanciaAleatoria();

        // 2. PROTOTYPE (Vigilancia hereda de Drone)
        Drone clon = clonar((Drone) vigilanciaConstruida);

        // 3. COMPOSITE
        Sensorcomposite arbolSensores = construirArbolSensores();

        // 4. ADAPTER
        String rutaJson = exportarMisionAJson(mision);

        // Retorno agrupado
        return new EjecucionCompleta(
                vigilanciaConstruida,
                clon,
                arbolSensores,
                rutaJson
        );
    }

    /**
     * Contiene los resultados de ejecutar los cuatro patrones.
     */
    public static final class EjecucionCompleta {

        private final Vigilancia vigilanciaConstruida;
        private final Drone clon;
        private final Sensorcomposite arbolSensores;
        private final String rutaJson;

        // Constructor interno visible para la fachada
        EjecucionCompleta(
                Vigilancia vigilanciaConstruida,
                Drone clon,
                Sensorcomposite arbolSensores,
                String rutaJson) {

            this.vigilanciaConstruida = vigilanciaConstruida;
            this.clon = clon;
            this.arbolSensores = arbolSensores;
            this.rutaJson = rutaJson;
        }

        public Vigilancia getVigilanciaConstruida() {
            return vigilanciaConstruida;
        }

        public Drone getClon() {
            return clon;
        }

        public Sensorcomposite getArbolSensores() {
            return arbolSensores;
        }

        public String getRutaJson() {
            return rutaJson;
        }
    }
}
