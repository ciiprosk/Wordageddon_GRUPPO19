/**
 * @file UtenteDAO.java
 * @brief Interfaccia per l'accesso ai dati degli utenti
 */
package it.unisa.diem.dao.interfacce;

import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.utenti.Utente;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @interface UtenteDAO
 * @brief Interfaccia per le operazioni CRUD sugli utenti
 *
 * Estende DAO<Utente> e aggiunge operazioni specifiche per:
 * - Verifica esistenza utente
 * - Gestione tramite username
 * - Aggiornamento dati utente
 */
public interface UtenteDAO extends DAO<Utente> {

    /**
     * @brief Verifica se un'email è già registrata
     * @param email Email da verificare
     * @return true se l'email esiste già, false altrimenti
     * @throws SQLException per errori SQL
     * @throws DBException per errori applicativi
     */
    boolean emailAlreadyExists(String email) throws SQLException, DBException;

    /**
     * @brief Verifica se uno username è già in uso
     * @param username Username da verificare
     * @return true se lo username esiste già, false altrimenti
     * @throws SQLException per errori SQL
     * @throws DBException per errori applicativi
     */
    boolean usernameAlreadyExists(String username) throws SQLException, DBException;

    /**
     * @brief Ricerca un utente per username
     * @param username Username da cercare
     * @return Optional contenente l'utente se trovato
     * @throws SQLException per errori SQL
     * @throws DBException per errori applicativi
     */
    Optional<Utente> selectByUsername(String username) throws SQLException, DBException;

    /**
     * @brief Aggiorna i dati di un utente
     * @param oldUsername Username attuale dell'utente
     * @param utente Nuovi dati dell'utente
     * @throws SQLException per errori SQL
     * @throws DBException per errori applicativi
     */
    void update(String oldUsername, Utente utente) throws SQLException, DBException;
}