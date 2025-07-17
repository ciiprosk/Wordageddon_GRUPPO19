package it.unisa.diem.dao.interfacce;

import it.unisa.diem.exceptions.DBException;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia generica per le operazioni CRUD (Create, Read, Update, Delete)
 * su entità di tipo T
 *
 * @param <T> il tipo di entità gestito dal DAO
 */
public interface DAO<T> {

    /**
     * Restituisce la lista completa degli oggetti presenti nel database.
     *
     * @return una lista di oggetti  T
     * @throws SQLException se si verifica un errore JDBC
     * @throws DBException  se si verifica un errore di business logic o di mapping
     */
    List<T> selectAll() throws SQLException, DBException;

    /**
     * Inserisce un nuovo oggetto nel database.
     *
     * @param t l'oggetto da inserire
     * @throws SQLException se si verifica un errore JDBC
     * @throws DBException  se si verifica un errore di business logic o di vincoli
     */
    void insert(T t) throws SQLException, DBException;

    /**
     * Aggiorna un oggetto esistente nel database.
     *
     * @param t l'oggetto da aggiornare
     * @throws SQLException se si verifica un errore JDBC
     * @throws DBException  se si verifica un errore di business logic
     */
    void update(T t) throws SQLException, DBException;

    /**
     * Elimina un oggetto dal database.
     *
     * @param t l'oggetto da eliminare
     * @throws SQLException se si verifica un errore JDBC
     * @throws DBException  se si verifica un errore durante l'eliminazione
     */
    void delete(T t) throws SQLException, DBException;
}
