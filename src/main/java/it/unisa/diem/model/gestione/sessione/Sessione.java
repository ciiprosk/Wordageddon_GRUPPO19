package it.unisa.diem.model.gestione.sessione;

import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Lingua;
import it.unisa.diem.model.gestione.utenti.Utente;

import java.time.LocalDateTime;

/**
 * Classe che rappresenta una sessione di gioco di un utente
 */
public class Sessione {

    private long id;
    private final Utente utente;
    private final LocalDateTime inizio;
    private LocalDateTime fine;
    private int punteggio;

    /**
     * Costruttore per creare una nuova sessione
     * @param utente L'utente associato alla sessione
     * @param inizio Data e ora di inizio della sessione
     */
    public Sessione(Utente utente, LocalDateTime inizio) {
        this.utente = utente;
        this.inizio = inizio;
        this.punteggio = 0;
        this.fine = LocalDateTime.now();
    }

    /**
     * Costruttore per creare una sessione con tutti i parametri
     * @param id Identificativo della sessione
     * @param utente L'utente associato alla sessione
     * @param inizio Data e ora di inizio della sessione
     * @param punteggio Punteggio ottenuto nella sessione
     * @param fine Data e ora di fine della sessione
     */
    public Sessione(long id, Utente utente, LocalDateTime inizio, int punteggio, LocalDateTime fine) {
        this.id = id;
        this.utente = utente;
        this.inizio = inizio;
        this.punteggio = punteggio;
        this.fine = fine;
    }

    /**
     * Costruttore per creare una sessione con solo l'ID
     * @param id Identificativo della sessione
     */
    public Sessione(long id) {
        this.id = id;
        this.utente = null;
        this.inizio = null;
        this.fine = null;
        this.punteggio = 0;
    }

    /**
     * Restituisce l'utente associato alla sessione
     * @return L'utente della sessione
     */
    public Utente getUtente() {
        return utente;
    }

    /**
     * Restituisce la data e ora di inizio della sessione
     * @return Data e ora di inizio
     */
    public LocalDateTime getInizio() {
        return inizio;
    }

    /**
     * Restituisce l'ID della sessione
     * @return ID della sessione
     */
    public long getId() {
        return id;
    }

    /**
     * Restituisce il punteggio della sessione
     * @return Punteggio della sessione
     */
    public int getPunteggio() {
        return punteggio;
    }

    /**
     * Restituisce la data e ora di fine della sessione
     * @return Data e ora di fine
     */
    public LocalDateTime getFine() {
        return fine;
    }

    /**
     * Imposta l'ID della sessione
     * @param id Nuovo ID della sessione
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Imposta il punteggio della sessione
     * @param punteggio Nuovo punteggio della sessione
     */
    public void setPunteggio(int punteggio) {
        this.punteggio = punteggio;
    }

    /**
     * Imposta la data e ora di fine della sessione
     * @param fine Nuova data e ora di fine
     */
    public void setFine(LocalDateTime fine) {
        this.fine = fine;
    }

    /**
     * Confronta due sessioni per verificarne l'uguaglianza
     * @param o Oggetto da confrontare
     * @return true se le sessioni sono uguali, false altrimenti
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (this.getClass() != o.getClass()) return false;
        Sessione sessione = (Sessione) o;
        return this.id == sessione.id;
    }

    /**
     * Restituisce l'hash code della sessione
     * @return Hash code calcolato sull'ID
     */
    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}