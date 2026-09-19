package co.edu.poli.sw2.service.bridge;
 
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
 
import java.util.UUID;
 
import static org.junit.jupiter.api.Assertions.*;
 
/**
 * Pruebas del almacenamiento en memoria {@link RegistroControlDron}.
 * <p>
 * El registro es estatico y no tiene metodo para vaciarlo, asi que cada
 * prueba usa seriales unicos (UUID) y compara tamanos de forma relativa.
 *
 * @author Alejandra Cano y Juan Rosero
 */
class RegistroControlDronTest {
 
    private String serialUnico() {
        return "TEST-" + UUID.randomUUID();
    }
 
    @Test
    @DisplayName("Lo guardado se puede consultar por serial")
    void guardarYConsultar() {
        String serial = serialUnico();
 
        RegistroControlDron.guardar(serial, "control X");
 
        assertEquals("control X", RegistroControlDron.consultar(serial));
    }
 
    @Test
    @DisplayName("Consultar un serial sin control devuelve null")
    void consultarInexistente_devuelveNull() {
        assertNull(RegistroControlDron.consultar(serialUnico()));
    }
 
    @Test
    @DisplayName("Guardar sobre un serial existente reemplaza el valor y no crece el registro")
    void guardarDeNuevo_reemplaza() {
        String serial = serialUnico();
        RegistroControlDron.guardar(serial, "primero");
        int tamanoAntes = RegistroControlDron.tamano();
 
        RegistroControlDron.guardar(serial, "segundo");
 
        assertEquals("segundo", RegistroControlDron.consultar(serial));
        assertEquals(tamanoAntes, RegistroControlDron.tamano());
    }
 
    @Test
    @DisplayName("Guardar un serial nuevo aumenta el tamano en uno")
    void guardarNuevo_aumentaElTamano() {
        int antes = RegistroControlDron.tamano();
 
        RegistroControlDron.guardar(serialUnico(), "algo");
 
        assertEquals(antes + 1, RegistroControlDron.tamano());
    }
 
    @Test
    @DisplayName("Los controles de dos drones distintos no se mezclan")
    void serialesDistintos_noSeMezclan() {
        String a = serialUnico();
        String b = serialUnico();
 
        RegistroControlDron.guardar(a, "de A");
        RegistroControlDron.guardar(b, "de B");
 
        assertEquals("de A", RegistroControlDron.consultar(a));
        assertEquals("de B", RegistroControlDron.consultar(b));
    }
}
 