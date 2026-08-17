package co.edu.poli.sw2.dao;
 
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Piloto;
import co.edu.poli.sw2.modelo.Sensor;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
 
/**
 * DAO de Drone respaldado en PostgreSQL.
 *
 * Unica clase con CRUD completo del proyecto (regla de negocio:
 * el CRUD se hace solo sobre Dron). Piloto y Sensor se leen desde
 * la BD mediante CatalogoRepositorio, pero no se crean/editan/
 * eliminan desde aqui.
 *
 * Cada metodo publico abre su propia conexion con try-with-resources
 * (se cierra automaticamente al terminar), lo que evita fugas de
 * conexiones y problemas de conexiones compartidas entre hilos.
 */
public class DroneDAOImpl implements DroneDAO {
 
    @Override
    public List<Drone> listar() {
        List<Drone> drones = new ArrayList<>();
 
        String sql = "SELECT d.id, d.serial, d.fabricante, d.peso, "
                   + "       p.id AS piloto_id, p.nombre, p.experiencia, p.telefono "
                   + "FROM dron d "
                   + "LEFT JOIN piloto p ON d.piloto_id = p.id "
                   + "ORDER BY d.id";
 
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
 
            while (rs.next()) {
                Drone drone = mapearDrone(rs);
                drone.setSensores(obtenerSensoresDeDrone(con, drone.getId()));
                drones.add(drone);
            }
 
        } catch (SQLException e) {
            System.err.println("Error al listar drones: " + e.getMessage());
        }
 
        return drones;
    }
 
    @Override
    public Drone crear(Drone drone) {
        String sqlInsert = "INSERT INTO dron (serial, fabricante, peso, piloto_id) "
                          + "VALUES (?, ?, ?, ?) RETURNING id";
 
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sqlInsert)) {
 
            setearParametrosDron(ps, drone);
 
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    drone.setId(rs.getInt("id"));
                }
            }
 
            guardarSensoresDeDrone(con, drone);
 
        } catch (SQLException e) {
            System.err.println("Error al crear drone: " + e.getMessage());
        }
 
        return drone;
    }
 
    @Override
    public void eliminar(Drone drone) {
        String sql = "DELETE FROM dron WHERE id = ?";
 
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
 
            ps.setInt(1, drone.getId());
            ps.executeUpdate();
            // Las filas en dron_sensor se eliminan solas por el
            // ON DELETE CASCADE definido en el DDL.
 
        } catch (SQLException e) {
            System.err.println("Error al eliminar drone: " + e.getMessage());
        }
    }
 
    @Override
    public Drone actualizar(Drone drone) {
        String sqlUpdate = "UPDATE dron SET serial = ?, fabricante = ?, peso = ?, piloto_id = ? "
                          + "WHERE id = ?";
 
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sqlUpdate)) {
 
            setearParametrosDron(ps, drone);
            ps.setInt(5, drone.getId());
            ps.executeUpdate();
 
            // Estrategia simple para la relacion N:M -> borrar y
            // reinsertar las asociaciones vigentes de sensores.
            eliminarSensoresDeDrone(con, drone.getId());
            guardarSensoresDeDrone(con, drone);
 
        } catch (SQLException e) {
            System.err.println("Error al actualizar drone: " + e.getMessage());
        }
 
        return drone;
    }
 
    // -------------------- Helpers privados --------------------
 
    private void setearParametrosDron(PreparedStatement ps, Drone drone) throws SQLException {
        ps.setString(1, drone.getSerial());
        ps.setString(2, drone.getFabricante());
        ps.setDouble(3, drone.getPeso());
        if (drone.getPiloto() != null) {
            ps.setInt(4, drone.getPiloto().getId());
        } else {
            ps.setNull(4, Types.INTEGER);
        }
    }
 
    private Drone mapearDrone(ResultSet rs) throws SQLException {
        Piloto piloto = null;
        int pilotoId = rs.getInt("piloto_id");
        if (!rs.wasNull()) {
            piloto = new Piloto(
                    pilotoId,
                    rs.getString("nombre"),
                    rs.getInt("experiencia"),
                    rs.getString("telefono")
            );
        }
        return new Drone(
                rs.getInt("id"),
                rs.getString("serial"),
                rs.getString("fabricante"),
                rs.getDouble("peso"),
                piloto
        );
    }
 
    private List<Sensor> obtenerSensoresDeDrone(Connection con, int droneId) throws SQLException {
        List<Sensor> sensores = new ArrayList<>();
        String sql = "SELECT s.id, s.tipo, s.fabricante "
                   + "FROM sensor s "
                   + "JOIN dron_sensor ds ON ds.sensor_id = s.id "
                   + "WHERE ds.dron_id = ?";
 
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, droneId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sensores.add(new Sensor(rs.getInt("id"), rs.getString("tipo"), rs.getString("fabricante")));
                }
            }
        }
        return sensores;
    }
 
    private void guardarSensoresDeDrone(Connection con, Drone drone) throws SQLException {
        if (drone.getSensores().isEmpty()) {
            return;
        }
        String sql = "INSERT INTO dron_sensor (dron_id, sensor_id) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (Sensor s : drone.getSensores()) {
                ps.setInt(1, drone.getId());
                ps.setInt(2, s.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
 
    private void eliminarSensoresDeDrone(Connection con, int droneId) throws SQLException {
        String sql = "DELETE FROM dron_sensor WHERE dron_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, droneId);
            ps.executeUpdate();
        }
    }
}
 