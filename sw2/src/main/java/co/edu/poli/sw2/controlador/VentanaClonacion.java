package co.edu.poli.sw2.controlador;

import co.edu.poli.sw2.modelo.Agricultura;
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Vigilancia;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.List;

/**
 * Ventana modal que evidencia el resultado del patron Prototype.
 * <p>
 * Muestra lado a lado el dron original y su copia, con los atributos de
 * cada uno y su posicion en memoria, mas un bloque de verificaciones que
 * confirma que se trata de dos objetos distintos.
 * <p>
 * Se arma por codigo y no por FXML porque su contenido es dinamico: las
 * filas de atributos dependen de la subclase concreta del dron.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see co.edu.poli.sw2.service.DronePrototype
 */
public final class VentanaClonacion {

    /** Color de acento del panel del dron original. */
    private static final String COLOR_ORIGINAL = "#2c3e50";

    /** Color de acento del panel del dron clonado. */
    private static final String COLOR_CLON = "#1e8449";

    /** Fuente monoespaciada usada en las referencias de memoria. */
    private static final String FUENTE_CODIGO =
            "-fx-font-family: 'Consolas', 'Courier New', monospace;";

    /**
     * Impide instanciar esta clase de utilidad.
     */
    private VentanaClonacion() {
    }

    /**
     * Abre la ventana de comparacion y bloquea la ventana principal
     * hasta que el usuario la cierre.
     *
     * @param original    dron seleccionado en la tabla
     * @param clon        copia producida por el prototipo
     * @param propietario ventana desde la que se invoco; admite {@code null}
     */
    public static void mostrar(Drone original, Drone clon, Window propietario) {
        VBox raiz = new VBox(18);
        raiz.setPadding(new Insets(22));
        raiz.setStyle("-fx-background-color: #eef2f7;");

        raiz.getChildren().add(construirEncabezado());
        raiz.getChildren().add(construirComparacion(original, clon));
        raiz.getChildren().add(construirVerificaciones(original, clon));
        raiz.getChildren().add(construirPie());

        Stage ventana = new Stage();
        ventana.setTitle("Patron Prototype - Clonacion de drone");
        ventana.initModality(Modality.APPLICATION_MODAL);
        if (propietario != null) {
            ventana.initOwner(propietario);
        }
        ventana.setScene(new Scene(raiz, 900, 650));
        ventana.setResizable(false);

        Button btnCerrar = new Button("Cerrar");
        btnCerrar.setPrefWidth(140);
        btnCerrar.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 8 18 8 18;");
        btnCerrar.setOnAction(e -> ventana.close());

        HBox contenedorBoton = new HBox(btnCerrar);
        contenedorBoton.setAlignment(Pos.CENTER);
        raiz.getChildren().add(contenedorBoton);

        ventana.showAndWait();
    }

    /**
     * Construye el titulo y la explicacion breve de la ventana.
     *
     * @return el bloque de encabezado
     */
    private static VBox construirEncabezado() {
        Label titulo = new Label("Patron Prototype");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1b2838;");

        Label subtitulo = new Label(
                "DronePrototype.clonar() produjo una copia del dron seleccionado. "
                        + "El clon vive solo en memoria: la base de datos no se modifico.");
        subtitulo.setWrapText(true);
        subtitulo.setStyle("-fx-font-size: 13px; -fx-text-fill: #5d6d7e;");

        return new VBox(4, titulo, subtitulo);
    }

    /**
     * Construye los dos paneles de comparacion con la flecha intermedia.
     *
     * @param original dron de partida
     * @param clon     copia generada
     * @return el bloque central de la ventana
     */
    private static HBox construirComparacion(Drone original, Drone clon) {
        VBox panelOriginal = construirPanel(
                "ORIGINAL  (registrado en la base de datos)", COLOR_ORIGINAL, original);
        VBox panelClon = construirPanel(
                "CLON  (solo en memoria)", COLOR_CLON, clon);

        Label flecha = new Label("\u2192");
        flecha.setStyle("-fx-font-size: 30px; -fx-text-fill: #85929e; -fx-font-weight: bold;");

        Label etiquetaFlecha = new Label("clonar()");
        etiquetaFlecha.setStyle("-fx-font-size: 11px; -fx-text-fill: #85929e;" + FUENTE_CODIGO);

        VBox medio = new VBox(2, flecha, etiquetaFlecha);
        medio.setAlignment(Pos.CENTER);
        medio.setMinWidth(80);

        HBox.setHgrow(panelOriginal, Priority.ALWAYS);
        HBox.setHgrow(panelClon, Priority.ALWAYS);

        HBox fila = new HBox(0, panelOriginal, medio, panelClon);
        fila.setAlignment(Pos.CENTER);
        return fila;
    }

    /**
     * Construye una tarjeta con los atributos y la referencia de un dron.
     *
     * @param titulo encabezado de la tarjeta
     * @param color  color de fondo del encabezado
     * @param dron   dron a describir
     * @return la tarjeta lista para insertar en el contenedor
     */
    private static VBox construirPanel(String titulo, String color, Drone dron) {
        Label cabecera = new Label(titulo);
        cabecera.setMaxWidth(Double.MAX_VALUE);
        cabecera.setAlignment(Pos.CENTER);
        cabecera.setPadding(new Insets(11));
        cabecera.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; "
                + "-fx-background-radius: 10 10 0 0; -fx-font-size: 13px; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(9);
        grid.setPadding(new Insets(16, 16, 12, 16));

        ColumnConstraints colEtiqueta = new ColumnConstraints();
        colEtiqueta.setMinWidth(125);
        ColumnConstraints colValor = new ColumnConstraints();
        colValor.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(colEtiqueta, colValor);

        List<String[]> filas = describirAtributos(dron);
        for (int i = 0; i < filas.size(); i++) {
            Label etiqueta = new Label(filas.get(i)[0]);
            etiqueta.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d;");

            Label valor = new Label(filas.get(i)[1]);
            valor.setStyle("-fx-font-size: 13px; -fx-text-fill: #1b2838; -fx-font-weight: bold;");
            valor.setWrapText(true);

            grid.add(etiqueta, 0, i);
            grid.add(valor, 1, i);
        }

        Label tituloMemoria = new Label("Posicion en memoria");
        tituloMemoria.setStyle("-fx-font-size: 11px; -fx-text-fill: #95a5a6;");

        Label lblReferencia = new Label(referenciaDe(dron));
        lblReferencia.setWrapText(true);
        lblReferencia.setStyle("-fx-text-fill: #7ee787; -fx-font-size: 12px;" + FUENTE_CODIGO);

        Label lblHash = new Label("identityHashCode = " + System.identityHashCode(dron));
        lblHash.setStyle("-fx-text-fill: #8b949e; -fx-font-size: 11px;" + FUENTE_CODIGO);

        VBox cajaMemoria = new VBox(4, lblReferencia, lblHash);
        cajaMemoria.setPadding(new Insets(10, 12, 10, 12));
        cajaMemoria.setStyle("-fx-background-color: #22272e; -fx-background-radius: 6;");

        VBox bloqueMemoria = new VBox(5, tituloMemoria, cajaMemoria);
        bloqueMemoria.setPadding(new Insets(0, 16, 16, 16));

        Region separador = new Region();
        VBox.setVgrow(separador, Priority.ALWAYS);

        VBox tarjeta = new VBox(0, cabecera, grid, separador, bloqueMemoria);
        tarjeta.setMinWidth(360);
        tarjeta.setStyle("-fx-background-color: white; -fx-background-radius: 10; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.16), 12, 0, 0, 4);");
        return tarjeta;
    }

    /**
     * Arma la lista de pares etiqueta-valor de un dron, incluyendo el
     * atributo propio de su subclase.
     *
     * @param d dron a describir
     * @return las filas a pintar en la tarjeta
     */
    private static List<String[]> describirAtributos(Drone d) {
        List<String[]> filas = new ArrayList<>();
        filas.add(new String[]{"Clase", d.getClass().getSimpleName()});
        filas.add(new String[]{"Tipo", d.getTipo()});
        filas.add(new String[]{"ID", d.getId() == 0
                ? "0   (sin persistir)" : String.valueOf(d.getId())});
        filas.add(new String[]{"Serial", textoSeguro(d.getSerial())});
        filas.add(new String[]{"Fabricante", textoSeguro(d.getFabricante())});
        filas.add(new String[]{"Modelo", textoSeguro(d.getModelo())});
        filas.add(new String[]{"Peso (kg)", String.valueOf(d.getPeso())});

        if (d instanceof Agricultura) {
            filas.add(new String[]{"Capacidad (L)",
                    String.valueOf(((Agricultura) d).getCapacidadTanque())});
        } else if (d instanceof Vigilancia) {
            filas.add(new String[]{"Deteccion termica",
                    ((Vigilancia) d).isDeteccionTermica() ? "Si" : "No"});
        }
        return filas;
    }

    /**
     * Construye el bloque de comprobaciones del patron.
     *
     * @param original dron de partida
     * @param clon     copia generada
     * @return el bloque de verificaciones
     */
    private static VBox construirVerificaciones(Drone original, Drone clon) {
        Label titulo = new Label("Verificacion");
        titulo.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1b2838;");

        VBox lista = new VBox(7);
        lista.getChildren().add(filaVerificacion(original != clon,
                "original != clon  ->  son dos objetos diferentes en memoria"));
        lista.getChildren().add(filaVerificacion(
                System.identityHashCode(original) != System.identityHashCode(clon),
                "Los identityHashCode no coinciden"));
        lista.getChildren().add(filaVerificacion(
                original.getClass() == clon.getClass(),
                "El clon conserva la clase concreta del original"));
        lista.getChildren().add(filaVerificacion(
                mismosAtributos(original, clon),
                "Todos los atributos de negocio son identicos"));
        lista.getChildren().add(filaVerificacion(clon.getId() == 0,
                "El clon tiene id 0: no se inserto en la base de datos"));

        VBox caja = new VBox(10, titulo, lista);
        caja.setPadding(new Insets(15, 18, 15, 18));
        caja.setStyle("-fx-background-color: white; -fx-background-radius: 10; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 10, 0, 0, 3);");
        return caja;
    }

    /**
     * Construye una fila de verificacion con su marca de aprobado o fallido.
     *
     * @param cumple      resultado de la comprobacion
     * @param descripcion texto explicativo de lo que se comprobo
     * @return la fila lista para insertar en la lista
     */
    private static HBox filaVerificacion(boolean cumple, String descripcion) {
        Label marca = new Label(cumple ? "\u2714" : "\u2716");
        marca.setMinWidth(20);
        marca.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: "
                + (cumple ? "#1e8449" : "#c0392b") + ";");

        Label texto = new Label(descripcion);
        texto.setStyle("-fx-font-size: 12px; -fx-text-fill: #34495e;" + FUENTE_CODIGO);
        texto.setWrapText(true);

        HBox fila = new HBox(8, marca, texto);
        fila.setAlignment(Pos.CENTER_LEFT);
        return fila;
    }

    /**
     * Compara los atributos de negocio de dos drones, sin tener en
     * cuenta el id, que se deja en 0 deliberadamente en la copia.
     *
     * @param a primer dron
     * @param b segundo dron
     * @return {@code true} si todos los atributos coinciden
     */
    private static boolean mismosAtributos(Drone a, Drone b) {
        if (a.getClass() != b.getClass()) {
            return false;
        }
        boolean comunes = iguales(a.getSerial(), b.getSerial())
                && iguales(a.getFabricante(), b.getFabricante())
                && iguales(a.getModelo(), b.getModelo())
                && Double.compare(a.getPeso(), b.getPeso()) == 0;
        if (!comunes) {
            return false;
        }
        if (a instanceof Agricultura) {
            return Double.compare(((Agricultura) a).getCapacidadTanque(),
                    ((Agricultura) b).getCapacidadTanque()) == 0;
        }
        if (a instanceof Vigilancia) {
            return ((Vigilancia) a).isDeteccionTermica()
                    == ((Vigilancia) b).isDeteccionTermica();
        }
        return true;
    }

    /**
     * Arma la representacion de la posicion en memoria de un objeto.
     * <p>
     * Java no expone la direccion real de memoria, por lo que se usa
     * {@code System.identityHashCode}, que es el mismo valor que la JVM
     * imprime en {@code Object.toString()} y que identifica de forma
     * unica a la instancia mientras vive.
     *
     * @param d dron del que se quiere la referencia
     * @return cadena con el nombre de la clase y el hash en hexadecimal
     */
    private static String referenciaDe(Drone d) {
        return d.getClass().getName() + "@"
                + Integer.toHexString(System.identityHashCode(d));
    }

    /**
     * Compara dos cadenas admitiendo valores nulos.
     *
     * @param a primera cadena
     * @param b segunda cadena
     * @return {@code true} si ambas son nulas o si son equivalentes
     */
    private static boolean iguales(String a, String b) {
        return a == null ? b == null : a.equals(b);
    }

    /**
     * Construye la nota final que explica por que el clon no se guarda.
     *
     * @return la etiqueta con la nota
     */
    private static Label construirPie() {
        Label nota = new Label(
                "Nota: el clon conserva el serial del original, que en la tabla dron tiene "
                        + "restriccion UNIQUE. Por eso no se persiste: el objetivo de esta "
                        + "operacion es demostrar la copia en memoria, no crear un registro nuevo.");
        nota.setWrapText(true);
        nota.setStyle("-fx-font-size: 11px; -fx-text-fill: #7f8c8d; -fx-font-style: italic;");
        return nota;
    }

    /**
     * Sustituye los valores nulos por un texto legible.
     *
     * @param valor texto original; puede ser {@code null}
     * @return el texto recibido o un marcador si venia nulo
     */
    private static String textoSeguro(String valor) {
        return (valor == null || valor.isBlank()) ? "(sin dato)" : valor;
    }
}