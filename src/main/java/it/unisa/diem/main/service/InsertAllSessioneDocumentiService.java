package it.unisa.diem.main.service;

import it.unisa.diem.dao.interfacce.SessioneDocumentoDAO;
import it.unisa.diem.dao.postgres.SessioneDocumentoDAOPostgres;
import it.unisa.diem.model.gestione.analisi.Analisi;
import it.unisa.diem.model.gestione.sessione.GameSession;
import it.unisa.diem.model.gestione.sessione.SessioneDocumento;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

/**
 * Servizio per l'inserimento di tutti i documenti di sessione associati a una lista di analisi.
 * Estende la classe {@link Service} di JavaFX per l'esecuzione in background.
 */
public class InsertAllSessioneDocumentiService extends Service<Void> {
    private final List<Analisi> analisiList;
    private final SessioneDocumentoDAO sessioneDocumentoDAO;
    private final long sessioneId;
    private final GameSession gameSession;

    /**
     * Costruttore della classe.
     *
     * @param analisiList lista delle analisi da associare ai documenti di sessione
     * @param sessioneDocumentoDAO DAO per l'accesso ai dati dei documenti di sessione
     * @param sessioneId ID della sessione a cui associare i documenti
     * @param gameSession sessione di gioco relativa ai documenti
     */
    public InsertAllSessioneDocumentiService(List<Analisi> analisiList,
                                             SessioneDocumentoDAO sessioneDocumentoDAO,
                                             long sessioneId,
                                             GameSession gameSession) {
        this.analisiList = analisiList;
        this.sessioneDocumentoDAO = sessioneDocumentoDAO;
        this.sessioneId = sessioneId;
        this.gameSession = gameSession;
    }

    /**
     * Crea e restituisce un Task per l'inserimento dei documenti di sessione.
     *
     * @return Task che esegue l'inserimento dei documenti
     */
    @Override
    protected Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                for (Analisi analisi : analisiList) {
                    SessioneDocumento sd = new SessioneDocumento(sessioneId, analisi.getTitolo());
                    sessioneDocumentoDAO.insert(sd);
                }
                return null;
            }
        };
    }
}