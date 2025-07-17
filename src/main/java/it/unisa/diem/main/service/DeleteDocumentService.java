package it.unisa.diem.main.service;

import it.unisa.diem.dao.interfacce.DocumentoDAO;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Analisi;
import it.unisa.diem.model.gestione.analisi.Documento;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.Optional;

/**
 * Servizio JavaFX per l'eliminazione asincrona di un documento dal sistema.
 * Include la rimozione del file di analisi, del documento stesso e della
 * relativa voce nel database (con eliminazione in cascata, se definita).
 */
public class DeleteDocumentService extends Service<Void> {

    private final DocumentoDAO dao;
    private String selectedTitle;

    /**
     * Crea un servizio per la cancellazione di un documento.
     *
     * @param dao l'oggetto DocumentoDAO usato per accedere al database
     */
    public DeleteDocumentService(DocumentoDAO dao) {
        this.dao = dao;
    }

    /**
     * Imposta il titolo del documento da eliminare.
     *
     * @param selectedTitle il titolo del documento
     */
    public void setSelectedTitle(String selectedTitle) {
        this.selectedTitle = selectedTitle;
    }

    /**
     * Crea il task che esegue l'eliminazione del documento. Il processo include:
     *
     *     Verifica della validità del titolo
     *     Ricerca del documento nel database
     *     Eliminazione del file di analisi
     *     Rimozione del documento dal database
     *     Eliminazione del file fisico del documento
     *
     * @return il task da eseguire
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

                Analisi analisi = new Analisi(documento);
                analisi.eliminaAnalisi();

                dao.delete(documento);
                documento.eliminaDocumento();

                return null;
            }
        };
    }
}
