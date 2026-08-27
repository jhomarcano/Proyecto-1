package co.edu.poli.sw2.service;

import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Vigilancia;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Genera drones de vigilancia aleatorios usando como base los drones de
 * vigilancia ya registrados en la base de datos.
 * <p>
 * Toma el fabricante y el modelo de un registro existente elegido al
 * azar, y calcula el peso dentro del rango minimo-maximo observado en el
 * historial. Si todavia no hay drones de vigilancia registrados, recurre
 * a valores por defecto razonables para no bloquear la generacion.
 * <p>
 * Se apoya en {@link VigilanciaBuilder} porque los atributos del dron se
 * van determinando de forma incremental a medida que se muestrean los
 * datos historicos.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see VigilanciaBuilder
 */
public class VigilanciaGeneradorAleatorio {

    /** Fabricantes usados cuando no hay historial de vigilancia en la BD. */
    private static final String[] FABRICANTES_POR_DEFECTO = {"DJI", "Autel", "Parrot", "Skydio"};

    /** Modelos usados cuando no hay historial de vigilancia en la BD. */
    private static final String[] MODELOS_POR_DEFECTO = {"EVO II", "Mavic 3", "Anafi", "X10"};

    /** Peso minimo por defecto, en kilogramos, si no hay historial. */
    private static final double PESO_MINIMO_POR_DEFECTO = 0.3;

    /** Peso maximo por defecto, en kilogramos, si no hay historial. */
    private static final double PESO_MAXIMO_POR_DEFECTO = 5.0;

    /** Generador de numeros aleatorios reutilizado en cada llamada. */
    private final Random azar = new Random();

    /**
     * Construye un dron de vigilancia con datos aleatorios plausibles.
     *
     * @param dronesExistentes drones ya registrados en el sistema, de cualquier tipo
     * @return un dron de vigilancia nuevo, con id en 0, aun no persistido
     */
    public Vigilancia generar(List<Drone> dronesExistentes) {
        List<Vigilancia> previas = dronesExistentes.stream()
                .filter(Vigilancia.class::isInstance)
                .map(Vigilancia.class::cast)
                .collect(Collectors.toList());

        return new VigilanciaBuilder()
                .conSerial(generarSerial())
                .conFabricante(elegirFabricante(previas))
                .conModelo(elegirModelo(previas))
                .conPeso(elegirPeso(previas))
                .conDeteccionTermica(azar.nextBoolean())
                .build();
    }

    /**
     * Elige un fabricante tomado de un registro previo, o uno por defecto si no hay historial.
     *
     * @param previas drones de vigilancia ya registrados
     * @return el fabricante elegido
     */
    private String elegirFabricante(List<Vigilancia> previas) {
        if (previas.isEmpty()) {
            return FABRICANTES_POR_DEFECTO[azar.nextInt(FABRICANTES_POR_DEFECTO.length)];
        }
        return previas.get(azar.nextInt(previas.size())).getFabricante();
    }

    /**
     * Elige un modelo tomado de un registro previo, o uno por defecto si no hay historial.
     *
     * @param previas drones de vigilancia ya registrados
     * @return el modelo elegido
     */
    private String elegirModelo(List<Vigilancia> previas) {
        if (previas.isEmpty()) {
            return MODELOS_POR_DEFECTO[azar.nextInt(MODELOS_POR_DEFECTO.length)];
        }
        return previas.get(azar.nextInt(previas.size())).getModelo();
    }

    /**
     * Calcula un peso aleatorio dentro del rango observado en el historial.
     * <p>
     * Si solo hay un valor de peso registrado (minimo igual al maximo), se
     * amplia el rango en un kilogramo para no devolver siempre el mismo valor.
     *
     * @param previas drones de vigilancia ya registrados
     * @return el peso elegido, redondeado a dos decimales
     */
    private double elegirPeso(List<Vigilancia> previas) {
        double minimo = previas.stream().mapToDouble(Vigilancia::getPeso).min().orElse(PESO_MINIMO_POR_DEFECTO);
        double maximo = previas.stream().mapToDouble(Vigilancia::getPeso).max().orElse(PESO_MAXIMO_POR_DEFECTO);
        if (maximo <= minimo) {
            maximo = minimo + 1.0;
        }
        double peso = minimo + azar.nextDouble() * (maximo - minimo);
        return Math.round(peso * 100.0) / 100.0;
    }

    /**
     * Genera un serial unico con prefijo identificable, para distinguir
     * facilmente los drones creados por generacion aleatoria.
     *
     * @return un serial en formato {@code VIG-XXXXXX}
     */
    private String generarSerial() {
        return "VIG-" + Integer.toHexString(azar.nextInt(0xFFFFFF)).toUpperCase();
    }
}