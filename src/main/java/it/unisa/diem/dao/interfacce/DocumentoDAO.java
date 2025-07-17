package it.unisa.diem.dao.interfacce;

import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Documento;

import java.util.List;
import java.util.Optional;

/**
 * Interfaccia DAO per la gestione degli oggetti Documento
 * Estende NotEditableDAO
 */
public interface DocumentoDAO extends NotEditableDAO<Documento> {

    /**
     * Restituisce un documento a partire dal suo titolo.
     *
     * @param titolo il titolo del documento da cercare
     * @return un Optional contenente il documento, se trovato; altrimenti Optional#empty()
     * @throws DBException se si verifica un errore durante l'accesso al database
     */
    Optional<Documento> selectByTitle(String titolo) throws DBException;

    /**
     * Restituisce la lista di tutti i titoli dei documenti presenti nel database.
     *
     * @return una lista di stringhe contenenti i titoli dei documenti
     * @throws DBException se si verifica un errore durante la lettura dal database
     */
    List<String> selectAllTitles() throws DBException;
}
