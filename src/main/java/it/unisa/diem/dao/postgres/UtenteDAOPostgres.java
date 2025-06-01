package main.java.it.unisa.diem.dao.postgres;

import main.java.it.unisa.diem.dao.interfacce.UtenteDAO;
import main.java.it.unisa.diem.model.gestioneutenti.Utente;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UtenteDAOPostgres implements UtenteDAO {

    @Override
    public Optional<Utente> selectById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Utente> selectAll() {
        return Collections.emptyList();
    }

    @Override
    public void insert(Utente utente) {

    }

    @Override
    public void update(Utente utente) {

    }

    @Override
    public void delete(long id) {

    }

}
