package co.edu.poli.sw2.dao;

import java.util.List;

/**
 * Contrato generico de acceso a datos para cualquier entidad del dominio.
 * <p>
 * Al estar parametrizada con {@code <T>}, una misma interfaz sirve para
 * todas las entidades: basta con crear una implementacion que fije el
 * tipo, por ejemplo {@code class PilotoDAOImpl implements GenericDAO<Piloto>}.
 * Esto evita repetir la firma de las cuatro operaciones CRUD en cada DAO.
 *
 * @param <T> tipo de entidad que gestiona la implementacion
 * @author Alejandra Cano y Juan Rosero
 */
public interface GenericDAO<T> {

    /**
     * Recupera todos los registros de la entidad.
     *
     * @return la lista de entidades encontradas; vacia si no hay registros
     */
    List<T> listar();

    /**
     * Persiste una nueva entidad.
     *
     * @param entidad la entidad a guardar
     * @return la entidad guardada, con el identificador asignado por la base de datos
     */
    T crear(T entidad);

    /**
     * Elimina una entidad existente.
     *
     * @param entidad la entidad a eliminar; debe tener un id valido
     */
    void eliminar(T entidad);

    /**
     * Actualiza los datos de una entidad existente.
     *
     * @param entidad la entidad con los datos modificados
     * @return la entidad actualizada
     */
    T actualizar(T entidad);
}