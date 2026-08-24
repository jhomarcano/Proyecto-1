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
 * Carga el archivo una unica vez (la primera vez que se usa la clase) y
 * expone sus valores como pares clave/valor. Si el archivo no existe,
 * simplemente queda vacio y la aplicacion puede recurrir a las variables
 * de entorno del sistema operativo.
 * <p>
 * Formato admitido: una entrada por linea con la forma {@code CLAVE=VALOR}.
 * Se ignoran las lineas en blanco y las que comienzan con {@code #}.
 */
public final class CargadorEnv {

    /** Rutas donde se busca el archivo, en orden de prioridad. */
    private static final String[] RUTAS_CANDIDATAS = { ".env", "sw2/.env", "../.env" };

    private static final Map<String, String> VALORES = cargar();

    private CargadorEnv() { }

    /**
     * Devuelve el valor asociado a una clave del archivo .env.
     *
     * @param clave nombre de la variable (por ejemplo {@code DB_USER})
     * @return el valor encontrado, o {@code null} si la clave no existe
     */
    public static String obtener(String clave) {
        return VALORES.get(clave);
    }

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
            // Si el archivo existe pero no se puede leer, se continua sin el.
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(mapa);
    }

    private static Path localizarArchivo() {
        for (String ruta : RUTAS_CANDIDATAS) {
            Path candidata = Paths.get(ruta);
            if (Files.isReadable(candidata)) {
                return candidata;
            }
        }
        return null;
    }

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

    /** Permite escribir DB_PASSWORD="mi clave con espacios" en el .env. */
    private static String quitarComillas(String valor) {
        if (valor.length() >= 2
                && ((valor.startsWith("\"") && valor.endsWith("\""))
                 || (valor.startsWith("'") && valor.endsWith("'")))) {
            return valor.substring(1, valor.length() - 1);
        }
        return valor;
    }
}