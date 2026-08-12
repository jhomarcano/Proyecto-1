package co.edu.poli.sw2.dao;

import java.util.List;

/**
 * Contrato genérico de acceso a datos. Cualquier entidad futura
 * (Piloto, Sensor, Mision, etc.) podrá implementar este DAO
 * simplemente creando su propia clase XxxDAOImpl implements GenericDAO<Xxx>.
 */
public interface GenericDAO<T> {

    List<T> listar();

    T crear(T entidad);

    void eliminar(T entidad);

    T actualizar(T entidad);
}