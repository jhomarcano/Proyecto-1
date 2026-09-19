package co.edu.poli.sw2.controlador;
 
import co.edu.poli.sw2.service.Composite.Sensor;
import co.edu.poli.sw2.service.Composite.Sensorcomposite;
import co.edu.poli.sw2.service.Composite.Sensorwrapper;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
 
/**
 * Ventana que dibuja como organigrama el arbol construido con el patron
 * <b>Composite</b> de {@link co.edu.poli.sw2.service.composite}.
 * <p>
 * Recorre el {@link SensorComposite} recibido apoyandose unicamente en
 * {@link SensorWrapper#getSensor()}: nunca asume que un nodo es hoja o
 * agrupacion salvo por su tipo real ({@code instanceof SensorComposite}),
 * lo que permite dibujar arboles de cualquier profundidad, no solo el de
 * la demostracion.
 * <p>
 * El calculo de posiciones se hace en dos pasadas: primero
 * {@link #anchoUnidades(Sensor)} determina cuantas "columnas" ocupa cada
 * subarbol (una hoja ocupa 1, una agrupacion la suma de sus hijos), y
 * luego {@link #diagramar} recorre el arbol asignando coordenadas en
 * pixeles y dibujando las cajas y los conectores en angulo recto propios
 * de un organigrama.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see co.edu.poli.sw2.service.composite.SensorComposite
 * @see co.edu.poli.sw2.service.composite.SensorCompositeDemo
 */
public final class VentanaSensorComposite {
 
    /** Ancho en pixeles reservado por cada unidad de columna. */
    private static final double ANCHO_UNIDAD = 180;
 
    /** Ancho fijo de cada caja. */
    private static final double ANCHO_CAJA = 150;
 
    /** Alto fijo de cada caja. */
    private static final double ALTO_CAJA = 50;
 
    /** Separacion vertical entre un nivel del arbol y el siguiente. */
    private static final double ALTO_NIVEL = 110;
 
    /** Margen alrededor de todo el dibujo. */
    private static final double MARGEN = 30;
 
    /** Color de la caja raiz ("Sensor General"). */
    private static final String COLOR_RAIZ = "#5dade2";
 
    /** Color de las hojas que quedan a tres o mas niveles de la raiz (ej. SPI, UART). */
    private static final String COLOR_HOJA_PROFUNDA = "#aed6f1";
 
    /** Paleta que se asigna, en orden, a cada rama de primer nivel (Temperatura, Camara, Sonido...). */
    private static final String[] PALETA_RAMA = {"#a9dfbf", "#f8c471", "#d2b4de", "#7dcea0"};
 
    /** Color de las lineas conectoras. */
    private static final Color COLOR_LINEA = Color.web("#2c3e50");
 
    /** Impide instanciar esta clase de utilidad. */
    private VentanaSensorComposite() {
    }
 
    /**
     * Abre la ventana con el organigrama del arbol de sensores y bloquea
     * la ventana principal hasta que el usuario la cierre.
     *
     * @param raiz        raiz del arbol construido con el Composite (por
     *                    ejemplo, {@code SensorCompositeDemo.construirArbolSensores()})
     * @param propietario ventana desde la que se invoco; admite {@code null}
     */
    public static void mostrar(Sensorcomposite raiz, Window propietario) {
        double ancho = anchoUnidades(raiz) * ANCHO_UNIDAD + MARGEN * 2;
        double alto = (profundidadMaxima(raiz) + 1) * ALTO_NIVEL + MARGEN * 2;
 
        Pane lienzo = new Pane();
        lienzo.setPrefSize(ancho, alto);
        lienzo.setStyle("-fx-background-color: white;");
 
        diagramar(raiz, 0, 0, COLOR_RAIZ, lienzo);
 
        ScrollPane scroll = new ScrollPane(lienzo);
        scroll.setPannable(true);
        scroll.setFitToWidth(ancho < 900);
        scroll.setFitToHeight(alto < 620);
        scroll.setStyle("-fx-background: white; -fx-background-color: white;");
 
        Stage ventana = new Stage();
        ventana.setTitle("Patron Composite - Jerarquia de sensores");
        ventana.initModality(Modality.APPLICATION_MODAL);
        if (propietario != null) {
            ventana.initOwner(propietario);
        }
        ventana.setScene(new Scene(scroll, Math.min(ancho, 950), Math.min(alto, 650)));
        ventana.showAndWait();
    }
 
    /**
     * Dibuja un nodo del arbol y, recursivamente, todos sus descendientes.
     *
     * @param nodo             sensor (hoja o composite) a dibujar
     * @param xInicioUnidades  columna, en unidades, donde empieza el subarbol de este nodo
     * @param profundidad      nivel del arbol; 0 es la raiz
     * @param colorRama        color asignado a la rama de primer nivel a la que pertenece este nodo
     * @param lienzo           panel donde se agregan las cajas y las lineas
     */
    private static void diagramar(Sensor nodo, double xInicioUnidades, int profundidad,
                                  String colorRama, Pane lienzo) {
        double ancho = anchoUnidades(nodo);
        double xCentroPx = (xInicioUnidades + ancho / 2.0) * ANCHO_UNIDAD;
        double yPx = MARGEN + profundidad * ALTO_NIVEL;
        boolean esRaiz = profundidad == 0;
 
        String color = esRaiz ? COLOR_RAIZ
                : profundidad >= 3 ? COLOR_HOJA_PROFUNDA
                : colorRama;
 
        dibujarCaja(lienzo, xCentroPx, yPx, nombreDe(nodo), esRaiz, color);
 
        List<Sensorwrapper> hijos = hijosDe(nodo);
        if (hijos.isEmpty()) {
            return;
        }
 
        double cursorUnidades = xInicioUnidades;
        List<Double> centrosHijosPx = new ArrayList<>();
        for (int i = 0; i < hijos.size(); i++) {
            Sensor hijo = hijos.get(i).getSensor();
            String colorRamaHijo = esRaiz ? PALETA_RAMA[i % PALETA_RAMA.length] : colorRama;
            double anchoHijo = anchoUnidades(hijo);
 
            diagramar(hijo, cursorUnidades, profundidad + 1, colorRamaHijo, lienzo);
 
            centrosHijosPx.add((cursorUnidades + anchoHijo / 2.0) * ANCHO_UNIDAD);
            cursorUnidades += anchoHijo;
        }
        dibujarConector(lienzo, xCentroPx, yPx + ALTO_CAJA, centrosHijosPx, yPx + ALTO_NIVEL);
    }
 
    /**
     * Crea y posiciona la caja que representa un nodo del arbol.
     *
     * @param lienzo   panel donde se agrega la caja
     * @param xCentro  posicion horizontal, en pixeles, del centro de la caja
     * @param yArriba  posicion vertical, en pixeles, del borde superior de la caja
     * @param texto    nombre o descripcion que se muestra dentro de la caja
     * @param esRaiz   {@code true} si es la caja raiz, para darle un estilo distinto
     * @param color    color de fondo de la caja, en formato {@code #RRGGBB}
     */
    private static void dibujarCaja(Pane lienzo, double xCentro, double yArriba,
                                    String texto, boolean esRaiz, String color) {
        Label etiqueta = new Label(texto);
        etiqueta.setWrapText(true);
        etiqueta.setAlignment(Pos.CENTER);
        etiqueta.setTextAlignment(TextAlignment.CENTER);
        etiqueta.setPrefSize(ANCHO_CAJA, ALTO_CAJA);
        etiqueta.setMinSize(ANCHO_CAJA, ALTO_CAJA);
        etiqueta.setMaxSize(ANCHO_CAJA, ALTO_CAJA);
        etiqueta.setLayoutX(xCentro - ANCHO_CAJA / 2.0);
        etiqueta.setLayoutY(yArriba);
 
        String estilo = String.format(
                "-fx-background-color: %s; -fx-background-radius: 10; "
                        + "-fx-border-color: derive(%s, -25%%); -fx-border-width: 1.5; "
                        + "-fx-border-radius: 10; -fx-alignment: center; -fx-padding: 6;",
                color, color)
                + (esRaiz
                        ? " -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;"
                        : " -fx-text-fill: #1b2838; -fx-font-size: 12px; -fx-font-weight: bold;");
        etiqueta.setStyle(estilo);
 
        lienzo.getChildren().add(etiqueta);
    }
 
    /**
     * Dibuja el conector en angulo recto entre un nodo y sus hijos: un
     * tramo vertical que baja del padre, un tramo horizontal que abarca
     * a todos los hijos y un tramo vertical hacia cada uno de ellos.
     *
     * @param lienzo         panel donde se agregan las lineas
     * @param xPadre         centro horizontal, en pixeles, del nodo padre
     * @param yPadreAbajo    borde inferior, en pixeles, de la caja del padre
     * @param centrosHijos   centros horizontales, en pixeles, de cada hijo
     * @param yHijosArriba   borde superior, en pixeles, de las cajas de los hijos
     */
    private static void dibujarConector(Pane lienzo, double xPadre, double yPadreAbajo,
                                        List<Double> centrosHijos, double yHijosArriba) {
        double yMedio = (yPadreAbajo + yHijosArriba) / 2.0;
 
        lienzo.getChildren().add(nuevaLinea(xPadre, yPadreAbajo, xPadre, yMedio));
 
        double xMin = Collections.min(centrosHijos);
        double xMax = Collections.max(centrosHijos);
        lienzo.getChildren().add(nuevaLinea(xMin, yMedio, xMax, yMedio));
 
        for (double xHijo : centrosHijos) {
            lienzo.getChildren().add(nuevaLinea(xHijo, yMedio, xHijo, yHijosArriba));
        }
    }
 
    /**
     * Crea una linea con el color y grosor usados en todo el diagrama.
     *
     * @param x1 coordenada x del punto inicial
     * @param y1 coordenada y del punto inicial
     * @param x2 coordenada x del punto final
     * @param y2 coordenada y del punto final
     * @return la linea lista para agregarse al panel
     */
    private static Line nuevaLinea(double x1, double y1, double x2, double y2) {
        Line linea = new Line(x1, y1, x2, y2);
        linea.setStroke(COLOR_LINEA);
        linea.setStrokeWidth(1.6);
        return linea;
    }
 
    /**
     * Calcula cuantas columnas ocupa el subarbol de un nodo: una hoja
     * ocupa 1 columna; una agrupacion ocupa la suma de las columnas de
     * sus hijos.
     *
     * @param nodo sensor (hoja o composite) a medir
     * @return el ancho del subarbol, en unidades de columna
     */
    private static double anchoUnidades(Sensor nodo) {
        List<Sensorwrapper> hijos = hijosDe(nodo);
        if (hijos.isEmpty()) {
            return 1;
        }
        double total = 0;
        for (Sensorwrapper hijo : hijos) {
            total += anchoUnidades(hijo.getSensor());
        }
        return Math.max(total, 1);
    }
 
    /**
     * Calcula la profundidad maxima del subarbol de un nodo.
     *
     * @param nodo sensor (hoja o composite) a medir
     * @return 0 si es una hoja; en otro caso, 1 mas la profundidad maxima de sus hijos
     */
    private static int profundidadMaxima(Sensor nodo) {
        List<Sensorwrapper> hijos = hijosDe(nodo);
        if (hijos.isEmpty()) {
            return 0;
        }
        int maximo = 0;
        for (Sensorwrapper hijo : hijos) {
            maximo = Math.max(maximo, profundidadMaxima(hijo.getSensor()));
        }
        return 1 + maximo;
    }
 
    /**
     * Devuelve el texto que se muestra en la caja de un nodo: el nombre
     * de la agrupacion si es un {@link SensorComposite}, o su propia
     * descripcion si es una hoja.
     *
     * @param nodo sensor (hoja o composite) a describir
     * @return el texto a mostrar en la caja
     */
    private static String nombreDe(Sensor nodo) {
        if (nodo instanceof Sensorcomposite) {
            return ((Sensorcomposite) nodo).getNombre();
        }
        return nodo.descripcion();
    }
 
    /**
     * Devuelve los hijos directos de un nodo, ya envueltos.
     *
     * @param nodo sensor (hoja o composite) a inspeccionar
     * @return los hijos del composite, o una lista vacia si es una hoja
     */
    private static List<Sensorwrapper> hijosDe(Sensor nodo) {
        if (nodo instanceof Sensorcomposite) {
            return ((Sensorcomposite) nodo).getSensores();
        }
        return Collections.emptyList();
    }
}
 