package it.unisa.diem.main.service;

import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.sessione.Domanda;
import it.unisa.diem.model.gestione.sessione.DomandaFactory;
import it.unisa.diem.model.gestione.analisi.Analisi;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

public class GenerateQuestionsService extends Service<List<Domanda>> {

    private List<Analisi> analyses;
    private Difficolta difficolta;

    public void setAnalyses(List<Analisi> analyses) { this.analyses = analyses; }
    public void setDifficolta(Difficolta difficolta) { this.difficolta = difficolta; }

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
