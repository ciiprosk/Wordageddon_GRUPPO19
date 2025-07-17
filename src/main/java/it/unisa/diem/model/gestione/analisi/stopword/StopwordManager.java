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

    /**
     * Costruttore di default che inizializza l'insieme delle parole
     */
    public StopwordManager() {
        this.parole = new HashSet<>();
    }

    /**
     * Costruttore che inizializza l'insieme delle parole e associa un documento
     * @param documento il documento da associare
     */
    public StopwordManager(Documento documento) {
        this.documento = documento;
        this.parole = new HashSet<>();
    }

    /**
     * Restituisce il documento associato
     * @return il documento associato
     */
    public Documento getDocumento() {
        return documento;
    }

    /**
     * Aggiunge una parola all'insieme delle stopword
     * @param parola la parola da aggiungere
     */
    public void aggiungi(String parola) {
        parole.add(parola.toLowerCase());
    }

    /**
     * Restituisce la lista delle stopword
     * @return lista delle stopword
     */
    public List<String> getParole() {
        return new ArrayList<>(parole);
    }

    /**
     * Rimuove una parola dall'insieme delle stopword
     * @param parola la parola da rimuovere
     */
    public void rimuovi(String parola) {
        parole.remove(parola.toLowerCase());
    }

    /**
     * Modifica una parola esistente con una nuova parola
     * @param vecchia la parola da modificare
     * @param nuova la nuova parola
     */
    public void modifica(String vecchia, String nuova) {
        String vecchiaNorm = vecchia.toLowerCase().trim();
        String nuovaNorm = nuova.toLowerCase().trim();
        if (parole.contains(vecchiaNorm)) {
            parole.remove(vecchiaNorm);
            parole.add(nuovaNorm);
        }
    }

    /**
     * Carica le stopword in base ai parametri specificati
     * @param articoli true per includere gli articoli
     * @param preposizioni true per includere le preposizioni
     * @param pronomi true per includere i pronomi
     * @param verbiAvere true per includere i verbi avere
     * @param verbiEssere true per includere i verbi essere
     * @param connettiviCongiunzioni true per includere connettivi e congiunzioni
     */
    public void caricaStopword(boolean articoli, boolean preposizioni, boolean pronomi, boolean verbiAvere, boolean verbiEssere, boolean connettiviCongiunzioni) {
        if (articoli) aggiungiArray(getArticoli());
        if (preposizioni) aggiungiArray(getPreposizioni());
        if (pronomi) aggiungiArray(getPronomi());
        if (verbiAvere) aggiungiArray(getVerbiAvere());
        if (verbiEssere) aggiungiArray(getVerbiEssere());
        if (connettiviCongiunzioni) aggiungiArray(getConnettiviCongiunzioni());
        aggiungiArray(getPunteggiatura());
    }

    /**
     * Aggiunge tutte le parole di un array all'insieme delle stopword
     * @param array l'array di parole da aggiungere
     */
    private void aggiungiArray(String[] array) {
        for (String parola : array) {
            parole.add(parola.toLowerCase());
        }
    }

    /**
     * Restituisce l'array degli articoli
     * @return array degli articoli
     */
    protected abstract String[] getArticoli();

    /**
     * Restituisce l'array delle preposizioni
     * @return array delle preposizioni
     */
    protected abstract String[] getPreposizioni();

    /**
     * Restituisce l'array dei pronomi
     * @return array dei pronomi
     */
    protected abstract String[] getPronomi();

    /**
     * Restituisce l'array dei verbi avere
     * @return array dei verbi avere
     */
    protected abstract String[] getVerbiAvere();

    /**
     * Restituisce l'array dei verbi essere
     * @return array dei verbi essere
     */
    protected abstract String[] getVerbiEssere();

    /**
     * Restituisce l'array dei connettivi e congiunzioni
     * @return array dei connettivi e congiunzioni
     */
    protected abstract String[] getConnettiviCongiunzioni();

    /**
     * Restituisce l'array dei simboli di punteggiatura
     * @return array dei simboli di punteggiatura
     */
    public String[] getPunteggiatura() {
        return new String[]{
                ".", ",", ";", ":", "!", "?", "...", "-", "_", "(", ")", "[", "]",
                "{", "}", ";", "\"", "'", "<", ">", "«", "»", "·", "/", "|", "\\",
                "*", "@", "#", "&"
        };
    }

    /**
     * Svuota l'insieme delle stopword
     */
    public void clear() {
        parole.clear();
    }

    /**
     * Restituisce una rappresentazione stringa dell'oggetto
     * @return rappresentazione stringa dell'oggetto
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "parole=" + parole + '}';
    }
}