package co.edu.poli.sw2.modelo;


import java.io.Serializable;

public class Drone implements Serializable {
    //private static final long serialVersionUID = 1L;
    
    private int id;
    private String serial;
    private String fabricante;
    private double peso;

    public Drone(int id, String serial, String fabricante, double peso) {
        this.id = id;
        this.serial = serial;
        this.fabricante = fabricante;
        this.peso = peso;
    }

    // ---- Getters / Setters normales ----
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSerial() { return serial; }
    public void setSerial(String serial) { this.serial = serial; }

    public String getFabricante() { return fabricante; }
    public void setFabricante(String fabricante) { this.fabricante = fabricante; }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
    
    private static final long serialVersionUID = 2L; // se incrementa: cambio de forma serializada
}