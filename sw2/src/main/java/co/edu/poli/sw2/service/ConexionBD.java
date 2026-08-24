package co.edu.poli.sw2.service;

import co.edu.poli.sw2.exception.ConexionBDException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestor unico de conexiones JDBC a PostgreSQL.
 * <p>
 * Implementa el patron <b>Singleton</b> mediante <i>double-checked locking</i>:
 * solo existe una instancia en toda la aplicacion, responsable de resolver
 * las credenciales y de entregar conexiones a la capa DAO.
 * <p>
 * Las credenciales se resuelven en este orden de prioridad:
 * <ol>
 *   <li>Variables de entorno del sistema operativo</li>
 *   <li>Archivo {@code .env} en la raiz del proyecto</li>
 *   <li>Archivo {@code db.properties} en el classpath (compatibilidad)</li>
 * </ol>
 * Ninguno de los dos archivos se versiona en el repositorio.
 */
public final class ConexionBD {

    private static volatile ConexionBD instancia;

    private final String urlBD;
    private final String usuarioBD;
    private final String passwordBD;

    private ConexionBD() {
        Properties props = cargarPropiedadesSilenciosamente();
        this.urlBD = resolver(props, "DB_URL", "db.url",
                "jdbc:postgresql://localhost:5432/dronesdb");
        this.usuarioBD = resolver(props, "DB_USER", "db.user", null);
        this.passwordBD = resolver(props, "DB_PASSWORD", "db.password", null);

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ConexionBDException("No se encontro el driver de PostgreSQL en el classpath.");
        }
    }

    /**
     * Punto de acceso global a la unica instancia de la clase.
     *
     * @return la instancia compartida de {@code ConexionBD}
     */
    public static ConexionBD getInstancia() {
        ConexionBD resultado = instancia;
        if (resultado == null) {
            synchronized (ConexionBD.class) {
                resultado = instancia;
                if (resultado == null) {
                    instancia = resultado = new ConexionBD();
                }
            }
        }
        return resultado;
    }

    /**
     * Abre una nueva conexion con la base de datos.
     *
     * @return una conexion JDBC lista para usar; quien la obtiene es
     *         responsable de cerrarla
     * @throws ConexionBDException si faltan credenciales o el servidor no responde
     */
    public Connection obtenerConexion() {
        if (usuarioBD == null || passwordBD == null) {
            throw new ConexionBDException(
                    "Faltan credenciales de base de datos. Defina DB_USER y DB_PASSWORD "
                            + "en el archivo .env de la raiz del proyecto, o como variables de entorno.");
        }
        try {
            return DriverManager.getConnection(urlBD, usuarioBD, passwordBD);
        } catch (SQLException e) {
            throw new ConexionBDException(
                    "No fue posible conectar con la base de datos. Intente nuevamente mas tarde.");
        }
    }

    /**
     * Resuelve un valor de configuracion recorriendo las fuentes por prioridad.
     *
     * @param props      propiedades leidas de db.properties (puede venir vacio)
     * @param claveEnv   nombre de la variable de entorno / clave del .env
     * @param clavePropiedad nombre de la clave en db.properties
     * @param porDefecto valor a usar si ninguna fuente aporta un dato
     * @return el primer valor no vacio encontrado, o {@code porDefecto}
     */
    private static String resolver(Properties props, String claveEnv,
                                   String clavePropiedad, String porDefecto) {
        String valor = System.getenv(claveEnv);
        if (esUtil(valor)) return valor;

        valor = CargadorEnv.obtener(claveEnv);
        if (esUtil(valor)) return valor;

        valor = props.getProperty(clavePropiedad);
        if (esUtil(valor)) return valor;

        return porDefecto;
    }

    private static boolean esUtil(String valor) {
        return valor != null && !valor.isBlank();
    }

    private static Properties cargarPropiedadesSilenciosamente() {
        Properties props = new Properties();
        try (InputStream in = ConexionBD.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            // Fuente opcional: si falla se continua con las demas.
        }
        return props;
    }

    /** Nunca expone usuario ni password, ni siquiera por un println accidental. */
    @Override
    public String toString() {
        return "ConexionBD[configurada]";
    }
}