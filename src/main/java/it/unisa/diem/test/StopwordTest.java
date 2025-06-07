package it.unisa.diem.test;

import it.unisa.diem.model.gestione.analisi.stopword.StopwordENG;
import it.unisa.diem.model.gestione.analisi.stopword.StopwordITA;

public class StopwordTest {
    public static void main(String[] args) {
        StopwordENG stopword= new StopwordENG();

        System.out.println(stopword); //la lista è vuota

        stopword.aggiungi("cammello");
        stopword.aggiungi("omar");

        stopword.rimuovi("cammello");
        System.out.println(stopword);

        stopword.modifica("cammello", "omar"); //non dovrebbe cambiare nulla
        System.out.println(stopword);
        stopword.modifica("omar", "cammello");
        System.out.println(stopword);

        stopword.caricaStopword(true, false, false, false, true, false);
        System.out.println(stopword);

    }
}
