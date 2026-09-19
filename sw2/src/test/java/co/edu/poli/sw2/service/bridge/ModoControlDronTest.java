package co.edu.poli.sw2.service.bridge;
 
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Vigilancia;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
 
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
 
import static org.junit.jupiter.api.Assertions.*;
 
/**
 * Pruebas de la abstraccion del <b>Bridge</b>: {@link ModoControlDron}.
 * <p>
 * Usan un {@link ControlDron} falso (lambda) para demostrar que la
 * abstraccion delega en cualquier implementacion, sin conocer cual es.
 *
 * @author Alejandra Cano y Juan Rosero
 */
class ModoControlDronTest {
 
    /** Crea un dron con serial unico para no chocar con el registro estatico. */
    private Drone droneUnico() {
        return new Vigilancia(1, "TEST-" + UUID.randomUUID(), "F", "M", 1.0, false);
    }
 
    @Test
    @DisplayName("asignar delega en la implementacion recibida y devuelve su resultado")
    void asignar_delegaEnLaImplementacion() {
        ControlDron falso = d -> "FALSO:" + d.getSerial();
        Drone drone = droneUnico();
 
        String resultado = new ModoControlDron(falso).asignar(drone);
 
        assertEquals("FALSO:" + drone.getSerial(), resultado);
    }
 
    @Test
    @DisplayName("asignar guarda el resultado en el registro en memoria, indexado por serial")
    void asignar_guardaEnRegistro() {
        Drone drone = droneUnico();
 
        String resultado = new ModoControlDron(new ControlBasico()).asignar(drone);
 
        assertEquals(resultado, RegistroControlDron.consultar(drone.getSerial()));
    }
 
    @Test
    @DisplayName("La misma abstraccion funciona con implementaciones intercambiables")
    void abstraccion_conImplementacionesIntercambiables() {
        Drone d1 = droneUnico();
        Drone d2 = droneUnico();
 
        String basico = new ModoControlDron(new ControlBasico()).asignar(d1);
        String autonomo = new ModoControlDron(new ControlAutonomo()).asignar(d2);
 
        assertTrue(basico.startsWith("Control BASICO"));
        assertTrue(autonomo.startsWith("Control AUTONOMO"));
    }
 
    @Test
    @DisplayName("Reasignar otro control al mismo dron reemplaza el anterior")
    void reasignar_reemplazaElControl() {
        Drone drone = droneUnico();
 
        new ModoControlDron(new ControlBasico()).asignar(drone);
        String nuevo = new ModoControlDron(new ControlAutonomo()).asignar(drone);
 
        assertEquals(nuevo, RegistroControlDron.consultar(drone.getSerial()));
    }
 
    @Test
    @DisplayName("La abstraccion invoca a la implementacion exactamente una vez")
    void asignar_invocaUnaSolaVez() {
        AtomicInteger llamadas = new AtomicInteger();
        ControlDron contador = d -> {
            llamadas.incrementAndGet();
            return "ok";
        };
 
        new ModoControlDron(contador).asignar(droneUnico());
 
        assertEquals(1, llamadas.get());
    }
}
 