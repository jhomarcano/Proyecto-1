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
 * Pruebas unitarias de la fachada que sirve de entrada a los patrones
 * Builder, Prototype, Composite y Adapter.
 */
class PatronesFacadeTest {

    @Test
    @DisplayName("Builder se ejecuta a traves de la fachada")
    void builder_aTravesDeLaFachada() {
        Vigilancia esperado = new Vigilancia(
                0, "VIG-FACADE", "DJI", "Mavic 3", 1.2, true);

        DroneService servicioFalso = new DroneService() {
            @Override
            public Vigilancia generarVigilanciaAleatoria() {
                return esperado;
            }
        };

        PatronesFacade facade = new PatronesFacade(
                servicioFalso, new DronePrototype(), mision -> "no-usado");

        assertSame(esperado, facade.generarVigilanciaAleatoria());
    }

    @Test
    @DisplayName("Prototype se ejecuta a traves de la fachada")
    void prototype_aTravesDeLaFachada() {
        Drone original = new Vigilancia(
                10, "SN-1", "Autel", "EVO", 1.0, true);

        PatronesFacade facade = new PatronesFacade(
                new DroneService(), new DronePrototype(), mision -> "no-usado");

        Drone clon = facade.clonar(original);

        assertNotSame(original, clon);
        assertEquals(0, clon.getId());
        assertEquals(original.getSerial(), clon.getSerial());
    }

    @Test
    @DisplayName("Composite se ejecuta a traves de la fachada")
    void composite_aTravesDeLaFachada() {
        PatronesFacade facade = new PatronesFacade(
                new DroneService(), new DronePrototype(), mision -> "no-usado");

        Sensorcomposite raiz = facade.construirArbolSensores();

        assertNotNull(raiz);
        assertEquals("Sensor General", raiz.getNombre());
        assertTrue(raiz.descripcion().contains("Sensor Temperatura"));
        assertTrue(raiz.descripcion().contains("Sensor Digital"));
    }

    @Test
    @DisplayName("Adapter se ejecuta a traves de la fachada")
    void adapter_aTravesDeLaFachada() {
        Mision mision = new Mision(
                1, "Inspeccion", "Bogota", "2026-09-20");

        AdaptadorMision adapterFalso = m -> "ruta/fachada.json";
        PatronesFacade facade = new PatronesFacade(
                new DroneService(), new DronePrototype(), adapterFalso);

        assertEquals("ruta/fachada.json", facade.exportarMisionAJson(mision));
    }

    @Test
    @DisplayName("La fachada rechaza dependencias nulas")
    void dependenciasNulas_rechazadas() {
        assertThrows(IllegalArgumentException.class,
                () -> new PatronesFacade(null, new DronePrototype(), m -> "ok"));
        assertThrows(IllegalArgumentException.class,
                () -> new PatronesFacade(new DroneService(), null, m -> "ok"));
        assertThrows(IllegalArgumentException.class,
                () -> new PatronesFacade(new DroneService(), new DronePrototype(), null));
    }
}
