package it.unisa.diem.model.gestione.sessione;

import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Lingua;
import it.unisa.diem.model.gestione.utenti.Utente;

import java.time.LocalDateTime;

//ciao


public class Sessione {

    private static long id=0;
    private final Utente utente;
    private boolean completato;
    private Difficolta difficolta;    //tipo enum?????????????????????????????????
    private Lingua lingua;
    private final LocalDateTime inizio;
    private int punteggio;
    //private LocalDateTime fine;   //giusto oppure creiamo la sessione passata??????
    //documenti e domande????

    public Sessione(Utente utente, Difficolta difficolta, LocalDateTime inizio) {

        this.utente = utente;
        this.difficolta = difficolta;
        this.inizio = inizio;
        //this.fine=null;
        this.punteggio = 0;
        this.completato = false;

    }

    public Utente getUtente() {
        return utente;
    }



    public LocalDateTime getInizio() {
        return inizio;
    }

    public int getPunteggio() {
        return punteggio;
    }

    public void setPunteggio(int punteggio) {
        this.punteggio = punteggio;
    }

    public boolean isCompletato() {
        return completato;
    }

    public void setCompletato(boolean completato) {
        this.completato = completato;

        //if (completato)
           // this.fine=LocalDateTime.now();
    }



}
