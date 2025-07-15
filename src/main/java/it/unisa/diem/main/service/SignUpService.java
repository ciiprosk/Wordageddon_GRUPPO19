package it.unisa.diem.main.service;

import it.unisa.diem.dao.postgres.UtenteDAOPostgres;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.utenti.Utente;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.sql.SQLException;

public class SignUpService extends Service<Boolean> {

    private final String email;
    private final String username;
    private final String password;
    private final UtenteDAOPostgres utentePostgres;

    private boolean emailInUse = false;
    private boolean usernameInUse = false;

    public SignUpService(String email, String username, String password, UtenteDAOPostgres utentePostgres) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.utentePostgres = utentePostgres;
    }

    public boolean isEmailInUse() {
        return emailInUse;
    }

    public boolean isUsernameInUse() {
        return usernameInUse;
    }

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
