package co.edu.poli.sw2.modelo;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Drone {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty senal = new SimpleStringProperty();
    private final StringProperty modelo = new SimpleStringProperty();
    private final StringProperty fabricante = new SimpleStringProperty();
    private final DoubleProperty peso = new SimpleDoubleProperty();

    // Relacion 1 a 1: un dron tiene un unico piloto
    private final ObjectProperty<Piloto> piloto = new SimpleObjectProperty<>();

    // Relacion 1 a muchos: un dron pertenece a una mision (una mision -> muchos drones)
    private final ObjectProperty<Mision> mision = new SimpleObjectProperty<>();

    // Relacion muchos a muchos: un dron puede tener varios sensores
    private final ObservableList<Sensor> sensores = FXCollections.observableArrayList();

    public Drone(int id, String senal, String modelo, String fabricante, double peso,
                 Piloto piloto, Mision mision) {
        this.id.set(id);
        this.senal.set(senal);
        this.modelo.set(modelo);
        this.fabricante.set(fabricante);
        this.peso.set(peso);
        this.piloto.set(piloto);
        this.mision.set(mision);
    }

    // ---- Propiedades (para PropertyValueFactory de la TableView) ----
    public IntegerProperty idProperty() { return id; }
    public StringProperty senalProperty() { return senal; }
    public StringProperty modeloProperty() { return modelo; }
    public StringProperty fabricanteProperty() { return fabricante; }
    public DoubleProperty pesoProperty() { return peso; }
    public ObjectProperty<Piloto> pilotoProperty() { return piloto; }
    public ObjectProperty<Mision> misionProperty() { return mision; }

    // ---- Getters / Setters normales ----
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }

    public String getSenal() { return senal.get(); }
    public void setSenal(String senal) { this.senal.set(senal); }

    public String getModelo() { return modelo.get(); }
    public void setModelo(String modelo) { this.modelo.set(modelo); }

    public String getFabricante() { return fabricante.get(); }
    public void setFabricante(String fabricante) { this.fabricante.set(fabricante); }

    public double getPeso() { return peso.get(); }
    public void setPeso(double peso) { this.peso.set(peso); }

    public Piloto getPiloto() { return piloto.get(); }
    public void setPiloto(Piloto piloto) { this.piloto.set(piloto); }

    public Mision getMision() { return mision.get(); }
    public void setMision(Mision mision) { this.mision.set(mision); }

    public ObservableList<Sensor> getSensores() { return sensores; }
    public void setSensores(ObservableList<Sensor> nuevos) {
        sensores.setAll(nuevos);
    }

    public String getPilotoNombre() {
        return piloto.get() != null ? piloto.get().getNombre() : "Sin asignar";
    }

    public String getMisionNombre() {
        return mision.get() != null ? mision.get().getNombre() : "Sin asignar";
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
