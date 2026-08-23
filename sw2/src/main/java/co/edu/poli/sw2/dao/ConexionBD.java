package co.edu.poli.sw2.dao;

import co.edu.poli.sw2.exception.ConexionBDException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConexionBD {

    private static volatile ConexionBD instancia;

    private final String urlBD;
    private final String usuarioBD;
    private final String passwordBD;

    // Constructor privado: nadie fuera de esta clase puede instanciarla
    private ConexionBD() {
        Properties props = cargarPropiedadesSilenciosamente();
        this.urlBD = obtener(props, "DB_URL", "db.url",
                "jdbc:postgresql://localhost:5432/dronesdb");
        this.usuarioBD = obtener(props, "DB_USER", "db.user", null);
        this.passwordBD = obtener(props, "DB_PASSWORD", "db.password", null);

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ConexionBDException("No se encontro el driver de PostgreSQL en el classpath.");
        }
    }

    /** Punto de acceso global a la unica instancia de ConexionBD. */
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

    /** Ahora es un metodo de instancia, no estatico: se obtiene via getInstancia(). */
    public Connection obtenerConexion() {
        if (usuarioBD == null || passwordBD == null) {
            throw new ConexionBDException(
                    "Faltan credenciales de base de datos. Configure DB_USER y DB_PASSWORD "
                            + "como variables de entorno, o cree db.properties (no versionado).");
        }
        try {
            return DriverManager.getConnection(urlBD, usuarioBD, passwordBD);
        } catch (SQLException e) {
            throw new ConexionBDException(
                    "No fue posible conectar con la base de datos. Intente nuevamente mas tarde.");
        }
    }

    private static Properties cargarPropiedadesSilenciosamente() {
        Properties props = new Properties();
        try (InputStream in = ConexionBD.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            // se ignora: si falla, se intenta seguir solo con variables de entorno
        }
        return props;
    }

    private static String obtener(Properties props, String envKey, String propKey, String porDefecto) {
        String valor = System.getenv(envKey);
        if (valor != null && !valor.isBlank()) return valor;
        valor = props.getProperty(propKey);
        if (valor != null && !valor.isBlank()) return valor;
        return porDefecto;
    }

    /** Nunca expone usuario/password, ni por un println accidental. */
    @Override
    public String toString() {
        return "ConexionBD[configurada]";
    }
}