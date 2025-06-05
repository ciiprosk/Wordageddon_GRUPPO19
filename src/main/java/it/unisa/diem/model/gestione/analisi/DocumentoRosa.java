package it.unisa.diem.model.gestione.analisi;

import java.io.*;
import java.util.Arrays;
import java.util.stream.Stream;

public class DocumentoRosa {

    private String titolo;
    private Lingua lingua;
    private Difficolta difficolta;
    private Stopword stopword;

    public DocumentoRosa(String titolo, Lingua lingua, Difficolta difficolta, Stopword stopword) {
        this.titolo = titolo;
        this.lingua = lingua;
        this.difficolta = difficolta;
        this.stopword = stopword;
    }

    public String getTitolo() {
        return titolo;
    }

    public Lingua getLingua() {
        return lingua;
    }

    public Stopword getStopword() {
        return stopword;
    }

    public void setStopword(Stopword stopword) {
        this.stopword = stopword;
    }

    public Difficolta getDifficolta() {
        return difficolta;
    }

    /*public Analisi analisiDocumento() {
        if()
    }*/


    public void convertiTxtToBin(File inputFile) throws IOException {
        File outputFile = new File("data" +"/"+ lingua + "/" + difficolta + "/" + titolo + ".bin");
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(outputFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                writer.writeObject(line);
            }
        }
    }
}


