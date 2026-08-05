package co.edu.poli.sw2.modelo;

public class Sensor {

    private int id;
    private String tipo;
    private String fabricante;

    public Sensor(int id, String tipo, String fabricante) {
        this.id = id;
        this.tipo = tipo;
        this.fabricante = fabricante;
    }

    public int getId() { return id; }
    public String getTipo() { return tipo; }
    public String getFabricante() { return fabricante; }

    @Override
    public String toString() {
        return tipo + " - " + fabricante;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sensor)) return false;
        return id == ((Sensor) o).id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
