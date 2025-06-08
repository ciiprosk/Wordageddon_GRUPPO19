package it.unisa.diem.dao.postgres;

import it.unisa.diem.dao.interfacce.StopwordDAO;
import it.unisa.diem.model.gestione.analisi.stopword.StopwordManager;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StopwordDAOPostgres implements StopwordDAO {


    public Optional<StopwordManager> selectById(long id) {
        return Optional.empty();
    }

    @Override
    public List<StopwordManager> selectAll() {
        return Collections.emptyList();
    }

    @Override
    public void insert(StopwordManager stopword) {

    }

    @Override
    public void update(StopwordManager stopword) {

    }

    @Override
    public void delete(StopwordManager stopword) {

    }

}
