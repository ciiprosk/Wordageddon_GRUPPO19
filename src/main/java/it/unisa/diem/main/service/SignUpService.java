package it.unisa.diem.main.service;

import it.unisa.diem.dao.interfacce.UtenteDAO;
import it.unisa.diem.dao.postgres.UtenteDAOPostgres;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.utenti.Utente;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.sql.SQLException;

/**
 * Servizio per la registrazione di un nuovo utente.
 * Verifica la disponibilità di email e username e, se disponibili, procede con l'inserimento nel database.
 */
public class SignUpService extends Service<Boolean> {

    private final String email;
    private final String username;
    private final String password;
    private final UtenteDAO utentePostgres;

    private boolean emailInUse = false;
    private boolean usernameInUse = false;

    /**
     * Costruttore del servizio di registrazione.
     *
     * @param email L'email dell'utente da registrare
     * @param username Lo username dell'utente da registrare
     * @param password La password dell'utente da registrare
     * @param utentePostgres L'istanza del DAO per l'interazione con il database
     */
    public SignUpService(String email, String username, String password, UtenteDAO utentePostgres) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.utentePostgres = utentePostgres;
    }

    /**
     * Verifica se l'email è già in uso.
     *
     * @return true se l'email è già in uso, false altrimenti
     */
    public boolean isEmailInUse() {
        return emailInUse;
    }

    /**
     * Verifica se lo username è già in uso.
     *
     * @return true se lo username è già in uso, false altrimenti
     */
    public boolean isUsernameInUse() {
        return usernameInUse;
    }

    /**
     * Crea e restituisce il task per la registrazione dell'utente.
     * Il task verifica la disponibilità di email e username e, se disponibili, inserisce il nuovo utente nel database.
     *
     * @return Il task per la registrazione
     */
    @Override
    protected Task<Boolean> createTask() {
        return new Task<>() {
            @Override
            protected Boolean call() {
                try {
                    if (utentePostgres.emailAlreadyExists(email)) {
                        emailInUse = true;
                        return false;
                    }

                    if (utentePostgres.usernameAlreadyExists(username)) {
                        usernameInUse = true;
                        return false;
                    }

                    utentePostgres.insert(new Utente(username, email, password));
                    return true;

                } catch (SQLException | DBException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        };
    }
}