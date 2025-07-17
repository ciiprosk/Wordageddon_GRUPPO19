package it.unisa.diem.model.gestione.utenti;

import static it.unisa.diem.model.gestione.utenti.SicurezzaPassword.*;

/**
 * Classe che rappresenta un utente del sistema.
 * Gestisce le informazioni dell'utente come username, email, password hashata, ruolo e salt.
 */
public class Utente {

    private String username;
    private String email;
    private String hashedPassword;
    private Ruolo ruolo;
    private final byte[] salt;

    /**
     * Costruttore per la registrazione di un nuovo utente.
     * Genera automaticamente il salt e hasha la password fornita.
     * Assegna il ruolo di default USER.
     *
     * @param username Nome utente
     * @param email Email dell'utente
     * @param password Password in chiaro da hashare
     */
    public Utente(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.salt = generaSalt();
        this.hashedPassword = hashPassword(password, salt);
        this.ruolo = Ruolo.USER;
    }

    /**
     * Costruttore per il login e il recupero da database.
     * Utilizza valori già esistenti per password hashata, salt e ruolo.
     *
     * @param username Nome utente
     * @param email Email dell'utente
     * @param hashedPassword Password già hashata
     * @param Salt già generato
     * @param ruolo Ruolo dell'utente
     */
    public Utente(String username, String email, String hashedPassword, byte[] salt, Ruolo ruolo) {
        this.username = username;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
        this.ruolo = ruolo;
    }

    /**
     * @return Il nome utente
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username Nuovo nome utente da impostare
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @param email Nuova email da impostare
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param hashedPassword Nuova password hashata da impostare
     */
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    /**
     * @param ruolo Nuovo ruolo da impostare
     */
    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

    /**
     * @return L'email dell'utente
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return La password hashata
     */
    public String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * @return Il ruolo dell'utente
     */
    public Ruolo getRuolo() {
        return ruolo;
    }

    /**
     * @return Il salt utilizzato per l'hashing della password
     */
    public byte[] getSalt() {
        return salt;
    }

    /**
     * Confronta questo utente con un altro oggetto per verificarne l'uguaglianza.
     * Due utenti sono considerati uguali se hanno lo stesso username.
     *
     * @param o Oggetto da confrontare
     * @return true se gli oggetti sono uguali, false altrimenti
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != Utente.class) return false;

        Utente temp = (Utente) o;
        return temp.username.equals(this.username);
    }

    /**
     * @return Una stringa che rappresenta l'utente con tutte le sue informazioni
     */
    @Override
    public String toString() {
        return this.username + " " + this.email + " " + this.hashedPassword + " " + this.ruolo.toString();
    }
}