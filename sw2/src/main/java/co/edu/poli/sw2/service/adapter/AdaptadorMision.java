package co.edu.poli.sw2.service.adapter;

import co.edu.poli.sw2.modelo.Mision;

/**
 * Target del patron <b>Adapter</b>.
 * <p>
 * Define el contrato que espera la aplicacion para exportar una
 * {@link Mision}, sin importar el formato final en el que quede
 * representada. La aplicacion programa contra esta interfaz, nunca
 * contra el detalle de como se escribe el archivo.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see MisionJsonAdapter
 */
public interface AdaptadorMision {

    /**
     * Exporta una mision a un archivo.
     *
     * @param mision mision a exportar; no deberia ser {@code null}
     * @return la ruta del archivo generado
     */
    String exportar(Mision mision);
}