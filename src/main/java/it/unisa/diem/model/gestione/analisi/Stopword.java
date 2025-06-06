package it.unisa.diem.model.gestione.analisi;

import java.util.ArrayList;
import java.util.List;

public class Stopword {
    private final List<String> parole;        //oppure un insieme di parole?????????
//array d default --> preposizioni, articoli, pronomi, verbi essere e avere, connettivi e congiunzioni
    private String[] articoli= {"il","la","lo","i","e","un","una","uno","un'","une","le","l'","l"};
    private String[] preposizioni={"di","a", "da", "in", "con", "su", "per", "tra", "fra", "della", "delle", "degli"};
    private String[] pronomi;
    private String[] verbiEssereAvere;
    private String[] connettivi;
    private String[] congiunzioni;
    public Stopword() {
        parole=new ArrayList<>();
    }


}
