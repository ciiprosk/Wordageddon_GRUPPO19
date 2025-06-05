package it.unisa.diem.dao.postgres;

import it.unisa.diem.dao.interfacce.UtenteDAO;
import it.unisa.diem.model.gestione.utenti.Utente;

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
        //inserisci il nuovo utente con insert
        //prima bisogna fare hasing della password inserita
        // da dove prendo i dati???? me lo passa utente

    }

    @Override
    public void update(Utente utente) {

    }

    @Override
    public void delete(long id) {
    }

}
