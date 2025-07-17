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
 * Servizio JavaFX per l'esecuzione asincrona dell'analisi di un documento.
 * Include la conversione del file, il processo di analisi testuale,
 * il caricamento dei dati e il salvataggio nel database.
 */
public class AnalisiService extends Service<Void> {

    private final Documento documento;
    private final StopwordManager stopwordManager;
    private final File inputFile;

    /**
     * Crea un nuovo servizio per l'analisi di un documento.
     *
     * @param documento        il documento da analizzare
     * @param stopwordManager  il gestore delle stopword da utilizzare nell'analisi
     * @param inputFile        il file di input contenente il testo da analizzare
     */
    public AnalisiService(Documento documento, StopwordManager stopwordManager, File inputFile) {
        this.documento = documento;
        this.stopwordManager = stopwordManager;
        this.inputFile = inputFile;
    }

    /**
     * Crea il task che esegue l'analisi. Il task svolge le seguenti operazioni:
     *
     *     Converte il file di testo in formato binario
     *     Effettua l'analisi testuale utilizzando le stopword fornite
     *     Carica i dati dell'analisi
     *     Salva il documento e l'analisi nel database<
     *
     * @return il task da eseguire
     */
    @Override
    protected Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                updateMessage("Inizio conversione file...");
                documento.convertiTxtToBin(inputFile);

                System.out.println("Stopword utilizzate per l'analisi:");
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
