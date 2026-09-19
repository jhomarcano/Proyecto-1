package co.edu.poli.sw2.service.prototype;
 
import co.edu.poli.sw2.exception.DronValidacionException;
import co.edu.poli.sw2.modelo.Agricultura;
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Vigilancia;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
 
import static org.junit.jupiter.api.Assertions.*;
 
/**
 * Pruebas del patron <b>Prototype</b>: {@link DronePrototype}.
 * No requieren base de datos.
 *
 * @author Alejandra Cano y Juan Rosero
 */
class DronePrototypeTest {
 
    private final DronePrototype prototipo = new DronePrototype();
 
    @Test
    @DisplayName("Clonar una Vigilancia devuelve otro objeto, de la misma clase y con los mismos datos")
    void clonarVigilancia_copiaIndependiente() {
        Vigilancia original = new Vigilancia(5, "SN-V", "Autel", "EVO II", 1.2, true);
 
        Drone clon = prototipo.clonar(original);
 
        assertNotSame(original, clon);
        assertEquals(Vigilancia.class, clon.getClass());
        assertEquals("SN-V", clon.getSerial());
        assertEquals("Autel", clon.getFabricante());
        assertEquals("EVO II", clon.getModelo());
        assertEquals(1.2, clon.getPeso(), 0.0001);
        assertTrue(((Vigilancia) clon).isDeteccionTermica());
    }
 
    @Test
    @DisplayName("Clonar una Agricultura devuelve otro objeto, de la misma clase y con los mismos datos")
    void clonarAgricultura_copiaIndependiente() {
        Agricultura original = new Agricultura(8, "SN-A", "DJI", "Agras", 38.0, 40.0);
 
        Drone clon = prototipo.clonar(original);
 
        assertNotSame(original, clon);
        assertEquals(Agricultura.class, clon.getClass());
        assertEquals("SN-A", clon.getSerial());
        assertEquals("DJI", clon.getFabricante());
        assertEquals("Agras", clon.getModelo());
        assertEquals(38.0, clon.getPeso(), 0.0001);
        assertEquals(40.0, ((Agricultura) clon).getCapacidadTanque(), 0.0001);
    }
 
    @Test
    @DisplayName("El clon siempre tiene id 0, aunque el original ya este persistido")
    void clon_tieneIdCero() {
        assertEquals(0, prototipo.clonar(new Vigilancia(99, "S", "F", "M", 1.0, false)).getId());
        assertEquals(0, prototipo.clonar(new Agricultura(99, "S", "F", "M", 1.0, 1.0)).getId());
    }
 
    @Test
    @DisplayName("Modificar el clon no altera al original")
    void modificarClon_noAfectaAlOriginal() {
        Vigilancia original = new Vigilancia(1, "SN", "Autel", "EVO", 1.0, false);
        Vigilancia clon = (Vigilancia) prototipo.clonar(original);
 
        clon.setSerial("OTRO");
        clon.setPeso(9.9);
        clon.setDeteccionTermica(true);
 
        assertEquals("SN", original.getSerial());
        assertEquals(1.0, original.getPeso(), 0.0001);
        assertFalse(original.isDeteccionTermica());
    }
 
    @Test
    @DisplayName("Modificar el original despues de clonar no altera al clon")
    void modificarOriginal_noAfectaAlClon() {
        Agricultura original = new Agricultura(1, "SN", "DJI", "Agras", 30.0, 20.0);
        Agricultura clon = (Agricultura) prototipo.clonar(original);
 
        original.setCapacidadTanque(999.0);
        original.setFabricante("XAG");
 
        assertEquals(20.0, clon.getCapacidadTanque(), 0.0001);
        assertEquals("DJI", clon.getFabricante());
    }
 
    @Test
    @DisplayName("Clonar dos veces produce dos clones distintos")
    void clonarDosVeces_produceInstanciasDistintas() {
        Vigilancia original = new Vigilancia(1, "SN", "F", "M", 1.0, true);
 
        assertNotSame(prototipo.clonar(original), prototipo.clonar(original));
    }
 
    @Test
    @DisplayName("Clonar null lanza DronValidacionException")
    void clonarNulo_lanzaExcepcion() {
        DronValidacionException ex = assertThrows(DronValidacionException.class,
                () -> prototipo.clonar(null));
 
        assertTrue(ex.getMessage().toLowerCase().contains("clonarlo"));
    }
 
    @Test
    @DisplayName("Un tipo de dron no soportado lanza DronValidacionException")
    void clonarTipoNoSoportado_lanzaExcepcion() {
        Drone desconocido = new Drone(1, "S", "F", "M", 1.0) {
            @Override
            public String getTipo() {
                return "SUBMARINO";
            }
        };
 
        assertThrows(DronValidacionException.class, () -> prototipo.clonar(desconocido));
    }
}
 