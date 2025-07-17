/**
 * @file DeleteDocumentService.java
 * @brief Servizio per l'eliminazione di documenti in background
 */
package it.unisa.diem.main.service;

import it.unisa.diem.dao.interfacce.DocumentoDAO;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Analisi;
import it.unisa.diem.model.gestione.analisi.Documento;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import java.util.Optional;

/**
 * @class DeleteDocumentService
 * @brief Servizio JavaFX per l'eliminazione asincrona di documenti
 *
 * Estende javafx.concurrent.Service per eseguire l'eliminazione in background.
 * Gestisce l'intero processo di eliminazione:
 * 1. Ricerca del documento nel database
 * 2. Eliminazione dei file di analisi associati
 * 3. Rimozione dal database (con cancellazione a cascata)
 * 4. Eliminazione del file del documento
 */
public class DeleteDocumentService extends Service<Void> {

    private final DocumentoDAO dao;
    private String selectedTitle;

    /**
     * @brief Costruttore principale
     * @param dao DAO per l'accesso ai documenti
     */
    public DeleteDocumentService(DocumentoDAO dao) {
        this.dao = dao;
    }

    /**
     * @brief Imposta il titolo del documento da eliminare
     * @param selectedTitle Titolo del documento selezionato
     */
    public void setSelectedTitle(String selectedTitle) {
        this.selectedTitle = selectedTitle;
    }

    /**
     * @brief Crea il task per l'eliminazione asincrona
     * @return Task che esegue l'eliminazione
     * @throws DBException Se il titolo è vuoto o il documento non esiste
     *
     * Il processo esegue le seguenti operazioni:
     * 1. Verifica la presenza del titolo
     * 2. Ricerca il documento nel database
     * 3. Elimina i file di analisi associati
     * 4. Rimuove il record dal database (con cancellazione a cascata)
     * 5. Elimina il file del documento dal filesystem
     */
    @Override
    protected Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                if (selectedTitle == null || selectedTitle.isEmpty()) {
                    throw new DBException("Titolo documento nullo o vuoto.");
                }

                Optional<Documento> documentoOpt = dao.selectByTitle(selectedTitle);

                if (documentoOpt.isEmpty()) {
                    throw new DBException("Documento non trovato nel database.");
                }

                Documento documento = documentoOpt.get();

                // Elimina file analisi
                Analisi analisi = new Analisi(documento);
                analisi.eliminaAnalisi();

                // Elimina dal database (cascade)
                dao.delete(documento);

                // Elimina il file del documento
                documento.eliminaDocumento();

                return null;
            }
        };
    }
}