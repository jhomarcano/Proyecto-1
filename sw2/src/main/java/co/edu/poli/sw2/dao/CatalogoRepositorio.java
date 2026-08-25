package co.edu.poli.sw2.dao;

import co.edu.poli.sw2.exception.ConexionBDException;
import co.edu.poli.sw2.modelo.Piloto;
import co.edu.poli.sw2.modelo.Sensor;
import co.edu.poli.sw2.service.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio de solo lectura para los catalogos de pilotos y sensores.
 * <p>
 * A diferencia de {@link DroneDAOImpl}, estas entidades no requieren CRUD
 * completo en el alcance actual: solo se consultan para poblar controles
 * de seleccion. Por eso se expone como clase de utilidad con metodos
 * estaticos y constructor privado.
 *
 * @author Alejandra Cano y Juan Rosero
 */
public class CatalogoRepositorio {

    /** Constructor privado: la clase solo expone metodos estaticos. */
    private CatalogoRepositorio() {
    }

    /**
     * Recupera todos los pilotos registrados.
     *
     * @return la lista de pilotos ordenada por id; vacia si no hay registros
     * @throws ConexionBDException si falla la consulta a la base de datos
     */
    public static List<Piloto> listarPilotos() {
        List<Piloto> pilotos = new ArrayList<>();
        String sql = "SELECT id, nombre, experiencia, telefono FROM piloto ORDER BY id";

        try (Connection con = ConexionBD.getInstancia().obtenerConexion();
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
            throw new ConexionBDException("No fue posible listar los pilotos.", e);
        }
        return pilotos;
    }

    /**
     * Recupera todos los sensores registrados.
     *
     * @return la lista de sensores ordenada por id; vacia si no hay registros
     * @throws ConexionBDException si falla la consulta a la base de datos
     */
    public static List<Sensor> listarSensores() {
        List<Sensor> sensores = new ArrayList<>();
        String sql = "SELECT id, tipo, fabricante FROM sensor ORDER BY id";

        try (Connection con = ConexionBD.getInstancia().obtenerConexion();
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
            throw new ConexionBDException("No fue posible listar los sensores.", e);
        }
        return sensores;
    }
}