package it.unisa.diem.utility;

import it.unisa.diem.model.gestione.utenti.Utente;

/**
 * Gestisce la sessione corrente dell'utente nell'applicazione.
 * Implementa il pattern Singleton per garantire un'unica istanza condivisa.
 */
public class SessionManager {
    private static SessionManager instance;
    private Utente utenteLoggato;

    private SessionManager() {}

    /**
     * Restituisce l'unica istanza di SessionManager.
     *
     * @return l'istanza singleton di SessionManager
     */
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Effettua il login dell'utente specificato, salvandolo nella sessione.
     *
     * @param utente l'utente che ha effettuato l'accesso
     */
    public void login(Utente utente) {
        this.utenteLoggato = utente;
    }

    /**
     * Restituisce l'utente attualmente loggato nella sessione.
     *
     * @return l'utente loggato o null se nessun utente è loggato
     */
    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }

    /**
     * Esegue il logout dell'utente attualmente loggato, svuotando la sessione.
     */
    public void logout() {
        this.utenteLoggato = null;
    }

    /**
     * Verifica se un utente è attualmente loggato nella sessione.
     *
     * @return {@code true} se un utente è loggato, altrimenti {@code false}
     */
    public boolean isLoggedIn() {
        return utenteLoggato != null;
    }
}
