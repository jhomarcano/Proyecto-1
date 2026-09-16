package co.edu.poli.sw2.service.adapter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Adaptee del patron <b>Adapter</b>.
 * <p>
 * Sabe unicamente crear archivos de texto en disco a partir de un
 * contenido ya formateado; no conoce la clase {@link co.edu.poli.sw2.modelo.Mision}
 * ni el dominio de drones. Es la pieza "existente" que
 * {@link MisionJsonAdapter} adapta para que la aplicacion la use como
 * un {@link AdaptadorMision}.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see MisionJsonAdapter
 */
public class ArchivoJson {

    /** Carpeta donde se guardan los archivos generados. */
    private static final String CARPETA_SALIDA = "data/misiones";

    /** Formato usado para que cada archivo tenga un nombre unico. */
    private static final DateTimeFormatter FORMATO_NOMBRE =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS");

    /**
     * Crea un archivo con el contenido indicado.
     * <p>
     * La carpeta de destino se crea automaticamente si aun no existe.
     * El nombre incluye una marca de tiempo para que llamadas sucesivas
     * nunca se sobrescriban entre si.
     *
     * @param contenido texto a escribir en el archivo, ya formateado
     * @return la ruta absoluta del archivo creado
     * @throws RuntimeException si falla la escritura en disco
     */
    public String crearArchivo(String contenido) {
        try {
            Path carpeta = Paths.get(CARPETA_SALIDA);
            Files.createDirectories(carpeta);

            String nombreArchivo = "mision_" + FORMATO_NOMBRE.format(LocalDateTime.now()) + ".json";
            Path archivo = carpeta.resolve(nombreArchivo);

            Files.write(archivo, contenido.getBytes(StandardCharsets.UTF_8));

            return archivo.toAbsolutePath().normalize().toString();
        } catch (IOException e) {
            throw new RuntimeException("No fue posible crear el archivo JSON de la mision.", e);
        }
    }
}