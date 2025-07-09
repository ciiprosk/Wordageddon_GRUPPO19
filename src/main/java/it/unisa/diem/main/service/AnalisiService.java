package it.unisa.diem.main.service;

import it.unisa.diem.dao.postgres.AnalisiDAOPostgres;
import it.unisa.diem.dao.postgres.DocumentoDAOPostgres;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Analisi;
import it.unisa.diem.model.gestione.analisi.Documento;
import it.unisa.diem.model.gestione.analisi.stopword.StopwordManager;
import it.unisa.diem.utility.PropertiesLoader;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;

public class AnalisiService extends Service<Void> {

    private final Documento documento;
    private final StopwordManager stopwordManager;
    private final File inputFile;

    public AnalisiService(Documento documento, StopwordManager stopwordManager, File inputFile) {
        this.documento = documento;
        this.stopwordManager = stopwordManager;
        this.inputFile = inputFile;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                updateMessage("Inizio conversione file...");
                documento.convertiTxtToBin(inputFile);

                updateMessage("Analisi in corso...");
                Analisi analisi = new Analisi(documento, stopwordManager);
                analisi.analizza();
                analisi.caricaAnalisi();

                updateMessage("Inserimento nel database...");
                String url = PropertiesLoader.getProperty("database.url");
                String user = PropertiesLoader.getProperty("database.user");
                String pass = PropertiesLoader.getProperty("database.password");

                DocumentoDAOPostgres daoDoc = new DocumentoDAOPostgres(url, user, pass);
                AnalisiDAOPostgres daoAn = new AnalisiDAOPostgres(url, user, pass);

                daoDoc.insert(documento);
                daoAn.insert(analisi);

                updateMessage("Completato");
                return null;
            }
        };
    }
}

