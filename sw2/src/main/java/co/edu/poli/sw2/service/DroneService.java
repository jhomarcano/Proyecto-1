package co.edu.poli.sw2.service;

import co.edu.poli.sw2.dao.DroneDAO;
import co.edu.poli.sw2.dao.DroneDAOImpl;
import co.edu.poli.sw2.exception.DronValidacionException;
import co.edu.poli.sw2.modelo.Agricultura;
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Vigilancia;
import co.edu.poli.sw2.service.AgriculturaFactory;
import co.edu.poli.sw2.service.DroneFactory;
import co.edu.poli.sw2.service.VigilanciaFactory;

import java.util.List;

/**
 * Logica de negocio: valida los datos, delega la construccion del objeto
 * en la factoria correspondiente y persiste a traves del DAO.
 */
public class DroneService {

    private final DroneDAO droneDAO = new DroneDAOImpl();

    public List<Drone> listar() {
        return droneDAO.listar();
    }

    /**
     * @param tipo "AGRICULTURA" o "VIGILANCIA"
     * @param capacidadTexto capacidad del tanque (solo aplica a Agricultura)
     * @param deteccionTermica indicador termico (solo aplica a Vigilancia)
     */
    public Drone crear(String tipo, String serial, String fabricante, String modelo,
                       String pesoTexto, String capacidadTexto, boolean deteccionTermica) {

        ValidadorDrone.validarSerial(serial);
        ValidadorDrone.validarFabricante(fabricante);
        double peso = ValidadorDrone.parsearPeso(pesoTexto);

        DroneFactory factory = construirFactory(
                tipo, serial.trim(), fabricante.trim(),
                modelo == null ? null : modelo.trim(),
                peso, capacidadTexto, deteccionTermica);

        return droneDAO.crear(factory.crearDrone());
    }

    public Drone actualizar(Drone existente, String serial, String fabricante, String modelo,
                            String pesoTexto, String capacidadTexto, boolean deteccionTermica) {

        if (existente == null) {
            throw new DronValidacionException("No se puede actualizar un drone sin seleccion.");
        }
        ValidadorDrone.validarSerial(serial);
        ValidadorDrone.validarFabricante(fabricante);
        double peso = ValidadorDrone.parsearPeso(pesoTexto);

        existente.setSerial(serial.trim());
        existente.setFabricante(fabricante.trim());
        existente.setModelo(modelo == null ? null : modelo.trim());
        existente.setPeso(peso);

        if (existente instanceof Agricultura) {
            ((Agricultura) existente).setCapacidadTanque(
                    ValidadorDrone.parsearCapacidadTanque(capacidadTexto));
        } else if (existente instanceof Vigilancia) {
            ((Vigilancia) existente).setDeteccionTermica(deteccionTermica);
        }

        return droneDAO.actualizar(existente);
    }

    public void eliminar(Drone drone) {
        droneDAO.eliminar(drone);
    }

    /** Selecciona la factoria concreta segun el tipo elegido en la vista. */
    private DroneFactory construirFactory(String tipo, String serial, String fabricante,
                                          String modelo, double peso,
                                          String capacidadTexto, boolean deteccionTermica) {
        if (tipo == null || tipo.isBlank()) {
            throw new DronValidacionException("Debe seleccionar el tipo de drone.");
        }
        switch (tipo) {
            case "AGRICULTURA":
                double capacidad = ValidadorDrone.parsearCapacidadTanque(capacidadTexto);
                return new AgriculturaFactory(serial, fabricante, modelo, peso, capacidad);
            case "VIGILANCIA":
                return new VigilanciaFactory(serial, fabricante, modelo, peso, deteccionTermica);
            default:
                throw new DronValidacionException("Tipo de drone no soportado: " + tipo);
        }
    }
}