package it.unisa.diem.dao.interfacce;

import it.unisa.diem.model.gestione.utenti.Utente;

public interface UtenteDAO extends DAO<Utente> {



    void update(String oldUsername, Utente utente);
    void delete(Utente utente);

}
