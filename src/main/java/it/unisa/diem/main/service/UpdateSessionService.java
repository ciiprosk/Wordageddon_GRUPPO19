package it.unisa.diem.main.service;

import it.unisa.diem.dao.interfacce.SessioneDAO;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.sessione.GameSession;
import it.unisa.diem.model.gestione.sessione.Sessione;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.time.LocalDateTime;

/**
 * Servizio per l'aggiornamento di una sessione di gioco nel database.
 * Estende la classe di JavaFX per l'esecuzione in background.
 */
public class UpdateSessionService extends Service<Void> {
    private final SessioneDAO sessioneDAO;
    private GameSession gameSession;

    /**
     * Costruttore della classe.
     *
     * @param sessioneDAO DAO per l'accesso alle sessioni nel database
     * @param gameSession Sessione di gioco da aggiornare
     */
    public UpdateSessionService(SessioneDAO sessioneDAO, GameSession gameSession) {
        this.sessioneDAO = sessioneDAO;
        this.gameSession = gameSession;
    }

    /**
     * Crea e restituisce un Task per l'aggiornamento della sessione.
     *
     * @return Task per l'aggiornamento della sessione
     * @throws Exception se non trova la sessione con il giusto id
     */
    @Override
    protected Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Sessione sessione = sessioneDAO.selectById(gameSession.getSessioneId()).orElseThrow(() -> new DBException("Sessione con ID " + gameSession.getSessioneId() + " non trovata"));
                    sessione.setFine(LocalDateTime.now());
                    sessione.setPunteggio(gameSession.getScore());
                    sessioneDAO.update(sessione);
                } catch (DBException e) {
                    throw e;
                }
                return null;
            }
        };
    }
}