package it.unisa.diem.test;

import it.unisa.diem.model.gestione.analisi.*;

import java.io.File;
import java.io.IOException;

public class DocumentoAnalisiTest {
    public static void main(String[] args) {

        DocumentoRosa prova= new DocumentoRosa("storiella",Lingua.ITA, Difficolta.FACILE, null);
        try {
            prova.convertiTxtToBin(new File("data/ITA/facile/storiella.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //System.out.println(DocumentoAntonio.leggiDocumento("data/ITA/facile/principessa1.txt"));
       // AnalisiRosa analisi = new AnalisiRosa(prova);
        //analisi.getWordsDocument();

    }



}
