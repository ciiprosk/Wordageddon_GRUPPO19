package it.unisa.diem.main.service;

import it.unisa.diem.dao.postgres.StoricoSessioneDAOPostgres;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.classifica.VoceClassifica;
import it.unisa.diem.utility.PropertiesLoader;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

public class LoadLeaderboardService extends Service<List<VoceClassifica>> {

    private final StoricoSessioneDAOPostgres dao;
    private Difficolta difficolta;

    public LoadLeaderboardService() {
        dao = new StoricoSessioneDAOPostgres(
                PropertiesLoader.getProperty("database.url"),
                PropertiesLoader.getProperty("database.user"),
                PropertiesLoader.getProperty("database.password")
        );
    }

    public void setDifficolta(Difficolta difficolta) {
        this.difficolta = difficolta;
    }

    @Override
    protected Task<List<VoceClassifica>> createTask() {
        return new Task<>() {
            @Override
            protected List<VoceClassifica> call() throws DBException {
                return dao.selectByTopRanking(difficolta);
            }
        };
    }
}
