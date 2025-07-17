/**
 * @file DAO.java
 * @brief Interfaccia generica per l'accesso ai dati (Data Access Object).
 *
 * Definisce le operazioni CRUD di base per l'interazione con il database.
 */

package it.unisa.diem.dao.interfacce;

import it.unisa.diem.exceptions.DBException;
import java.sql.SQLException;
import java.util.List;

/**
 * @interface DAO
 * @brief Interfaccia generica per il pattern Data Access Object.
 *
 * @tparam T Il tipo dell'entità gestita dal DAO
 *
 * Fornisce i metodi fondamentali per le operazioni CRUD (Create, Read, Update, Delete)
 * su qualsiasi entità del dominio.
 */
public interface DAO<T> {

    /**
     * @brief Recupera tutte le istanze dell'entità dal database.
     *
     * @return Una lista contenente tutte le entità presenti nel database
     * @throws SQLException Se si verifica un errore SQL
     * @throws DBException Se si verifica un errore specifico dell'applicativo
     */
    List<T> selectAll() throws SQLException, DBException;

    /**
     * @brief Inserisce una nuova istanza dell'entità nel database.
     *
     * @param t L'entità da inserire
     * @throws SQLException Se si verifica un errore SQL
     * @throws DBException Se si verifica un errore specifico dell'applicativo
     */
    void insert(T t) throws SQLException, DBException;

    /**
     * @brief Aggiorna un'entità esistente nel database.
     *
     * @param t L'entità da aggiornare
     * @throws SQLException Se si verifica un errore SQL
     * @throws DBException Se si verifica un errore specifico dell'applicativo
     */
    void update(T t) throws SQLException, DBException;

    /**
     * @brief Elimina un'entità dal database.
     *
     * @param t L'entità da eliminare
     * @throws SQLException Se si verifica un errore SQL
     * @throws DBException Se si verifica un errore specifico dell'applicativo
     */
    void delete(T t) throws SQLException, DBException;
}