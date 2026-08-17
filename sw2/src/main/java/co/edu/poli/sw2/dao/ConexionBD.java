package co.edu.poli.sw2.dao;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
 
/**
 * Punto unico de conexion a PostgreSQL via JDBC.
 *
 * Ajusta URL, USUARIO y PASSWORD a tu entorno local.
 * En un proyecto real estos valores deberian venir de un
 * archivo de configuracion (application.properties, .env, etc.)
 * en lugar de estar quemados en el codigo; se dejan aqui como
 * constantes por simplicidad academica.
 */
public class ConexionBD {
 
    private static final String URL      = "jdbc:postgresql://localhost:5432/dronesdb";
    private static final String USUARIO  = "postgres";
    private static final String PASSWORD = "123";
 
    private ConexionBD() {
        // Clase de utilidades: no se instancia
    }
 
    /**
     * Abre (o reutiliza) una conexion a la base de datos.
     * Cada DAO debe usar esta conexion dentro de un try-with-resources
     * y cerrarla al terminar su operacion (ver DroneDAOImpl).
     */
    public static Connection obtenerConexion() throws SQLException {
        try {
            // Desde el driver 4.0 de PostgreSQL el registro es automatico
            // (JDBC 4 + Service Provider), pero se deja explicito por
            // claridad y compatibilidad con versiones anteriores.
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontro el driver de PostgreSQL en el classpath.", e);
        }
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}
 