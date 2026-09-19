package co.edu.poli.sw2.service.builder;
 
import co.edu.poli.sw2.modelo.Agricultura;
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Vigilancia;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
 
import static org.junit.jupiter.api.Assertions.*;
 
/**
 * Pruebas de {@link VigilanciaGeneradorAleatorio}, el cliente del Builder.
 * <p>
 * Como el resultado es aleatorio, las pruebas clave se repiten muchas veces
 * y verifican rangos y pertenencia a conjuntos, no valores exactos.
 * No requieren base de datos: se le pasa la lista de drones a mano.
 *
 * @author Alejandra Cano y Juan Rosero
 */
class VigilanciaGeneradorAleatorioTest {
 
    private static final List<String> FABRICANTES_DEFECTO =
            Arrays.asList("DJI", "Autel", "Parrot", "Skydio");
    private static final List<String> MODELOS_DEFECTO =
            Arrays.asList("EVO II", "Mavic 3", "Anafi", "X10");
 
    private final VigilanciaGeneradorAleatorio generador = new VigilanciaGeneradorAleatorio();
 
    @RepeatedTest(30)
    @DisplayName("Sin historial usa fabricantes, modelos y rango de peso por defecto")
    void sinHistorial_usaValoresPorDefecto() {
        Vigilancia v = generador.generar(Collections.emptyList());
 
        assertTrue(FABRICANTES_DEFECTO.contains(v.getFabricante()));
        assertTrue(MODELOS_DEFECTO.contains(v.getModelo()));
        assertTrue(v.getPeso() >= 0.3 && v.getPeso() <= 5.0,
                "Peso fuera del rango por defecto: " + v.getPeso());
    }
 
    @RepeatedTest(30)
    @DisplayName("El serial generado tiene formato VIG-XXXXXX y el id es 0")
    void serialConPrefijoYIdCero() {
        Vigilancia v = generador.generar(Collections.emptyList());
 
        assertTrue(v.getSerial().matches("VIG-[0-9A-F]{1,6}"),
                "Formato de serial inesperado: " + v.getSerial());
        assertEquals(0, v.getId());
    }
 
    @RepeatedTest(30)
    @DisplayName("Con historial toma fabricante y modelo de un registro existente")
    void conHistorial_tomaDatosDelHistorial() {
        List<Drone> historial = Arrays.asList(
                new Vigilancia(1, "A", "Solo", "M1", 2.0, true),
                new Vigilancia(2, "B", "Solo", "M1", 4.0, false));
 
        Vigilancia v = generador.generar(historial);
 
        assertEquals("Solo", v.getFabricante());
        assertEquals("M1", v.getModelo());
    }
 
    @RepeatedTest(30)
    @DisplayName("Con historial el peso queda dentro del minimo y maximo observados")
    void conHistorial_pesoDentroDelRango() {
        List<Drone> historial = Arrays.asList(
                new Vigilancia(1, "A", "F", "M", 2.0, true),
                new Vigilancia(2, "B", "F", "M", 4.0, false));
 
        Vigilancia v = generador.generar(historial);
 
        assertTrue(v.getPeso() >= 2.0 && v.getPeso() <= 4.0,
                "Peso fuera del rango historico: " + v.getPeso());
    }
 
    @RepeatedTest(30)
    @DisplayName("Con un solo peso registrado amplia el rango en 1 kg")
    void unSoloPeso_amplíaElRango() {
        List<Drone> historial = Collections.singletonList(
                new Vigilancia(1, "A", "F", "M", 2.0, true));
 
        Vigilancia v = generador.generar(historial);
 
        assertTrue(v.getPeso() >= 2.0 && v.getPeso() <= 3.0,
                "Peso fuera de [2.0, 3.0]: " + v.getPeso());
    }
 
    @Test
    @DisplayName("Los drones de agricultura no cuentan como historial de vigilancia")
    void ignoraDronesQueNoSonVigilancia() {
        List<Drone> historial = new ArrayList<>();
        historial.add(new Agricultura(1, "AG", "SoloAgro", "ModeloAgro", 99.0, 10.0));
 
        Vigilancia v = generador.generar(historial);
 
        assertNotEquals("SoloAgro", v.getFabricante());
        assertTrue(FABRICANTES_DEFECTO.contains(v.getFabricante()));
    }
 
    @Test
    @DisplayName("El peso se redondea a dos decimales")
    void pesoRedondeadoADosDecimales() {
        for (int i = 0; i < 100; i++) {
            double peso = generador.generar(Collections.emptyList()).getPeso();
            assertEquals(peso, Math.round(peso * 100.0) / 100.0, 1e-9);
        }
    }
}
 