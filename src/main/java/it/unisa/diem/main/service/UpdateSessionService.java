package it.unisa.diem.main.service;

import it.unisa.diem.dao.interfacce.SessioneDAO;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.sessione.GameSession;
import it.unisa.diem.model.gestione.sessione.Sessione;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.time.LocalDateTime;

public class UpdateSessionService extends Service<Void> {
    /*


        try {
            Sessione sessione = sessioneDAO.selectById(gameSession.getSessioneId()).orElseThrow();
            sessione.setCompletato(true);
            sessione.setPunteggio(gameSession.getScore());
            sessioneDAO.update(sessione);
            System.out.println("✅ Sessione completata e punteggio salvato: " + gameSession.getScore());
        } catch (DBException e) {
            showAlert("Errore nel salvataggio del punteggio: " + e.getMessage());
        }

     */
    private final SessioneDAO sessioneDAO;
    private GameSession gameSession;
    public UpdateSessionService(SessioneDAO sessioneDAO,GameSession gameSession) {
        this.sessioneDAO = sessioneDAO;

        this.gameSession = gameSession;
    }
    @Override
    protected  Task<Void> createTask(){
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Sessione sessione = sessioneDAO.selectById(gameSession.getSessioneId()).orElseThrow();
                    sessione.setFine(LocalDateTime.now());
                    sessione.setPunteggio(gameSession.getScore());
                    sessioneDAO.update(sessione);
                }catch (DBException e){
                    throw e;
                }
                return null;
            }
        };
    }
}
