package co.edu.poli.sw2.modelo;

/**
 * Dron especializado en vigilancia. Indica si cuenta con
 * capacidad de deteccion termica.
 */
public class Vigilancia extends Drone {

    private static final long serialVersionUID = 1L;

    private boolean deteccionTermica;

    public Vigilancia(int id, String serial, String fabricante, String modelo,
                      double peso, boolean deteccionTermica) {
        super(id, serial, fabricante, modelo, peso);
        this.deteccionTermica = deteccionTermica;
    }

    public boolean isDeteccionTermica() { return deteccionTermica; }
    public void setDeteccionTermica(boolean deteccionTermica) { this.deteccionTermica = deteccionTermica; }

    @Override
    public String getTipo() { return "VIGILANCIA"; }
}