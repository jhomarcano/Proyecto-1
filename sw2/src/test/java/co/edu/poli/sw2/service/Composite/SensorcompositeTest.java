package co.edu.poli.sw2.service.Composite;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias del patron Composite aplicado a sensores.
 */
class SensorcompositeTest {

    @Test
    @DisplayName("Un composite puede agregar un sensor envuelto")
    void agregar_sensor() {
        Sensorcomposite composite = new Sensorcomposite("Sensor Temperatura");
        Sensorwrapper sensor = new Sensorwrapper(() -> "RTD");

        String resultado = composite.agregar(sensor);

        assertEquals("Sensor agregado a 'Sensor Temperatura'.", resultado);
        assertEquals(1, composite.getSensores().size());
        assertEquals("Sensor Temperatura\n  RTD", composite.descripcion());
    }

    @Test
    @DisplayName("Un composite puede contener varios sensores")
    void agregar_varios_sensores() {
        Sensorcomposite composite = new Sensorcomposite("Sensor Camara");

        composite.agregar(new Sensorwrapper(() -> "Infrarrojo"));
        composite.agregar(new Sensorwrapper(() -> "RGB"));
        composite.agregar(new Sensorwrapper(() -> "Profundidad"));

        assertEquals(3, composite.getSensores().size());
        assertEquals(
                "Sensor Camara\n  Infrarrojo\n  RGB\n  Profundidad",
                composite.descripcion()
        );
    }

    @Test
    @DisplayName("Un composite puede eliminar un sensor que ya fue agregado")
    void eliminar_sensor_existente() {
        Sensorcomposite composite = new Sensorcomposite("Sensor Sonido");
        Sensorwrapper sensor = new Sensorwrapper(() -> "UART");

        composite.agregar(sensor);

        String resultado = composite.eliminar(sensor);

        assertEquals("Sensor eliminado de 'Sensor Sonido'.", resultado);
        assertTrue(composite.getSensores().isEmpty());
        assertEquals("Sensor Sonido", composite.descripcion());
    }

    @Test
    @DisplayName("Eliminar un sensor que no pertenece al composite no modifica la lista")
    void eliminar_sensor_inexistente() {
        Sensorcomposite composite = new Sensorcomposite("Sensor Sonido");
        Sensorwrapper sensor = new Sensorwrapper(() -> "UART");

        String resultado = composite.eliminar(sensor);

        assertEquals(
                "El sensor indicado no pertenece a 'Sensor Sonido'.",
                resultado
        );
        assertTrue(composite.getSensores().isEmpty());
    }

    @Test
    @DisplayName("Un composite puede contener otro composite mediante Sensorwrapper")
    void composite_anidado() {
        Sensorcomposite general = new Sensorcomposite("Sensor General");
        Sensorcomposite temperatura = new Sensorcomposite("Sensor Temperatura");

        temperatura.agregar(new Sensorwrapper(() -> "RTD"));
        temperatura.agregar(new Sensorwrapper(() -> "Termopar"));

        // Se envuelve el composite 'temperatura' dentro de un Sensorwrapper
        general.agregar(new Sensorwrapper(temperatura));

        // Corrección de indentación en la descripción anidada (4 espacios para las hojas internas)
        assertEquals(
                "Sensor General\n  Sensor Temperatura\n    RTD\n    Termopar",
                general.descripcion()
        );
    }

    @Test
    @DisplayName("Sensorwrapper delega la descripcion al sensor envuelto")
    void wrapper_delega_descripcion() {
        Sensor sensor = () -> "Sensor Digital";
        Sensorwrapper wrapper = new Sensorwrapper(sensor);

        assertEquals("Sensor Digital", wrapper.descripcion());
        assertSame(sensor, wrapper.getSensor());
    }

    @Test
    @DisplayName("Sensorwrapper.desde adapta correctamente un sensor del modelo")
    void wrapper_desde_sensor_modelo() {
        co.edu.poli.sw2.modelo.Sensor sensor =
                new co.edu.poli.sw2.modelo.Sensor(1, "Temperatura", "RTD");

        Sensorwrapper wrapper = Sensorwrapper.desde(sensor);

        assertNotNull(wrapper.getSensor());
        assertEquals(sensor.toString(), wrapper.descripcion());
    }

    @Test
    @DisplayName("El constructor sin argumentos crea Sensor General")
    void constructor_por_defecto() {
        Sensorcomposite composite = new Sensorcomposite();

        assertEquals("Sensor General", composite.getNombre());
        assertEquals("Sensor General", composite.descripcion());
    }

    @Test
    @DisplayName("getSensores devuelve una lista no modificable")
    void lista_no_modificable() {
        Sensorcomposite composite = new Sensorcomposite("Sensor General");
        composite.agregar(new Sensorwrapper(() -> "SPI"));

        assertThrows(
                UnsupportedOperationException.class,
                () -> composite.getSensores().clear()
        );
    }
}
