/**
 * @file AnalisiService.java
 * @brief Servizio per l'analisi dei documenti in background
 */
package it.unisa.diem.main.service;

import it.unisa.diem.dao.interfacce.AnalisiDAO;
import it.unisa.diem.dao.interfacce.DocumentoDAO;
import it.unisa.diem.dao.postgres.AnalisiDAOPostgres;
import it.unisa.diem.dao.postgres.DocumentoDAOPostgres;
import it.unisa.diem.model.gestione.analisi.Analisi;
import it.unisa.diem.model.gestione.analisi.Documento;
import it.unisa.diem.model.gestione.analisi.stopword.StopwordManager;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import java.io.File;

/**
 * @class AnalisiService
 * @brief Servizio JavaFX per l'analisi asincrona dei documenti
 *
 * Estende javafx.concurrent.Service per eseguire l'analisi in background.
 * Gestisce l'intero processo di analisi:
 * 1. Conversione del file
 * 2. Applicazione delle stopword
 * 3. Analisi del contenuto
 * 4. Salvataggio dei risultati nel database
 */
public class AnalisiService extends Service<Void> {

    private final Documento documento;
    private final StopwordManager stopwordManager;
    private final File inputFile;

    /**
     * @brief Costruttore principale
     * @param documento Documento da analizzare
     * @param stopwordManager Gestore delle stopword da applicare
     * @param inputFile File di input da processare
     */
    public AnalisiService(Documento documento, StopwordManager stopwordManager, File inputFile) {
        this.documento = documento;
        this.stopwordManager = stopwordManager;
        this.inputFile = inputFile;
    }

    /**
     * @brief Crea il task per l'analisi asincrona
     * @return Task che esegue l'analisi
     *
     * Il processo esegue le seguenti operazioni:
     * 1. Conversione del file in formato binario
     * 2. Stampa debug delle stopword utilizzate
     * 3. Esecuzione dell'analisi
     * 4. Salvataggio nel database
     * 5. Aggiornamento dello stato tramite messaggi
     */
    @Override
    protected Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                updateMessage("Inizio conversione file...");
                documento.convertiTxtToBin(inputFile);

                System.out.println("🔎 Stopword utilizzate per l'analisi:");
                for (String sw : stopwordManager.getParole()) {
                    System.out.println(sw);
                }

                updateMessage("Analisi in corso...");
                Analisi analisi = new Analisi(documento, stopwordManager);
                analisi.analizza();
                analisi.caricaAnalisi();

                DocumentoDAO daoDoc = new DocumentoDAOPostgres();
                AnalisiDAO daoAn = new AnalisiDAOPostgres();

                daoDoc.insert(documento);
                daoAn.insert(analisi);

                updateMessage("Completato");
                return null;
            }
        };
    }
}