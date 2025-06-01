package it.unisa.diem.dao.postgres;

import it.unisa.diem.dao.interfacce.AnalisiDAO;
import it.unisa.diem.model.gestione.analisi.Analisi;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AnalisiDAOPostgres implements AnalisiDAO {


    @Override
    public Optional<Analisi> selectById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Analisi> selectAll() {
        return Collections.emptyList();
    }

    @Override
    public void insert(Analisi analisi) {

    }

    @Override
    public void update(Analisi analisi) {

    }

    @Override
    public void delete(long id) {

    }
}
