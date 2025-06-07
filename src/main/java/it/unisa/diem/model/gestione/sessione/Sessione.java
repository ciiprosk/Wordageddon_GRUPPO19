package it.unisa.diem.model.gestione.sessione;

import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Lingua;
import it.unisa.diem.model.gestione.utenti.Utente;

import java.time.LocalDateTime;

//ciao


public class Sessione {

    private static long id=0;
    private Utente utente;
    private boolean completato;
    private Difficolta difficolta;    //tipo enum?????????????????????????????????
    private Lingua lingua;
    private LocalDateTime inizio;
    private int punteggio;
    private LocalDateTime fine;
    //documenti e domande????

    public Sessione(Utente utente, Difficolta difficolta, LocalDateTime inizio, LocalDateTime fine) {

        this.utente = utente;
        this.difficolta = difficolta;
        this.inizio = inizio;
        this.fine=null;
        this.punteggio = 0;
        this.completato = false;

    }

    //getter e setter
    public Utente getUtente() {return utente;}
    public void setUtente(Utente utente) {this.utente = utente;}
    public Difficolta getDifficolta() {return difficolta;}
    public void setDifficolta(Difficolta difficolta) {this.difficolta = difficolta;}
    public LocalDateTime getInizio() {return inizio;}
    public void setInizio(LocalDateTime inizio) {this.inizio=LocalDateTime.now();}
    public LocalDateTime getFine() {return fine;}
    public void setFine(LocalDateTime fine) {this.fine = fine;}
    public int getPunteggio() {return punteggio;}
    public void setPunteggio(int punteggio) {this.punteggio = punteggio;}
    public boolean isCompletato() {
        return completato;
    }

    public void setCompletato(boolean completato) {
        this.completato = completato;

        if (completato)
           this.fine=LocalDateTime.now();
    }



}
