package co.edu.poli.sw2.service.decorator;
 
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Vigilancia;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
 
import static org.junit.jupiter.api.Assertions.*;
 
/**
 * Pruebas del decorador concreto {@link BateriaAdicional}.
 *
 * @author Alejandra Cano y Juan Rosero
 */
class BateriaAdicionalTest {
 
    private Drone drone() {
        return new Vigilancia(1, "SN-1", "Autel", "EVO II", 1.2, true);
    }
 
    @Test
    @DisplayName("Conserva la descripcion base y agrega la de la bateria al final")
    void descripcion_agregaLaBateria() {
        Componente base = new DroneWrapper(drone());
 
        Componente decorado = new BateriaAdicional(base, "LiPo 5000mAh");
 
        assertEquals(base.descripcion() + " + Bateria adicional: LiPo 5000mAh",
                decorado.descripcion());
    }
 
    @Test
    @DisplayName("Se pueden apilar varios decoradores y se conserva el orden")
    void descripcion_apilaDecoradores() {
        Componente base = new DroneWrapper(drone());
        Componente uno = new BateriaAdicional(base, "Bateria A");
        Componente dos = new BateriaAdicional(uno, "Bateria B");
 
        String texto = dos.descripcion();
 
        assertTrue(texto.startsWith(base.descripcion()));
        assertTrue(texto.contains("Bateria adicional: Bateria A"));
        assertTrue(texto.endsWith("Bateria adicional: Bateria B"));
        assertTrue(texto.indexOf("Bateria A") < texto.indexOf("Bateria B"));
    }
 
    @Test
    @DisplayName("Decorar no modifica al componente envuelto")
    void decorar_noModificaAlBase() {
        Componente base = new DroneWrapper(drone());
        String antes = base.descripcion();
 
        new BateriaAdicional(base, "Extra");
 
        assertEquals(antes, base.descripcion());
    }
 
    @Test
    @DisplayName("Decorar no modifica al Drone envuelto")
    void decorar_noModificaAlDrone() {
        Drone d = drone();
        Componente decorado = new BateriaAdicional(new DroneWrapper(d), "Extra");
 
        decorado.descripcion();
 
        assertEquals("SN-1", d.getSerial());
        assertEquals(1.2, d.getPeso(), 0.0001);
        assertEquals(1, d.getId());
    }
 
    @Test
    @DisplayName("El decorador y el componente base son objetos distintos")
    void decorado_esOtroObjeto() {
        Componente base = new DroneWrapper(drone());
 
        assertNotSame(base, new BateriaAdicional(base, "Extra"));
    }
 
    @Test
    @DisplayName("Un componente nulo se trata como texto vacio, sin lanzar excepcion")
    void componenteNulo_noFalla() {
        Componente decorado = new BateriaAdicional(null, "Extra");
 
        assertEquals(" + Bateria adicional: Extra", decorado.descripcion());
    }
 
    @Test
    @DisplayName("getDescripcionBateria devuelve el texto recibido")
    void getDescripcionBateria() {
        assertEquals("Extra", new BateriaAdicional(new DroneWrapper(drone()), "Extra")
                .getDescripcionBateria());
    }
}
 