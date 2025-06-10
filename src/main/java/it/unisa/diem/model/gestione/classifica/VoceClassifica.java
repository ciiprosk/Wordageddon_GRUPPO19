package it.unisa.diem.model.gestione.classifica;

public class VoceClassifica {
    private final String username;
    private final double mediaPunteggio;
    private final int sommaPunteggio;

    public VoceClassifica(String username, int sommaPunteggio, double mediaPunteggio) {
        this.username = username;
        this.sommaPunteggio = sommaPunteggio;
        this.mediaPunteggio = mediaPunteggio;
    }

    public String getUsername() {
        return username;
    }

    public int getSommaPunteggio() {
        return sommaPunteggio;
    }

    public double getMediaPunteggio() {
        return mediaPunteggio;
    }
}
