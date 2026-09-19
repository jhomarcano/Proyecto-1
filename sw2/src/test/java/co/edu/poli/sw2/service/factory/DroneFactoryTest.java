package co.edu.poli.sw2.service.factory;
 
import co.edu.poli.sw2.modelo.Agricultura;
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Vigilancia;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
 
import static org.junit.jupiter.api.Assertions.*;
 
/**
 * Pruebas del patron <b>Factory Method</b>: {@link DroneFactory} y sus
 * factorias concretas. No requieren base de datos.
 *
 * @author Alejandra Cano y Juan Rosero
 */
class DroneFactoryTest {
 
    @Test
    @DisplayName("AgriculturaFactory produce una Agricultura con los datos recibidos")
    void agriculturaFactory_produceAgricultura() {
        DroneFactory factoria = new AgriculturaFactory("SN-1", "DJI", "Agras T40", 38.0, 40.0);
 
        Drone drone = factoria.crearDrone();
 
        assertTrue(drone instanceof Agricultura);
        assertEquals("AGRICULTURA", drone.getTipo());
        assertEquals("SN-1", drone.getSerial());
        assertEquals("DJI", drone.getFabricante());
        assertEquals("Agras T40", drone.getModelo());
        assertEquals(38.0, drone.getPeso(), 0.0001);
        assertEquals(40.0, ((Agricultura) drone).getCapacidadTanque(), 0.0001);
    }
 
    @Test
    @DisplayName("VigilanciaFactory produce una Vigilancia con los datos recibidos")
    void vigilanciaFactory_produceVigilancia() {
        DroneFactory factoria = new VigilanciaFactory("SN-2", "Autel", "EVO II", 1.2, true);
 
        Drone drone = factoria.crearDrone();
 
        assertTrue(drone instanceof Vigilancia);
        assertEquals("VIGILANCIA", drone.getTipo());
        assertEquals("SN-2", drone.getSerial());
        assertEquals("Autel", drone.getFabricante());
        assertEquals("EVO II", drone.getModelo());
        assertEquals(1.2, drone.getPeso(), 0.0001);
        assertTrue(((Vigilancia) drone).isDeteccionTermica());
    }
 
    @Test
    @DisplayName("Toda factoria devuelve drones nuevos con id 0 (aun no persistidos)")
    void factorias_devuelvenIdCero() {
        DroneFactory[] factorias = {
                new AgriculturaFactory("A", "F", "M", 1.0, 1.0),
                new VigilanciaFactory("V", "F", "M", 1.0, false)
        };
 
        for (DroneFactory f : factorias) {
            assertEquals(0, f.crearDrone().getId());
        }
    }
 
    @Test
    @DisplayName("Polimorfismo: el cliente usa DroneFactory sin conocer la clase concreta")
    void polimorfismo_distintosTiposDesdeLaMismaFirma() {
        DroneFactory f1 = new AgriculturaFactory("A", "F", "M", 1.0, 1.0);
        DroneFactory f2 = new VigilanciaFactory("V", "F", "M", 1.0, false);
 
        assertNotEquals(f1.crearDrone().getClass(), f2.crearDrone().getClass());
    }
 
    @Test
    @DisplayName("Cada llamada a crearDrone entrega una instancia distinta")
    void crearDrone_entregaInstanciasNuevas() {
        DroneFactory factoria = new VigilanciaFactory("V", "F", "M", 1.0, true);
 
        assertNotSame(factoria.crearDrone(), factoria.crearDrone());
    }
 
    @Test
    @DisplayName("El modelo puede ser null (campo opcional)")
    void modeloNulo_seConserva() {
        Drone drone = new AgriculturaFactory("A", "F", null, 1.0, 1.0).crearDrone();
 
        assertNull(drone.getModelo());
    }
}
 