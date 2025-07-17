package it.unisa.diem.main.service;

import it.unisa.diem.dao.interfacce.SessioneDAO;
import it.unisa.diem.dao.postgres.SessioneDAOPostgres;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.sessione.Sessione;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Servizio per l'inserimento di una sessione nel database.
 * Estende la classe Service di JavaFX per l'esecuzione in background.
 */
public class InsertSessionService extends Service<Sessione> {

    private SessioneDAO sessioneDAO;
    private Sessione sessione;

    /**
     * Costruttore della classe.
     *
     * @param sessioneDAO DAO per l'accesso alle sessioni
     * @param sessione Sessione da inserire nel database
     */
    public InsertSessionService(SessioneDAO sessioneDAO, Sessione sessione) {
        this.sessioneDAO = sessioneDAO;
        this.sessione = sessione;
    }

    /**
     * Crea e restituisce un Task per l'inserimento della sessione.
     *
     * @return Task per l'inserimento della sessione
     * @throws Exception se vi è un errore durante l'inserimento della sessione
     */
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