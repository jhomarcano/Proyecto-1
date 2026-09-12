package co.edu.poli.sw2.service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Almacenamiento en memoria de los controles asignados a los drones.
 * <p>
 * Igual que {@link DronePrototype}, esta clase nunca toca
 * {@link ConexionBD} ni ningun DAO: el modo de control (basico o
 * autonomo) es informacion transitoria de la aplicacion, no del dominio
 * persistente. Se pierde al reiniciar la aplicacion, lo cual es
 * intencional: la base de datos solo guarda los atributos propios del
 * drone (serial, fabricante, peso, etc.).
 * <p>
 * Se indexa por serial porque es el dato estable y unico del drone
 * (columna UNIQUE en la tabla {@code dron}).
 *
 * @author Alejandra Cano y Juan Rosero
 * @see ModoControlDron
 */
public final class RegistroControlDron {

    /** Mapa serial -> descripcion del control asignado, en memoria. */
    private static final Map<String, String> CONTROLES = new LinkedHashMap<>();

    /** Constructor privado: la clase solo expone metodos estaticos. */
    private RegistroControlDron() {
    }

    /**
     * Guarda (o reemplaza) el control asignado a un drone.
     *
     * @param serialDrone serial del drone
     * @param descripcion descripcion generada por el control aplicado
     */
    public static void guardar(String serialDrone, String descripcion) {
        CONTROLES.put(serialDrone, descripcion);
    }

    /**
     * Consulta el control asignado a un drone.
     *
     * @param serialDrone serial del drone
     * @return la descripcion guardada, o {@code null} si no tiene control asignado
     */
    public static String consultar(String serialDrone) {
        return CONTROLES.get(serialDrone);
    }

    /**
     * Indica cuantos drones tienen un control asignado en memoria.
     *
     * @return el tamanio actual del registro
     */
    public static int tamano() {
        return CONTROLES.size();
    }
}