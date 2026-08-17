package co.edu.poli.sw2.dao;
 
import co.edu.poli.sw2.modelo.Piloto;
import co.edu.poli.sw2.modelo.Sensor;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
 
/**
 * Acceso de SOLO LECTURA a los catalogos de Piloto y Sensor.
 *
 * Se mantiene deliberadamente fuera del patron GenericDAO/DroneDAO:
 * la regla de negocio del proyecto establece que el CRUD (crear,
 * modificar, eliminar) solo aplica a la entidad Dron. Piloto y
 * Sensor se administran directamente en la base de datos (por
 * ejemplo, con INSERT manuales o el script schema.sql) y la
 * aplicacion unicamente los consulta para poblar el ComboBox y
 * el ListView del formulario.
 *
 * Reemplaza a los metodos crearPilotos()/crearSensores() que
 * antes estaban "quemados" en DroneController.
 */
public class CatalogoRepositorio {
 
    private CatalogoRepositorio() {
        // Clase de utilidades: no se instancia
    }
 
    public static List<Piloto> listarPilotos() {
        List<Piloto> pilotos = new ArrayList<>();
        String sql = "SELECT id, nombre, experiencia, telefono FROM piloto ORDER BY id";
 
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
 
            while (rs.next()) {
                pilotos.add(new Piloto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("experiencia"),
                        rs.getString("telefono")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pilotos desde la base de datos: " + e.getMessage());
        }
        return pilotos;
    }
 
    public static List<Sensor> listarSensores() {
        List<Sensor> sensores = new ArrayList<>();
        String sql = "SELECT id, tipo, fabricante FROM sensor ORDER BY id";
 
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
 
            while (rs.next()) {
                sensores.add(new Sensor(
                        rs.getInt("id"),
                        rs.getString("tipo"),
                        rs.getString("fabricante")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar sensores desde la base de datos: " + e.getMessage());
        }
        return sensores;
    }
}
 