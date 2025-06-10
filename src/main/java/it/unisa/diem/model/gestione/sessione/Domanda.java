package it.unisa.diem.model.gestione.sessione;

import java.util.List;
import it.unisa.diem.utility.TipoDomanda;

//ciao


public class Domanda {

    private long id;
    private final Sessione sessione;
    private final int numeroDomanda;
    private final String testoDomanda;
    private final TipoDomanda tipo;
    private List<String> opzioni;  // 4 opzioni di risposta
    private final String rispostaCorretta;
    private int tempoLimiteSecondi;

    public Domanda(Sessione sessione, int numeroDomanda, String testoDomanda, TipoDomanda tipo, List<String> opzioni, String rispostaCorretta) {

        this.sessione = sessione;
        this.numeroDomanda = numeroDomanda;
        this.testoDomanda = testoDomanda;
        this.tipo = tipo;
        this.opzioni = opzioni;
        this.rispostaCorretta = rispostaCorretta;
        this.tempoLimiteSecondi = 15;

    }

    public Domanda(long id, Sessione sessione, int numeroDomanda, String testoDomanda, TipoDomanda tipo, String rispostaCorretta, int tempoLimiteSecondi) {

        this.id = id;
        this.sessione = sessione;
        this.numeroDomanda = numeroDomanda;
        this.testoDomanda = testoDomanda;
        this.tipo = tipo;
        this.rispostaCorretta = rispostaCorretta;
        this.tempoLimiteSecondi = tempoLimiteSecondi;

    }

    // Getter
    public String getTestoDomanda() {
        return testoDomanda;
    }

    public TipoDomanda getTipo() {
        return tipo;
    }

    public List<String> getOpzioni() {
        return opzioni;
    }

    public String getRispostaCorretta() {
        return rispostaCorretta;
    }

    public int getTempoLimiteSecondi() {
        return tempoLimiteSecondi;
    }

    // Verifica se la risposta dell'utente è corretta
    public boolean verificaRisposta(String rispostaUtente) {
        return rispostaCorretta.equalsIgnoreCase(rispostaUtente);
    }

    public long getId() {
        return id;
    }

    public Sessione getSessione() {
        return sessione;
    }

    public int getNumeroDomanda() {
        return numeroDomanda;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null)  return false;

        if (this == o) return true;

        if (o.getClass() != this.getClass()) return false;

        Domanda domanda = (Domanda) o;

        return this.getId() == domanda.getId();

    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.getId());
    }


}

