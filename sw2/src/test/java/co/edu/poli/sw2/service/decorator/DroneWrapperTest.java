package co.edu.poli.sw2.service.decorator;
 
import co.edu.poli.sw2.modelo.Agricultura;
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Vigilancia;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
 
import static org.junit.jupiter.api.Assertions.*;
 
/**
 * Pruebas del componente concreto del patron <b>Decorator</b>:
 * {@link DroneWrapper}.
 *
 * @author Alejandra Cano y Juan Rosero
 */
class DroneWrapperTest {
 
    @Test
    @DisplayName("Describe un dron de vigilancia con sus datos y la deteccion termica")
    void descripcion_vigilancia() {
        Drone drone = new Vigilancia(7, "SN-V", "Autel", "EVO II", 1.2, true);
 
        String texto = new DroneWrapper(drone).descripcion();
 
        assertTrue(texto.contains("Drone VIGILANCIA"));
        assertTrue(texto.contains("[id=7]"));
        assertTrue(texto.contains("serial: SN-V"));
        assertTrue(texto.contains("fabricante: Autel"));
        assertTrue(texto.contains("modelo: EVO II"));
        assertTrue(texto.contains("peso: 1.2 kg"));
        assertTrue(texto.contains("deteccion termica: Si"));
    }
 
    @Test
    @DisplayName("Describe un dron de agricultura con la capacidad del tanque")
    void descripcion_agricultura() {
        Drone drone = new Agricultura(3, "SN-A", "DJI", "Agras", 38.0, 40.0);
 
        String texto = new DroneWrapper(drone).descripcion();
 
        assertTrue(texto.contains("Drone AGRICULTURA"));
        assertTrue(texto.contains("capacidad tanque: 40.0 L"));
    }
 
    @Test
    @DisplayName("Deteccion termica desactivada se muestra como No")
    void descripcion_sinDeteccionTermica() {
        Drone drone = new Vigilancia(1, "S", "F", "M", 1.0, false);
 
        assertTrue(new DroneWrapper(drone).descripcion().contains("deteccion termica: No"));
    }
 
    @Test
    @DisplayName("Un modelo nulo se muestra como (sin dato)")
    void descripcion_modeloNulo() {
        Drone drone = new Vigilancia(1, "S", "F", null, 1.0, false);
 
        assertTrue(new DroneWrapper(drone).descripcion().contains("modelo: (sin dato)"));
    }
 
    @Test
    @DisplayName("Un wrapper sin dron responde con un mensaje en lugar de fallar")
    void descripcion_droneNulo() {
        assertEquals("Drone no disponible", new DroneWrapper(null).descripcion());
    }
 
    @Test
    @DisplayName("getDrone devuelve la misma referencia, no una copia")
    void getDrone_devuelveElOriginal() {
        Drone drone = new Vigilancia(1, "S", "F", "M", 1.0, false);
 
        assertSame(drone, new DroneWrapper(drone).getDrone());
    }
 
    @Test
    @DisplayName("DroneWrapper cumple el contrato Componente")
    void esUnComponente() {
        Componente c = new DroneWrapper(new Vigilancia(1, "S", "F", "M", 1.0, false));
 
        assertNotNull(c.descripcion());
    }
}
 