package it.unisa.diem.main.service;

import it.unisa.diem.dao.interfacce.DocumentoDAO;
import it.unisa.diem.dao.postgres.DocumentoDAOPostgres;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Analisi;
import it.unisa.diem.model.gestione.analisi.Documento;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.Optional;

public class DeleteDocumentService extends Service<Void> {

    private final DocumentoDAO dao;
    private String selectedTitle;

    public DeleteDocumentService(DocumentoDAO dao) {
        this.dao = dao;
    }

    public void setSelectedTitle(String selectedTitle) {
        this.selectedTitle = selectedTitle;
    }

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
