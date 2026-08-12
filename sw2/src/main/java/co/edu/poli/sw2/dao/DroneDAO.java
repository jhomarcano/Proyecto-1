package co.edu.poli.sw2.dao;

import co.edu.poli.sw2.modelo.Drone;

/**
 * DAO especifico de Drone. Por ahora es el unico con CRUD real;
 * el resto de entidades (Piloto, Sensor) se manejan como catalogos
 * fijos dentro de DroneDAOImpl, sin ser parte de este contrato.
 */
public interface DroneDAO extends GenericDAO<Drone> {
}