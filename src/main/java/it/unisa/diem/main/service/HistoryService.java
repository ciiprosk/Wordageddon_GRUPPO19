package it.unisa.diem.main.service;

import it.unisa.diem.dao.interfacce.SessioneDAO;
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

/**
 * Servizio per il recupero della cronologia delle sessioni di un utente, organizzate per difficoltà.
 */
public class HistoryService extends Service<Map<Difficolta, List<VoceStorico>>> {

    private final SessioneDAOPostgres dao;
    private String username;

    /**
     * Costruttore che inizializza il DAO per l'accesso al database.
     */
    public HistoryService() {
        this.dao = new SessioneDAOPostgres();
    }

    /**
     * Imposta il nome utente per cui recuperare la cronologia.
     * @param username Il nome utente di cui recuperare la cronologia
     */
    public void setParameters(String username) {
        this.username = username;
    }

    /**
     * Crea e restituisce un task per il recupero della cronologia delle sessioni.
     * @return Task che recupera la cronologia organizzata per difficoltà
     */
    @Override
    protected Task<Map<Difficolta, List<VoceStorico>>> createTask() {
        return new Task<>() {
            @Override
            protected Map<Difficolta, List<VoceStorico>> call() throws DBException {
                Map<Difficolta, List<VoceStorico>> risultati = new HashMap<>();
                for (Difficolta d : Difficolta.values()) {
                    System.out.println("Recupero sessioni per difficoltà: " + d);
                    List<VoceStorico> lista = dao.selectByLastSessions(username, d);
                    System.out.println("Trovate " + lista.size() + " sessioni per " + d);
                    risultati.put(d, lista);
                }
                return risultati;
            }
        };
    }

    /**
     * Restituisce la mappa contenente le sessioni organizzate per difficoltà.
     * @return Mappa con difficoltà come chiave e lista di sessioni come valore
     */
    public Map<Difficolta, List<VoceStorico>> getValueMap() {
        return getValue();
    }
}