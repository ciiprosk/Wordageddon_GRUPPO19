package it.unisa.diem.model.gestione.sessione;

import it.unisa.diem.model.gestione.utenti.Utente;

import java.time.LocalDateTime;

//ciao


public class Sessione {

    private static long id=0;
    private final Utente utente;
    private boolean completato;
    private final String modalita;      //tipo enum?????????????????????????????????
    private final LocalDateTime inizio;
    private int punteggio;
    private LocalDateTime fine;   //giusto oppure creiamo la sessione passata??????
    //documenti e domande????

    public Sessione(Utente utente, String modalita, LocalDateTime inizio) {

        this.utente = utente;
        this.modalita = modalita;
        this.inizio = inizio;
        this.fine=null;
        this.punteggio = 0;
        this.completato = false;
        id++;
        this.id = id;

    }

    public Utente getUtente() {
        return utente;
    }

    public String getModalita() {
        return modalita;
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

        if (completato)
            this.fine=LocalDateTime.now();
    }



}
