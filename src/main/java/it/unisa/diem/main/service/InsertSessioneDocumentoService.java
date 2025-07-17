package it.unisa.diem.main.service;

import it.unisa.diem.dao.interfacce.SessioneDocumentoDAO;
import it.unisa.diem.model.gestione.analisi.Analisi;
import it.unisa.diem.model.gestione.sessione.GameSession;
import it.unisa.diem.model.gestione.sessione.SessioneDocumento;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Servizio per l'inserimento di una SessioneDocumento nel database.
 * Estende la classe Service di JavaFX per eseguire l'operazione in background.
 */
public class InsertSessioneDocumentoService extends Service<Void> {
    final private SessioneDocumentoDAO dao;
    final private SessioneDocumento sd;

    /**
     * Costruttore della classe InsertSessioneDocumentoService.
     *
     * @param dao DAO per l'accesso ai dati delle sessioni documento
     * @param sd SessioneDocumento da inserire nel database
     * @param gs GameSession associata (non utilizzata nel costruttore)
     * @param analisi Analisi associata (non utilizzata nel costruttore)
     */
    public InsertSessioneDocumentoService(SessioneDocumentoDAO dao, SessioneDocumento sd, GameSession gs, Analisi analisi) {
        this.dao = dao;
        this.sd = sd;
    }

    /**
     * Crea e restituisce un Task per l'inserimento della SessioneDocumento.
     *
     * @return Task<Void> che esegue l'operazione di inserimento
     */
    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            /**
             * Metodo chiamato quando il Task viene eseguito.
             * Esegue l'inserimento della SessioneDocumento nel database.
             *
             * @return null
             * @throws Exception se si verifica un errore durante l'inserimento
             */
            @Override
            protected Void call() throws Exception {
                dao.insert(sd);
                return null;
            }
        };
    }
}