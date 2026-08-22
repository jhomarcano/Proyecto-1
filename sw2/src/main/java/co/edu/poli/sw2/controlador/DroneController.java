package co.edu.poli.sw2.controlador;
 
import co.edu.poli.sw2.exception.DronException;
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.service.DroneService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
 
import java.net.URL;
import java.util.ResourceBundle;
 
public class DroneController implements Initializable {
 
    // ---- Formulario ----
    @FXML private TextField txtSerial;
    @FXML private TextField txtFabricante;
    @FXML private TextField txtPeso;
    @FXML private Label lblMensaje;
 
    // ---- Tabla ----
    @FXML private TableView<Drone> tablaDrones;
    @FXML private TableColumn<Drone, Integer> colId;
    @FXML private TableColumn<Drone, String> colSerial;
    @FXML private TableColumn<Drone, String> colFabricante;
    @FXML private TableColumn<Drone, Double> colPeso;
 
    private final DroneService droneService = new DroneService();
    private Drone droneSeleccionado;
 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        colSerial.setCellValueFactory(new PropertyValueFactory<>("serial"));
        colFabricante.setCellValueFactory(new PropertyValueFactory<>("fabricante"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
 
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
 
    // -------------------- CRUD (contra DroneService -> PostgreSQL) --------------------
 
    @FXML
    private void agregarDrone() {
    	try {
            droneService.crear(txtSerial.getText(), txtFabricante.getText(), txtPeso.getText());
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
    		droneService.actualizar(droneSeleccionado, txtSerial.getText(), txtFabricante.getText(), txtPeso.getText());
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
        tablaDrones.getSelectionModel().clearSelection();
        droneSeleccionado = null;
        lblMensaje.setText("");
    }
 
    // -------------------- Utilidades internas del controlador --------------------
 
    private void cargarFormulario(Drone d) {
        txtSerial.setText(d.getSerial());
        txtFabricante.setText(d.getFabricante());
        txtPeso.setText(String.valueOf(d.getPeso()));
    }
 
 
    private void mostrarMensaje(String texto, boolean esError) {
        lblMensaje.setText(texto);
        lblMensaje.setStyle(esError ? "-fx-text-fill: #c0392b;" : "-fx-text-fill: #27ae60;");
    }
}
 