package it.unisa.diem.main.service;

import it.unisa.diem.dao.postgres.SessioneDAOPostgres;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.classifica.VoceClassifica;
import it.unisa.diem.utility.PropertiesLoader;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadLeaderboardService extends Service<Map<Difficolta, List<VoceClassifica>>> {

    private final SessioneDAOPostgres dao;
    private String username;

    public LoadLeaderboardService() {
        dao = new SessioneDAOPostgres(
                PropertiesLoader.getProperty("database.url"),
                PropertiesLoader.getProperty("database.user"),
                PropertiesLoader.getProperty("database.password")
        );
    }

    @Override
    protected Task<Map<Difficolta, List<VoceClassifica>>> createTask() {
        return new Task<>() {
            @Override
            protected Map<Difficolta, List<VoceClassifica>> call() throws DBException {
                Map<Difficolta, List<VoceClassifica>> risultati = new HashMap<>();
                for (Difficolta d : Difficolta.values()) {
                    List<VoceClassifica> lista = dao.selectByTopRanking(d);
                    risultati.put(d, lista);
                }
                return risultati;
            }
        };
    }

    public Map<Difficolta, List<VoceClassifica>> getValueMap() {
        return getValue();
    }
}
