
package co.edu.poli.sw2.service;

import co.edu.poli.sw2.exception.DronValidacionException;
import java.util.regex.Pattern;

public final class ValidadorDrone {

    private static final Pattern TIENE_ALFANUMERICO = Pattern.compile("[A-Za-zÀ-ÿ0-9]");
    private static final Pattern SOLO_NUMERICO = Pattern.compile("^\\s*[0-9]+([.,][0-9]+)?\\s*$");

    private ValidadorDrone() {}

    public static void validarSerial(String serial) {
        if (serial == null || serial.isBlank()) {
            throw new DronValidacionException("El serial es obligatorio.");
        }
        if (!TIENE_ALFANUMERICO.matcher(serial).find()) {
            throw new DronValidacionException("El serial no puede contener unicamente caracteres especiales.");
        }
    }

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
}