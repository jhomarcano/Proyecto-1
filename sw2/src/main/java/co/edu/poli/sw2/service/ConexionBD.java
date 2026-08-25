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
 * Implementa el patron <b>Singleton</b> mediante <i>double-checked
 * locking</i>: el constructor es privado, de modo que ninguna otra clase
 * puede crear instancias con {@code new}, y {@link #getInstancia()} es el
 * unico punto de acceso. Asi toda la aplicacion comparte una misma fuente
 * de configuracion, sin repetir la lectura de credenciales.
 * <p>
 * Las credenciales se resuelven en este orden de prioridad:
 * <ol>
 *   <li>Variables de entorno del sistema operativo</li>
 *   <li>Archivo {@code .env} en la raiz del proyecto</li>
 *   <li>Archivo {@code db.properties} en el classpath</li>
 * </ol>
 * Ninguno de esos dos archivos se versiona en el repositorio, para evitar
 * exponer credenciales.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see CargadorEnv
 */
public final class ConexionBD {

    /** Unica instancia de la clase. Es volatil para que el double-checked locking sea seguro. */
    private static volatile ConexionBD instancia;

    /** URL JDBC de la base de datos. */
    private final String urlBD;

    /** Usuario con el que se autentica la aplicacion. */
    private final String usuarioBD;

    /** Contrasenia del usuario de base de datos. */
    private final String passwordBD;

    /**
     * Resuelve las credenciales y verifica que el driver este disponible.
     * <p>
     * Es privado para garantizar que la clase solo pueda instanciarse
     * desde {@link #getInstancia()}.
     *
     * @throws ConexionBDException si el driver de PostgreSQL no esta en el classpath
     */
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
     * <p>
     * La primera llamada construye la instancia; las siguientes devuelven
     * siempre la misma. El bloque sincronizado con doble verificacion
     * evita que dos hilos concurrentes creen instancias distintas, sin
     * pagar el costo de sincronizar en cada llamada.
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
     * @param props          propiedades leidas de db.properties; puede venir vacio
     * @param claveEnv       nombre de la variable de entorno y clave del archivo .env
     * @param clavePropiedad nombre de la clave en db.properties
     * @param porDefecto     valor a usar si ninguna fuente aporta un dato
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

    /**
     * Indica si un valor de configuracion es utilizable.
     *
     * @param valor cadena a evaluar
     * @return {@code true} si no es nula ni esta en blanco
     */
    private static boolean esUtil(String valor) {
        return valor != null && !valor.isBlank();
    }

    /**
     * Carga el archivo db.properties del classpath, si existe.
     * <p>
     * Es una fuente opcional: si el archivo no esta o no se puede leer,
     * se devuelve un objeto vacio y la resolucion continua con las demas
     * fuentes.
     *
     * @return las propiedades leidas, o un objeto vacio si no hubo archivo
     */
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

    /**
     * Devuelve una representacion textual segura del objeto.
     * <p>
     * Nunca expone el usuario ni la contrasenia, ni siquiera ante un
     * {@code println} accidental durante la depuracion.
     *
     * @return una cadena fija que no revela credenciales
     */
    @Override
    public String toString() {
        return "ConexionBD[configurada]";
    }
}