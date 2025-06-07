package it.unisa.diem.test;

import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.DocumentoRosa;
import it.unisa.diem.model.gestione.analisi.Lingua;
import it.unisa.diem.model.gestione.analisi.stopword.StopwordITA;
import it.unisa.diem.model.gestione.analisi.stopword.StopwordManager;

import java.io.File;

public class Test {
    public static void main(String[] args)throws Exception{

            // Creiamo prima un documento di test e lo convertiamo in binario

            StopwordManager s=new StopwordITA();
            s.caricaStopword(false, false, false, false, false, false);

            DocumentoRosa dr = new DocumentoRosa("testo_prova", Lingua.ITA, Difficolta.FACILE, s);
            dr.convertiTxtToBin(new File("data/ITA/facile/storiella.txt"));
        System.out.println(dr.analisiDocumento());


           // System.out.println(a.getFrequenzeTestiRosa());



    }

}