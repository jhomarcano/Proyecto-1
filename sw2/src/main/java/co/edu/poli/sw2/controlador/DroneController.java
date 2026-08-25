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
 * Controlador de la vista de gestion de drones.
 * <p>
 * Captura los eventos de la interfaz definida en {@code drone.fxml},
 * delega la logica en {@link DroneService} y traduce las excepciones de
 * dominio en mensajes visibles para el usuario. Ninguna excepcion se
 * imprime en consola: todas se muestran mediante
 * {@link ManejadorErroresUI} y en la etiqueta de mensajes del formulario.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see DroneService
 * @see ManejadorErroresUI
 */
public class DroneController implements Initializable {

    /** Valor del selector de tipo correspondiente a los drones de agricultura. */
    private static final String TIPO_AGRICULTURA = "AGRICULTURA";

    /** Valor del selector de tipo correspondiente a los drones de vigilancia. */
    private static final String TIPO_VIGILANCIA = "VIGILANCIA";

    /** Selector del tipo de dron a registrar. */
    @FXML private ComboBox<String> cmbTipo;

    /** Campo de captura del serial. */
    @FXML private TextField txtSerial;

    /** Campo de captura del fabricante. */
    @FXML private TextField txtFabricante;

    /** Campo de captura del modelo. */
    @FXML private TextField txtModelo;

    /** Campo de captura del peso. */
    @FXML private TextField txtPeso;

    /** Etiqueta del campo de capacidad; se oculta cuando el tipo no es agricultura. */
    @FXML private Label lblCapacidad;

    /** Campo de captura de la capacidad del tanque. */
    @FXML private TextField txtCapacidad;

    /** Casilla de deteccion termica; se oculta cuando el tipo no es vigilancia. */
    @FXML private CheckBox chkDeteccionTermica;

    /** Etiqueta donde se muestran los mensajes de exito o error. */
    @FXML private Label lblMensaje;

    /** Tabla que lista los drones registrados. */
    @FXML private TableView<Drone> tablaDrones;

    /** Columna del identificador. */
    @FXML private TableColumn<Drone, Integer> colId;

    /** Columna del tipo de dron. */
    @FXML private TableColumn<Drone, String> colTipo;

    /** Columna del serial. */
    @FXML private TableColumn<Drone, String> colSerial;

    /** Columna del fabricante. */
    @FXML private TableColumn<Drone, String> colFabricante;

    /** Columna del modelo. */
    @FXML private TableColumn<Drone, String> colModelo;

    /** Columna del peso. */
    @FXML private TableColumn<Drone, Double> colPeso;

    /** Columna que muestra el atributo propio de cada subclase. */
    @FXML private TableColumn<Drone, String> colEspecifico;

    /** Servicio que concentra la logica de negocio. */
    private final DroneService droneService = new DroneService();

    /** Dron actualmente seleccionado en la tabla; {@code null} si no hay seleccion. */
    private Drone droneSeleccionado;

    /**
     * Inicializa la vista despues de cargar el archivo FXML.
     * <p>
     * Configura el selector de tipos, las columnas de la tabla, carga los
     * datos iniciales y registra el listener que llena el formulario al
     * seleccionar una fila.
     *
     * @param location  ubicacion del archivo FXML; lo aporta JavaFX
     * @param resources recursos de internacionalizacion; lo aporta JavaFX
     */
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

    /**
     * Asocia cada columna de la tabla con la propiedad del modelo que le corresponde.
     * <p>
     * La columna de atributo especifico se calcula en tiempo de ejecucion,
     * ya que su contenido depende de la subclase de cada fila.
     */
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

    /**
     * Construye el texto de la columna de atributo especifico.
     *
     * @param d dron de la fila
     * @return la capacidad del tanque o el estado de deteccion termica, segun la subclase
     */
    private String describirAtributoEspecifico(Drone d) {
        if (d instanceof Agricultura) {
            return "Tanque: " + ((Agricultura) d).getCapacidadTanque() + " L";
        }
        if (d instanceof Vigilancia) {
            return "Termica: " + (((Vigilancia) d).isDeteccionTermica() ? "Si" : "No");
        }
        return "";
    }

    /**
     * Muestra solo los campos que aplican al tipo de dron seleccionado.
     * <p>
     * Se ajustan tanto la visibilidad como la propiedad {@code managed},
     * para que el espacio del campo oculto no quede en blanco.
     *
     * @param tipo tipo de dron actualmente seleccionado
     */
    private void ajustarCamposEspecificos(String tipo) {
        boolean esAgricultura = TIPO_AGRICULTURA.equals(tipo);

        lblCapacidad.setVisible(esAgricultura);
        lblCapacidad.setManaged(esAgricultura);
        txtCapacidad.setVisible(esAgricultura);
        txtCapacidad.setManaged(esAgricultura);

        chkDeteccionTermica.setVisible(!esAgricultura);
        chkDeteccionTermica.setManaged(!esAgricultura);
    }

    /**
     * Recarga la tabla con los drones registrados en la base de datos.
     * <p>
     * Cualquier fallo se muestra al usuario mediante una alerta, nunca
     * por consola.
     */
    private void refrescarTabla() {
        try {
            tablaDrones.setItems(FXCollections.observableArrayList(droneService.listar()));
        } catch (DronException ex) {
            ManejadorErroresUI.mostrar(ex);
        } catch (Exception ex) {
            ManejadorErroresUI.mostrarInesperado(ex);
        }
    }

    /**
     * Registra un dron nuevo con los datos del formulario.
     * <p>
     * Si la operacion tiene exito, recarga la tabla y limpia el
     * formulario. Si falla, muestra el mensaje de la excepcion en la
     * etiqueta y en una alerta.
     */
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

    /**
     * Actualiza el dron seleccionado con los datos del formulario.
     * <p>
     * Requiere que haya una fila seleccionada en la tabla; de lo
     * contrario el servicio lanza una excepcion de validacion.
     */
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

    /**
     * Elimina el dron seleccionado en la tabla.
     * <p>
     * Si no hay ninguna fila seleccionada, avisa al usuario sin llamar
     * al servicio.
     */
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

    /**
     * Vacia todos los campos del formulario y cancela la seleccion actual.
     * <p>
     * Tambien vuelve a habilitar el selector de tipo, que permanece
     * bloqueado mientras hay un dron seleccionado.
     */
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

    /**
     * Vuelca los datos de un dron en los campos del formulario.
     * <p>
     * Bloquea el selector de tipo, ya que cambiar el tipo de un dron
     * existente implicaria mover su registro entre tablas hijas.
     *
     * @param d dron seleccionado en la tabla
     */
    private void cargarFormulario(Drone d) {
        cmbTipo.getSelectionModel().select(d.getTipo());
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

    /**
     * Muestra un mensaje en la etiqueta inferior del formulario.
     *
     * @param texto   mensaje a mostrar
     * @param esError {@code true} para mostrarlo en rojo, {@code false} en verde
     */
    private void mostrarMensaje(String texto, boolean esError) {
        lblMensaje.setText(texto);
        lblMensaje.setStyle(esError ? "-fx-text-fill: #c0392b;" : "-fx-text-fill: #27ae60;");
    }
}