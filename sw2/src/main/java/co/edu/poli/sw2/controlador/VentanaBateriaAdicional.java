package co.edu.poli.sw2.controlador;

import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.service.Componente;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Ventana modal que evidencia el resultado del patron Decorator.
 * <p>
 * Muestra la descripcion del dron antes y despues de aplicar el
 * decorador {@code BateriaAdicional}, resaltando en color el fragmento
 * que el decorador anadio, junto con la cadena de objetos construida y
 * las comprobaciones que confirman que el anadido no modifico la
 * entidad ni la base de datos.
 * <p>
 * Se arma por codigo y no por FXML porque su contenido depende por
 * completo del dron y de la descripcion capturados en tiempo de ejecucion.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see co.edu.poli.sw2.service.BateriaAdicional
 */
public final class VentanaBateriaAdicional {

    /** Color de acento del panel sin decorar. */
    private static final String COLOR_BASE = "#2c3e50";

    /** Color de acento del panel decorado y del texto anadido. */
    private static final String COLOR_DECORADO = "#8e44ad";

    /** Fuente monoespaciada usada en los bloques de texto tecnico. */
    private static final String FUENTE_CODIGO =
            "-fx-font-family: 'Consolas', 'Courier New', monospace;";

    /**
     * Impide instanciar esta clase de utilidad.
     */
    private VentanaBateriaAdicional() {
    }

    /**
     * Abre la ventana de comparacion y bloquea la ventana principal
     * hasta que el usuario la cierre.
     *
     * @param base        componente sin decorar
     * @param decorado    componente con la bateria adicional
     * @param drone       dron que viaja dentro de la cadena
     * @param descripcion descripcion de la bateria capturada
     * @param propietario ventana desde la que se invoco; admite {@code null}
     */
    public static void mostrar(Componente base, Componente decorado, Drone drone,
                               String descripcion, Window propietario) {

        String textoBase = base.descripcion();
        String textoDecorado = decorado.descripcion();
        String anadido = calcularAnadido(textoBase, textoDecorado);

        VBox contenido = new VBox(14);
        contenido.setPadding(new Insets(22));
        contenido.setFillWidth(true);

        contenido.getChildren().add(construirEncabezado());
        contenido.getChildren().add(construirPanelBase(textoBase));
        contenido.getChildren().add(construirFlecha(descripcion));
        contenido.getChildren().add(construirPanelDecorado(textoBase, anadido));
        contenido.getChildren().add(construirResaltado(anadido));
        contenido.getChildren().add(construirCadena(descripcion));
        contenido.getChildren().add(construirVerificaciones(textoBase, textoDecorado, base, decorado, drone));
        contenido.getChildren().add(construirPie());

        ScrollPane scroll = new ScrollPane(contenido);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background: #eef2f7; -fx-background-color: #eef2f7; "
                + "-fx-border-color: transparent;");

        Stage ventana = new Stage();
        ventana.setTitle("Patron Decorator - Bateria adicional");
        ventana.initModality(Modality.APPLICATION_MODAL);
        if (propietario != null) {
            ventana.initOwner(propietario);
        }

        Button btnCerrar = new Button("Cerrar");
        btnCerrar.setPrefWidth(150);
        btnCerrar.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 9 18 9 18;");
        btnCerrar.setOnAction(e -> ventana.close());

        HBox pieBoton = new HBox(btnCerrar);
        pieBoton.setAlignment(Pos.CENTER);
        pieBoton.setPadding(new Insets(12, 22, 16, 22));
        pieBoton.setStyle("-fx-background-color: #eef2f7;");

        BorderPane raiz = new BorderPane();
        raiz.setCenter(scroll);
        raiz.setBottom(pieBoton);
        raiz.setStyle("-fx-background-color: #eef2f7;");

        ventana.setScene(new Scene(raiz, 980, 760));
        ventana.setMinWidth(820);
        ventana.setMinHeight(600);
        ventana.showAndWait();
    }

    /**
     * Extrae el fragmento que el decorador agrego a la descripcion base.
     *
     * @param textoBase     descripcion sin decorar
     * @param textoDecorado descripcion con el decorador aplicado
     * @return el texto anadido, o la descripcion completa si no hubo prefijo comun
     */
    private static String calcularAnadido(String textoBase, String textoDecorado) {
        if (textoDecorado.startsWith(textoBase)) {
            return textoDecorado.substring(textoBase.length());
        }
        return textoDecorado;
    }

    /**
     * Construye el titulo y la explicacion breve de la ventana.
     *
     * @return el bloque de encabezado
     */
    private static VBox construirEncabezado() {
        Label titulo = new Label("Patron Decorator");
        titulo.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #1b2838;");

        Label subtitulo = new Label(
                "El decorador agrego la bateria adicional en tiempo de ejecucion, "
                        + "sin modificar la clase Drone ni la base de datos.");
        subtitulo.setWrapText(true);
        subtitulo.setMaxWidth(Double.MAX_VALUE);
        subtitulo.setStyle("-fx-font-size: 13px; -fx-text-fill: #5d6d7e;");

        VBox caja = new VBox(4, titulo, subtitulo);
        caja.setFillWidth(true);
        return caja;
    }

    /**
     * Construye la tarjeta con la descripcion sin decorar.
     *
     * @param textoBase resultado de la llamada al componente base
     * @return la tarjeta lista para insertar
     */
    private static VBox construirPanelBase(String textoBase) {
        Text fragmento = new Text(textoBase);
        fragmento.setStyle("-fx-fill: #1b2838; -fx-font-size: 13px;" + FUENTE_CODIGO);

        return construirTarjeta(
                "1.  SIN DECORAR      DroneWrapper.descripcion()",
                COLOR_BASE, new TextFlow(fragmento));
    }

    /**
     * Construye la tarjeta con la descripcion decorada, resaltando en
     * color el fragmento que agrego el decorador.
     *
     * @param textoBase parte heredada del componente envuelto
     * @param anadido   parte aportada por el decorador
     * @return la tarjeta lista para insertar
     */
    private static VBox construirPanelDecorado(String textoBase, String anadido) {
        Text heredado = new Text(textoBase);
        heredado.setStyle("-fx-fill: #1b2838; -fx-font-size: 13px;" + FUENTE_CODIGO);

        Text agregado = new Text(anadido);
        agregado.setStyle("-fx-fill: " + COLOR_DECORADO + "; -fx-font-size: 13px; "
                + "-fx-font-weight: bold;" + FUENTE_CODIGO);

        return construirTarjeta(
                "2.  DECORADO         BateriaAdicional.descripcion()",
                COLOR_DECORADO, new TextFlow(heredado, agregado));
    }

    /**
     * Arma una tarjeta blanca con encabezado de color y contenido envolvente.
     *
     * @param titulo    texto del encabezado
     * @param color     color de fondo del encabezado
     * @param contenido nodo de texto que se pinta en el cuerpo
     * @return la tarjeta completa
     */
    private static VBox construirTarjeta(String titulo, String color, TextFlow contenido) {
        Label cabecera = new Label(titulo);
        cabecera.setMaxWidth(Double.MAX_VALUE);
        cabecera.setPadding(new Insets(10, 15, 10, 15));
        cabecera.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; "
                + "-fx-background-radius: 10 10 0 0; -fx-font-size: 12px; "
                + "-fx-font-weight: bold;" + FUENTE_CODIGO);

        contenido.setLineSpacing(3);
        contenido.setMaxWidth(Double.MAX_VALUE);

        VBox cuerpo = new VBox(contenido);
        cuerpo.setPadding(new Insets(14, 16, 16, 16));
        cuerpo.setFillWidth(true);

        VBox tarjeta = new VBox(0, cabecera, cuerpo);
        tarjeta.setFillWidth(true);
        tarjeta.setMaxWidth(Double.MAX_VALUE);
        tarjeta.setStyle("-fx-background-color: white; -fx-background-radius: 10; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.14), 10, 0, 0, 3);");
        return tarjeta;
    }

    /**
     * Construye la flecha que indica el paso por el decorador.
     *
     * @param descripcion descripcion de la bateria capturada
     * @return el bloque intermedio entre las dos tarjetas
     */
    private static VBox construirFlecha(String descripcion) {
        Label flecha = new Label("\u2193");
        flecha.setStyle("-fx-font-size: 26px; -fx-text-fill: " + COLOR_DECORADO
                + "; -fx-font-weight: bold;");

        Label etiqueta = new Label("new BateriaAdicional(componente, \"" + descripcion + "\")");
        etiqueta.setWrapText(true);
        etiqueta.setMaxWidth(Double.MAX_VALUE);
        etiqueta.setAlignment(Pos.CENTER);
        etiqueta.setStyle("-fx-font-size: 12px; -fx-text-fill: #7d3c98;" + FUENTE_CODIGO);

        VBox caja = new VBox(2, flecha, etiqueta);
        caja.setAlignment(Pos.CENTER);
        caja.setFillWidth(true);
        return caja;
    }

    /**
     * Construye el recuadro que aisla el fragmento aportado por el decorador.
     *
     * @param anadido texto que agrego el decorador
     * @return el bloque de resaltado
     */
    private static VBox construirResaltado(String anadido) {
        Label titulo = new Label("Lo unico que agrego el decorador");
        titulo.setStyle("-fx-font-size: 11px; -fx-text-fill: #95a5a6;");

        Label texto = new Label(anadido.trim());
        texto.setWrapText(true);
        texto.setMaxWidth(Double.MAX_VALUE);
        texto.setStyle("-fx-font-size: 13px; -fx-text-fill: " + COLOR_DECORADO
                + "; -fx-font-weight: bold;" + FUENTE_CODIGO);

        VBox caja = new VBox(texto);
        caja.setPadding(new Insets(11, 15, 11, 15));
        caja.setFillWidth(true);
        caja.setStyle("-fx-background-color: #f4ecf7; -fx-background-radius: 6; "
                + "-fx-border-color: " + COLOR_DECORADO + "; -fx-border-width: 0 0 0 4; "
                + "-fx-border-radius: 6 0 0 6;");

        VBox bloque = new VBox(5, titulo, caja);
        bloque.setFillWidth(true);
        return bloque;
    }

    /**
     * Construye el bloque que muestra la cadena de objetos construida.
     *
     * @param descripcion descripcion de la bateria capturada
     * @return el bloque de la cadena de decoracion
     */
    private static VBox construirCadena(String descripcion) {
        Label titulo = new Label("Cadena de decoracion");
        titulo.setStyle("-fx-font-size: 11px; -fx-text-fill: #95a5a6;");

        VBox lineas = new VBox(2);
        lineas.getChildren().add(lineaCodigo("Componente c = new BateriaAdicional("));
        lineas.getChildren().add(lineaCodigo("                   new DroneWrapper(drone),"));
        lineas.getChildren().add(lineaCodigo("                   \"" + descripcion + "\");"));

        VBox cajaCodigo = new VBox(lineas);
        cajaCodigo.setPadding(new Insets(12, 15, 12, 15));
        cajaCodigo.setFillWidth(true);
        cajaCodigo.setStyle("-fx-background-color: #22272e; -fx-background-radius: 6;");

        VBox bloque = new VBox(5, titulo, cajaCodigo);
        bloque.setFillWidth(true);
        return bloque;
    }

    /**
     * Crea una linea individual del bloque de codigo.
     *
     * @param texto contenido de la linea
     * @return la etiqueta con el estilo de codigo aplicado
     */
    private static Label lineaCodigo(String texto) {
        Label linea = new Label(texto);
        linea.setWrapText(true);
        linea.setMaxWidth(Double.MAX_VALUE);
        linea.setStyle("-fx-text-fill: #7ee787; -fx-font-size: 12px;" + FUENTE_CODIGO);
        return linea;
    }

    /**
     * Construye el bloque de comprobaciones del patron.
     *
     * @param textoBase     descripcion sin decorar
     * @param textoDecorado descripcion decorada
     * @param base          componente sin decorar
     * @param decorado      componente con la bateria
     * @param drone         dron envuelto
     * @return el bloque de verificaciones
     */
    private static VBox construirVerificaciones(String textoBase, String textoDecorado,
                                                Componente base, Componente decorado,
                                                Drone drone) {
        Label titulo = new Label("Verificacion");
        titulo.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1b2838;");

        VBox lista = new VBox(8);
        lista.setFillWidth(true);
        lista.getChildren().add(filaVerificacion(
                textoDecorado.startsWith(textoBase),
                "El decorador delego en el componente: conserva toda la descripcion base"));
        lista.getChildren().add(filaVerificacion(
                textoDecorado.length() > textoBase.length(),
                "El decorador agrego informacion que el componente base no tenia"));
        lista.getChildren().add(filaVerificacion(
                base != decorado,
                "base != decorado  ->  el original no fue modificado, se envolvio"));
        lista.getChildren().add(filaVerificacion(
                drone != null && drone.getId() > 0,
                "El drone si quedo guardado en la base de datos (id="
                        + (drone == null ? "?" : drone.getId()) + ")"));
        lista.getChildren().add(filaVerificacion(true,
                "La bateria adicional NO se guardo: la tabla dron no tiene columna para ella"));

        VBox caja = new VBox(11, titulo, lista);
        caja.setPadding(new Insets(16, 18, 16, 18));
        caja.setFillWidth(true);
        caja.setMaxWidth(Double.MAX_VALUE);
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
        marca.setMinWidth(22);
        marca.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: "
                + (cumple ? "#1e8449" : "#c0392b") + ";");

        Label texto = new Label(descripcion);
        texto.setWrapText(true);
        texto.setMaxWidth(Double.MAX_VALUE);
        texto.setStyle("-fx-font-size: 12px; -fx-text-fill: #34495e;" + FUENTE_CODIGO);
        HBox.setHgrow(texto, Priority.ALWAYS);

        HBox fila = new HBox(9, marca, texto);
        fila.setAlignment(Pos.TOP_LEFT);
        return fila;
    }

    /**
     * Construye la nota final que explica el alcance de la operacion.
     *
     * @return la etiqueta con la nota
     */
    private static Label construirPie() {
        Label nota = new Label(
                "Nota: la bateria adicional es una responsabilidad anadida en tiempo de "
                        + "ejecucion. Vive unicamente en el objeto Componente que se acaba de "
                        + "construir y desaparece al cerrar esta ventana. El drone si quedo "
                        + "registrado, con sus atributos normales.");
        nota.setWrapText(true);
        nota.setMaxWidth(Double.MAX_VALUE);
        nota.setStyle("-fx-font-size: 11px; -fx-text-fill: #7f8c8d; -fx-font-style: italic;");
        return nota;
    }
}