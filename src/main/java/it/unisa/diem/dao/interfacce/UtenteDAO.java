package it.unisa.diem.dao.interfacce;

import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.utenti.Utente;

import java.sql.SQLException;
import java.util.Optional;

public interface UtenteDAO extends DAO<Utente> {

    boolean emailAlreadyExists(String email) throws SQLException, DBException;

    boolean usernameAlreadyExists(String username) throws SQLException, DBException;

    Optional<Utente> selectByUsername(String username) throws SQLException, DBException;

    void update(String oldUsername, Utente utente) throws SQLException, DBException;

}
