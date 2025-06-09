package it.unisa.diem.test;

import it.unisa.diem.model.gestione.analisi.*;
import it.unisa.diem.model.gestione.analisi.stopword.StopwordITA;
import it.unisa.diem.model.gestione.analisi.stopword.StopwordManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DocumentoAnalisiTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Documento dr = new Documento("testo_prova2", Lingua.ITA, Difficolta.FACILE);
        dr.convertiTxtToBin(new File("data/ITA/facile/storiella.txt"));
        StopwordManager s=new StopwordITA();
        s.caricaStopword(false, false, false, false, false, false);
        s.aggiungi("dio");
        s.aggiungi("volta");

        Analisi a=new Analisi(dr, s);
        a.frequenzeDocumento();
        a.caricaAnalisi();

       dr.cambiaNomeDocumento("ciaoRicchoni");
        System.out.println(dr.getTitolo());
       a.modificaNomeAnalisi(dr.getTitolo());
    }

}
