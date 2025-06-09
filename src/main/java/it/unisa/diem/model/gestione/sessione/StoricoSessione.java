package it.unisa.diem.model.gestione.sessione;

import java.time.LocalDateTime;

public class StoricoSessione {

    private final Sessione sessione;
    private final LocalDateTime dataFine;

    public StoricoSessione(Sessione sessione) {
        this.sessione = sessione;
        this.dataFine = LocalDateTime.now();
    }

    public StoricoSessione(Sessione sessione, LocalDateTime dataFine) {
        this.sessione = sessione;
        this.dataFine = dataFine;
    }

    public Sessione getSessione() {
        return sessione;
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

        return this.sessione.equals(s.sessione);
    }

    @Override
    public int hashCode() {
        return sessione.hashCode();
    }

}
