package co.edu.poli.sw2.service.proxy;

import co.edu.poli.sw2.dao.DroneDAO;
import co.edu.poli.sw2.dao.DroneDAOImpl;
import co.edu.poli.sw2.exception.DronNoEncontradoException;
import co.edu.poli.sw2.modelo.Drone;

/**
 * RealSubject del patron <b>Proxy</b>.
 * <p>
 * Es quien realmente borra el registro en la base de datos. No sabe nada
 * de contrasenas ni de permisos: su unica responsabilidad es ejecutar la
 * eliminacion. Toda la proteccion vive en {@link DronProxy}.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see EliminarDron
 * @see DronProxy
 */
public class EliminadorDron implements EliminarDron {

    /** Acceso a datos usado para localizar y borrar el dron. */
    private final DroneDAO droneDAO;

    /**
     * Crea el eliminador con su propia instancia de DAO.
     */
    public EliminadorDron() {
        this(new DroneDAOImpl());
    }

    /**
     * Crea el eliminador reutilizando un DAO ya existente.
     *
     * @param droneDAO acceso a datos a utilizar
     */
    public EliminadorDron(DroneDAO droneDAO) {
        this.droneDAO = droneDAO;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Localiza el dron por su id dentro del listado del DAO y delega el
     * borrado en {@code DroneDAOImpl}.
     *
     * @throws DronNoEncontradoException si no existe un dron con ese id
     */
    @Override
    public void eliminarDron(int id) {
        Drone objetivo = droneDAO.listar().stream()
                .filter(d -> d.getId() == id)
                .findFirst()
                .orElseThrow(() -> new DronNoEncontradoException(id));

        droneDAO.eliminar(objetivo);
    }
}