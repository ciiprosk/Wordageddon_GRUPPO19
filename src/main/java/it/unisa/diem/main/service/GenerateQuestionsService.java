package it.unisa.diem.main.service;

import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.sessione.Domanda;
import it.unisa.diem.model.gestione.sessione.DomandaFactory;
import it.unisa.diem.model.gestione.analisi.Analisi;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

/**
 * Servizio JavaFX per la generazione asincrona di domande a partire da analisi testuali.
 * Utilizza DomandaFactory per produrre una lista di domande in base alla difficoltà specificata.
 */
public class GenerateQuestionsService extends Service<List<Domanda>> {

    private List<Analisi> analyses;
    private Difficolta difficolta;

    /**
     * Imposta la lista delle analisi su cui basare la generazione delle domande.
     *
     * @param analyses la lista di oggetti Analisi
     */
    public void setAnalyses(List<Analisi> analyses) {
        this.analyses = analyses;
    }

    /**
     * Imposta il livello di difficoltà delle domande da generare.
     *
     * @param difficolta la difficoltà
     */
    public void setDifficolta(Difficolta difficolta) {
        this.difficolta = difficolta;
    }

    /**
     * Crea il task che genera domande basate sulle analisi e sulla difficoltà specificata.
     *
     * @return il task da eseguire, che restituisce una lista di domande
     */
    @Override
    protected Task<List<Domanda>> createTask() {
        return new Task<>() {
            @Override
            protected List<Domanda> call() {
                DomandaFactory factory = new DomandaFactory(analyses);
                return factory.generaDomande(difficolta);
            }
        };
    }
}
