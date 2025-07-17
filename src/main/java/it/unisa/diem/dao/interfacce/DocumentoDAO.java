/**
 * @file DocumentoDAO.java
 * @brief Interfaccia DAO per la gestione dei documenti
 */
package it.unisa.diem.dao.interfacce;

import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Documento;
import java.util.List;
import java.util.Optional;

/**
 * @interface DocumentoDAO
 * @brief Interfaccia per le operazioni sui documenti
 */
public interface DocumentoDAO extends NotEditableDAO<Documento> {

    /**
     * @brief Cerca documento per titolo
     * @param titolo Titolo da ricercare
     * @return Optional contenente il documento se trovato
     * @throws DBException in caso di errori DB
     */
    Optional<Documento> selectByTitle(String titolo) throws DBException;

    /**
     * @brief Recupera tutti i titoli disponibili
     * @return Lista dei titoli dei documenti
     * @throws DBException in caso di errori DB
     */
    List<String> selectAllTitles() throws DBException;
}