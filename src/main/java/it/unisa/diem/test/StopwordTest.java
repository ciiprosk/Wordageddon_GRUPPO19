package it.unisa.diem.test;

import it.unisa.diem.model.gestione.analisi.stopword.StopwordITA;

public class StopwordTest {
    public static void main(String[] args) {
        StopwordITA stopword= new StopwordITA(false, true, true, true, true);

        System.out.println(stopword); //la lista è vuota

        stopword.aggiungi("cammello");
        stopword.aggiungi("omar");

        stopword.rimuovi("cammello");
        System.out.println(stopword);

        stopword.modifica("cammello", "omar"); //non dovrebbe cambiare nulla
        System.out.println(stopword);
        stopword.modifica("omar", "cammello");
        System.out.println(stopword);

        stopword.caricaStopword();
        System.out.println(stopword);

    }
}
