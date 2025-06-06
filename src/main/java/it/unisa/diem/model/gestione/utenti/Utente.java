package it.unisa.diem.model.gestione.utenti;

public class Utente {
//is admin va nel costruttore.--> final , lo prendi dal db
    private String username;
    private String email;
    private String hashedPassword;
    private Ruolo ruolo;
    private final byte[] salt = SicurezzaPassword.generaSalt();



    public Utente(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.hashedPassword = SicurezzaPassword.hashPassword(password, salt);
        this.ruolo = Ruolo.USER;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

}
