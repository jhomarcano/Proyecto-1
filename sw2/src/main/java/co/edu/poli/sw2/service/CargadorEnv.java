package co.edu.poli.sw2.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lector del archivo {@code .env} ubicado en la raiz del proyecto.
 * <p>
 * Carga el archivo una unica vez, la primera vez que se usa la clase, y
 * expone sus valores como pares clave/valor. Si el archivo no existe,
 * el mapa queda vacio y {@link ConexionBD} recurre a las demas fuentes
 * de configuracion.
 * <p>
 * Se implemento sin librerias externas para evitar conflictos con el
 * sistema de modulos de Java declarado en {@code module-info.java}.
 * <p>
 * Formato admitido: una entrada por linea con la forma {@code CLAVE=VALOR}.
 * Se ignoran las lineas en blanco y las que comienzan con {@code #}.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see ConexionBD
 */
public final class CargadorEnv {

    /** Rutas donde se busca el archivo, en orden de prioridad. */
    private static final String[] RUTAS_CANDIDATAS = { ".env", "sw2/.env", "../.env" };

    /** Valores leidos del archivo. Se carga una sola vez al inicializar la clase. */
    private static final Map<String, String> VALORES = cargar();

    /** Constructor privado: la clase solo expone metodos estaticos. */
    private CargadorEnv() { }

    /**
     * Devuelve el valor asociado a una clave del archivo .env.
     *
     * @param clave nombre de la variable, por ejemplo {@code DB_USER}
     * @return el valor encontrado, o {@code null} si la clave no existe
     */
    public static String obtener(String clave) {
        return VALORES.get(clave);
    }

    /**
     * Localiza y procesa el archivo .env.
     *
     * @return un mapa inmutable con las variables leidas, o vacio si no hubo archivo
     */
    private static Map<String, String> cargar() {
        Path archivo = localizarArchivo();
        if (archivo == null) {
            return Collections.emptyMap();
        }

        Map<String, String> mapa = new HashMap<>();
        try {
            List<String> lineas = Files.readAllLines(archivo, StandardCharsets.UTF_8);
            for (String linea : lineas) {
                procesarLinea(linea, mapa);
            }
        } catch (IOException e) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(mapa);
    }

    /**
     * Busca el archivo .env en las rutas candidatas.
     * <p>
     * Se prueban varias rutas porque el directorio de trabajo cambia
     * segun se ejecute la aplicacion desde el IDE o desde la terminal.
     *
     * @return la primera ruta legible encontrada, o {@code null} si no hay ninguna
     */
    private static Path localizarArchivo() {
        for (String ruta : RUTAS_CANDIDATAS) {
            Path candidata = Paths.get(ruta);
            if (Files.isReadable(candidata)) {
                return candidata;
            }
        }
        return null;
    }

    /**
     * Convierte una linea del archivo en una entrada del mapa.
     * <p>
     * Descarta lineas vacias, comentarios y lineas sin separador.
     *
     * @param linea linea leida del archivo
     * @param mapa  mapa donde se acumulan las variables
     */
    private static void procesarLinea(String linea, Map<String, String> mapa) {
        String limpia = linea.trim();
        if (limpia.isEmpty() || limpia.startsWith("#")) {
            return;
        }
        int separador = limpia.indexOf('=');
        if (separador <= 0) {
            return;
        }
        String clave = limpia.substring(0, separador).trim();
        String valor = quitarComillas(limpia.substring(separador + 1).trim());
        if (!clave.isEmpty()) {
            mapa.put(clave, valor);
        }
    }

    /**
     * Retira las comillas envolventes de un valor, si las tiene.
     * <p>
     * Permite escribir en el archivo valores con espacios, por ejemplo
     * {@code DB_PASSWORD="mi clave con espacios"}.
     *
     * @param valor valor leido del archivo
     * @return el valor sin comillas envolventes
     */
    private static String quitarComillas(String valor) {
        if (valor.length() >= 2
                && ((valor.startsWith("\"") && valor.endsWith("\""))
                 || (valor.startsWith("'") && valor.endsWith("'")))) {
            return valor.substring(1, valor.length() - 1);
        }
        return valor;
    }
}