package it.unisa.diem.main.service;

import it.unisa.diem.dao.interfacce.DocumentoDAO;
import it.unisa.diem.dao.postgres.DocumentoDAOPostgres;
import it.unisa.diem.utility.PropertiesLoader;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

public class LoadTitlesService extends Service<List<String>> {

    private final DocumentoDAO dao;

    public LoadTitlesService() {
        this.dao = new DocumentoDAOPostgres();
        /*
        this.dao = new DocumentoDAOPostgres(
                PropertiesLoader.getProperty("database.url"),
                PropertiesLoader.getProperty("database.user"),
                PropertiesLoader.getProperty("database.password")
        );

         */
    }

    @Override
    protected Task<List<String>> createTask() {
        return new Task<>() {
            @Override
            protected List<String> call() throws Exception {
                return dao.selectAllTitles();
            }
        };
    }
}
