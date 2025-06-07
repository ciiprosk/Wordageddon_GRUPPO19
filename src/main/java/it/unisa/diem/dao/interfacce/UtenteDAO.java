package it.unisa.diem.dao.interfacce;

import it.unisa.diem.model.gestione.utenti.Utente;

import java.util.Optional;

public interface UtenteDAO extends DAO<Utente> {

    Optional<Utente> selectByUsername(String username);

    void update(String oldUsername, Utente utente);

}
