package it.unisa.diem.main.service;

import it.unisa.diem.dao.interfacce.SessioneDocumentoDAO;
import it.unisa.diem.model.gestione.analisi.Analisi;
import it.unisa.diem.model.gestione.sessione.GameSession;
import it.unisa.diem.model.gestione.sessione.SessioneDocumento;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class InsertSessioneDocumentoService extends Service<Void> {
    final private SessioneDocumentoDAO dao;
    final private SessioneDocumento sd;

    public InsertSessioneDocumentoService(SessioneDocumentoDAO dao, SessioneDocumento sd, GameSession gs, Analisi analisi) {
        this.dao = dao;
        this.sd = sd;

    }



    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                dao.insert(sd);
               return null;
            }
        };
    }
}
