package co.edu.poli.sw2.dao;

import co.edu.poli.sw2.modelo.Drone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * DAO de Drone. Concentra el acceso a los datos (en este caso en memoria,
 * pero de aqui se podria cambiar facilmente a JDBC/JPA sin tocar el
 * controlador ni la vista).
 */
public class DroneDAO {

    private final ObservableList<Drone> drones = FXCollections.observableArrayList();
    private int contadorId = 1;

    public ObservableList<Drone> listar() {
        return drones;
    }

    public Drone crear(Drone drone) {
        drone.setId(contadorId++);
        drones.add(drone);
        return drone;
    }

    public void actualizar(Drone drone) {
        // Como Drone usa propiedades JavaFX, los cambios ya quedan reflejados
        // en el objeto de la lista; este metodo queda para dejar explicita
        // la operacion de actualizar dentro del DAO.
        int index = drones.indexOf(drone);
        if (index >= 0) {
            drones.set(index, drone);
        }
    }

    public void eliminar(Drone drone) {
        drones.remove(drone);
    }

    public int siguienteId() {
        return contadorId;
    }
}
