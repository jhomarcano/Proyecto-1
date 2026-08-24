package co.edu.poli.sw2.dao;

import co.edu.poli.sw2.modelo.Agricultura;
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Vigilancia;
import co.edu.poli.sw2.exception.ConexionBDException;
import co.edu.poli.sw2.exception.DronDuplicadoException;
import co.edu.poli.sw2.exception.DronNoEncontradoException;
import co.edu.poli.sw2.exception.DronValidacionException;
import co.edu.poli.sw2.service.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion JDBC del acceso a datos para {@link Drone} y sus subclases.
 * Usa herencia por tablas: la tabla {@code dron} guarda los atributos comunes
 * y las tablas {@code dron_agricultura} / {@code dron_vigilancia} los especificos.
 */
public class DroneDAOImpl implements DroneDAO {

    private static final String SQLSTATE_UNIQUE_VIOLATION = "23505";
    private static final String SQLSTATE_CHECK_VIOLATION = "23514";
    private static final String SQLSTATE_NOT_NULL_VIOLATION = "23502";

    private static final String SQL_LISTAR =
            "SELECT d.id, d.serial, d.fabricante, d.modelo, d.peso, d.tipo, "
          + "       a.capacidad_tanque, v.deteccion_termica "
          + "FROM dron d "
          + "LEFT JOIN dron_agricultura a ON a.id_dron = d.id "
          + "LEFT JOIN dron_vigilancia  v ON v.id_dron = d.id "
          + "ORDER BY d.id";

    @Override
    public List<Drone> listar() {
        List<Drone> drones = new ArrayList<>();

        try (Connection con = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR);
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
        String sqlBase = "INSERT INTO dron (serial, fabricante, modelo, peso, tipo) "
                       + "VALUES (?, ?, ?, ?, ?) RETURNING id";

        Connection con = null;
        try {
            con = ConexionBD.getInstancia().obtenerConexion();
            con.setAutoCommit(false);

            int idGenerado;
            try (PreparedStatement ps = con.prepareStatement(sqlBase)) {
                ps.setString(1, drone.getSerial());
                ps.setString(2, drone.getFabricante());
                ps.setString(3, drone.getModelo());
                ps.setDouble(4, drone.getPeso());
                ps.setString(5, drone.getTipo());

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new ConexionBDException("La base de datos no devolvio el id del drone creado.");
                    }
                    idGenerado = rs.getInt("id");
                }
            }

            drone.setId(idGenerado);
            insertarHija(con, drone);

            con.commit();
            return drone;

        } catch (SQLException e) {
            revertir(con);
            throw traducirExcepcion(e, drone.getSerial());
        } catch (RuntimeException e) {
            revertir(con);
            throw e;
        } finally {
            cerrar(con);
        }
    }

    @Override
    public Drone actualizar(Drone drone) {
        String sqlBase = "UPDATE dron SET serial = ?, fabricante = ?, modelo = ?, peso = ? WHERE id = ?";

        Connection con = null;
        try {
            con = ConexionBD.getInstancia().obtenerConexion();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sqlBase)) {
                ps.setString(1, drone.getSerial());
                ps.setString(2, drone.getFabricante());
                ps.setString(3, drone.getModelo());
                ps.setDouble(4, drone.getPeso());
                ps.setInt(5, drone.getId());

                if (ps.executeUpdate() == 0) {
                    throw new DronNoEncontradoException(drone.getId());
                }
            }

            actualizarHija(con, drone);

            con.commit();
            return drone;

        } catch (SQLException e) {
            revertir(con);
            throw traducirExcepcion(e, drone.getSerial());
        } catch (RuntimeException e) {
            revertir(con);
            throw e;
        } finally {
            cerrar(con);
        }
    }

    @Override
    public void eliminar(Drone drone) {
        // ON DELETE CASCADE se encarga de la fila hija.
        String sql = "DELETE FROM dron WHERE id = ?";

        try (Connection con = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, drone.getId());
            if (ps.executeUpdate() == 0) {
                throw new DronNoEncontradoException(drone.getId());
            }

        } catch (SQLException e) {
            throw new ConexionBDException("No fue posible eliminar el drone.", e);
        }
    }

    // -------------------- Helpers privados --------------------

    private void insertarHija(Connection con, Drone drone) throws SQLException {
        if (drone instanceof Agricultura) {
            String sql = "INSERT INTO dron_agricultura (id_dron, capacidad_tanque) VALUES (?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, drone.getId());
                ps.setDouble(2, ((Agricultura) drone).getCapacidadTanque());
                ps.executeUpdate();
            }
        } else if (drone instanceof Vigilancia) {
            String sql = "INSERT INTO dron_vigilancia (id_dron, deteccion_termica) VALUES (?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, drone.getId());
                ps.setBoolean(2, ((Vigilancia) drone).isDeteccionTermica());
                ps.executeUpdate();
            }
        }
    }

    private void actualizarHija(Connection con, Drone drone) throws SQLException {
        if (drone instanceof Agricultura) {
            String sql = "UPDATE dron_agricultura SET capacidad_tanque = ? WHERE id_dron = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setDouble(1, ((Agricultura) drone).getCapacidadTanque());
                ps.setInt(2, drone.getId());
                ps.executeUpdate();
            }
        } else if (drone instanceof Vigilancia) {
            String sql = "UPDATE dron_vigilancia SET deteccion_termica = ? WHERE id_dron = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setBoolean(1, ((Vigilancia) drone).isDeteccionTermica());
                ps.setInt(2, drone.getId());
                ps.executeUpdate();
            }
        }
    }

    private Drone mapearDrone(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String serial = rs.getString("serial");
        String fabricante = rs.getString("fabricante");
        String modelo = rs.getString("modelo");
        double peso = rs.getDouble("peso");
        String tipo = rs.getString("tipo");

        if ("AGRICULTURA".equals(tipo)) {
            return new Agricultura(id, serial, fabricante, modelo, peso,
                    rs.getDouble("capacidad_tanque"));
        }
        if ("VIGILANCIA".equals(tipo)) {
            return new Vigilancia(id, serial, fabricante, modelo, peso,
                    rs.getBoolean("deteccion_termica"));
        }
        throw new ConexionBDException("Tipo de drone desconocido en la base de datos: " + tipo);
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

    private void revertir(Connection con) {
        if (con != null) {
            try { con.rollback(); } catch (SQLException ignored) { }
        }
    }

    private void cerrar(Connection con) {
        if (con != null) {
            try { con.setAutoCommit(true); con.close(); } catch (SQLException ignored) { }
        }
    }
}