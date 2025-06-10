package it.unisa.diem.test;

import it.unisa.diem.exceptions.UpdateException;
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
        Documento dr = new Documento("ciaoRenamed", Lingua.ITA, Difficolta.FACILE);
        //dr.convertiTxtToBin(new File("data/ITA/facile/storiella.txt"));
        StopwordManager s=new StopwordITA();
       s.caricaStopword(false, false, false, false, false, false);
        s.aggiungi("dio");
        s.aggiungi("volta");

        //Analisi a=new Analisi(dr);
        //a.frequenzeDocumento();
        //a.caricaAnalisi();
       Analisi a= Analisi.leggiAnalisi(dr);
       dr.eliminaDocumento();
       a.eliminaAnalisi();
   /*
        try {
            dr.cambiaNomeDocumento("ciaoRenamed");
            a.modificaNomeAnalisi(dr.getTitolo());
            System.out.println(dr.getTitolo());
            System.out.println(dr.getPath());
        } catch (UpdateException e) {
            throw new RuntimeException(e);
        }
    */
       // System.out.println(a.getTitolo());
        //System.out.println(a.getPathAnalisi());
    }

}
