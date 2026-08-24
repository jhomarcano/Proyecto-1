package co.edu.poli.sw2.controlador;

import co.edu.poli.sw2.exception.DronException;
import co.edu.poli.sw2.modelo.Agricultura;
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Vigilancia;
import co.edu.poli.sw2.service.DroneService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador de la vista de gestion de drones. Captura los eventos de la
 * interfaz, delega en {@link DroneService} y traduce las excepciones de
 * dominio en mensajes visibles para el usuario.
 */
public class DroneController implements Initializable {

    private static final String TIPO_AGRICULTURA = "AGRICULTURA";
    private static final String TIPO_VIGILANCIA = "VIGILANCIA";

    // ---- Formulario ----
    @FXML private ComboBox<String> cmbTipo;
    @FXML private TextField txtSerial;
    @FXML private TextField txtFabricante;
    @FXML private TextField txtModelo;
    @FXML private TextField txtPeso;
    @FXML private Label lblCapacidad;
    @FXML private TextField txtCapacidad;
    @FXML private CheckBox chkDeteccionTermica;
    @FXML private Label lblMensaje;

    // ---- Tabla ----
    @FXML private TableView<Drone> tablaDrones;
    @FXML private TableColumn<Drone, Integer> colId;
    @FXML private TableColumn<Drone, String> colTipo;
    @FXML private TableColumn<Drone, String> colSerial;
    @FXML private TableColumn<Drone, String> colFabricante;
    @FXML private TableColumn<Drone, String> colModelo;
    @FXML private TableColumn<Drone, Double> colPeso;
    @FXML private TableColumn<Drone, String> colEspecifico;

    private final DroneService droneService = new DroneService();
    private Drone droneSeleccionado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbTipo.setItems(FXCollections.observableArrayList(TIPO_AGRICULTURA, TIPO_VIGILANCIA));
        cmbTipo.getSelectionModel().select(TIPO_AGRICULTURA);
        cmbTipo.valueProperty().addListener((obs, viejo, nuevo) -> ajustarCamposEspecificos(nuevo));
        ajustarCamposEspecificos(TIPO_AGRICULTURA);

        configurarTabla();
        refrescarTabla();

        tablaDrones.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nuevo) -> {
            if (nuevo != null) {
                droneSeleccionado = nuevo;
                cargarFormulario(nuevo);
            }
        });
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colSerial.setCellValueFactory(new PropertyValueFactory<>("serial"));
        colFabricante.setCellValueFactory(new PropertyValueFactory<>("fabricante"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));

        colEspecifico.setCellValueFactory(datos ->
                new SimpleStringProperty(describirAtributoEspecifico(datos.getValue())));
    }

    private String describirAtributoEspecifico(Drone d) {
        if (d instanceof Agricultura) {
            return "Tanque: " + ((Agricultura) d).getCapacidadTanque() + " L";
        }
        if (d instanceof Vigilancia) {
            return "Termica: " + (((Vigilancia) d).isDeteccionTermica() ? "Si" : "No");
        }
        return "";
    }

    /** Muestra solo los campos que aplican al tipo seleccionado. */
    private void ajustarCamposEspecificos(String tipo) {
        boolean esAgricultura = TIPO_AGRICULTURA.equals(tipo);

        lblCapacidad.setVisible(esAgricultura);
        lblCapacidad.setManaged(esAgricultura);
        txtCapacidad.setVisible(esAgricultura);
        txtCapacidad.setManaged(esAgricultura);

        chkDeteccionTermica.setVisible(!esAgricultura);
        chkDeteccionTermica.setManaged(!esAgricultura);
    }

    private void refrescarTabla() {
        try {
            tablaDrones.setItems(FXCollections.observableArrayList(droneService.listar()));
        } catch (DronException ex) {
            ManejadorErroresUI.mostrar(ex);
        } catch (Exception ex) {
            ManejadorErroresUI.mostrarInesperado(ex);
        }
    }

    // -------------------- CRUD --------------------

    @FXML
    private void agregarDrone() {
        try {
            droneService.crear(
                    cmbTipo.getValue(),
                    txtSerial.getText(),
                    txtFabricante.getText(),
                    txtModelo.getText(),
                    txtPeso.getText(),
                    txtCapacidad.getText(),
                    chkDeteccionTermica.isSelected());
            refrescarTabla();
            limpiarFormulario();
            mostrarMensaje("Drone agregado correctamente.", false);
        } catch (DronException ex) {
            mostrarMensaje(ex.getMessage(), true);
            ManejadorErroresUI.mostrar(ex);
        } catch (Exception ex) {
            ManejadorErroresUI.mostrarInesperado(ex);
        }
    }

    @FXML
    private void modificarDrone() {
        try {
            droneService.actualizar(
                    droneSeleccionado,
                    txtSerial.getText(),
                    txtFabricante.getText(),
                    txtModelo.getText(),
                    txtPeso.getText(),
                    txtCapacidad.getText(),
                    chkDeteccionTermica.isSelected());
            refrescarTabla();
            limpiarFormulario();
            mostrarMensaje("Drone modificado correctamente.", false);
        } catch (DronException ex) {
            mostrarMensaje(ex.getMessage(), true);
            ManejadorErroresUI.mostrar(ex);
        } catch (Exception ex) {
            ManejadorErroresUI.mostrarInesperado(ex);
        }
    }

    @FXML
    private void eliminarDrone() {
        Drone seleccionado = tablaDrones.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarMensaje("Selecciona un drone de la tabla para eliminar.", true);
            return;
        }
        try {
            droneService.eliminar(seleccionado);
            refrescarTabla();
            limpiarFormulario();
            mostrarMensaje("Drone eliminado correctamente.", false);
        } catch (DronException ex) {
            mostrarMensaje(ex.getMessage(), true);
            ManejadorErroresUI.mostrar(ex);
        } catch (Exception ex) {
            ManejadorErroresUI.mostrarInesperado(ex);
        }
    }

    @FXML
    private void limpiarFormulario() {
        txtSerial.clear();
        txtFabricante.clear();
        txtModelo.clear();
        txtPeso.clear();
        txtCapacidad.clear();
        chkDeteccionTermica.setSelected(false);
        cmbTipo.setDisable(false);
        cmbTipo.getSelectionModel().select(TIPO_AGRICULTURA);
        tablaDrones.getSelectionModel().clearSelection();
        droneSeleccionado = null;
        lblMensaje.setText("");
    }

    // -------------------- Utilidades internas --------------------

    private void cargarFormulario(Drone d) {
        cmbTipo.getSelectionModel().select(d.getTipo());
        // El tipo no se puede cambiar en una modificacion.
        cmbTipo.setDisable(true);

        txtSerial.setText(d.getSerial());
        txtFabricante.setText(d.getFabricante());
        txtModelo.setText(d.getModelo());
        txtPeso.setText(String.valueOf(d.getPeso()));

        if (d instanceof Agricultura) {
            txtCapacidad.setText(String.valueOf(((Agricultura) d).getCapacidadTanque()));
            chkDeteccionTermica.setSelected(false);
        } else if (d instanceof Vigilancia) {
            txtCapacidad.clear();
            chkDeteccionTermica.setSelected(((Vigilancia) d).isDeteccionTermica());
        }
    }

    private void mostrarMensaje(String texto, boolean esError) {
        lblMensaje.setText(texto);
        lblMensaje.setStyle(esError ? "-fx-text-fill: #c0392b;" : "-fx-text-fill: #27ae60;");
    }
}