package co.edu.poli.sw2.service;

import co.edu.poli.sw2.exception.DronValidacionException;
import co.edu.poli.sw2.modelo.Agricultura;
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Vigilancia;

/**
 * Implementacion del patron <b>Prototype</b> como servicio externo.
 * <p>
 * Produce una copia independiente de un dron existente sin que las
 * clases del paquete modelo tengan que participar: {@link Drone} y sus
 * subclases siguen siendo entidades puras, sin metodos de clonacion ni
 * dependencias hacia esta capa. Toda la responsabilidad de duplicar vive
 * aqui, de modo que el modelo permanece desacoplado del patron.
 * <p>
 * Para ensamblar la copia no se escribe logica de construccion nueva:
 * se reutilizan las piezas que ya existen en el proyecto,
 * {@link VigilanciaBuilder} para los drones de vigilancia y
 * {@link AgriculturaFactory} para los de agricultura. El prototipo
 * aporta el <i>que</i> copiar; esas clases siguen aportando el
 * <i>como</i> construir.
 * <p>
 * Diferencia con {@link DroneFactory}: la factoria construye un dron a
 * partir de datos sueltos capturados en el formulario; el prototipo lo
 * construye a partir de otro dron que ya existe en memoria.
 * <p>
 * La copia <b>nunca se persiste</b>: se devuelve con el id en 0 para
 * dejar claro que no corresponde a ninguna fila de la tabla
 * {@code dron}. Esta clase no conoce el DAO.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see VigilanciaBuilder
 * @see AgriculturaFactory
 */
public class DronePrototype {

    /**
     * Devuelve una copia independiente del dron recibido.
     * <p>
     * La copia es superficial, lo cual es seguro con los atributos
     * actuales de la entidad: todos son primitivos o {@link String},
     * y String es inmutable, de modo que compartir la referencia no
     * permite que un objeto altere al otro.
     *
     * @param original dron a duplicar; no puede ser {@code null}
     * @return un dron nuevo con los mismos datos y el id en 0
     * @throws DronValidacionException si no se recibio dron o su tipo
     *                                 no esta soportado
     */
    public Drone clonar(Drone original) {
        if (original == null) {
            throw new DronValidacionException(
                    "Selecciona un drone de la tabla para clonarlo.");
        }
        if (original instanceof Vigilancia) {
            return clonarVigilancia((Vigilancia) original);
        }
        if (original instanceof Agricultura) {
            return clonarAgricultura((Agricultura) original);
        }
        throw new DronValidacionException(
                "Tipo de drone no soportado para clonacion: " + original.getTipo());
    }

    /**
     * Ensambla la copia de un dron de vigilancia reutilizando el builder.
     *
     * @param original dron de vigilancia a duplicar
     * @return la copia, con id en 0
     */
    private Drone clonarVigilancia(Vigilancia original) {
        return new VigilanciaBuilder()
                .conId(0)
                .conSerial(original.getSerial())
                .conFabricante(original.getFabricante())
                .conModelo(original.getModelo())
                .conPeso(original.getPeso())
                .conDeteccionTermica(original.isDeteccionTermica())
                .build();
    }

    /**
     * Ensambla la copia de un dron de agricultura reutilizando su factoria.
     *
     * @param original dron de agricultura a duplicar
     * @return la copia, con id en 0
     */
    private Drone clonarAgricultura(Agricultura original) {
        return new AgriculturaFactory(
                original.getSerial(),
                original.getFabricante(),
                original.getModelo(),
                original.getPeso(),
                original.getCapacidadTanque()).crearDrone();
    }
}