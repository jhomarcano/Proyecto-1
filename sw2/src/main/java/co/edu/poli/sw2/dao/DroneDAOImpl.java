package co.edu.poli.sw2.dao;
 
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.exception.ConexionBDException;
import co.edu.poli.sw2.exception.DronDuplicadoException;
import co.edu.poli.sw2.exception.DronNoEncontradoException;
import co.edu.poli.sw2.exception.DronValidacionException;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
 
public class DroneDAOImpl implements DroneDAO {
	private static final String SQLSTATE_UNIQUE_VIOLATION = "23505";
	private static final String SQLSTATE_CHECK_VIOLATION = "23514";
	private static final String SQLSTATE_NOT_NULL_VIOLATION = "23502";
 
    @Override
    public List<Drone> listar() {
        List<Drone> drones = new ArrayList<>();
 
        String sql = "SELECT id, serial, fabricante, peso FROM dron ORDER BY id";
 
        try (Connection con = ConexionBD.getInstancia().obtenerConexion();
        	     PreparedStatement ps = con.prepareStatement(sql);
        	     ResultSet rs = ps.executeQuery()) {
 
            while (rs.next()) {
            	drones.add(mapearDrone(rs));
            }
            return drones;
 
        } catch (SQLException e) {
        	throw new ConexionBDException("No fue posible obtener el listado de drones.", e);
        }
    }
 
    @Override
    public Drone crear(Drone drone) {
    	String sqlInsert = "INSERT INTO dron (serial, fabricante, peso) VALUES (?, ?, ?) RETURNING id";
 
    	try (Connection con = ConexionBD.getInstancia().obtenerConexion();
    		     PreparedStatement ps = con.prepareStatement(sqlInsert)) {
 
            setearParametrosDron(ps, drone);
 
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    drone.setId(rs.getInt("id"));
                }
            }
 
            return drone;
 
        } catch (SQLException e) {
        	throw traducirExcepcion(e, drone.getSerial());
        }
 
    }
 
    @Override
    public void eliminar(Drone drone) {
        String sql = "DELETE FROM dron WHERE id = ?";
 
        try (Connection con = ConexionBD.getInstancia().obtenerConexion();
        	     PreparedStatement ps = con.prepareStatement(sql);
        	     ResultSet rs = ps.executeQuery()) {
 
            ps.setInt(1, drone.getId());
            int filas = ps.executeUpdate();
            if (filas == 0) {
            throw new DronNoEncontradoException(drone.getId());
            }
 
        } catch (SQLException e) {
        	throw new ConexionBDException("No fue posible eliminar el drone.", e);
        }
    }
 
    @Override
    public Drone actualizar(Drone drone) {
    	String sqlUpdate = "UPDATE dron SET serial = ?, fabricante = ?, peso = ? WHERE id = ?";
 
    	try (Connection con = ConexionBD.getInstancia().obtenerConexion();
    		     PreparedStatement ps = con.prepareStatement(sqlUpdate);
    		     ResultSet rs = ps.executeQuery()) {
 
            setearParametrosDron(ps, drone);
            ps.setInt(4, drone.getId());
            int filas = ps.executeUpdate();
            if (filas == 0) {
            throw new DronNoEncontradoException(drone.getId());
            }
            return drone;
 
        } catch (SQLException e) {
        	throw traducirExcepcion(e, drone.getSerial());
        }
 
    }
 
    // -------------------- Helpers privados --------------------
 
    private void setearParametrosDron(PreparedStatement ps, Drone drone) throws SQLException {
        ps.setString(1, drone.getSerial());
        ps.setString(2, drone.getFabricante());
        ps.setDouble(3, drone.getPeso());
    }
 
    private Drone mapearDrone(ResultSet rs) throws SQLException {
    	return new Drone(
    			rs.getInt("id"),
                rs.getString("serial"),
                rs.getString("fabricante"),
                rs.getDouble("peso")
          );
      }
    
    private RuntimeException traducirExcepcion(SQLException e, String serial) {
    	String estado = e.getSQLState();
    	if (SQLSTATE_UNIQUE_VIOLATION.equals(estado)) {
    	return new DronDuplicadoException(serial);
    	}
    	if (SQLSTATE_CHECK_VIOLATION.equals(estado) || SQLSTATE_NOT_NULL_VIOLATION.equals(estado)) {
    	return new DronValidacionException(
    	"Los datos del drone no cumplen las reglas de validacion de la base de datos.");
    	}
    	return new ConexionBDException("No fue posible guardar el drone.", e);
   
    
    }
}
 