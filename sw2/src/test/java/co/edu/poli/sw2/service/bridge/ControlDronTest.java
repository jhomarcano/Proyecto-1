package co.edu.poli.sw2.service.bridge;
 
import co.edu.poli.sw2.modelo.Agricultura;
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Vigilancia;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
 
import static org.junit.jupiter.api.Assertions.*;
 
/**
 * Pruebas de las implementaciones concretas del Bridge:
 * {@link ControlBasico} y {@link ControlAutonomo}.
 *
 * @author Alejandra Cano y Juan Rosero
 */
class ControlDronTest {
 
    private final Drone vigilancia = new Vigilancia(1, "SN-V", "Autel", "EVO", 1.0, true);
    private final Drone agricultura = new Agricultura(2, "SN-A", "DJI", "Agras", 30.0, 20.0);
 
    @Test
    @DisplayName("ControlBasico describe la operacion manual e incluye serial y tipo")
    void controlBasico() {
        String texto = new ControlBasico().controlar(vigilancia);
 
        assertTrue(texto.startsWith("Control BASICO"));
        assertTrue(texto.contains("SN-V"));
        assertTrue(texto.contains("VIGILANCIA"));
        assertTrue(texto.contains("mando remoto"));
    }
 
    @Test
    @DisplayName("ControlAutonomo describe la ruta programada e incluye serial y tipo")
    void controlAutonomo() {
        String texto = new ControlAutonomo().controlar(agricultura);
 
        assertTrue(texto.startsWith("Control AUTONOMO"));
        assertTrue(texto.contains("SN-A"));
        assertTrue(texto.contains("AGRICULTURA"));
        assertTrue(texto.contains("GPS"));
    }
 
    @Test
    @DisplayName("Las dos implementaciones producen descripciones distintas para el mismo dron")
    void implementacionesDifieren() {
        assertNotEquals(new ControlBasico().controlar(vigilancia),
                new ControlAutonomo().controlar(vigilancia));
    }
 
    @Test
    @DisplayName("Ambas cumplen el contrato ControlDron")
    void cumplenElContrato() {
        ControlDron[] controles = {new ControlBasico(), new ControlAutonomo()};
 
        for (ControlDron c : controles) {
            assertNotNull(c.controlar(vigilancia));
        }
    }
}
