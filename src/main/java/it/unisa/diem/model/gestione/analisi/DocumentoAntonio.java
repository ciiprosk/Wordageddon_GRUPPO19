package it.unisa.diem.model.gestione.analisi;

import java.io.*;
import java.util.stream.Stream;

public class DocumentoAntonio implements Serializable {

    private String titolo;
    private Lingua lingua;
    private Difficolta difficolta;
    private Stopword stopword;

    public DocumentoAntonio(String titolo, Lingua lingua, Difficolta difficolta, Stopword stopword) {
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

    public static DocumentoAntonio leggiDocumento(String filename) {
        DocumentoAntonio da = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            da = (DocumentoAntonio) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return da;
    }

    public void scriviDocumento(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}