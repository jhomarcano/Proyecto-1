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
 * Capa de logica de negocio para la gestion de drones.
 * <p>
 * Actua como intermediaria entre el controlador y el DAO. Se encarga de
 * validar los datos antes de tocar la base de datos, seleccionar la
 * factoria adecuada segun el tipo de dron y delegar la persistencia.
 * <p>
 * Gracias al patron Factory, esta clase no instancia directamente las
 * subclases concretas de {@link Drone} al crear un registro nuevo.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see ValidadorDrone
 * @see DroneFactory
 */
public class DroneService {

    /** Acceso a datos de drones. Se declara con la interfaz, no con la implementacion. */
    private final DroneDAO droneDAO = new DroneDAOImpl();

    /**
     * Recupera todos los drones registrados.
     *
     * @return la lista de drones; vacia si no hay registros
     */
    public List<Drone> listar() {
        return droneDAO.listar();
    }

    /**
     * Valida los datos capturados y persiste un dron nuevo.
     * <p>
     * Las validaciones se ejecutan antes de abrir la conexion, de modo
     * que un dato invalido no llegue a la base de datos.
     *
     * @param tipo             tipo de dron: {@code "AGRICULTURA"} o {@code "VIGILANCIA"}
     * @param serial           codigo unico del dron
     * @param fabricante       empresa fabricante
     * @param modelo           referencia comercial; admite {@code null}
     * @param pesoTexto        peso en texto, tal como se capturo en el formulario
     * @param capacidadTexto   capacidad del tanque en texto; solo aplica a Agricultura
     * @param deteccionTermica indicador termico; solo aplica a Vigilancia
     * @return el dron creado, con el id asignado por la base de datos
     * @throws DronValidacionException si algun dato no cumple las reglas de negocio
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

    /**
     * Valida los datos capturados y actualiza un dron existente.
     * <p>
     * El tipo de dron no se puede modificar: se conserva el de la
     * instancia seleccionada y solo se actualiza el atributo especifico
     * que corresponda a esa subclase.
     *
     * @param existente        dron seleccionado en la tabla; no puede ser {@code null}
     * @param serial           nuevo codigo unico
     * @param fabricante       nueva empresa fabricante
     * @param modelo           nueva referencia comercial; admite {@code null}
     * @param pesoTexto        nuevo peso en texto
     * @param capacidadTexto   nueva capacidad del tanque en texto; solo aplica a Agricultura
     * @param deteccionTermica nuevo indicador termico; solo aplica a Vigilancia
     * @return el dron actualizado
     * @throws DronValidacionException si no hay seleccion o algun dato es invalido
     */
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

    /**
     * Elimina un dron del sistema.
     *
     * @param drone el dron a eliminar
     */
    public void eliminar(Drone drone) {
        droneDAO.eliminar(drone);
    }

    /**
     * Selecciona e instancia la factoria concreta segun el tipo elegido.
     * <p>
     * Es el unico punto del servicio que conoce los tipos disponibles;
     * agregar un nuevo tipo de dron implica anadir un caso aqui y su
     * factoria correspondiente.
     *
     * @param tipo             tipo de dron seleccionado en la vista
     * @param serial           codigo unico ya normalizado
     * @param fabricante       fabricante ya normalizado
     * @param modelo           modelo ya normalizado
     * @param peso             peso ya validado y convertido
     * @param capacidadTexto   capacidad del tanque en texto, pendiente de validar
     * @param deteccionTermica indicador termico
     * @return la factoria que corresponde al tipo indicado
     * @throws DronValidacionException si el tipo esta vacio o no esta soportado
     */
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