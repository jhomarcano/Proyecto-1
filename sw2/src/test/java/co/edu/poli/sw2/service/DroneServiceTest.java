package co.edu.poli.sw2.service;

import co.edu.poli.sw2.exception.DronValidacionException;
import co.edu.poli.sw2.modelo.Agricultura;
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Vigilancia;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integracion de {@link DroneService}.
 * <p>
 * Verifican que el patron Factory seleccione la factoria correcta segun
 * el tipo solicitado, que el resultado se persista en la base de datos y
 * que las validaciones de negocio se apliquen antes de abrir la conexion.
 * <p>
 * Requieren que PostgreSQL este corriendo y que las credenciales esten
 * configuradas en el archivo {@code .env}.
 *
 * @author Alejandra Cano y Juan Rosero
 */
class DroneServiceTest {

    /** Servicio bajo prueba. */
    private final DroneService servicio = new DroneService();

    /** Registros creados durante la prueba, para eliminarlos al final. */
    private final List<Drone> creados = new ArrayList<>();

    /**
     * Genera un serial que no puede chocar con datos existentes.
     *
     * @return un serial unico con prefijo TEST
     */
    private String serialUnico() {
        return "TEST-" + UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Elimina los registros creados por la prueba que acaba de ejecutarse.
     */
    @AfterEach
    void limpiarDatosDePrueba() {
        for (Drone d : creados) {
            try {
                servicio.eliminar(d);
            } catch (RuntimeException ignorada) {
                // Ya pudo haber sido eliminado.
            }
        }
        creados.clear();
    }

    /**
     * Verifica que el tipo AGRICULTURA produzca una instancia de la
     * subclase correcta, con su atributo especifico persistido.
     */
    @Test
    @DisplayName("El tipo AGRICULTURA produce una instancia de Agricultura")
    void crearAgricultura_usaLaFactoriaCorrecta() {
        Drone creado = servicio.crear("AGRICULTURA", serialUnico(), "DJI",
                "Agras T40", "38.0", "40.0", false);
        creados.add(creado);

        assertTrue(creado instanceof Agricultura);
        assertEquals(40.0, ((Agricultura) creado).getCapacidadTanque(), 0.0001);
        assertTrue(creado.getId() > 0);
    }

    /**
     * Verifica que el tipo VIGILANCIA produzca una instancia de la
     * subclase correcta, con su atributo especifico persistido.
     */
    @Test
    @DisplayName("El tipo VIGILANCIA produce una instancia de Vigilancia")
    void crearVigilancia_usaLaFactoriaCorrecta() {
        Drone creado = servicio.crear("VIGILANCIA", serialUnico(), "Autel",
                "EVO II", "1.2", null, true);
        creados.add(creado);

        assertTrue(creado instanceof Vigilancia);
        assertTrue(((Vigilancia) creado).isDeteccionTermica());
    }

    /**
     * Verifica que un tipo no soportado se rechace antes de tocar la base de datos.
     */
    @Test
    @DisplayName("Un tipo no soportado es rechazado antes de tocar la BD")
    void crearTipoInvalido_lanzaExcepcion() {
        assertThrows(DronValidacionException.class,
                () -> servicio.crear("SUBMARINO", serialUnico(), "DJI",
                        "X", "1.0", "10.0", false));
    }

    /**
     * Verifica que la capacidad del tanque sea obligatoria para los drones
     * de agricultura.
     */
    @Test
    @DisplayName("Un dron de agricultura sin capacidad de tanque es rechazado")
    void crearAgriculturaSinCapacidad_lanzaExcepcion() {
        assertThrows(DronValidacionException.class,
                () -> servicio.crear("AGRICULTURA", serialUnico(), "DJI",
                        "Agras", "20.0", "", false));
    }

    /**
     * Verifica que no se pueda actualizar sin haber seleccionado un dron.
     */
    @Test
    @DisplayName("Actualizar sin seleccion previa es rechazado")
    void actualizarSinSeleccion_lanzaExcepcion() {
        assertThrows(DronValidacionException.class,
                () -> servicio.actualizar(null, "SN-1", "DJI", "X", "1.0", "10.0", false));
    }
}
