package co.edu.poli.sw2.service;

import co.edu.poli.sw2.exception.DronValidacionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de las reglas de negocio de {@link ValidadorDrone}.
 * <p>
 * No requieren base de datos ni ninguna otra dependencia externa: validan
 * unicamente la logica de las reglas de validacion. Por eso se ejecutan
 * en milisegundos y pueden correrse en cualquier maquina.
 *
 * @author Alejandra Cano y Juan Rosero
 */
class ValidadorDroneTest {

    /**
     * Verifica que un serial alfanumerico valido sea aceptado.
     */
    @Test
    @DisplayName("Un serial alfanumerico es aceptado")
    void serialValido_noLanzaExcepcion() {
        assertDoesNotThrow(() -> ValidadorDrone.validarSerial("SN-00123"));
    }

    /**
     * Verifica que un serial en blanco sea rechazado.
     */
    @Test
    @DisplayName("Un serial vacio es rechazado")
    void serialVacio_lanzaExcepcion() {
        assertThrows(DronValidacionException.class,
                () -> ValidadorDrone.validarSerial("   "));
    }

    /**
     * Verifica que un serial nulo sea rechazado sin provocar NullPointerException.
     */
    @Test
    @DisplayName("Un serial nulo es rechazado")
    void serialNulo_lanzaExcepcion() {
        assertThrows(DronValidacionException.class,
                () -> ValidadorDrone.validarSerial(null));
    }

    /**
     * Verifica que un serial compuesto solo por simbolos sea rechazado.
     */
    @Test
    @DisplayName("Un serial de solo simbolos es rechazado")
    void serialSoloSimbolos_lanzaExcepcion() {
        assertThrows(DronValidacionException.class,
                () -> ValidadorDrone.validarSerial("###--##"));
    }

    /**
     * Verifica que un nombre de fabricante valido sea aceptado.
     */
    @Test
    @DisplayName("Un fabricante valido es aceptado")
    void fabricanteValido_noLanzaExcepcion() {
        assertDoesNotThrow(() -> ValidadorDrone.validarFabricante("DJI"));
    }

    /**
     * Verifica que un fabricante compuesto solo por digitos sea rechazado.
     */
    @Test
    @DisplayName("Un fabricante puramente numerico es rechazado")
    void fabricanteNumerico_lanzaExcepcion() {
        assertThrows(DronValidacionException.class,
                () -> ValidadorDrone.validarFabricante("33"));
    }

    /**
     * Verifica que un fabricante vacio sea rechazado.
     */
    @Test
    @DisplayName("Un fabricante vacio es rechazado")
    void fabricanteVacio_lanzaExcepcion() {
        assertThrows(DronValidacionException.class,
                () -> ValidadorDrone.validarFabricante(""));
    }

    /**
     * Verifica la conversion de un peso escrito con punto decimal.
     * <p>
     * El tercer argumento de assertEquals es la tolerancia: los numeros
     * de tipo double tienen error de redondeo, asi que se compara que la
     * diferencia sea menor a ese margen.
     */
    @Test
    @DisplayName("El peso con punto decimal se convierte correctamente")
    void pesoConPunto_seParsea() {
        assertEquals(0.9, ValidadorDrone.parsearPeso("0.9"), 0.0001);
    }

    /**
     * Verifica que tambien se acepte la coma como separador decimal.
     */
    @Test
    @DisplayName("El peso con coma decimal tambien se acepta")
    void pesoConComa_seParsea() {
        assertEquals(1.5, ValidadorDrone.parsearPeso("1,5"), 0.0001);
    }

    /**
     * Verifica que se rechacen pesos de cero o negativos.
     */
    @Test
    @DisplayName("Un peso de cero o negativo es rechazado")
    void pesoNoPositivo_lanzaExcepcion() {
        assertThrows(DronValidacionException.class, () -> ValidadorDrone.parsearPeso("0"));
        assertThrows(DronValidacionException.class, () -> ValidadorDrone.parsearPeso("-2.5"));
    }

    /**
     * Verifica que un peso no numerico sea rechazado con mensaje de dominio.
     */
    @Test
    @DisplayName("Un peso que no es numero es rechazado")
    void pesoNoNumerico_lanzaExcepcion() {
        assertThrows(DronValidacionException.class,
                () -> ValidadorDrone.parsearPeso("pesado"));
    }

    /**
     * Verifica la conversion de una capacidad de tanque valida.
     */
    @Test
    @DisplayName("Una capacidad de tanque valida se convierte correctamente")
    void capacidadValida_seParsea() {
        assertEquals(10.5, ValidadorDrone.parsearCapacidadTanque("10.5"), 0.0001);
    }

    /**
     * Verifica que se rechace una capacidad vacia o no positiva.
     */
    @Test
    @DisplayName("Una capacidad de tanque vacia o no positiva es rechazada")
    void capacidadInvalida_lanzaExcepcion() {
        assertThrows(DronValidacionException.class,
                () -> ValidadorDrone.parsearCapacidadTanque(""));
        assertThrows(DronValidacionException.class,
                () -> ValidadorDrone.parsearCapacidadTanque("0"));
    }
}
