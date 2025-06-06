package it.unisa.diem.model.gestione.analisi.stopword;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class StopwordManager {

    protected final Set<String> parole;
    protected boolean articoliBool, preposizioniBool, pronomiBool, verbiEssereAvereBool, connettiviCongiunzioniBool;

    public StopwordManager(boolean articoli, boolean preposizioni, boolean pronomi, boolean verbiEssereAvere, boolean connettiviCongiunzioni) {
        this.parole = new HashSet<>();
        this.articoliBool = articoli;
        this.preposizioniBool = preposizioni;
        this.pronomiBool = pronomi;
        this.verbiEssereAvereBool = verbiEssereAvere;
        this.connettiviCongiunzioniBool = connettiviCongiunzioni;
    }

    public void aggiungi(String parola) {
        parole.add(parola.toLowerCase());
    }

    public List<String> getParole() {
        return new ArrayList<>(parole);
    }

    public void rimuovi(String parola) {
        parole.remove(parola.toLowerCase());
    }

    public void modifica(String vecchia, String nuova) {
        String vecchiaNorm = vecchia.toLowerCase().trim();
        String nuovaNorm = nuova.toLowerCase().trim();
        if (parole.contains(vecchiaNorm)) {
            parole.remove(vecchiaNorm);
            parole.add(nuovaNorm);
        }
    }

    public void caricaStopword() {
        if (articoliBool) aggiungiArray(getArticoli());
        if (preposizioniBool) aggiungiArray(getPreposizioni());
        if (pronomiBool) aggiungiArray(getPronomi());
        if (verbiEssereAvereBool) aggiungiArray(getVerbiEssereAvere());
        if (connettiviCongiunzioniBool) aggiungiArray(getConnettiviCongiunzioni());
    }

    private void aggiungiArray(String[] array) {
        for (String parola : array) {
            parole.add(parola.toLowerCase());
        }
    }

    protected abstract String[] getArticoli();
    protected abstract String[] getPreposizioni();
    protected abstract String[] getPronomi();
    protected abstract String[] getVerbiEssereAvere();
    protected abstract String[] getConnettiviCongiunzioni();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "parole=" + parole + '}';
    }
}

