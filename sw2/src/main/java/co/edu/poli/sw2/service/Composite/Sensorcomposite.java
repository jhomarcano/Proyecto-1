package co.edu.poli.sw2.service.Composite;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
 
/**
 * Composite del patron homonimo aplicado a la jerarquia de tipos de
 * sensor (ver diagrama: Sensor General -&gt; Sensor Temperatura / Sensor
 * Camara / Sensor Sonido / Sensor Inteligente -&gt; subtipos).
 * <p>
 * Agrupa varios {@link SensorWrapper} -nunca instancias sueltas de
 * {@link Sensor}- de modo que esta clase nunca depende directamente de
 * la interfaz: toda incorporacion pasa primero por un wrapper. Como la
 * propia clase implementa {@link Sensor}, un {@code SensorComposite} se
 * puede envolver a su vez en un {@link SensorWrapper} y agregarse a otro
 * composite, lo que permite construir arboles de varios niveles, por
 * ejemplo "Sensor Sonido" conteniendo a "Sensor Digital", que a su vez
 * contiene "SPI" y "UART".
 *
 * @author Alejandra Cano y Juan Rosero
 * @see Sensor
 * @see SensorWrapper
 */
public class Sensorcomposite implements Sensor {
 
    /** Nombre de esta agrupacion, por ejemplo "Sensor Temperatura". */
    private final String nombre;
 
    /** Sensores (hojas o subcomposites, siempre envueltos) que pertenecen a esta agrupacion. */
    private final List<Sensorwrapper> sensores = new ArrayList<>();
 
    /**
     * Crea una agrupacion generica de sensores.
     * <p>
     * Corresponde al constructor sin argumentos del diagrama de clases;
     * equivale a construirla con el nombre por defecto "Sensor General".
     */
    public Sensorcomposite() {
        this("Sensor General");
    }
 
    /**
     * Crea una agrupacion de sensores con un nombre identificable.
     *
     * @param nombre nombre de la categoria, por ejemplo "Sensor Camara"
     */
    public Sensorcomposite(String nombre) {
        this.nombre = nombre;
    }
 
    /**
     * Agrega un sensor (hoja o subcomposite ya envuelto) a esta agrupacion.
     *
     * @param sensor sensor envuelto a incorporar
     * @return un mensaje que confirma la operacion
     */
    public String agregar(Sensorwrapper sensor) {
        sensores.add(sensor);
        return "Sensor agregado a '" + nombre + "'.";
    }
 
    /**
     * Elimina un sensor previamente agregado a esta agrupacion.
     *
     * @param sensor sensor envuelto a retirar
     * @return un mensaje que confirma si la eliminacion tuvo efecto
     */
    public String eliminar(Sensorwrapper sensor) {
        boolean eliminado = sensores.remove(sensor);
        return eliminado
                ? "Sensor eliminado de '" + nombre + "'."
                : "El sensor indicado no pertenece a '" + nombre + "'.";
    }
 
    /**
     * Devuelve el nombre de esta agrupacion.
     *
     * @return el nombre, por ejemplo "Sensor Temperatura"
     */
    public String getNombre() {
        return nombre;
    }
 
    /**
     * Devuelve los sensores (hojas o subcomposites, siempre envueltos)
     * que pertenecen directamente a esta agrupacion.
     * <p>
     * Se expone como lista de solo lectura para que quien la consulte
     * -por ejemplo, la ventana que dibuja el arbol- no pueda alterar la
     * estructura interna del composite.
     *
     * @return una vista inmodificable de los sensores hijos
     */
    public List<Sensorwrapper> getSensores() {
        return Collections.unmodifiableList(sensores);
    }
 
    /**
     * {@inheritDoc}
     * <p>
     * Construye el nombre de esta agrupacion seguido de la descripcion
     * de cada uno de sus sensores, indentada un nivel mas por cada
     * profundidad del arbol, de modo que una jerarquia de varios niveles
     * se lea como un arbol de texto.
     *
     * @return el nombre de la agrupacion y la descripcion de sus sensores
     */
    @Override
    public String descripcion() {
        StringBuilder sb = new StringBuilder(nombre);
        for (Sensorwrapper hijo : sensores) {
            for (String linea : hijo.descripcion().split("\n")) {
                sb.append("\n  ").append(linea);
            }
        }
        return sb.toString();
    }
}
 