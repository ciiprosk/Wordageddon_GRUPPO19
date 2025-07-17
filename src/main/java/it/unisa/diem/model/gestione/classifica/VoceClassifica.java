package it.unisa.diem.model.gestione.classifica;

/**
 * Rappresenta una voce all'interno di una classifica, contenente informazioni
 * relative all'utente e ai punteggi ottenuti.
 */
public class VoceClassifica {
    private final String username;
    private final double mediaPunteggio;
    private final int sommaPunteggio;

    /**
     * Costruttore per creare una nuova voce di classifica.
     *
     * @param username il nome utente del giocatore
     * @param sommaPunteggio la somma totale dei punteggi ottenuti
     * @param mediaPunteggio la media dei punteggi ottenuti
     */
    public VoceClassifica(String username, int sommaPunteggio, double mediaPunteggio) {
        this.username = username;
        this.sommaPunteggio = sommaPunteggio;
        this.mediaPunteggio = mediaPunteggio;
    }

    /**
     * Restituisce il nome utente associato a questa voce di classifica.
     *
     * @return il nome utente
     */
    public String getUsername() {
        return username;
    }

    /**
     * Restituisce la somma totale dei punteggi ottenuti dall'utente.
     *
     * @return la somma dei punteggi
     */
    public int getSommaPunteggio() {
        return sommaPunteggio;
    }

    /**
     * Restituisce la media dei punteggi ottenuti dall'utente.
     *
     * @return la media dei punteggi
     */
    public double getMediaPunteggio() {
        return mediaPunteggio;
    }
}