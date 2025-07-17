/**
 * @file SessioneDAO.java
 * @brief Interfaccia per l'accesso ai dati delle sessioni
 */
package it.unisa.diem.dao.interfacce;

import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.classifica.VoceClassifica;
import it.unisa.diem.model.gestione.sessione.Sessione;
import it.unisa.diem.model.gestione.sessione.VoceStorico;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * @interface SessioneDAO
 * @brief Interfaccia per le operazioni CRUD sulle sessioni
 *
 * Estende DAO<Sessione> e aggiunge operazioni specifiche per:
 * - Gestione sessioni utente
 * - Recupero storico sessioni
 * - Gestione classifiche
 */
public interface SessioneDAO extends DAO<Sessione> {

    /**
     * @brief Cerca l'ultima sessione di un utente
     * @param username Nome utente da cercare
     * @return Optional contenente la sessione se trovata
     */
    Optional<Sessione> selectByUser(String username) throws SQLException, DBException;

    /**
     * @brief Cerca una sessione per ID
     * @param id ID della sessione
     * @return Optional contenente la sessione se trovata
     */
    Optional<Sessione> selectById(long id) throws SQLException, DBException;

    /**
     * @brief Elimina una sessione per ID
     * @param sessioneId ID della sessione da eliminare
     */
    void delete(long sessioneId) throws DBException;

    /**
     * @brief Recupera le ultime sessioni di un utente per difficoltà
     * @param username Nome utente
     * @param difficolta Livello di difficoltà
     * @return Lista di voci dello storico
     */
    List<VoceStorico> selectByLastSessions(String username, Difficolta difficolta) throws DBException;

    /**
     * @brief Recupera la classifica per difficoltà
     * @param difficolta Livello di difficoltà
     * @return Lista di voci di classifica
     */
    List<VoceClassifica> selectByTopRanking(Difficolta difficolta) throws DBException;
}