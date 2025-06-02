package it.unisa.diem.model.gestione.utenti;

public class Utente {

    private String username;
    private String email;
    private String password;


    public Utente(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;

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

    public String getPassword() {
        return password;
    }


    public boolean equals(Utente o) {

        if (o == null)  return false;

        if (o == this) return true;

        if (o.getClass() != Utente.class) return false;

        Utente temp = (Utente) o;
        return true;
       // return this.getId() == temp.getId();
    }

}
