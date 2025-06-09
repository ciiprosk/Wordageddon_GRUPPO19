package it.unisa.diem.test;

import it.unisa.diem.model.gestione.analisi.Analisi;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Documento;
import it.unisa.diem.model.gestione.analisi.Lingua;
import it.unisa.diem.model.gestione.analisi.stopword.StopwordITA;
import it.unisa.diem.model.gestione.analisi.stopword.StopwordManager;

import java.io.File;

public class Test {
    public static void main(String[] args)throws Exception{

            // Creiamo prima un documento di test e lo convertiamo in binario

            StopwordManager s=new StopwordITA();
            s.caricaStopword(false, false, false, false, false, false);

            Documento dr = new Documento("testo_prova", Lingua.ITA, Difficolta.FACILE);
            dr.convertiTxtToBin(new File("data/ITA/facile/storiella.txt"));





           // System.out.println(a.getFrequenzeTestiRosa());



    }

}