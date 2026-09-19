package co.edu.poli.sw2.service;
 
import co.edu.poli.sw2.exception.DronValidacionException;
import co.edu.poli.sw2.modelo.Agricultura;
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Vigilancia;
import co.edu.poli.sw2.service.decorator.BateriaAdicional;
import co.edu.poli.sw2.service.decorator.Componente;
import co.edu.poli.sw2.service.decorator.DroneWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
 
import java.util.UUID;
 
import static org.junit.jupiter.api.Assertions.*;
 
/**
 * Pruebas UNITARIAS de como {@link DroneService} integra los patrones
 * Prototype, Decorator y Bridge. Ninguna toca la base de datos: crear
 * {@code DroneService} no abre conexion, y estos metodos no llaman al DAO.
 * <p>
 * Complementa a {@code DroneServiceTest}, que es de integracion.
 *
 * @author Alejandra Cano y Juan Rosero
 */
class DroneServicePatronesTest {
 
    private final DroneService servicio = new DroneService();
 
    private Drone droneUnico() {
        return new Vigilancia(1, "TEST-" + UUID.randomUUID(), "Autel", "EVO", 1.0, true);
    }
 
    // ---------- Prototype ----------
 
    @Test
    @DisplayName("clonar devuelve una copia con id 0")
    void clonar_devuelveCopia() {
        Drone original = new Agricultura(4, "SN", "DJI", "Agras", 30.0, 20.0);
 
        Drone clon = servicio.clonar(original);
 
        assertNotSame(original, clon);
        assertEquals(0, clon.getId());
        assertEquals("SN", clon.getSerial());
    }
 
    @Test
    @DisplayName("clonar sin seleccion lanza DronValidacionException")
    void clonar_nuloLanzaExcepcion() {
        assertThrows(DronValidacionException.class, () -> servicio.clonar(null));
    }
 
    // ---------- Decorator ----------
 
    @Test
    @DisplayName("envolver devuelve un DroneWrapper que contiene el mismo dron")
    void envolver_devuelveWrapper() {
        Drone drone = droneUnico();
 
        Componente c = servicio.envolver(drone);
 
        assertTrue(c instanceof DroneWrapper);
        assertSame(drone, ((DroneWrapper) c).getDrone());
    }
 
    @Test
    @DisplayName("envolver sin dron lanza DronValidacionException")
    void envolver_nuloLanzaExcepcion() {
        assertThrows(DronValidacionException.class, () -> servicio.envolver(null));
    }
 
    @Test
    @DisplayName("envolverConBateria devuelve un BateriaAdicional con la descripcion recortada")
    void envolverConBateria_ok() {
        Componente base = servicio.envolver(droneUnico());
 
        Componente decorado = servicio.envolverConBateria(base, "   LiPo 5000mAh  ");
 
        assertTrue(decorado instanceof BateriaAdicional);
        assertEquals("LiPo 5000mAh", ((BateriaAdicional) decorado).getDescripcionBateria());
        assertTrue(decorado.descripcion().endsWith("Bateria adicional: LiPo 5000mAh"));
    }
 
    @Test
    @DisplayName("envolverConBateria rechaza componente nulo")
    void envolverConBateria_componenteNulo() {
        assertThrows(DronValidacionException.class,
                () -> servicio.envolverConBateria(null, "algo"));
    }
 
    @Test
    @DisplayName("envolverConBateria rechaza descripcion nula o en blanco")
    void envolverConBateria_descripcionInvalida() {
        Componente base = servicio.envolver(droneUnico());
 
        assertThrows(DronValidacionException.class, () -> servicio.envolverConBateria(base, null));
        assertThrows(DronValidacionException.class, () -> servicio.envolverConBateria(base, "   "));
    }
 
    // ---------- Bridge ----------
 
    @Test
    @DisplayName("asignarControl BASICO devuelve la descripcion y la deja consultable")
    void asignarControl_basico() {
        Drone drone = droneUnico();
 
        String resultado = servicio.asignarControl(drone, "BASICO");
 
        assertTrue(resultado.startsWith("Control BASICO"));
        assertEquals(resultado, servicio.consultarControl(drone));
    }
 
    @Test
    @DisplayName("asignarControl AUTONOMO devuelve la descripcion y la deja consultable")
    void asignarControl_autonomo() {
        Drone drone = droneUnico();
 
        String resultado = servicio.asignarControl(drone, "AUTONOMO");
 
        assertTrue(resultado.startsWith("Control AUTONOMO"));
        assertEquals(resultado, servicio.consultarControl(drone));
    }
 
    @Test
    @DisplayName("asignarControl sin dron lanza DronValidacionException")
    void asignarControl_droneNulo() {
        assertThrows(DronValidacionException.class, () -> servicio.asignarControl(null, "BASICO"));
    }
 
    @Test
    @DisplayName("asignarControl con un tipo desconocido lanza DronValidacionException")
    void asignarControl_tipoNoSoportado() {
        assertThrows(DronValidacionException.class,
                () -> servicio.asignarControl(droneUnico(), "TELEPATICO"));
    }
 
    @Test
    @DisplayName("consultarControl de un dron sin control devuelve null")
    void consultarControl_sinControl() {
        assertNull(servicio.consultarControl(droneUnico()));
    }
 
    @Test
    @DisplayName("consultarControl con dron nulo devuelve null")
    void consultarControl_droneNulo() {
        assertNull(servicio.consultarControl(null));
    }
 
    // ---------- Factory (validaciones previas al DAO) ----------
 
    @Test
    @DisplayName("crear sin tipo se rechaza antes de tocar la BD")
    void crear_sinTipo() {
        assertThrows(DronValidacionException.class,
                () -> servicio.crear(null, "SN-1", "DJI", "M", "1.0", "10", false));
        assertThrows(DronValidacionException.class,
                () -> servicio.crear("  ", "SN-1", "DJI", "M", "1.0", "10", false));
    }
}
 