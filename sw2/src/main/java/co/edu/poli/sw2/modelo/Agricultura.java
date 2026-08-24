package co.edu.poli.sw2.modelo;

/**
 * Dron especializado en labores agricolas. Agrega la capacidad
 * del tanque de fumigacion.
 */
public class Agricultura extends Drone {

    private static final long serialVersionUID = 1L;

    private double capacidadTanque;

    public Agricultura(int id, String serial, String fabricante, String modelo,
                       double peso, double capacidadTanque) {
        super(id, serial, fabricante, modelo, peso);
        this.capacidadTanque = capacidadTanque;
    }

    public double getCapacidadTanque() { return capacidadTanque; }
    public void setCapacidadTanque(double capacidadTanque) { this.capacidadTanque = capacidadTanque; }

    @Override
    public String getTipo() { return "AGRICULTURA"; }
}