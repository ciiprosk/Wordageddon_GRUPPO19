package it.unisa.diem.dao.postgres;

import it.unisa.diem.dao.interfacce.DAO;
import it.unisa.diem.model.gestione.sessione.Sessione;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SessioneDAOPostgres implements DAO<Sessione> {
    @Override
    public Optional<Sessione> selectById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Sessione> selectAll() {
        return Collections.emptyList();
    }

    @Override
    public void insert(Sessione sessione) {

    }

    @Override
    public void update(Sessione sessione) {

    }

    @Override
    public void delete(long id) {

    }
}
