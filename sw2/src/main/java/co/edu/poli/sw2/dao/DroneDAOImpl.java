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
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion JDBC del acceso a datos para {@link Drone} y sus subclases.
 * <p>
 * Aplica el esquema de <b>herencia por tablas</b>: la tabla {@code dron}
 * guarda los atributos comunes junto con una columna discriminadora
 * {@code tipo}, mientras que {@code dron_agricultura} y
 * {@code dron_vigilancia} guardan los atributos especificos de cada
 * subclase. Las tablas hijas referencian a la padre mediante su llave
 * primaria, con {@code ON DELETE CASCADE}.
 * <p>
 * Las operaciones que tocan dos tablas se ejecutan dentro de una
 * transaccion, de modo que un fallo parcial no deje registros huerfanos.
 * Los errores de PostgreSQL se traducen a excepciones de dominio para que
 * el controlador pueda mostrar mensajes claros al usuario.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see DroneDAO
 * @see ConexionBD
 */
public class DroneDAOImpl implements DroneDAO {

    /** SQLState de PostgreSQL para violacion de restriccion UNIQUE. */
    private static final String SQLSTATE_UNIQUE_VIOLATION = "23505";

    /** SQLState de PostgreSQL para violacion de restriccion CHECK. */
    private static final String SQLSTATE_CHECK_VIOLATION = "23514";

    /** SQLState de PostgreSQL para violacion de restriccion NOT NULL. */
    private static final String SQLSTATE_NOT_NULL_VIOLATION = "23502";

    /** Consulta que une la tabla padre con ambas tablas hijas. */
    private static final String SQL_LISTAR =
            "SELECT d.id, d.serial, d.fabricante, d.modelo, d.peso, d.tipo, "
          + "       a.capacidad_tanque, v.deteccion_termica "
          + "FROM dron d "
          + "LEFT JOIN dron_agricultura a ON a.id_dron = d.id "
          + "LEFT JOIN dron_vigilancia  v ON v.id_dron = d.id "
          + "ORDER BY d.id";

    /**
     * Recupera todos los drones registrados.
     * <p>
     * Usa LEFT JOIN sobre las dos tablas hijas para traer en una sola
     * consulta tanto los atributos comunes como los especificos. Cada fila
     * se convierte en la subclase que corresponda segun la columna
     * {@code tipo}.
     *
     * @return la lista de drones ordenada por id; vacia si no hay registros
     * @throws ConexionBDException si falla la consulta a la base de datos
     */
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

    /**
     * Persiste un nuevo dron en la base de datos.
     * <p>
     * Inserta primero en la tabla {@code dron} y recupera el id generado;
     * luego inserta la fila correspondiente en la tabla hija. Ambas
     * operaciones van en una misma transaccion: si la segunda falla, se
     * revierte la primera.
     *
     * @param drone el dron a guardar; debe ser una instancia de una subclase concreta
     * @return el mismo dron con el id asignado por la base de datos
     * @throws DronDuplicadoException si ya existe un dron con ese serial
     * @throws DronValidacionException si los datos violan una restriccion de la tabla
     * @throws ConexionBDException si falla la comunicacion con la base de datos
     */
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

    /**
     * Actualiza los datos de un dron existente.
     * <p>
     * Modifica la fila de la tabla padre y la de la tabla hija dentro de
     * una misma transaccion. El tipo de dron no se puede cambiar, ya que
     * implicaria mover el registro entre tablas hijas.
     *
     * @param drone el dron con los datos modificados; debe tener un id valido
     * @return el mismo dron recibido
     * @throws DronNoEncontradoException si no existe un dron con ese id
     * @throws DronDuplicadoException si el nuevo serial ya pertenece a otro dron
     * @throws DronValidacionException si los datos violan una restriccion de la tabla
     * @throws ConexionBDException si falla la comunicacion con la base de datos
     */
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

    /**
     * Elimina un dron de la base de datos.
     * <p>
     * Solo borra la fila de la tabla padre; la restriccion
     * {@code ON DELETE CASCADE} se encarga de borrar automaticamente la
     * fila correspondiente en la tabla hija.
     *
     * @param drone el dron a eliminar; debe tener un id valido
     * @throws DronNoEncontradoException si no existe un dron con ese id
     * @throws ConexionBDException si falla la comunicacion con la base de datos
     */
    @Override
    public void eliminar(Drone drone) {
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

    /**
     * Inserta la fila especifica del dron en la tabla hija que corresponda.
     *
     * @param con   conexion activa dentro de la transaccion en curso
     * @param drone dron ya insertado en la tabla padre, con su id asignado
     * @throws SQLException si falla la insercion
     */
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

    /**
     * Actualiza la fila especifica del dron en la tabla hija que corresponda.
     *
     * @param con   conexion activa dentro de la transaccion en curso
     * @param drone dron con los datos especificos modificados
     * @throws SQLException si falla la actualizacion
     */
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

    /**
     * Construye la subclase de {@link Drone} que corresponda a una fila del resultado.
     * <p>
     * Decide que clase instanciar leyendo la columna discriminadora
     * {@code tipo}.
     *
     * @param rs cursor posicionado sobre la fila a convertir
     * @return una instancia de {@link Agricultura} o {@link Vigilancia}
     * @throws SQLException si falla la lectura de alguna columna
     * @throws ConexionBDException si la columna tipo contiene un valor no reconocido
     */
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

    /**
     * Traduce un error de PostgreSQL a la excepcion de dominio correspondiente.
     * <p>
     * Se apoya en el SQLState estandar para distinguir entre serial
     * duplicado, dato invalido y fallo general de conexion.
     *
     * @param e      excepcion original lanzada por el driver JDBC
     * @param serial serial del dron involucrado, para componer el mensaje
     * @return la excepcion de dominio que debe propagarse al controlador
     */
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

    /**
     * Revierte la transaccion en curso, si la conexion sigue abierta.
     *
     * @param con conexion a revertir; puede ser {@code null}
     */
    private void revertir(Connection con) {
        if (con != null) {
            try { con.rollback(); } catch (SQLException ignored) { }
        }
    }

    /**
     * Restaura el modo autocommit y cierra la conexion.
     *
     * @param con conexion a cerrar; puede ser {@code null}
     */
    private void cerrar(Connection con) {
        if (con != null) {
            try { con.setAutoCommit(true); con.close(); } catch (SQLException ignored) { }
        }
    }
}