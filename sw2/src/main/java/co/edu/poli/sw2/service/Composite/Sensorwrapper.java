package co.edu.poli.sw2.service.Composite;
 
/**
 * Envoltorio (wrapper) del patron <b>Composite</b>.
 * <p>
 * Adapta cualquier {@link Sensor} -ya sea una hoja individual descrita
 * con una expresion lambda, o un {@link SensorComposite} completo- a un
 * objeto uniforme que {@link SensorComposite} puede almacenar sin
 * depender directamente de la interfaz {@link Sensor}: toda
 * incorporacion al arbol pasa primero por un {@code SensorWrapper}.
 * <p>
 * Esta indireccion es justamente lo que permite anidar composites dentro
 * de otros composites: como {@link SensorComposite} tambien implementa
 * {@link Sensor}, basta con envolver un {@code SensorComposite} en un
 * {@code SensorWrapper} para tratarlo como una hoja mas dentro de otro
 * composite (ver {@link #desde(co.edu.poli.sw2.modelo.Sensor)} para
 * envolver ademas un sensor real leido de la base de datos).
 *
 * @author Alejandra Cano y Juan Rosero
 * @see Sensor
 * @see SensorComposite
 */
public class Sensorwrapper implements Sensor {
 
    /** Sensor envuelto: puede ser una hoja o un {@link SensorComposite}. */
    private Sensor sensor;
 
    /**
     * Envuelve el sensor indicado.
     *
     * @param sensor sensor (hoja o composite) a envolver; no deberia ser {@code null}
     */
    public Sensorwrapper(Sensor sensor) {
        this.sensor = sensor;
    }
 
    /**
     * Devuelve el sensor envuelto, sin pasar por la delegacion de
     * {@link #descripcion()}.
     * <p>
     * Lo usa por ejemplo la capa de presentacion para recorrer el arbol
     * y distinguir si un nodo es una hoja o un {@link SensorComposite}
     * anidado, sin tener que interpretar el texto de la descripcion.
     *
     * @return el sensor (hoja o composite) envuelto
     */
    public Sensor getSensor() {
        return sensor;
    }
 
    /**
     * {@inheritDoc}
     *
     * @return la descripcion del sensor envuelto
     */
    @Override
    public String descripcion() {
        return sensor.descripcion();
    }
 
    /**
     * Adapta un sensor real, leido por ejemplo desde
     * {@link co.edu.poli.sw2.dao.CatalogoRepositorio#listarSensores()}, a
     * este patron, sin que {@link co.edu.poli.sw2.modelo.Sensor} tenga
     * que conocer ni implementar la interfaz {@link Sensor}.
     *
     * @param sensorBD sensor del modelo de datos a envolver
     * @return un {@code SensorWrapper} cuya descripcion delega en
     *         {@code sensorBD.toString()}
     */
    public static Sensorwrapper desde(co.edu.poli.sw2.modelo.Sensor sensorBD) {
        return new Sensorwrapper(sensorBD::toString);
    }
}