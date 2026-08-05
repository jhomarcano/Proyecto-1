package co.edu.poli.sw2.modelo;

public class Mision {

    private int id;
    private String nombre;
    private String ubicacion;
    private String fecha;

    public Mision(int id, String nombre, String ubicacion, String fecha) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getUbicacion() { return ubicacion; }
    public String getFecha() { return fecha; }

    @Override
    public String toString() {
        return nombre + " (" + ubicacion + ")";
    }
}
