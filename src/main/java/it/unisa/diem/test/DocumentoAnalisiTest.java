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
        Documento documento = new Documento("PROVA", Lingua.ENG, Difficolta.FACILE);
        documento.convertiTxtToBin(new File("A Cup of Tea.txt"));
        Analisi a=new Analisi(documento);
        a.analizza();
        System.out.println(a.getFrequenzeTesto());
        a.caricaAnalisi();

    }

}
