package it.unisa.diem.model.gestione.analisi;

import java.io.*;

public class DocumentoAntonio implements Serializable {

    private String nome;
    private Lingua lingua;
    private Difficolta difficolta;
    private Stopword stopword;

    public DocumentoAntonio(String nome, Lingua lingua, Difficolta difficolta, Stopword stopword) {
        this.nome = nome;
        this.lingua = lingua;
        this.difficolta = difficolta;
        this.stopword = stopword;
    }

    /*public Analisi analisiDocumento() {
        if()
    }*/

    public DocumentoAntonio leggiDocumento(String filename) {
        DocumentoAntonio da=null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            da=(DocumentoAntonio) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return da;
    }

    public void scriviDocumento(String filename) {
        try(ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(filename))){
            oos.writeObject(this);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
