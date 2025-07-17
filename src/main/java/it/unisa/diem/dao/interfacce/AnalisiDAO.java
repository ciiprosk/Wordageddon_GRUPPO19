package it.unisa.diem.dao.interfacce;

import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Analisi;

import java.util.Optional;

/**
 * Interfaccia DAO per la gestione delle operazioni di accesso ai dati relativi all'entità {@link Analisi}.
 * Estende NotEditableDAO
 */
public interface AnalisiDAO extends NotEditableDAO<Analisi> {

    /**
     * Seleziona un'analisi a partire dal titolo del documento associato.
     *
     * @param titolo il titolo del documento
     * @return un Optional contenente l'analisi se trovata, altrimenti vuoto
     * @throws DBException se si verifica un errore nell'accesso al database
     */
    Optional<Analisi> selectAnalisiByTitle(String titolo) throws DBException;

    /**
     * Inserisce una nuova analisi nel database.
     *
     * @param analisi l'oggetto Analisi} da inserire
     * @throws DBException se si verifica un errore nell'inserimento
     */
    void insert(Analisi analisi) throws DBException;

    /**
     * Elimina un'analisi dal database.
     *
     * @param analisi l'oggetto Analisi da eliminare
     * @throws DBException se si verifica un errore durante l'eliminazione
     */
    void delete(Analisi analisi) throws DBException;
}
