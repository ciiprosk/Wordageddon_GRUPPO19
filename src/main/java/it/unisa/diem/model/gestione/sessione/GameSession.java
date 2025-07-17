package it.unisa.diem.model.gestione.sessione;

import it.unisa.diem.model.gestione.analisi.Analisi;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Lingua;
import it.unisa.diem.model.gestione.utenti.Utente;

import java.util.List;

public class GameSession {

    private Utente utente;
    private Lingua lingua;
    private Difficolta difficolta;
    private long sessioneId;
    private List<Analisi> analyses;
    private List<Domanda> domande;
    private int currentQuestionIndex;
    private int score;

    /**
     * Costruttore per creare una nuova sessione di gioco.
     *
     * @param utente L'utente associato alla sessione
     * @param lingua La lingua selezionata per la sessione
     * @param difficolta Il livello di difficoltà selezionato per la sessione
     */
    public GameSession(Utente utente, Lingua lingua, Difficolta difficolta) {
        this.utente = utente;
        this.lingua = lingua;
        this.difficolta = difficolta;
        this.currentQuestionIndex = 0;
        this.score = 0;
    }

    public Utente getUtente() {
        return utente;
    }

    public Lingua getLingua() {
        return lingua;
    }

    public Difficolta getDifficolta() {
        return difficolta;
    }

    public List<Analisi> getAnalyses() {
        return analyses;
    }

    public void setAnalyses(List<Analisi> analyses) {
        this.analyses = analyses;
    }

    public List<Domanda> getDomande() {
        return domande;
    }

    public void setDomande(List<Domanda> domande) {
        this.domande = domande;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Restituisce la domanda corrente.
     *
     * @return La domanda corrente, o null se non ci sono più domande
     */
    public Domanda getCurrentQuestion() {
        if (currentQuestionIndex >= 0 && currentQuestionIndex < domande.size()) {
            return domande.get(currentQuestionIndex);
        }
        return null;
    }

    /**
     * Verifica se ci sono altre domande disponibili.
     *
     * @return true se ci sono altre domande, false altrimenti
     */
    public boolean hasNextQuestion() {
        return currentQuestionIndex < domande.size();
    }

    /**
     * Incrementa l'indice della domanda corrente.
     */
    public void incrementQuestionIndex() {
        currentQuestionIndex++;
    }

    /**
     * Incrementa il punteggio della sessione.
     */
    public void incrementScore() {
        score++;
    }

    public long getSessioneId() {
        return sessioneId;
    }

    public void setSessioneId(long sessioneId) {
        this.sessioneId = sessioneId;
    }
}