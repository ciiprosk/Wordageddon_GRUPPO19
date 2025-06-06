package it.unisa.diem.model.gestione.analisi.stopword;

public class StopwordITA extends StopwordManager {

    public StopwordITA(boolean articoli, boolean preposizioni, boolean pronomi, boolean verbiEssereAvere, boolean connettiviCongiunzioni) {
        super(articoli, preposizioni, pronomi, verbiEssereAvere, connettiviCongiunzioni);
    }

    @Override protected String[] getArticoli() { return new String[]{
            "il", "lo", "la", "i", "gli", "le", "l'",  // determinativi
            "un", "uno", "una", "un'", "dei", "degli", "delle" // indeterminativi e partitivi
    }; }

    @Override protected String[] getPreposizioni() { return new String[]{
            // semplici
            "di", "a", "da", "in", "con", "su", "per", "tra", "fra",
            // articolate
            "del", "dello", "della", "dei", "degli", "delle",
            "al", "allo", "alla", "ai", "agli", "alle",
            "dal", "dallo", "dalla", "dai", "dagli", "dalle",
            "nel", "nello", "nella", "nei", "negli", "nelle",
            "col", "coi", // forme contratte di "con il", "con i"
            "sul", "sullo", "sulla", "sui", "sugli", "sulle"
    }; }

    @Override protected String[] getPronomi() { return new String[]{
            // soggetto
            "io", "tu", "lui", "lei", "egli", "ella", "noi", "voi", "essi", "esse", "loro",
            // riflessivi e oggetto diretto/indiretto
            "mi", "ti", "si", "ci", "vi", "lo", "la", "li", "le", "gli", "ne"
    }; }

    @Override protected String[] getVerbiEssereAvere() { return new String[]{
            // essere
            "essere", "sono", "sei", "è", "siamo", "siete", "erano", "ero", "eri", "era", "fui", "fosti", "fu", "fummo", "foste", "furono",
            "sarò", "sarai", "sarà", "saremo", "sarete", "saranno", "sarei", "saresti", "sarebbe", "saremmo", "sareste", "sarebbero",
            // avere
            "avere", "ho", "hai", "ha", "abbiamo", "avete", "hanno", "avevo", "avevi", "aveva", "avevamo", "avevate", "avevano",
            "ebbi", "avesti", "ebbe", "avemmo", "aveste", "ebbero", "avrò", "avrai", "avrà", "avremo", "avrete", "avranno",
            "avrei", "avresti", "avrebbe", "avremmo", "avreste", "avrebbero"
    }; }

    @Override protected String[] getConnettiviCongiunzioni() { return new String[]{
            "e", "ma", "però", "oppure", "infatti", "perché", "quindi", "dunque", "cioè",
            "anche", "sebbene", "poiché", "affinché", "mentre", "nonostante", "anzi", "ovvero", "neanche", "neppure", "inoltre", "comunque",
            "pertanto", "tuttavia", "siccome", "quando", "dopo che", "prima che", "cosicché", "finché", "purché", "quantunque", "benchè", "anzi", "anzi che"
    }; }
}