package it.unisa.diem.test;

import it.unisa.diem.model.gestione.analisi.Analisi;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Documento;
import it.unisa.diem.model.gestione.analisi.Lingua;

import java.io.File;
import java.io.IOException;

public class DocumentoAnalisiTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("ciao");
        Documento documento = new Documento("ciao", Lingua.ITA, Difficolta.FACILE);
        documento.convertiTxtToBin(new File("data/ITA/facile/storiella.txt"));
        Analisi a=new Analisi(documento);
        a.frequenzeDocumento();
        a.caricaAnalisi();
    }

}
