package co.edu.poli.sw2.service.Composite;
 
/**
 * Componente del patron <b>Composite</b> aplicado a la jerarquia de tipos
 * de sensor.
 * <p>
 * Es una interfaz funcional: tanto un sensor individual (una hoja del
 * arbol, por ejemplo "RTD" o "SPI") como una agrupacion completa de
 * sensores ({@link SensorComposite}, por ejemplo "Sensor Temperatura")
 * se tratan de forma uniforme a traves de este contrato, ya que ambos
 * saben producir su propia descripcion.
 * <p>
 * Al ser {@code @FunctionalInterface}, una hoja se puede crear sin
 * escribir una clase nueva, con una expresion lambda:
 * {@code () -> "Sensor Infrarrojo"}.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see SensorWrapper
 * @see SensorComposite
 */
@FunctionalInterface
public interface Sensor {
 
    /**
     * Devuelve la descripcion de este sensor o agrupacion de sensores.
     *
     * @return el texto descriptivo correspondiente
     */
    String descripcion();
}
