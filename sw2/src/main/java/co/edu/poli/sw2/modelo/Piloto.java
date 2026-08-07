package co.edu.poli.sw2.modelo;

public class Piloto {

    private int id;
    private String nombre;
    private int experiencia;
    private String telefono;

    public Piloto(int id, String nombre, int experiencia, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.experiencia = experiencia;
        this.telefono = telefono;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getExperiencia() { return experiencia; }
    public String getTelefono() { return telefono; }

    @Override
    public String toString() {
        return nombre + " (Exp: " + experiencia + " años)";
    }
}