package it.unisa.diem.main.service;

import it.unisa.diem.dao.interfacce.UtenteDAO;
import it.unisa.diem.dao.postgres.UtenteDAOPostgres;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.utenti.Utente;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.sql.SQLException;
import java.util.Optional;

import static it.unisa.diem.model.gestione.utenti.SicurezzaPassword.verificaPassword;

public class LoginService extends Service<Utente> {

    private final String username;
    private final String password;
    private final UtenteDAO utentePostgres;

    public LoginService(String username, String password, UtenteDAO utentePostgres) {
        this.username = username;
        this.password = password;
        this.utentePostgres = utentePostgres;
    }

    @Override
    protected Task<Utente> createTask() {
        return new Task<>() {
            @Override
            protected Utente call() {
                try {
                    Optional<Utente> optionalUser = utentePostgres.selectByUsername(username);
                    if (optionalUser.isEmpty()) {
                        return null;
                    }

                    Utente utente = optionalUser.get();
                    boolean passwordCorretta = verificaPassword(password, utente.getHashedPassword(), utente.getSalt());

                    return passwordCorretta ? utente : null;

                } catch (SQLException | DBException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }
}
