package it.unisa.diem.main.service;

import it.unisa.diem.dao.postgres.DomandaDAOPostgres;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.sessione.Domanda;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

public class InsertQuestionsService extends Service<Boolean> {

    private final DomandaDAOPostgres domandaDAO;
    private final List<Domanda> domande;

    public InsertQuestionsService(DomandaDAOPostgres domandaDAO, List<Domanda> domande) {
        this.domandaDAO = domandaDAO;
        this.domande = domande;
    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                try {
                    int numero = 1;
                    for (Domanda d : domande) {
                        d.setNumeroDomanda(numero++); // Imposta numero domanda progressivo se necessario
                        domandaDAO.insert(d);
                    }
                    return true;
                } catch (DBException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        };
    }
}
