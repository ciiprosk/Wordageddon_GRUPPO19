package it.unisa.diem.main.service;

import it.unisa.diem.dao.postgres.StoricoSessioneDAOPostgres;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.sessione.VoceStorico;
import it.unisa.diem.utility.PropertiesLoader;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

public class HistoryService extends Service<List<VoceStorico>> {

    private final StoricoSessioneDAOPostgres dao;
    private String username;
    private Difficolta difficolta;

    public HistoryService() {
        this.dao = new StoricoSessioneDAOPostgres(
                PropertiesLoader.getProperty("database.url"),
                PropertiesLoader.getProperty("database.user"),
                PropertiesLoader.getProperty("database.password")
        );
    }

    public void setParameters(String username, Difficolta difficolta) {
        this.username = username;
        this.difficolta = difficolta;
    }

    @Override
    protected Task<List<VoceStorico>> createTask() {
        return new Task<>() {
            @Override
            protected List<VoceStorico> call() throws DBException {
                return dao.selectByLastSessions(username, difficolta);
            }
        };
    }
}
