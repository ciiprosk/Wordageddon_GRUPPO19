package it.unisa.diem.main.service;

import it.unisa.diem.dao.interfacce.UtenteDAO;
import it.unisa.diem.dao.postgres.UtenteDAOPostgres;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.utenti.Utente;
import it.unisa.diem.utility.AlertUtils;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.util.Optional;

import static it.unisa.diem.model.gestione.utenti.SicurezzaPassword.verificaPassword;

/**
 * Servizio per il login di un utente.
 * Estende la classe Service di JavaFX per eseguire l'operazione in background.
 */
public class LoginService extends Service<Utente> {

    private final String username;
    private final String password;
    private final UtenteDAO utentePostgres;

    /**
     * Costruttore del servizio di login.
     *
     * @param username lo username dell'utente che sta effettuando il login
     * @param password la password dell'utente che sta effettuando il login
     * @param utentePostgres l'DAO per l'accesso ai dati degli utenti
     */
    public LoginService(String username, String password, UtenteDAO utentePostgres) {
        this.username = username;
        this.password = password;
        this.utentePostgres = utentePostgres;
    }

    /**
     * Crea e restituisce un Task per l'operazione di login.
     *
     * @return un Task che esegue il login e restituisce l'utente se autenticato, null altrimenti
     */
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