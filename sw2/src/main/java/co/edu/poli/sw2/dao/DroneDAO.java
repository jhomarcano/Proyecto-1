package co.edu.poli.sw2.dao;

import co.edu.poli.sw2.modelo.Drone;

/**
 * Contrato de acceso a datos especifico para la entidad {@link Drone}.
 * <p>
 * Fija el parametro generico de {@link GenericDAO} en {@code Drone}, de
 * modo que las capas superiores dependan de esta interfaz y no de su
 * implementacion concreta.
 *
 * @author Alejandra Cano y Juan Rosero
 * @see GenericDAO
 * @see DroneDAOImpl
 */
public interface DroneDAO extends GenericDAO<Drone> {
}