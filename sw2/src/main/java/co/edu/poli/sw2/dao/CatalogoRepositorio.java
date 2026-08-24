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

public class CatalogoRepositorio {
 
    private CatalogoRepositorio() {

    }
 
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
 