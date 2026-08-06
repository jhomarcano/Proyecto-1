package co.edu.poli.sw2.dao;

import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Mision;
import co.edu.poli.sw2.modelo.Piloto;
import co.edu.poli.sw2.modelo.Sensor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Piloto, Sensor y Mision no tienen CRUD en este ejercicio,
 * por eso se dejan como datos fijos (quemados) en memoria.
 * Drone si tiene CRUD, pero por simplicidad tambien se maneja
 * aqui en memoria (sin DAO) mientras el ejercicio lo permita.
 */
public class DatosQuemados {

    private static final ObservableList<Piloto> PILOTOS = FXCollections.observableArrayList(
            new Piloto(1, "Carlos Ramirez", "Avanzado", "3001112233"),
            new Piloto(2, "Laura Gomez", "Intermedio", "3002223344"),
            new Piloto(3, "Andres Torres", "Basico", "3003334455")
    );

    private static final ObservableList<Sensor> SENSORES = FXCollections.observableArrayList(
            new Sensor(1, "Camara termica", "FLIR"),
            new Sensor(2, "LIDAR", "Velodyne"),
            new Sensor(3, "GPS RTK", "u-blox"),
            new Sensor(4, "Sensor multiespectral", "MicaSense")
    );

    private static final ObservableList<Mision> MISIONES = FXCollections.observableArrayList(
            new Mision(1, "Inspeccion de linea electrica", "Antioquia", "2026-08-10"),
            new Mision(2, "Mapeo agricola", "Tolima", "2026-08-15"),
            new Mision(3, "Vigilancia forestal", "Cauca", "2026-08-20")
    );

    // ---- Drones: unica entidad con CRUD, pero sin DAO por ahora ----
    private static final ObservableList<Drone> DRONES = FXCollections.observableArrayList();
    private static int contadorDroneId = 1;

    public static ObservableList<Piloto> getPilotos() { return PILOTOS; }
    public static ObservableList<Sensor> getSensores() { return SENSORES; }
    public static ObservableList<Mision> getMisiones() { return MISIONES; }
    public static ObservableList<Drone> getDrones() { return DRONES; }

    public static Drone crearDrone(Drone drone) {
        drone.setId(contadorDroneId++);
        DRONES.add(drone);
        return drone;
    }

    public static void eliminarDrone(Drone drone) {
        DRONES.remove(drone);
    }
}