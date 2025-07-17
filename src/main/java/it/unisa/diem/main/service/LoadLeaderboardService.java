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

/**
 * Servizio per il caricamento della classifica dei punteggi per ogni livello di difficoltà.
 * Estende {@link Service} per fornire un'esecuzione asincrona del task.
 */
public class LoadLeaderboardService extends Service<Map<Difficolta, List<VoceClassifica>>> {

    private final SessioneDAOPostgres dao;
    private String username;

    /**
     * Costruttore della classe. Inizializza il DAO per l'accesso al database.
     */
    public LoadLeaderboardService() {
        dao = new SessioneDAOPostgres();
    }

    /**
     * Crea e restituisce un task per il caricamento della classifica.
     * Il task esegue una query per ogni livello di difficoltà e restituisce una mappa con i risultati.
     *
     * @return Task per il caricamento della classifica
     * @throws Exception se si verifica un errore durante l'accesso al database
     */
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

    /**
     * Restituisce la mappa contenente le classifiche per ogni livello di difficoltà.
     *
     * @return Mappa con le classifiche divise per difficoltà
     */
    public Map<Difficolta, List<VoceClassifica>> getValueMap() {
        return getValue();
    }
}