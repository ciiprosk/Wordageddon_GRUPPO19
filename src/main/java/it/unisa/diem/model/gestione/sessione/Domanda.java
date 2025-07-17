package it.unisa.diem.model.gestione.sessione;

import java.util.ArrayList;
import java.util.List;
import it.unisa.diem.utility.TipoDomanda;

public class Domanda {

    private long id;
    private Sessione sessione;
    private int numeroDomanda;
    private final String testoDomanda;
    private final TipoDomanda tipo;
    private List<String> opzioni;
    private final String rispostaCorretta;
    private String rispostaUtente;
    private int tempoLimiteSecondi;

    /**
     * Costruttore per creare una domanda completa
     * @param sessione La sessione a cui appartiene la domanda
     * @param numeroDomanda Il numero della domanda
     * @param testoDomanda Il testo della domanda
     * @param tipo Il tipo di domanda
     * @param opzioni La lista delle opzioni di risposta
     * @param rispostaCorretta La risposta corretta
     */
    public Domanda(Sessione sessione, int numeroDomanda, String testoDomanda, TipoDomanda tipo, List<String> opzioni, String rispostaCorretta) {
        this.sessione = sessione;
        this.numeroDomanda = numeroDomanda;
        this.testoDomanda = testoDomanda;
        this.tipo = tipo;
        this.opzioni = opzioni;
        this.rispostaCorretta = rispostaCorretta;
        this.tempoLimiteSecondi = 15;
    }

    /**
     * Costruttore alternativo per creare una domanda
     * @param id L'identificativo della domanda
     * @param sessione La sessione a cui appartiene la domanda
     * @param numeroDomanda Il numero della domanda
     * @param testoDomanda Il testo della domanda
     * @param tipo Il tipo di domanda
     * @param rispostaCorretta La risposta corretta
     * @param tempoLimiteSecondi Il tempo limite in secondi
     */
    public Domanda(long id, Sessione sessione, int numeroDomanda, String testoDomanda, TipoDomanda tipo, String rispostaCorretta, int tempoLimiteSecondi) {
        this.id = id;
        this.sessione = sessione;
        this.numeroDomanda = numeroDomanda;
        this.testoDomanda = testoDomanda;
        this.tipo = tipo;
        this.rispostaCorretta = rispostaCorretta;
        this.tempoLimiteSecondi = tempoLimiteSecondi;
    }

    /**
     * Costruttore semplificato per creare una domanda
     * @param testoDomanda Il testo della domanda
     * @param tipo Il tipo di domanda
     * @param opzioni La lista delle opzioni di risposta
     * @param rispostaCorretta La risposta corretta
     */
    public Domanda(String testoDomanda, TipoDomanda tipo, List<String> opzioni, String rispostaCorretta) {
        this.opzioni = opzioni;
        this.testoDomanda = testoDomanda;
        this.tipo = tipo;
        this.rispostaCorretta = rispostaCorretta;
    }

    /**
     * Restituisce il testo della domanda
     * @return Il testo della domanda
     */
    public String getTestoDomanda() {
        return testoDomanda;
    }

    /**
     * Restituisce il tipo di domanda
     * @return Il tipo di domanda
     */
    public TipoDomanda getTipo() {
        return tipo;
    }

    /**
     * Restituisce la lista delle opzioni di risposta
     * @return La lista delle opzioni
     */
    public List<String> getOpzioni() {
        return opzioni;
    }

    /**
     * Restituisce la risposta corretta
     * @return La risposta corretta
     */
    public String getRispostaCorretta() {
        return rispostaCorretta;
    }

    /**
     * Restituisce il tempo limite in secondi
     * @return Il tempo limite in secondi
     */
    public int getTempoLimiteSecondi() {
        return tempoLimiteSecondi;
    }

    /**
     * Verifica se la risposta dell'utente è corretta
     * @param rispostaUtente La risposta data dall'utente
     * @return true se la risposta è corretta, false altrimenti
     */
    public boolean verificaRisposta(String rispostaUtente) {
        return rispostaCorretta.equalsIgnoreCase(rispostaUtente);
    }

    /**
     * Restituisce l'identificativo della domanda
     * @return L'identificativo
     */
    public long getId() {
        return id;
    }

    /**
     * Restituisce la sessione a cui appartiene la domanda
     * @return La sessione
     */
    public Sessione getSessione() {
        return sessione;
    }

    /**
     * Restituisce il numero della domanda
     * @return Il numero della domanda
     */
    public int getNumeroDomanda() {
        return numeroDomanda;
    }

    /**
     * Imposta l'identificativo della domanda
     * @param id L'identificativo da impostare
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Confronta questa domanda con un altro oggetto
     * @param o L'oggetto da confrontare
     * @return true se gli oggetti sono uguali, false altrimenti
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (o.getClass() != this.getClass()) return false;
        Domanda domanda = (Domanda) o;
        return this.getId() == domanda.getId();
    }

    /**
     * Imposta la sessione a cui appartiene la domanda
     * @param sessione La sessione da impostare
     */
    public void setSessione(Sessione sessione) {
        this.sessione = sessione;
    }

    /**
     * Imposta il numero della domanda
     * @param numeroDomanda Il numero da impostare
     */
    public void setNumeroDomanda(int numeroDomanda) {
        this.numeroDomanda = numeroDomanda;
    }

    /**
     * Restituisce la risposta data dall'utente
     * @return La risposta dell'utente
     */
    public String getRispostaUtente() {
        return rispostaUtente;
    }

    /**
     * Imposta la risposta data dall'utente
     * @param rispostaUtente La risposta da impostare
     */
    public void setRispostaUtente(String rispostaUtente) {
        this.rispostaUtente = rispostaUtente;
    }

    /**
     * Restituisce il valore hash della domanda
     * @return Il valore hash
     */
    @Override
    public int hashCode() {
        return Long.hashCode(this.getId());
    }
}