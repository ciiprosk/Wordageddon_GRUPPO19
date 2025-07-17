package it.unisa.diem.model.gestione.analisi.stopword;

import it.unisa.diem.model.gestione.analisi.Documento;

/**
 * Implementazione concreta di StopwordManager per la lingua inglese
 */
public class StopwordENG extends StopwordManager {

    /**
     * Costruttore di default
     */
    public StopwordENG() {
        super();
    }

    /**
     * Costruttore che associa un documento
     * @param documento il documento da associare
     */
    public StopwordENG(Documento documento) {
        super(documento);
    }

    /**
     * Restituisce gli articoli in inglese
     * @return array di articoli determinativi e indeterminativi
     */
    @Override
    protected String[] getArticoli() {
        return new String[]{
                "the", "a", "an"
        };
    }

    /**
     * Restituisce le preposizioni in inglese
     * @return array di preposizioni semplici e composte
     */
    @Override
    protected String[] getPreposizioni() {
        return new String[]{
                // semplici
                "of", "in", "on", "at", "by", "with", "about", "against", "between", "into", "through", "during", "before", "after",
                "above", "below", "to", "from", "up", "down", "off", "over", "under", "around", "among",
                // composte/frasali
                "according to", "ahead of", "apart from", "as for", "as of", "as per", "as to", "as well as",
                "because of", "close to", "due to", "except for", "far from", "inside of", "instead of", "near to",
                "next to", "on account of", "on top of", "out of", "outside of", "prior to", "pursuant to", "regardless of", "thanks to"
        };
    }

    /**
     * Restituisce i pronomi in inglese
     * @return array di pronomi soggetto, oggetto, possessivi e riflessivi
     */
    @Override
    protected String[] getPronomi() {
        return new String[]{
                // soggetto
                "i", "you", "he", "she", "it", "we", "they",
                // oggetto
                "me", "you", "him", "her", "it", "us", "them",
                // aggettivi possessivi
                "my", "your", "his", "her", "its", "our", "their",
                // pronomi possessivi
                "mine", "yours", "his", "hers", "ours", "theirs",
                // riflessivi
                "myself", "yourself", "himself", "herself", "itself", "ourselves", "yourselves", "themselves"
        };
    }

    /**
     * Restituisce le coniugazioni del verbo avere (to have)
     * @return array di forme verbali del verbo avere
     */
    @Override
    protected String[] getVerbiAvere() {
        return new String[]{
                "have", "has", "had", "having"
        };
    }

    /**
     * Restituisce le coniugazioni del verbo essere (to be)
     * @return array di forme verbali del verbo essere
     */
    @Override
    protected String[] getVerbiEssere() {
        return new String[]{
                "be", "am", "is", "are", "was", "were", "being", "been"
        };
    }

    /**
     * Restituisce connettivi e congiunzioni in inglese
     * @return array di connettivi e congiunzioni
     */
    @Override
    protected String[] getConnettiviCongiunzioni() {
        return new String[]{
                "and", "but", "or", "so", "for", "nor", "yet", "although", "though", "even though", "because",
                "since", "unless", "until", "while", "whereas", "whether", "as", "once", "after", "before",
                "however", "therefore", "thus", "moreover", "furthermore", "nonetheless", "nevertheless",
                "in addition", "on the other hand", "in contrast", "otherwise"
        };
    }
}