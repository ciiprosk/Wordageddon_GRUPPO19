package it.unisa.diem.model.gestione.utenti;

import static it.unisa.diem.model.gestione.utenti.SicurezzaPassword.*;

public class Utente {

    private String username;
    private String email;
    private String hashedPassword;
    private Ruolo ruolo;
    private final byte[] salt;


    //serve per la registrazione
    public Utente(String username, String email, String password) {
        //Costruttore utilizzato nella registrazione di un nuovo utente
        //password da hashare, salt da generare, ruolo di default USER
        this.username = username;
        this.email = email;
        this.salt = generaSalt();
        this.hashedPassword = hashPassword(password, salt);
        this.ruolo = Ruolo.USER;
    }

    //serve per il login
    public Utente(String username, String email, String hashedPassword, byte[] salt, Ruolo ruolo) {
        //Costruttore utilizzato per recuperare un utente da DB
        //password già hashata, salt già generato
        this.username = username;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
        this.ruolo = ruolo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }


    public String getEmail() {
        return email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public byte[] getSalt() {
        return salt;
    }



    @Override
    public boolean equals(Object o) {

        if (o == null)  return false;

        if (o == this) return true;

        if (o.getClass() != Utente.class) return false;

        Utente temp = (Utente) o;

        return temp.username.equals(this.username);
    }

    /*
    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    MANCA UN METODO PER VERIFICARE CHE LA PASSWORD INSERITA SIA CORRETTA
    CREDO ANDREBBE CREATA UN'ALTRA CLASSE PER FARE QUESTA COSA
    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */

    @Override
    public String toString() {
        return this.username+ " "+ this.email + " "+ this.hashedPassword+ " "+ this.ruolo.toString();
    }

}
