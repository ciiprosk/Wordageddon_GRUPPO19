package it.unisa.diem.main.service;

import it.unisa.diem.dao.interfacce.DocumentoDAO;
import it.unisa.diem.dao.postgres.DocumentoDAOPostgres;
import it.unisa.diem.utility.PropertiesLoader;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

/**
 * Servizio per il caricamento dei titoli dei documenti in background.
 * Estende la classe Service di JavaFX per operazioni asincrone.
 */
public class LoadTitlesService extends Service<List<String>> {

    private final DocumentoDAO dao;

    /**
     * Costruttore della classe. Inizializza il DAO per l'accesso ai documenti.
     */
    public LoadTitlesService() {
        this.dao = new DocumentoDAOPostgres();
    }

    /**
     * Crea e restituisce un Task per il recupero dei titoli dei documenti.
     *
     * @return Task che rappresenta l'operazione di recupero dei titoli
     */
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