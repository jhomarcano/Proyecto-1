package co.edu.poli.sw2.service;

import co.edu.poli.sw2.dao.DroneDAO;
import co.edu.poli.sw2.dao.DroneDAOImpl;
import co.edu.poli.sw2.exception.DronValidacionException;
import co.edu.poli.sw2.modelo.Drone;
import java.util.List;

/** Logica de negocio: valida ANTES de tocar la BD y delega en el DAO. */
public class DroneService {

    private final DroneDAO droneDAO = new DroneDAOImpl();

    public List<Drone> listar() {
        return droneDAO.listar();
    }
    public Drone crear(String serial, String fabricante, String pesoTexto) {
        ValidadorDrone.validarSerial(serial);
        ValidadorDrone.validarFabricante(fabricante);
        double peso = ValidadorDrone.parsearPeso(pesoTexto);
        return droneDAO.crear(new Drone(0, serial.trim(), fabricante.trim(), peso));
    }

    public Drone actualizar(Drone existente, String serial, String fabricante, String pesoTexto) {
        if (existente == null) {
            throw new DronValidacionException("No se puede actualizar un drone sin seleccion.");
        }
        ValidadorDrone.validarSerial(serial);
        ValidadorDrone.validarFabricante(fabricante);
        double peso = ValidadorDrone.parsearPeso(pesoTexto);
        existente.setSerial(serial.trim());
        existente.setFabricante(fabricante.trim());
        existente.setPeso(peso);
        return droneDAO.actualizar(existente);
    }

    public void eliminar(Drone drone) {
        droneDAO.eliminar(drone);
    }
}