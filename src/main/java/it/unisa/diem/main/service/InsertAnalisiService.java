package it.unisa.diem.main.service;

import it.unisa.diem.dao.interfacce.AnalisiDAO;
import it.unisa.diem.model.gestione.analisi.Analisi;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class InsertAnalisiService extends Service<Void> {

    AnalisiDAO analisiDao;
    Analisi analisi;

    public InsertAnalisiService(AnalisiDAO analisiDao, Analisi analisi) {
        this.analisiDao = analisiDao;
        this.analisi = analisi;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                analisiDao.insert(analisi);
                return null;
            }
        };
    }

}
