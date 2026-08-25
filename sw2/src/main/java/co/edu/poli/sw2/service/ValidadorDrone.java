package co.edu.poli.sw2.service;

import co.edu.poli.sw2.exception.DronValidacionException;
import java.util.regex.Pattern;

/**
 * Reglas de validacion de los datos de un dron.
 * <p>
 * Centraliza las validaciones de negocio que se aplican antes de enviar
 * datos a la base de datos. Cada metodo lanza
 * {@link DronValidacionException} con un mensaje entendible para el
 * usuario final, que el controlador muestra en la interfaz.
 * <p>
 * Es una clase de utilidad: constructor privado y metodos estaticos.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see DroneService
 */
public final class ValidadorDrone {

    /** Detecta la presencia de al menos un caracter alfanumerico, incluyendo acentos. */
    private static final Pattern TIENE_ALFANUMERICO = Pattern.compile("[A-Za-zA-y0-9]");

    /** Detecta cadenas compuestas unicamente por digitos, con decimal opcional. */
    private static final Pattern SOLO_NUMERICO = Pattern.compile("^\\s*[0-9]+([.,][0-9]+)?\\s*$");

    /** Constructor privado: la clase solo expone metodos estaticos. */
    private ValidadorDrone() {}

    /**
     * Valida el serial de un dron.
     * <p>
     * Exige que no este vacio y que contenga al menos un caracter
     * alfanumerico, para evitar seriales compuestos solo por simbolos.
     *
     * @param serial serial capturado en el formulario
     * @throws DronValidacionException si esta vacio o solo tiene caracteres especiales
     */
    public static void validarSerial(String serial) {
        if (serial == null || serial.isBlank()) {
            throw new DronValidacionException("El serial es obligatorio.");
        }
        if (!TIENE_ALFANUMERICO.matcher(serial).find()) {
            throw new DronValidacionException("El serial no puede contener unicamente caracteres especiales.");
        }
    }

    /**
     * Valida el fabricante de un dron.
     * <p>
     * Ademas de exigir contenido alfanumerico, rechaza valores puramente
     * numericos, ya que un nombre de empresa no puede ser solo un numero.
     *
     * @param fabricante fabricante capturado en el formulario
     * @throws DronValidacionException si esta vacio, solo tiene simbolos o es solo numerico
     */
    public static void validarFabricante(String fabricante) {
        if (fabricante == null || fabricante.isBlank()) {
            throw new DronValidacionException("El fabricante es obligatorio.");
        }
        if (!TIENE_ALFANUMERICO.matcher(fabricante).find()) {
            throw new DronValidacionException("El fabricante no puede contener unicamente caracteres especiales.");
        }
        if (SOLO_NUMERICO.matcher(fabricante).matches()) {
            throw new DronValidacionException("El fabricante no puede ser puramente numerico (ej: '33').");
        }
    }

    /**
     * Convierte y valida el peso capturado como texto.
     * <p>
     * Acepta tanto punto como coma decimal, para no obligar al usuario a
     * cambiar su forma habitual de escribir numeros.
     *
     * @param pesoTexto peso tal como se escribio en el formulario
     * @return el peso convertido a numero
     * @throws DronValidacionException si esta vacio, no es numerico o no es mayor que cero
     */
    public static double parsearPeso(String pesoTexto) {
        if (pesoTexto == null || pesoTexto.isBlank()) {
            throw new DronValidacionException("El peso es obligatorio.");
        }
        double peso;
        try {
            peso = Double.parseDouble(pesoTexto.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new DronValidacionException("El peso debe ser un numero valido (ej: 0.9).");
        }
        if (peso <= 0) {
            throw new DronValidacionException("El peso debe ser mayor que 0.");
        }
        return peso;
    }

    /**
     * Convierte y valida la capacidad del tanque capturada como texto.
     * <p>
     * Solo aplica a los drones de agricultura.
     *
     * @param texto capacidad tal como se escribio en el formulario
     * @return la capacidad convertida a numero
     * @throws DronValidacionException si esta vacia, no es numerica o no es mayor que cero
     */
    public static double parsearCapacidadTanque(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new DronValidacionException("La capacidad del tanque es obligatoria para un dron de agricultura.");
        }
        double capacidad;
        try {
            capacidad = Double.parseDouble(texto.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new DronValidacionException("La capacidad del tanque debe ser un numero valido (ej: 10.5).");
        }
        if (capacidad <= 0) {
            throw new DronValidacionException("La capacidad del tanque debe ser mayor que 0.");
        }
        return capacidad;
    }
}