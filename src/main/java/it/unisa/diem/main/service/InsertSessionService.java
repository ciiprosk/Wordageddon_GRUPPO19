package it.unisa.diem.main.service;

import it.unisa.diem.dao.interfacce.SessioneDAO;
import it.unisa.diem.dao.postgres.SessioneDAOPostgres;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.sessione.Sessione;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class InsertSessionService extends Service<Sessione> {

    private SessioneDAO sessioneDAO;
    private Sessione sessione;

    public InsertSessionService(SessioneDAO sessioneDAO, Sessione sessione) {
        this.sessioneDAO = sessioneDAO;
        this.sessione = sessione;
    }

    @Override
    protected Task<Sessione> createTask() {
        return new Task<>() {
            @Override
            protected Sessione call() throws Exception {
                try {
                    sessioneDAO.insert(sessione);
                    return sessione;
                } catch (DBException e) {
                    throw new Exception("Errore nell'inserimento della sessione: " + e.getMessage(), e);
                }
            }
        };
    }
}
