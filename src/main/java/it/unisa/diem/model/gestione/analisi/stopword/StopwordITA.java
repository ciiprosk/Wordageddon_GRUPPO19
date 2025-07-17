package it.unisa.diem.model.gestione.analisi.stopword;

import it.unisa.diem.model.gestione.analisi.Documento;

/**
 * Implementazione concreta di StopwordManager per la lingua italiana
 */
public class StopwordITA extends StopwordManager {

    /**
     * Costruttore di default
     */
    public StopwordITA() {
        super();
    }

    /**
     * Costruttore che associa un documento
     * @param documento il documento da associare
     */
    public StopwordITA(Documento documento) {
        super(documento);
    }

    /**
     * Restituisce gli articoli in italiano
     * @return array di articoli determinativi, indeterminativi e partitivi
     */
    @Override
    protected String[] getArticoli() {
        return new String[]{
                "il", "lo", "la", "i", "gli", "le", "l'",  // determinativi
                "un", "uno", "una", "un'", "dei", "degli", "delle" // indeterminativi e partitivi
        };
    }

    /**
     * Restituisce le preposizioni in italiano
     * @return array di preposizioni semplici e articolate
     */
    @Override
    protected String[] getPreposizioni() {
        return new String[]{
                // semplici
                "di", "a", "da", "in", "con", "su", "per", "tra", "fra",
                // articolate
                "del", "dello", "della", "dei", "degli", "delle",
                "al", "allo", "alla", "ai", "agli", "alle",
                "dal", "dallo", "dalla", "dai", "dagli", "dalle",
                "nel", "nello", "nella", "nei", "negli", "nelle",
                "col", "coi", // forme contratte di "con il", "con i"
                "sul", "sullo", "sulla", "sui", "sugli", "sulle"
        };
    }

    /**
     * Restituisce i pronomi in italiano
     * @return array di pronomi soggetto, riflessivi e oggetto
     */
    @Override
    protected String[] getPronomi() {
        return new String[]{
                // soggetto
                "io", "tu", "lui", "lei", "egli", "ella", "noi", "voi", "essi", "esse", "loro",
                // riflessivi e oggetto diretto/indiretto
                "mi", "ti", "si", "ci", "vi", "lo", "la", "li", "le", "gli", "ne"
        };
    }

    /**
     * Restituisce le coniugazioni del verbo avere
     * @return array di forme verbali del verbo avere
     */
    @Override
    protected String[] getVerbiAvere() {
        return new String[]{
                "avere", "ho", "hai", "ha", "abbiamo", "avete", "hanno", "avevo", "avevi", "aveva", "avevamo", "avevate", "avevano",
                "ebbi", "avesti", "ebbe", "avemmo", "aveste", "ebbero", "avrò", "avrai", "avrà", "avremo", "avrete", "avranno",
                "avrei", "avresti", "avrebbe", "avremmo", "avreste", "avrebbero"
        };
    }

    /**
     * Restituisce le coniugazioni del verbo essere
     * @return array di forme verbali del verbo essere
     */
    @Override
    protected String[] getVerbiEssere() {
        return new String[]{
                "essere", "sono", "sei", "è", "siamo", "siete", "erano", "ero", "eri", "era", "fui", "fosti", "fu", "fummo", "foste", "furono",
                "sarò", "sarai", "sarà", "saremo", "sarete", "saranno", "sarei", "saresti", "sarebbe", "saremmo", "sareste", "sarebbero",
        };
    }

    /**
     * Restituisce connettivi e congiunzioni in italiano
     * @return array di connettivi e congiunzioni
     */
    @Override
    protected String[] getConnettiviCongiunzioni() {
        return new String[]{
                "e", "ma", "però", "oppure", "infatti", "perché", "quindi", "dunque", "cioè",
                "anche", "sebbene", "poiché", "affinché", "mentre", "nonostante", "anzi", "ovvero", "neanche", "neppure", "inoltre", "comunque",
                "pertanto", "tuttavia", "siccome", "quando", "dopo che", "prima che", "cosicché", "finché", "purché", "quantunque", "benchè", "anzi", "anzi che"
        };
    }
}