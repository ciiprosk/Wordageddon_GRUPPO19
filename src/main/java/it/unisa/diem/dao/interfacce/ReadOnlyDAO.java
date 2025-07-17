/**
 * @file ReadOnlyDAO.java
 *  Interfaccia per operazioni di sola lettura su database
 */
package it.unisa.diem.dao.interfacce;

import it.unisa.diem.exceptions.DBException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 *  ReadOnlyDAO
 *  Interfaccia per accesso in sola lettura alle entità
 * @tparam T Tipo dell'entità gestita dal DAO
 */
public interface ReadOnlyDAO<T> {

    /**
     *  Recupera tutte le entità
     * @return Lista contenente tutte le entità
     * @throws SQLException in caso di errori SQL
     * @throws DBException in caso di errori applicativi
     */
    List<T> selectAll() throws SQLException, DBException;

    /**
     *  Cerca un'entità per ID
     * @param id Identificativo dell'entità da cercare
     * @return Optional contenente l'entità se trovata
     * @throws SQLException in caso di errori SQL
     * @throws DBException in caso di errori applicativi
     */
    Optional<T> selectById(long id) throws SQLException, DBException;
}