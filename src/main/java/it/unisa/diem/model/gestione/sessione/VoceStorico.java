package it.unisa.diem.model.gestione.sessione;

import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Lingua;

import java.time.LocalDateTime;

public class VoceStorico {

    private final LocalDateTime dataFine;
    private final int punteggio;
    private final Lingua lingua;

    public VoceStorico(LocalDateTime dataFine, int punteggio, Lingua lingua) {
        this.dataFine = dataFine;
        this.punteggio = punteggio;
        this.lingua = lingua;
    }

    public LocalDateTime getDataFine() {
        return dataFine;
    }

    public int getPunteggio() {
        return punteggio;
    }

    public Lingua getLingua() {
        return lingua;
    }

}
