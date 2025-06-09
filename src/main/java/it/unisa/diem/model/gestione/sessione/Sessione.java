package it.unisa.diem.model.gestione.sessione;

import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Lingua;
import it.unisa.diem.model.gestione.utenti.Utente;

import java.time.LocalDateTime;

//ciao


public class Sessione {

    private long id;
    private final Utente utente;
    private boolean completato;
    private final LocalDateTime inizio;
    private int punteggio;


    public Sessione(Utente utente, LocalDateTime inizio) {

        this.utente = utente;
        this.inizio = inizio;
        this.punteggio = 0;
        this.completato = false;

    }

    public Sessione (long id, Utente utente, LocalDateTime inizio, int punteggio, boolean completato) {

        this.id = id;
        this.utente = utente;
        this.inizio = inizio;
        this.punteggio = punteggio;
        this.completato = completato;

    }

    //getter e setter
    public Utente getUtente() {
        return utente;
    }

    public LocalDateTime getInizio() {
        return inizio;
    }

    public long getId() {
        return id;
    }

    public int getPunteggio() {
        return punteggio;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPunteggio(int punteggio) {
        this.punteggio = punteggio;
    }

    public boolean isCompletato() {
        return completato;
    }


    public void setCompletato(boolean completato) {

        if (this.completato)    //se la sessione era già completata, non cambia nulla
            return;

        this.completato = completato;

    }

    @Override
    public boolean equals(Object o) {

        if (o == null) return false;

        if (this == o)  return true;

        if (this.getClass() != o.getClass()) return false;

        Sessione sessione = (Sessione) o;

        return this.id == sessione.id;

    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

}
