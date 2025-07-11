package it.unisa.diem.model.gestione.analisi.stopword;

import it.unisa.diem.dao.interfacce.DocumentoDAO;
import it.unisa.diem.model.gestione.analisi.Documento;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class StopwordManager {
    private Documento documento;

    protected final Set<String> parole;

    public StopwordManager() {
        this.parole = new HashSet<>();
    }
    public StopwordManager(Documento documento) {
        this.documento = documento;
        this.parole = new HashSet<>();
    }
    public Documento getDocumento() {
        return documento;
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

    public void caricaStopword(boolean articoli, boolean preposizioni, boolean pronomi, boolean verbiAvere, boolean verbiEssere, boolean connettiviCongiunzioni) {
        if (articoli) aggiungiArray(getArticoli());
        if (preposizioni) aggiungiArray(getPreposizioni());
        if (pronomi) aggiungiArray(getPronomi());
        if (verbiAvere) aggiungiArray(getVerbiAvere());
        if (verbiEssere) aggiungiArray(getVerbiEssere());
        if (connettiviCongiunzioni) aggiungiArray(getConnettiviCongiunzioni());
        aggiungiArray(getPunteggiatura());

    }


    private void aggiungiArray(String[] array) {
        for (String parola : array) {
            parole.add(parola.toLowerCase());
        }
    }

    protected abstract String[] getArticoli();

    protected abstract String[] getPreposizioni();

    protected abstract String[] getPronomi();

    protected abstract String[] getVerbiAvere();

    protected abstract String[] getVerbiEssere();

    protected abstract String[] getConnettiviCongiunzioni();

    public String[] getPunteggiatura() {
        return new String[]{
                ".", ",", ";", ":", "!", "?", "...", "-", "_", "(", ")", "[", "]",
                "{", "}", ";", "\"", "'", "<", ">", "«", "»", "·", "/", "|", "\\",
                "*", "@", "#", "&"

        };
    }

    public void clear() {
        parole.clear();
    }

    @Override
        public String toString() {
            return getClass().getSimpleName() + "{" + "parole=" + parole + '}';
        }

}

