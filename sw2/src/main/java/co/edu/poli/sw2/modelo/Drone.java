package co.edu.poli.sw2.modelo;

import java.util.ArrayList;
import java.util.List;

public class Drone {

    private int id;
    private String serial;
    private String fabricante;
    private double peso;

    // Relacion 1 a 1: un dron tiene un unico piloto
    private Piloto piloto;

    // Relacion muchos a muchos: un dron puede tener varios sensores
    private List<Sensor> sensores = new ArrayList<>();

    public Drone(int id, String serial, String fabricante, double peso, Piloto piloto) {
        this.id = id;
        this.serial = serial;
        this.fabricante = fabricante;
        this.peso = peso;
        this.piloto = piloto;
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

    public Piloto getPiloto() { return piloto; }
    public void setPiloto(Piloto piloto) { this.piloto = piloto; }

    public List<Sensor> getSensores() { return sensores; }
    public void setSensores(List<Sensor> nuevos) {
        this.sensores = new ArrayList<>(nuevos);
    }

    public String getPilotoNombre() {
        return piloto != null ? piloto.getNombre() : "Sin asignar";
    }

    public String getSensoresTexto() {
        if (sensores.isEmpty()) return "Ninguno";
        StringBuilder sb = new StringBuilder();
        for (Sensor s : sensores) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(s.getTipo());
        }
        return sb.toString();
    }
}