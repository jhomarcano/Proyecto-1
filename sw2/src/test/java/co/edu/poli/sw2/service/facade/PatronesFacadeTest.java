package co.edu.poli.sw2.service.facade;

import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Mision;
import co.edu.poli.sw2.modelo.Vigilancia;
import co.edu.poli.sw2.service.DroneService;
import co.edu.poli.sw2.service.Composite.Sensorcomposite;
import co.edu.poli.sw2.service.adapter.AdaptadorMision;
import co.edu.poli.sw2.service.prototype.DronePrototype;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias del patron Facade.
 *
 * La fachada proporciona un punto de acceso simplificado a:
 *
 * - Builder
 * - Prototype
 * - Composite
 * - Adapter
 *
 * Ademas se verifica que cada patron pueda seguir utilizandose
 * de forma independiente.
 */
class PatronesFacadeTest {

    // ============================================================
    // BUILDER
    // ============================================================

    @Test
    @DisplayName("Facade permite ejecutar Builder")
    void builder_aTravesDeLaFachada() {

        Vigilancia esperado = new Vigilancia(
                0,
                "VIG-FACADE",
                "DJI",
                "Mavic 3",
                1.2,
                true
        );

        /*
         * Se sustituye DroneService por una implementacion controlada
         * para que la prueba no dependa de datos de la base de datos.
         */
        DroneService servicioFalso = new DroneService() {

            @Override
            public Vigilancia generarVigilanciaAleatoria() {
                return esperado;
            }
        };

        PatronesFacade facade = new PatronesFacade(
                servicioFalso,
                new DronePrototype(),
                mision -> "no-usado"
        );

        Vigilancia resultado =
                facade.generarVigilanciaAleatoria();

        assertNotNull(resultado);
        assertSame(esperado, resultado);
    }


    // ============================================================
    // PROTOTYPE
    // ============================================================

    @Test
    @DisplayName("Facade permite ejecutar Prototype")
    void prototype_aTravesDeLaFachada() {

        Drone original = new Vigilancia(
                10,
                "SN-1",
                "Autel",
                "EVO",
                1.0,
                true
        );

        PatronesFacade facade = new PatronesFacade(
                new DroneService(),
                new DronePrototype(),
                mision -> "no-usado"
        );

        Drone clon = facade.clonar(original);

        assertNotNull(clon);

        // Debe ser un objeto diferente.
        assertNotSame(original, clon);

        // El Prototype genera el clon con id 0.
        assertEquals(0, clon.getId());

        // Los datos deben conservarse.
        assertEquals(
                original.getSerial(),
                clon.getSerial()
        );

        assertEquals(
                original.getFabricante(),
                clon.getFabricante()
        );

        assertEquals(
                original.getModelo(),
                clon.getModelo()
        );

        assertEquals(
                original.getPeso(),
                clon.getPeso()
        );
    }


    // ============================================================
    // COMPOSITE
    // ============================================================

    @Test
    @DisplayName("Facade permite ejecutar Composite")
    void composite_aTravesDeLaFachada() {

        PatronesFacade facade = new PatronesFacade(
                new DroneService(),
                new DronePrototype(),
                mision -> "no-usado"
        );

        Sensorcomposite raiz =
                facade.construirArbolSensores();

        assertNotNull(raiz);

        assertEquals(
                "Sensor General",
                raiz.getNombre()
        );

        String descripcion =
                raiz.descripcion();

        assertTrue(
                descripcion.contains("Sensor Temperatura")
        );

        assertTrue(
                descripcion.contains("Sensor Digital")
        );

        assertTrue(
                descripcion.contains("Sensor Sonido")
        );
    }


    // ============================================================
    // ADAPTER
    // ============================================================

    @Test
    @DisplayName("Facade permite ejecutar Adapter")
    void adapter_aTravesDeLaFachada() {

        Mision mision = new Mision(
                1,
                "Inspeccion",
                "Bogota",
                "2026-09-20"
        );

        /*
         * Adapter falso para que la prueba no dependa
         * de la creacion real del archivo JSON.
         */
        AdaptadorMision adapterFalso =
                m -> "ruta/fachada.json";

        PatronesFacade facade = new PatronesFacade(
                new DroneService(),
                new DronePrototype(),
                adapterFalso
        );

        String resultado =
                facade.exportarMisionAJson(mision);

        assertNotNull(resultado);

        assertEquals(
                "ruta/fachada.json",
                resultado
        );
    }


    // ============================================================
    // FACADE COMPLETA
    // ============================================================

    @Test
    @DisplayName(
            "Facade ejecuta Builder, Prototype, Composite y Adapter"
            + " mediante una sola llamada"
    )
    void ejecutarTodos_ejecutaLosCuatroPatrones() {

        /*
         * -----------------------------
         * Builder
         * -----------------------------
         */

        Vigilancia construido = new Vigilancia(
                0,
                "VIG-TODOS",
                "DJI",
                "Mavic 3",
                1.2,
                true
        );

        DroneService servicioFalso =
                new DroneService() {

                    @Override
                    public Vigilancia generarVigilanciaAleatoria() {
                        return construido;
                    }
                };


        /*
         * -----------------------------
         * Adapter
         * -----------------------------
         */

        Mision mision = new Mision(
                1,
                "Inspeccion",
                "Bogota",
                "2026-09-20"
        );

        AdaptadorMision adapterFalso =
                m -> "ruta/facade-completa.json";


        /*
         * -----------------------------
         * Facade
         * -----------------------------
         */

        PatronesFacade facade =
                new PatronesFacade(
                        servicioFalso,
                        new DronePrototype(),
                        adapterFalso
                );


        /*
         * Una sola llamada debe ejecutar
         * los cuatro patrones.
         */
        PatronesFacade.EjecucionCompleta resultado =
                facade.ejecutarTodos(mision);


        assertNotNull(resultado);


        /*
         * -----------------------------
         * Verificar Builder
         * -----------------------------
         */

        assertSame(
                construido,
                resultado.getVigilanciaConstruida()
        );


        /*
         * -----------------------------
         * Verificar Prototype
         * -----------------------------
         */

        assertNotNull(
                resultado.getClon()
        );

        assertNotSame(
                construido,
                resultado.getClon()
        );

        assertEquals(
                construido.getSerial(),
                resultado.getClon().getSerial()
        );

        assertEquals(
                construido.getFabricante(),
                resultado.getClon().getFabricante()
        );


        /*
         * -----------------------------
         * Verificar Composite
         * -----------------------------
         */

        assertNotNull(
                resultado.getArbolSensores()
        );

        assertEquals(
                "Sensor General",
                resultado.getArbolSensores().getNombre()
        );


        /*
         * -----------------------------
         * Verificar Adapter
         * -----------------------------
         */

        assertEquals(
                "ruta/facade-completa.json",
                resultado.getRutaJson()
        );
    }


    // ============================================================
    // VALIDACION DE MISION
    // ============================================================

    @Test
    @DisplayName("Facade rechaza una mision nula")
    void ejecutarTodos_misionNula_lanzaExcepcion() {

        PatronesFacade facade =
                new PatronesFacade(
                        new DroneService(),
                        new DronePrototype(),
                        mision -> "ruta.json"
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> facade.ejecutarTodos(null)
        );
    }


    // ============================================================
    // VALIDACION DE DEPENDENCIAS
    // ============================================================

    @Test
    @DisplayName("Facade rechaza dependencias nulas")
    void dependenciasNulas_rechazadas() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PatronesFacade(
                        null,
                        new DronePrototype(),
                        mision -> "ok"
                )
        );


        assertThrows(
                IllegalArgumentException.class,
                () -> new PatronesFacade(
                        new DroneService(),
                        null,
                        mision -> "ok"
                )
        );


        assertThrows(
                IllegalArgumentException.class,
                () -> new PatronesFacade(
                        new DroneService(),
                        new DronePrototype(),
                        null
                )
        );
    }
}