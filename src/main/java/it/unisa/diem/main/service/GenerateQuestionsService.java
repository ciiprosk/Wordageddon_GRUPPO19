/**
 * @file GenerateQuestionsService.java
 * @brief Servizio per la generazione di domande in background
 */
package it.unisa.diem.main.service;

import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.sessione.Domanda;
import it.unisa.diem.model.gestione.sessione.DomandaFactory;
import it.unisa.diem.model.gestione.analisi.Analisi;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import java.util.List;

/**
 * @class GenerateQuestionsService
 * @brief Servizio JavaFX per la generazione asincrona di domande
 *
 * Estende javafx.concurrent.Service per generare domande in background.
 * Utilizza una DomandaFactory per creare domande basate su:
 * - Una lista di analisi
 * - Un livello di difficoltà specificato
 */
public class GenerateQuestionsService extends Service<List<Domanda>> {

    private List<Analisi> analyses;
    private Difficolta difficolta;

    /**
     * @brief Imposta la lista di analisi da usare per generare le domande
     * @param analyses Lista di oggetti Analisi contenenti i dati per le domande
     */
    public void setAnalyses(List<Analisi> analyses) {
        this.analyses = analyses;
    }

    /**
     * @brief Imposta il livello di difficoltà per le domande
     * @param difficolta Livello di difficoltà (FACILE, MEDIO, DIFFICILE)
     */
    public void setDifficolta(Difficolta difficolta) {
        this.difficolta = difficolta;
    }

    /**
     * @brief Crea il task per la generazione asincrona delle domande
     * @return Task che genera e restituisce una lista di Domanda
     *
     * Il processo:
     * 1. Crea una nuova DomandaFactory con le analisi fornite
     * 2. Genera le domande in base alla difficoltà specificata
     * 3. Restituisce la lista di domande generate
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