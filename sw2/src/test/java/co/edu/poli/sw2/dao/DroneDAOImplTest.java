package co.edu.poli.sw2.dao;

import co.edu.poli.sw2.exception.DronDuplicadoException;
import co.edu.poli.sw2.exception.DronNoEncontradoException;
import co.edu.poli.sw2.modelo.Agricultura;
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Vigilancia;
import co.edu.poli.sw2.service.ConexionBD;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integracion de {@link DroneDAOImpl} contra PostgreSQL.
 * <p>
 * A diferencia de las pruebas unitarias, estas requieren que el servidor
 * de base de datos este corriendo y que las credenciales esten
 * configuradas en el archivo {@code .env}.
 * <p>
 * Cada prueba crea sus propios registros con un serial unico generado con
 * UUID, de modo que no choquen entre corridas, y los elimina al terminar
 * mediante {@link #limpiarDatosDePrueba()}. Asi la base de datos queda
 * igual que antes de ejecutar la suite.
 *
 * @author Alejandra Cano y Juan Rosero
 */
class DroneDAOImplTest {

    /** Implementacion bajo prueba. */
    private final DroneDAO dao = new DroneDAOImpl();

    /** Registros creados durante la prueba, para eliminarlos al final. */
    private final List<Drone> creados = new ArrayList<>();

    /**
     * Genera un serial que no puede chocar con datos existentes.
     * <p>
     * Sin esto, la segunda ejecucion de la suite fallaria por violacion
     * de la restriccion UNIQUE del serial.
     *
     * @return un serial unico con prefijo TEST
     */
    private String serialUnico() {
        return "TEST-" + UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Guarda un dron y lo registra para su posterior limpieza.
     *
     * @param drone dron a persistir
     * @return el dron guardado, con id asignado
     */
    private Drone crearYRegistrar(Drone drone) {
        Drone guardado = dao.crear(drone);
        creados.add(guardado);
        return guardado;
    }

    /**
     * Elimina los registros creados por la prueba que acaba de ejecutarse.
     * <p>
     * Se ejecuta despues de cada test, tanto si paso como si fallo, para
     * no dejar datos basura acumulados en la base de datos.
     */
    @AfterEach
    void limpiarDatosDePrueba() {
        for (Drone d : creados) {
            try {
                dao.eliminar(d);
            } catch (RuntimeException ignorada) {
                // El test pudo haberlo borrado ya; no es un fallo.
            }
        }
        creados.clear();
    }

    /**
     * Verifica que al crear un dron de agricultura la base de datos le asigne un id.
     */
    @Test
    @DisplayName("Crear un dron de agricultura le asigna un id generado por la BD")
    void crearAgricultura_asignaId() {
        Agricultura nuevo = new Agricultura(0, serialUnico(), "DJI", "Agras T40", 38.0, 40.0);

        Drone guardado = crearYRegistrar(nuevo);

        assertTrue(guardado.getId() > 0, "La BD debio asignar un id positivo");
        assertEquals("AGRICULTURA", guardado.getTipo());
    }

    /**
     * Verifica que el atributo especifico de un dron de vigilancia se persista
     * y se recupere correctamente desde la tabla hija.
     */
    @Test
    @DisplayName("Crear un dron de vigilancia persiste su deteccion termica")
    void crearVigilancia_persisteDeteccionTermica() {
        Vigilancia nuevo = new Vigilancia(0, serialUnico(), "Autel", "EVO II", 1.2, true);

        Drone guardado = crearYRegistrar(nuevo);
        Drone leido = buscarEnListado(guardado.getId());

        assertNotNull(leido, "El drone creado deberia aparecer en el listado");
        assertTrue(leido instanceof Vigilancia, "Debe recuperarse como instancia de Vigilancia");
        assertTrue(((Vigilancia) leido).isDeteccionTermica());
    }

    /**
     * Verifica que la restriccion UNIQUE del serial se traduzca a la
     * excepcion de dominio correspondiente.
     */
    @Test
    @DisplayName("Crear dos drones con el mismo serial lanza DronDuplicadoException")
    void crearSerialDuplicado_lanzaExcepcion() {
        String serial = serialUnico();
        crearYRegistrar(new Agricultura(0, serial, "DJI", "Agras", 20.0, 10.0));

        Agricultura repetido = new Agricultura(0, serial, "DJI", "Agras", 20.0, 10.0);

        assertThrows(DronDuplicadoException.class, () -> dao.crear(repetido));
    }

    /**
     * Verifica que el listado reconstruya cada dron como la subclase que
     * indica su columna discriminadora.
     */
    @Test
    @DisplayName("Listar recupera cada dron como la subclase que le corresponde")
    void listar_devuelveSubclasesCorrectas() {
        Drone agro = crearYRegistrar(new Agricultura(0, serialUnico(), "DJI", "Agras", 30.0, 25.0));
        Drone vigi = crearYRegistrar(new Vigilancia(0, serialUnico(), "Parrot", "Anafi", 0.5, false));

        assertTrue(buscarEnListado(agro.getId()) instanceof Agricultura);
        assertTrue(buscarEnListado(vigi.getId()) instanceof Vigilancia);
    }

    /**
     * Verifica que la actualizacion modifique tanto la tabla padre como la hija.
     */
    @Test
    @DisplayName("Actualizar modifica tanto los campos comunes como los especificos")
    void actualizar_modificaCamposBaseYEspecificos() {
        Drone guardado = crearYRegistrar(
                new Agricultura(0, serialUnico(), "DJI", "Agras T10", 13.0, 8.0));

        guardado.setFabricante("XAG");
        guardado.setModelo("P100");
        guardado.setPeso(25.0);
        ((Agricultura) guardado).setCapacidadTanque(50.0);

        dao.actualizar(guardado);

        Drone leido = buscarEnListado(guardado.getId());
        assertNotNull(leido);
        assertEquals("XAG", leido.getFabricante());
        assertEquals("P100", leido.getModelo());
        assertEquals(25.0, leido.getPeso(), 0.0001);
        assertEquals(50.0, ((Agricultura) leido).getCapacidadTanque(), 0.0001);
    }

    /**
     * Verifica que la restriccion ON DELETE CASCADE funcione: al borrar el
     * registro padre, la fila de la tabla hija debe desaparecer sola.
     *
     * @throws SQLException si falla la consulta de verificacion
     */
    @Test
    @DisplayName("Eliminar un dron borra en cascada su fila en la tabla hija")
    void eliminar_borraTambienLaFilaHija() throws SQLException {
        Drone guardado = dao.crear(
                new Agricultura(0, serialUnico(), "DJI", "Agras", 30.0, 20.0));
        int id = guardado.getId();

        assertEquals(1, contarFilasHijas("dron_agricultura", id),
                "Antes de eliminar debe existir la fila hija");

        dao.eliminar(guardado);

        assertNull(buscarEnListado(id), "El drone no debe seguir en el listado");
        assertEquals(0, contarFilasHijas("dron_agricultura", id),
                "ON DELETE CASCADE debio borrar la fila hija");
    }

    /**
     * Verifica que intentar eliminar un registro inexistente produzca la
     * excepcion de dominio correspondiente.
     */
    @Test
    @DisplayName("Eliminar un dron inexistente lanza DronNoEncontradoException")
    void eliminarInexistente_lanzaExcepcion() {
        Agricultura fantasma = new Agricultura(-999, "NO-EXISTE", "X", "Y", 1.0, 1.0);

        assertThrows(DronNoEncontradoException.class, () -> dao.eliminar(fantasma));
    }

    /**
     * Busca un dron por id dentro del listado que devuelve el DAO.
     *
     * @param id identificador a buscar
     * @return el dron encontrado, o {@code null} si no esta en el listado
     */
    private Drone buscarEnListado(int id) {
        return dao.listar().stream()
                .filter(d -> d.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Consulta directa a la base de datos para verificar el estado de una tabla hija.
     * <p>
     * Se hace por fuera del DAO a proposito, para comprobar el efecto real
     * de la restriccion de integridad y no solo lo que reporta el DAO.
     *
     * @param tabla  nombre de la tabla hija a consultar
     * @param idDron identificador del dron padre
     * @return la cantidad de filas encontradas
     * @throws SQLException si falla la consulta
     */
    private int contarFilasHijas(String tabla, int idDron) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tabla + " WHERE id_dron = ?";
        try (Connection con = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDron);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }
}
