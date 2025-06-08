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
    public static void main(String[] args) {

        //1 carico il doc ma prima le stopword
        StopwordManager s=new StopwordITA();
        s.caricaStopword(true, false, false, false, false, false);
        Documento d= new Documento("diocane", Lingua.ITA, Difficolta.FACILE, s);
        //converto il documento dato in input da file choooser quindi è un file
        try {
            d.convertiTxtToBin(new File("data/ITA/facile/storiella.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //vediamo se ha inzializzato testo--> lo ha fatto
        System.out.println(d.getTesto());
        //primo test completato

        /*
        //2 vediamo la letura se va bene---> perfetto funziona
        try {
            Documento doc = Documento.leggiDocumento("data/ITA/facile/ciao.bin");
            System.out.println(doc.getTesto());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        */

        //3 vediamo come funziona analisi con documento
        Analisi a= new Analisi(d);
        try {
            a.frequenzeDocumento().entrySet().forEach(System.out::println);
            a.caricaAnalisi();
            System.out.println(Analisi.leggiAnalisi(d).getFrequenzeTesto().entrySet());
                    } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }




}
