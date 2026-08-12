package co.edu.poli.sw2.dao;

import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Piloto;
import co.edu.poli.sw2.modelo.Sensor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion de DroneDAO que persiste toda la "base de datos"
 * (pilotos, sensores y drones) en un archivo binario, para que la
 * informacion no se pierda al cerrar la app y pueda reutilizarse
 * desde cualquier controlador/vista.
 */
public class DroneDAOImpl implements DroneDAO {

    private static final String CARPETA_DATOS = "data";
    private static final String ARCHIVO_DB = CARPETA_DATOS + File.separator + "sw2_db.dat";

    private List<Piloto> pilotos;
    private List<Sensor> sensores;
    private List<Drone> drones;
    private int contadorDroneId;

    public DroneDAOImpl() {
        cargarDatos();
    }

    // -------------------- CRUD Drone --------------------

    @Override
    public List<Drone> listarDrones() {
        return drones;
    }

    @Override
    public Drone crear(Drone drone) {
        drone.setId(contadorDroneId++);
        drones.add(drone);
        guardarDatos();
        return drone;
    }

    @Override
    public void eliminar(Drone drone) {
        drones.remove(drone);
        guardarDatos();
    }

    @Override
    public void actualizar(Drone drone, String serial, String fabricante, double peso,
                            Piloto piloto, List<Sensor> sensoresSeleccionados) {
        drone.setSerial(serial);
        drone.setFabricante(fabricante);
        drone.setPeso(peso);
        drone.setPiloto(piloto);
        drone.setSensores(sensoresSeleccionados);
        guardarDatos();
    }

    // -------------------- Catalogos (Piloto / Sensor) --------------------

    @Override
    public List<Piloto> listarPilotos() {
        return pilotos;
    }

    @Override
    public List<Sensor> listarSensores() {
        return sensores;
    }

    // -------------------- Persistencia --------------------

    @SuppressWarnings("unchecked")
    private void cargarDatos() {
        File archivo = new File(ARCHIVO_DB);

        if (archivo.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivo))) {
                EstadoBaseDatos estado = (EstadoBaseDatos) in.readObject();
                this.pilotos = estado.pilotos;
                this.sensores = estado.sensores;
                this.drones = estado.drones;
                this.contadorDroneId = estado.contadorDroneId;
                return;
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("No se pudo leer la base de datos, se generan datos iniciales: " + e.getMessage());
            }
        }

        generarDatosIniciales();
    }

    private void generarDatosIniciales() {
        pilotos = new ArrayList<>();
        pilotos.add(new Piloto(1, "Carlos Ramirez", 10, "3001112233"));
        pilotos.add(new Piloto(2, "Laura Gomez", 5, "3002223344"));
        pilotos.add(new Piloto(3, "Andres Torres", 1, "3003334455"));

        sensores = new ArrayList<>();
        sensores.add(new Sensor(1, "Camara termica", "FLIR"));
        sensores.add(new Sensor(2, "LIDAR", "Velodyne"));
        sensores.add(new Sensor(3, "GPS RTK", "u-blox"));
        sensores.add(new Sensor(4, "Sensor multiespectral", "MicaSense"));

        drones = new ArrayList<>();
        contadorDroneId = 1;

        Drone d1 = new Drone(0, "SN-00123", "DJI", 0.9, pilotos.get(0));
        d1.getSensores().add(sensores.get(0));
        d1.getSensores().add(sensores.get(2));

        Drone d2 = new Drone(0, "SN-00456", "Autel Robotics", 1.2, pilotos.get(1));
        d2.getSensores().add(sensores.get(1));

        crear(d1); // asigna id y ya guarda en archivo
        crear(d2);
    }

    private void guardarDatos() {
        File carpeta = new File(CARPETA_DATOS);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        EstadoBaseDatos estado = new EstadoBaseDatos(pilotos, sensores, drones, contadorDroneId);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DB))) {
            out.writeObject(estado);
        } catch (IOException e) {
            System.err.println("No se pudo guardar la base de datos: " + e.getMessage());
        }
    }

    /** Snapshot serializable de toda la "base de datos". */
    private static class EstadoBaseDatos implements Serializable {
        private static final long serialVersionUID = 1L;

        private final List<Piloto> pilotos;
        private final List<Sensor> sensores;
        private final List<Drone> drones;
        private final int contadorDroneId;

        EstadoBaseDatos(List<Piloto> pilotos, List<Sensor> sensores, List<Drone> drones, int contadorDroneId) {
            this.pilotos = pilotos;
            this.sensores = sensores;
            this.drones = drones;
            this.contadorDroneId = contadorDroneId;
        }
    }
}