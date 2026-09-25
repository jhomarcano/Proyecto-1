package co.edu.poli.sw2.controlador;

import java.util.Optional;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Ventana emergente que solicita la contrasena de administrador antes de
 * eliminar un dron.
 * <p>
 * Solo captura el texto: no valida nada. La validacion es responsabilidad
 * de {@link co.edu.poli.sw2.service.proxy.DronProxy}.
 *
 * @author Alejandra Cano y Juan Rosero
 */
public final class VentanaClaveEliminacion {

    /** Clase de utilidad: no se instancia. */
    private VentanaClaveEliminacion() {
    }

    /**
     * Abre la ventana y bloquea la principal hasta que el usuario responda.
     *
     * @param serialDrone serial del dron que se va a eliminar
     * @param propietario ventana desde la que se invoco; admite {@code null}
     * @return la contrasena digitada, o vacio si el usuario cancelo
     */
    public static Optional<String> pedirClave(String serialDrone, Window propietario) {
        Stage ventana = new Stage();
        ventana.initModality(Modality.WINDOW_MODAL);
        if (propietario != null) {
            ventana.initOwner(propietario);
        }
        ventana.setTitle("Autorizacion requerida (Proxy)");
        ventana.setResizable(false);

        final String[] resultado = new String[1];

        Label titulo = new Label("ELIMINAR DRONE");
        titulo.getStyleClass().add("titulo-seccion");

        Label detalle = new Label(
                "Se eliminara el drone con serial: " + serialDrone
                + "\nIngresa la contrasena de administrador para continuar.");
        detalle.setWrapText(true);

        PasswordField txtClave = new PasswordField();
        txtClave.setPromptText("Contrasena");

        Button btnAceptar = new Button("Confirmar");
        btnAceptar.getStyleClass().addAll("boton", "boton-primario");
        btnAceptar.setDefaultButton(true);
        btnAceptar.setOnAction(e -> {
            resultado[0] = txtClave.getText();
            ventana.close();
        });

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.getStyleClass().addAll("boton", "boton-secundario");
        btnCancelar.setCancelButton(true);
        btnCancelar.setOnAction(e -> {
            resultado[0] = null;
            ventana.close();
        });

        HBox botones = new HBox(10, btnCancelar, btnAceptar);
        botones.setAlignment(Pos.CENTER_RIGHT);

        VBox raiz = new VBox(12, titulo, detalle, txtClave, botones);
        raiz.getStyleClass().add("tarjeta");
        raiz.setPadding(new Insets(20));
        raiz.setPrefWidth(360);

        Scene escena = new Scene(raiz);
        if (propietario != null && propietario.getScene() != null) {
            escena.getStylesheets().addAll(propietario.getScene().getStylesheets());
        }

        ventana.setScene(escena);
        ventana.setOnShown(e -> txtClave.requestFocus());
        ventana.showAndWait();

        return Optional.ofNullable(resultado[0]);
    }
}