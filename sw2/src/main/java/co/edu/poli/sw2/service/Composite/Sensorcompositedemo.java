package co.edu.poli.sw2.service.Composite;
 
/**
 * Demostracion del patron Composite: arma la jerarquia completa de tipos
 * de sensor (Sensor General -&gt; Temperatura / Camara / Sonido /
 * Inteligente -&gt; subtipos, con "Sensor Digital" anidado dentro de
 * "Sensor Sonido") y la imprime como arbol de texto.
 * <p>
 * No forma parte del flujo normal de la aplicacion: sirve para verificar
 * visualmente que el Composite arma el mismo arbol que el diagrama de
 * clases del dominio.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see SensorComposite
 * @see SensorWrapper
 */
public final class Sensorcompositedemo {
 
    /** Clase de utilidad: no se instancia. */
    private Sensorcompositedemo() {
    }
 
    /**
     * Construye el arbol completo de tipos de sensor.
     *
     * @return la raiz "Sensor General" con toda su jerarquia armada
     */
    public static Sensorcomposite construirArbolSensores() {
        Sensorcomposite raiz = new Sensorcomposite("Sensor General");
 
        Sensorcomposite temperatura = new Sensorcomposite("Sensor Temperatura");
        temperatura.agregar(hoja("Sensor Infrarrojo"));
        temperatura.agregar(hoja("RTD"));
 
        Sensorcomposite camara = new Sensorcomposite("Sensor Camara");
        camara.agregar(hoja("Sensor CMOS"));
        camara.agregar(hoja("Sensor CCD"));
 
        Sensorcomposite digital = new Sensorcomposite("Sensor Digital");
        digital.agregar(hoja("SPI"));
        digital.agregar(hoja("UART"));
 
        Sensorcomposite sonido = new Sensorcomposite("Sensor Sonido");
        sonido.agregar(hoja("Sensor Analogico"));
        sonido.agregar(rama(digital));
 
        raiz.agregar(rama(temperatura));
        raiz.agregar(rama(camara));
        raiz.agregar(rama(sonido));
        raiz.agregar(hoja("Sensor Inteligente"));
 
        return raiz;
    }
 
    /**
     * Envuelve un nombre como hoja del arbol (sin hijos).
     *
     * @param nombre texto de la hoja
     * @return el wrapper listo para agregarse a un composite
     */
    private static Sensorwrapper hoja(String nombre) {
        return new Sensorwrapper(() -> nombre);
    }
 
    /**
     * Envuelve un subcomposite para que pueda agregarse como un nodo mas
     * de otro composite, logrando la anidacion de varios niveles.
     *
     * @param subArbol composite a envolver
     * @return el wrapper listo para agregarse a un composite padre
     */
    private static Sensorwrapper rama(Sensorcomposite subArbol) {
        return new Sensorwrapper(subArbol);
    }
 
    /**
     * Punto de entrada para ejecutar la demostracion desde la linea de comandos.
     *
     * @param args no se utilizan
     */
    public static void main(String[] args) {
        System.out.println(construirArbolSensores().descripcion());
    }
}
 