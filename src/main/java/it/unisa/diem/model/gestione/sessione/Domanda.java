package it.unisa.diem.model.gestione.sessione;

import java.util.List;
import it.unisa.diem.utility.TipoDomanda;

//ciao


public class Domanda {
    private String testoDomanda;
    private TipoDomanda tipo;
    private List<String> opzioni;  // 4 opzioni di risposta
    private String rispostaCorretta;
    private int tempoLimiteSecondi;

    public Domanda(String testoDomanda, TipoDomanda tipo, List<String> opzioni, String rispostaCorretta) {
        this.testoDomanda = testoDomanda;
        this.tipo = tipo;
        this.opzioni = opzioni;
        this.rispostaCorretta = rispostaCorretta;
        this.tempoLimiteSecondi = 15;

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
}

