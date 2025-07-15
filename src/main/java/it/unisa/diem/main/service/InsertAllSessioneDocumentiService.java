package it.unisa.diem.main.service;

import it.unisa.diem.dao.interfacce.SessioneDocumentoDAO;
import it.unisa.diem.dao.postgres.SessioneDocumentoDAOPostgres;
import it.unisa.diem.model.gestione.analisi.Analisi;
import it.unisa.diem.model.gestione.sessione.GameSession;
import it.unisa.diem.model.gestione.sessione.SessioneDocumento;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

public class InsertAllSessioneDocumentiService extends Service<Void> {
    private final List<Analisi> analisiList;
    private final SessioneDocumentoDAO sessioneDocumentoDAO;
    private final long sessioneId;
    private final GameSession gameSession;

    public InsertAllSessioneDocumentiService(List<Analisi> analisiList,
                                             SessioneDocumentoDAO sessioneDocumentoDAO,
                                             long sessioneId,
                                             GameSession gameSession) {
        this.analisiList = analisiList;
        this.sessioneDocumentoDAO = sessioneDocumentoDAO;
        this.sessioneId = sessioneId;
        this.gameSession = gameSession;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                for (Analisi analisi : analisiList) {
                    SessioneDocumento sd = new SessioneDocumento(sessioneId, analisi.getTitolo());
                    sessioneDocumentoDAO.insert(sd); // inserimento sincrono
                }
                return null;
            }
        };
    }
}
