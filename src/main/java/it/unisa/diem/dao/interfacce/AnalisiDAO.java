/**
 * @file AnalisiDAO.java
 * @brief Interfaccia per l'accesso ai dati delle analisi nel database.
 *
 * Definisce le operazioni CRUD specifiche per le analisi, estendendo le funzionalità base di NotEditableDAO.
 */

package it.unisa.diem.dao.interfacce;

import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Analisi;

import java.sql.SQLException;
import java.util.Optional;

/**
 *Interfaccia per l'accesso ai dati delle analisi.
 *
 * Estende NotEditableDAO<Analisi> e aggiunge operazioni specifiche per la gestione delle analisi.
 * Fornisce metodi per selezionare, inserire ed eliminare analisi nel database.
 */
public interface AnalisiDAO extends NotEditableDAO<Analisi> {

    /**
     * @brief Ricerca un'analisi per titolo.
     *
     * @param titolo Il titolo dell'analisi da cercare
     * @return Un Optional contenente l'analisi se trovata, altrimenti Optional.empty()
     * @throws DBException Se si verifica un errore durante l'accesso al database
     */
    Optional<Analisi> selectAnalisiByTitle(String titolo) throws DBException;

    /**
     * @brief Inserisce una nuova analisi nel database.
     *
     * @param analisi L'analisi da inserire
     * @throws DBException Se si verifica un errore durante l'accesso al database
     */
    void insert(Analisi analisi) throws DBException;

    /**
     * @brief Elimina un'analisi dal database.
     *
     * @param analisi L'analisi da eliminare
     * @throws DBException Se si verifica un errore durante l'accesso al database
     */
    void delete(Analisi analisi) throws DBException;
}