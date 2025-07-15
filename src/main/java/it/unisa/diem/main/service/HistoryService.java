package it.unisa.diem.main.service;

import it.unisa.diem.dao.postgres.SessioneDAOPostgres;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.sessione.VoceStorico;
import it.unisa.diem.utility.PropertiesLoader;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryService extends Service<Map<Difficolta, List<VoceStorico>>> {

    private final SessioneDAOPostgres dao;
    private String username;

    public HistoryService() {
        this.dao = new SessioneDAOPostgres(
                PropertiesLoader.getProperty("database.url"),
                PropertiesLoader.getProperty("database.user"),
                PropertiesLoader.getProperty("database.password")
        );
    }

    public void setParameters(String username) {
        this.username = username;
    }

    @Override
    protected Task<Map<Difficolta, List<VoceStorico>>> createTask() {
        return new Task<>() {
            @Override
            protected Map<Difficolta, List<VoceStorico>> call() throws DBException {
                Map<Difficolta, List<VoceStorico>> risultati = new HashMap<>();
                for (Difficolta d : Difficolta.values()) {
                    List<VoceStorico> lista = dao.selectByLastSessions(username, d);
                    risultati.put(d, lista);
                }
                return risultati;
            }
        };
    }

    public Map<Difficolta, List<VoceStorico>> getValueMap() {
        return getValue();
    }
}
