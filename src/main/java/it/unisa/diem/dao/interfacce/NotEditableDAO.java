/**
 * @file NotEditableDAO.java
 *  Interfaccia base per DAO di entità non modificabili
 */
package it.unisa.diem.dao.interfacce;

import it.unisa.diem.exceptions.DBException;
import java.util.List;

/**
 *  NotEditableDAO
 *  Interfaccia per operazioni CRUD su entità senza modifica
 *  T Tipo dell'entità gestita
 */
public interface NotEditableDAO<T> {

    /**
     *  Recupera tutte le entità
     * @return Lista di tutte le entità
     * @throws DBException per errori database
     */
    List<T> selectAll() throws DBException;

    /**
     *  Inserisce una nuova entità
     * @param t Entità da inserire
     * @throws DBException per errori database
     */
    void insert(T t) throws DBException;

    /**
     *  Elimina un'entità
     * @param t Entità da eliminare
     * @throws DBException per errori database
     */
    void delete(T t) throws DBException;
}