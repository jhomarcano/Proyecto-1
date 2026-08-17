package co.edu.poli.sw2.controlador;
 
import co.edu.poli.sw2.dao.CatalogoRepositorio;
import co.edu.poli.sw2.dao.DroneDAO;
import co.edu.poli.sw2.dao.DroneDAOImpl;
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Piloto;
import co.edu.poli.sw2.modelo.Sensor;
import javafx.beans.property.SimpleStringProperty;
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
    @FXML private ComboBox<Piloto> cbPiloto;
    @FXML private ListView<Sensor> listSensores;
    @FXML private Label lblMensaje;
 
    // ---- Tabla ----
    @FXML private TableView<Drone> tablaDrones;
    @FXML private TableColumn<Drone, Integer> colId;
    @FXML private TableColumn<Drone, String> colSerial;
    @FXML private TableColumn<Drone, String> colFabricante;
    @FXML private TableColumn<Drone, Double> colPeso;
    @FXML private TableColumn<Drone, String> colPiloto;
    @FXML private TableColumn<Drone, String> colSensores;
 
    private final DroneDAO droneDAO = new DroneDAOImpl();
    private Drone droneSeleccionado;
 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTabla();
        cargarCatalogos();
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
 
        colPiloto.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPilotoNombre()));
        colSensores.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getSensoresTexto()));
    }
 
    /**
     * Antes: listas de Piloto/Sensor "quemadas" en el controlador
     * (crearPilotos()/crearSensores()).
     * Ahora: se consultan desde PostgreSQL a traves de
     * CatalogoRepositorio, que solo lee (no crea/edita/elimina),
     * respetando la regla de que el CRUD es exclusivo de Dron.
     */
    private void cargarCatalogos() {
        cbPiloto.setItems(FXCollections.observableArrayList(CatalogoRepositorio.listarPilotos()));
        listSensores.setItems(FXCollections.observableArrayList(CatalogoRepositorio.listarSensores()));
        listSensores.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
 
    private void refrescarTabla() {
        tablaDrones.setItems(FXCollections.observableArrayList(droneDAO.listar()));
    }
 
    // -------------------- CRUD (contra DroneDAO -> PostgreSQL) --------------------
 
    @FXML
    private void agregarDrone() {
        if (!validarFormulario()) return;
 
        Drone nuevo = new Drone(
                0,
                txtSerial.getText().trim(),
                txtFabricante.getText().trim(),
                Double.parseDouble(txtPeso.getText().trim()),
                cbPiloto.getValue()
        );
        nuevo.getSensores().addAll(listSensores.getSelectionModel().getSelectedItems());
 
        droneDAO.crear(nuevo);
        refrescarTabla();
        limpiarFormulario();
        mostrarMensaje("Drone agregado correctamente.", false);
    }
 
    @FXML
    private void modificarDrone() {
        if (droneSeleccionado == null) {
            mostrarMensaje("Selecciona un drone de la tabla para modificar.", true);
            return;
        }
        if (!validarFormulario()) return;
 
        droneSeleccionado.setSerial(txtSerial.getText().trim());
        droneSeleccionado.setFabricante(txtFabricante.getText().trim());
        droneSeleccionado.setPeso(Double.parseDouble(txtPeso.getText().trim()));
        droneSeleccionado.setPiloto(cbPiloto.getValue());
        droneSeleccionado.setSensores(listSensores.getSelectionModel().getSelectedItems());
 
        droneDAO.actualizar(droneSeleccionado);
 
        refrescarTabla();
        limpiarFormulario();
        mostrarMensaje("Drone modificado correctamente.", false);
    }
 
    @FXML
    private void eliminarDrone() {
        Drone seleccionado = tablaDrones.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarMensaje("Selecciona un drone de la tabla para eliminar.", true);
            return;
        }
        droneDAO.eliminar(seleccionado);
        refrescarTabla();
        limpiarFormulario();
        mostrarMensaje("Drone eliminado correctamente.", false);
    }
 
    @FXML
    private void limpiarFormulario() {
        txtSerial.clear();
        txtFabricante.clear();
        txtPeso.clear();
        cbPiloto.getSelectionModel().clearSelection();
        listSensores.getSelectionModel().clearSelection();
        tablaDrones.getSelectionModel().clearSelection();
        droneSeleccionado = null;
        lblMensaje.setText("");
    }
 
    // -------------------- Utilidades internas del controlador --------------------
 
    private void cargarFormulario(Drone d) {
        txtSerial.setText(d.getSerial());
        txtFabricante.setText(d.getFabricante());
        txtPeso.setText(String.valueOf(d.getPeso()));
        cbPiloto.setValue(d.getPiloto());
 
        listSensores.getSelectionModel().clearSelection();
        for (Sensor s : d.getSensores()) {
            listSensores.getSelectionModel().select(s);
        }
    }
 
    private boolean validarFormulario() {
        if (txtSerial.getText().isBlank()
                || txtFabricante.getText().isBlank() || txtPeso.getText().isBlank()) {
            mostrarMensaje("Todos los campos de texto son obligatorios.", true);
            return false;
        }
        if (cbPiloto.getValue() == null) {
            mostrarMensaje("Debes asignar un piloto (relacion 1 a 1).", true);
            return false;
        }
        try {
            double peso = Double.parseDouble(txtPeso.getText().trim());
            if (peso <= 0) {
                mostrarMensaje("El peso debe ser un numero mayor que 0.", true);
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarMensaje("El peso debe ser un numero valido (ej: 0.9).", true);
            return false;
        }
        return true;
    }
 
    private void mostrarMensaje(String texto, boolean esError) {
        lblMensaje.setText(texto);
        lblMensaje.setStyle(esError ? "-fx-text-fill: #c0392b;" : "-fx-text-fill: #27ae60;");
    }
}
 