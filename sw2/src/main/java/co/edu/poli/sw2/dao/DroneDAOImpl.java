package co.edu.poli.sw2.dao;

import co.edu.poli.sw2.modelo.Drone;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de Drone: unica responsabilidad es persistir y administrar
 * la lista de drones. No conoce nada de Piloto ni Sensor;
 * esos catalogos ahora viven en el controlador.
 */
public class DroneDAOImpl implements DroneDAO {

    private static final String CARPETA_DATOS = "data";
    private static final String ARCHIVO_DB = CARPETA_DATOS + File.separator + "sw2_drones.dat";

    private List<Drone> drones;
    private int contadorDroneId;

    public DroneDAOImpl() {
        cargarDatos();
    }

    @Override
    public List<Drone> listar() {
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
    public Drone actualizar(Drone drone) {
        // El drone ya fue modificado por el controlador (setters);
        // aqui solo se persiste el cambio.
        guardarDatos();
        return drone;
    }

    // -------------------- Persistencia --------------------

    @SuppressWarnings("unchecked")
    private void cargarDatos() {
        File archivo = new File(ARCHIVO_DB);

        if (archivo.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivo))) {
                EstadoDrones estado = (EstadoDrones) in.readObject();
                this.drones = estado.drones;
                this.contadorDroneId = estado.contadorDroneId;
                return;
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("No se pudo leer la base de datos de drones, se inicia vacia: " + e.getMessage());
            }
        }

        drones = new ArrayList<>();
        contadorDroneId = 1;
    }

    private void guardarDatos() {
        File carpeta = new File(CARPETA_DATOS);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        EstadoDrones estado = new EstadoDrones(drones, contadorDroneId);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DB))) {
            out.writeObject(estado);
        } catch (IOException e) {
            System.err.println("No se pudo guardar la base de datos de drones: " + e.getMessage());
        }
    }

    /** Snapshot serializable, ahora solo de Drones. */
    private static class EstadoDrones implements Serializable {
        private static final long serialVersionUID = 1L;

        private final List<Drone> drones;
        private final int contadorDroneId;

        EstadoDrones(List<Drone> drones, int contadorDroneId) {
            this.drones = drones;
            this.contadorDroneId = contadorDroneId;
        }
    }
}