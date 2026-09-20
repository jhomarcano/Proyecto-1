package co.edu.poli.sw2.service.adapter;

import co.edu.poli.sw2.modelo.Mision;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class MisionJsonAdapterTest {

    private Path archivoCreado;

    @AfterEach
    void limpiarArchivoGenerado() throws IOException {
        if (archivoCreado != null) {
            Files.deleteIfExists(archivoCreado);
        }
    }

    @Test
    @DisplayName("El adaptador exporta una mision y crea un archivo JSON")
    void exportar_crea_archivo_json() throws IOException {
        Mision mision = new Mision(10, "Inspeccion de cultivo", "Bogota", "2026-09-19");

        AdaptadorMision adaptador = new MisionJsonAdapter();

        String ruta = adaptador.exportar(mision);
        archivoCreado = Path.of(ruta);

        assertTrue(Files.exists(archivoCreado));
        assertTrue(ruta.endsWith(".json"));

        String contenido = Files.readString(archivoCreado, StandardCharsets.UTF_8);

        // Se corrigen las cadenas JSON escapando las comillas adecuadamente
        assertTrue(contenido.contains("\"id\": 10") || contenido.contains("\"id\": \"10\""));
        assertTrue(contenido.contains("\"nombre\": \"Inspeccion de cultivo\""));
        assertTrue(contenido.contains("\"ubicacion\": \"Bogota\""));
        assertTrue(contenido.contains("\"fecha\": \"2026-09-19\""));
    }

    @Test
    @DisplayName("El adaptador escapa comillas y barras invertidas")
    void exportar_escapa_caracteres_json() throws IOException {
        // Corrección en la cadena de entrada con comillas dobles
        Mision mision = new Mision(5, "Mision \"A\"", "Ruta \\ norte", "2026-09-20");

        String ruta = new MisionJsonAdapter().exportar(mision);
        archivoCreado = Path.of(ruta);

        String contenido = Files.readString(archivoCreado, StandardCharsets.UTF_8);

        // Verificación de comillas escapadas (\" -> \\\") y barras invertidas (\\ -> \\\\)
        assertTrue(contenido.contains("\"nombre\": \"Mision \\\"A\\\"\""));
        assertTrue(contenido.contains("\"ubicacion\": \"Ruta \\\\ norte\""));
    }

    @Test
    @DisplayName("Una mision nula produce una excepcion")
    void exportar_mision_nula() {
        MisionJsonAdapter adaptador = new MisionJsonAdapter();

        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> adaptador.exportar(null)
        );

        assertEquals("No hay mision para exportar.", excepcion.getMessage());
    }

    @Test
    @DisplayName("Los valores null se exportan como cadenas vacias")
    void exportar_valores_nulos() throws IOException {
        Mision mision = new Mision(8, null, null, null);

        String ruta = new MisionJsonAdapter().exportar(mision);
        archivoCreado = Path.of(ruta);

        String contenido = Files.readString(archivoCreado, StandardCharsets.UTF_8);

        assertTrue(contenido.contains("\"id\": 8") || contenido.contains("\"id\": \"8\""));
        assertTrue(contenido.contains("\"nombre\": \"\""));
        assertTrue(contenido.contains("\"ubicacion\": \"\""));
        assertTrue(contenido.contains("\"fecha\": \"\""));
    }

    @Test
    @DisplayName("MisionJsonAdapter implementa AdaptadorMision")
    void cumple_contrato() {
        AdaptadorMision adaptador = new MisionJsonAdapter();

        assertTrue(adaptador instanceof AdaptadorMision);
    }

    @Test
    @DisplayName("ArchivoJson puede crear directamente un archivo")
    void archivo_json_crea_archivo() throws IOException {
        ArchivoJson archivoJson = new ArchivoJson();
        String contenidoEsperado = "{\n  \"prueba\": true\n}\n";

        String ruta = archivoJson.crearArchivo(contenidoEsperado);
        archivoCreado = Path.of(ruta);

        assertTrue(Files.exists(archivoCreado));
        assertEquals(
                contenidoEsperado,
                Files.readString(archivoCreado, StandardCharsets.UTF_8)
        );
    }
}