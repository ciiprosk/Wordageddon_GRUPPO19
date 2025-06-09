package it.unisa.diem.model.gestione.sessione;

import it.unisa.diem.model.gestione.utenti.Utente;

import java.time.LocalDateTime;

public class StoricoSessione extends Sessione{

    private final LocalDateTime dataFine;

    public StoricoSessione(long id, Utente utente, LocalDateTime inizio, int punteggio) {

        super(id, utente, inizio, punteggio, true);
        this.dataFine = LocalDateTime.now();

    }

    public StoricoSessione(long id, Utente utente, LocalDateTime inizio, int punteggio, LocalDateTime dataFine) {
        super(id, utente, inizio, punteggio, true);
        this.dataFine = dataFine;
    }

    public LocalDateTime getDataFine() {
        return dataFine;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null)  return false;

        if (o == this) return true;

        if (o.getClass() != StoricoSessione.class) return false;

        StoricoSessione s = (StoricoSessione) o;

        return this.getId() == s.getId();
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.getId());
    }

    @Override
    public String toString() {
        return "Storico di id: " + this.getId() + ", data fine: " + this.dataFine + "e punteggio: " + super.getPunteggio();
    }

}
