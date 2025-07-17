package it.unisa.diem.model.gestione.sessione;

import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Lingua;

import java.time.LocalDateTime;

/**
 * Classe che rappresenta una voce dello storico delle sessioni.
 * Contiene informazioni sulla data di fine, il punteggio ottenuto e la lingua utilizzata.
 */
public class VoceStorico {

    private final LocalDateTime dataFine;
    private final int punteggio;
    private final Lingua lingua;

    /**
     * Costruttore per creare una nuova voce dello storico.
     *
     * @param dataFine la data e ora di fine della sessione
     * @param punteggio il punteggio ottenuto nella sessione
     * @param lingua la lingua utilizzata durante la sessione
     */
    public VoceStorico(LocalDateTime dataFine, int punteggio, Lingua lingua) {
        this.dataFine = dataFine;
        this.punteggio = punteggio;
        this.lingua = lingua;
    }

    /**
     * Restituisce la data di fine della sessione.
     *
     * @return la data di fine
     */
    public LocalDateTime getDataFine() {
        return dataFine;
    }

    /**
     * Restituisce il punteggio ottenuto nella sessione.
     *
     * @return il punteggio
     */
    public int getPunteggio() {
        return punteggio;
    }

    /**
     * Restituisce la lingua utilizzata durante la sessione.
     *
     * @return la lingua
     */
    public Lingua getLingua() {
        return lingua;
    }
}