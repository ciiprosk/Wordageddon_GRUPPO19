package it.unisa.diem.model.gestione.analisi;

import java.util.ArrayList;
import java.util.List;

public class Stopword {
    private final List<String> parole;        //oppure un insieme di parole?????????
//array d default --> preposizioni, articoli, pronomi, verbi essere e avere, connettivi e congiunzioni
    public Stopword() {
        parole=new ArrayList<>();
    }
}
