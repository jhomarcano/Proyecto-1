package co.edu.poli.sw2.service.adapter;

import co.edu.poli.sw2.modelo.Mision;

/**
 * Adapter concreto del patron <b>Adapter</b>.
 * <p>
 * Traduce una {@link Mision} al formato JSON y delega en
 * {@link ArchivoJson} la creacion del archivo en disco. Es el puente
 * entre lo que la aplicacion necesita ({@link AdaptadorMision}) y lo que
 * {@link ArchivoJson} sabe hacer (escribir texto plano a un archivo).
 * <p>
 * No se usa ninguna libreria externa de JSON: como {@link Mision} solo
 * tiene atributos simples, la serializacion se arma manualmente para no
 * agregar dependencias nuevas al modulo.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see AdaptadorMision
 * @see ArchivoJson
 */
public class MisionJsonAdapter implements AdaptadorMision {

    /** Adaptee: sabe crear archivos, pero no conoce el dominio de misiones. */
    private final ArchivoJson archivoJson;

    /**
     * Crea el adaptador con su propia instancia de {@link ArchivoJson}.
     */
    public MisionJsonAdapter() {
        this.archivoJson = new ArchivoJson();
    }

    /**
     * Crea el adaptador reutilizando un {@link ArchivoJson} ya existente.
     * <p>
     * Util para pruebas o para compartir configuracion de escritura.
     *
     * @param archivoJson adaptee a utilizar
     */
    public MisionJsonAdapter(ArchivoJson archivoJson) {
        this.archivoJson = archivoJson;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException si no se recibio una mision
     */
    @Override
    public String exportar(Mision mision) {
        if (mision == null) {
            throw new IllegalArgumentException("No hay mision para exportar.");
        }
        String json = construirJson(mision);
        return archivoJson.crearArchivo(json);
    }

    /**
     * Convierte los atributos de la mision a texto JSON.
     *
     * @param mision mision a serializar
     * @return el contenido JSON listo para escribirse en archivo
     */
    private String construirJson(Mision mision) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"id\": ").append(mision.getId()).append(",\n");
        json.append("  \"nombre\": \"").append(escapar(mision.getNombre())).append("\",\n");
        json.append("  \"ubicacion\": \"").append(escapar(mision.getUbicacion())).append("\",\n");
        json.append("  \"fecha\": \"").append(escapar(mision.getFecha())).append("\"\n");
        json.append("}\n");
        return json.toString();
    }

    /**
     * Escapa comillas y barras invertidas para que el JSON sea valido.
     *
     * @param valor texto original; puede ser {@code null}
     * @return el texto listo para insertarse dentro de comillas JSON
     */
    private String escapar(String valor) {
        return valor == null ? "" : valor.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}