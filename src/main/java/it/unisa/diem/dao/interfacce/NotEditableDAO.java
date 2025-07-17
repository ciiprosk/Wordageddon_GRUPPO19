/**
 * @file NotEditableDAO.java
 * @brief Interfaccia base per DAO di entità non modificabili
 */
package it.unisa.diem.dao.interfacce;

import it.unisa.diem.exceptions.DBException;
import java.util.List;

/**
 * @interface NotEditableDAO
 * @brief Interfaccia per operazioni CRUD su entità senza modifica
 * @tparam T Tipo dell'entità gestita
 */
public interface NotEditableDAO<T> {

    /**
     * @brief Recupera tutte le entità
     * @return Lista di tutte le entità
     * @throws DBException per errori database
     */
    List<T> selectAll() throws DBException;

    /**
     * @brief Inserisce una nuova entità
     * @param t Entità da inserire
     * @throws DBException per errori database
     */
    void insert(T t) throws DBException;

    /**
     * @brief Elimina un'entità
     * @param t Entità da eliminare
     * @throws DBException per errori database
     */
    void delete(T t) throws DBException;
}