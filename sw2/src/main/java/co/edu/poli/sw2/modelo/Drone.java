package co.edu.poli.sw2.modelo;

import java.io.Serializable;

/**
 * Entidad abstracta que representa un dron. Define los atributos comunes
 * a todos los tipos de dron; las subclases concretas ({@link Agricultura},
 * {@link Vigilancia}) agregan sus atributos especificos.
 */
public abstract class Drone implements Serializable {

    private static final long serialVersionUID = 3L;

    private int id;
    private String serial;
    private String fabricante;
    private String modelo;
    private double peso;

    protected Drone(int id, String serial, String fabricante, String modelo, double peso) {
        this.id = id;
        this.serial = serial;
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.peso = peso;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSerial() { return serial; }
    public void setSerial(String serial) { this.serial = serial; }

    public String getFabricante() { return fabricante; }
    public void setFabricante(String fabricante) { this.fabricante = fabricante; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    /** Discriminador usado por la capa DAO y por la vista. */
    public abstract String getTipo();
}