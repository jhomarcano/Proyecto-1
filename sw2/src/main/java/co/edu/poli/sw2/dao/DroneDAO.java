package co.edu.poli.sw2.dao;

import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Piloto;
import co.edu.poli.sw2.modelo.Sensor;

import java.util.List;

/**
 * Contrato del DAO de Drone. Es la unica entidad con CRUD real.
 * Piloto y Sensor se exponen como catalogos (datos iniciales/falsos)
 * necesarios para poder crear un Drone (relaciones 1-a-1 y muchos-a-muchos).
 */
public interface DroneDAO {

    List<Drone> listarDrones();

    Drone crear(Drone drone);

    void eliminar(Drone drone);

    void actualizar(Drone drone, String serial, String fabricante, double peso,
                     Piloto piloto, List<Sensor> sensores);

    List<Piloto> listarPilotos();

    List<Sensor> listarSensores();
}