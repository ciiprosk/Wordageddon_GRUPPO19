package it.unisa.diem.dao.postgres;

import it.unisa.diem.dao.interfacce.StopwordDAO;
import it.unisa.diem.model.gestione.analisi.Stopword;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StopwordDAOPostgres implements StopwordDAO {


    public Optional<Stopword> selectById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Stopword> selectAll() {
        return Collections.emptyList();
    }

    @Override
    public void insert(Stopword stopword) {

    }

    @Override
    public void update(Stopword stopword) {

    }

    @Override
    public void delete(Stopword stopword) {

    }

}
