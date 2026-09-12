package co.edu.poli.sw2.controlador;

import co.edu.poli.sw2.modelo.Drone;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Ventana modal que evidencia el resultado del patron Bridge aplicado
 * al control del drone.
 * <p>
 * Muestra la descripcion generada por {@link co.edu.poli.sw2.service.ModoControlDron}
 * y deja explicito que el dato vive en memoria
 * ({@link co.edu.poli.sw2.service.RegistroControlDron}), no en la base de datos.
 *
 * @author Alejandra Cano y Juan Rosero
 */
public final class VentanaControlDron {

    private VentanaControlDron() {
    }

    public static void mostrar(Drone drone, String descripcionControl, Window propietario) {
        Label titulo = new Label("Patron Bridge - Control del drone");
        titulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1b2838;");

        Label subtitulo = new Label(
                "ModoControlDron delego en la implementacion concreta del control. "
                        + "El resultado NO se guardo en la base de datos.");
        subtitulo.setWrapText(true);
        subtitulo.setStyle("-fx-font-size: 12px; -fx-text-fill: #5d6d7e;");

        Label lblDescripcion = new Label(descripcionControl);
        lblDescripcion.setWrapText(true);
        lblDescripcion.setStyle("-fx-font-size: 13px; -fx-text-fill: #1b2838; "
                + "-fx-font-weight: bold;");

        VBox cajaDescripcion = new VBox(lblDescripcion);
        cajaDescripcion.setPadding(new Insets(14));
        cajaDescripcion.setStyle("-fx-background-color: white; -fx-background-radius: 8; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 8, 0, 0, 2);");

        Label tituloUbicacion = new Label("Donde quedo guardado");
        tituloUbicacion.setStyle("-fx-font-size: 11px; -fx-text-fill: #95a5a6;");

        Label lblUbicacion = new Label(
                "Memoria temporal en tiempo de ejecucion.\n"
                + "Clase: co.edu.poli.sw2.service.RegistroControlDron\n"
                + "Clave (serial del drone): " + drone.getSerial() + "\n"
                + "No existe ninguna columna ni tabla en PostgreSQL para este dato.");
        lblUbicacion.setWrapText(true);
        lblUbicacion.setStyle("-fx-text-fill: #7ee787; -fx-font-size: 12px; "
                + "-fx-font-family: 'Consolas', 'Courier New', monospace;");

        VBox cajaUbicacion = new VBox(lblUbicacion);
        cajaUbicacion.setPadding(new Insets(12));
        cajaUbicacion.setStyle("-fx-background-color: #22272e; -fx-background-radius: 6;");

        Button btnCerrar = new Button("Cerrar");
        btnCerrar.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 8 18 8 18;");

        VBox raiz = new VBox(14, titulo, subtitulo, cajaDescripcion,
                tituloUbicacion, cajaUbicacion, btnCerrar);
        raiz.setPadding(new Insets(22));
        raiz.setAlignment(Pos.TOP_LEFT);
        raiz.setStyle("-fx-background-color: #eef2f7;");

        Stage ventana = new Stage();
        ventana.setTitle("Control asignado (Bridge)");
        ventana.initModality(Modality.APPLICATION_MODAL);
        if (propietario != null) {
            ventana.initOwner(propietario);
        }
        btnCerrar.setOnAction(e -> ventana.close());
        ventana.setScene(new Scene(raiz, 480, 420));
        ventana.setResizable(false);
        ventana.showAndWait();
    }
}