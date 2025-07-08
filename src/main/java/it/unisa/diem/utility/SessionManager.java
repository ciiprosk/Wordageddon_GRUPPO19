package it.unisa.diem.utility;

import it.unisa.diem.model.gestione.utenti.Utente;

public class SessionManager {
    private static SessionManager instance;
    private Utente utenteLoggato;

    private SessionManager() {}

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(Utente utente) {
        this.utenteLoggato = utente;
    }

    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }

    public void logout() {
        this.utenteLoggato = null;
    }

    public boolean isLoggedIn() {
        return utenteLoggato != null;
    }
}
