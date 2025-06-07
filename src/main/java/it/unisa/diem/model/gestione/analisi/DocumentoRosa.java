package it.unisa.diem.model.gestione.analisi;

import it.unisa.diem.utility.CryptoAlphabet;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DocumentoRosa {

    private String titolo;
    private Lingua lingua;
    private Difficolta difficolta;
    private Stopword stopword;
    private String path ;
    private List<String> parole;

    public DocumentoRosa(String titolo, Lingua lingua, Difficolta difficolta, Stopword stopword) {
        this.titolo = titolo;
        this.lingua = lingua;
        this.difficolta = difficolta;
        this.stopword = stopword;
        parole=new ArrayList<>();
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


    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public void setLingua(Lingua lingua) {
        this.lingua = lingua;
    }

    public void setDifficolta(Difficolta difficolta) {
        this.difficolta = difficolta;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Difficolta getDifficolta() {
        return difficolta;
    }

    /*public Analisi analisiDocumento() {
        if()
    }*/

   public List<String> getParole() throws IOException, ClassNotFoundException {
       return parole.stream().flatMap(line->{
           return Arrays.stream(line.split("[ ,]"));
       }).map(word-> word.toLowerCase()).collect(Collectors.toList());
   }

    public void convertiTxtToBin(File inputFile) throws IOException {
        File outputFile = new File("data" +"/"+ lingua + "/" + difficolta + "/" + titolo + ".bin");
        this.path=outputFile.toString();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             DataOutputStream dos = new DataOutputStream(new   BufferedOutputStream(new FileOutputStream(outputFile)))) {

            String line;
            /*while ((line = reader.readLine()) != null) {
                line=line.replaceAll("\t", "");
                dos.writeUTF(crittografia.cripta(line));
            }

             */
            while ((line = reader.readLine()) != null) {
                dos.writeUTF(CryptoAlphabet.cripta(line));
            }
        }
    }

    public static DocumentoRosa leggiDocumento(String filename) throws IOException, ClassNotFoundException {
        DocumentoRosa dr = null;
        /*
        COSA FARE SECONDO ROSA
        1. in input ho il nome del file da leggere, quindi ho il file e lo devo legggere dalla directory esatta
        2. quando lo leggo per mostrarlo devo decriptarlo

         */
        //lingua e difficolta dipendono dalla cartella in cui si trovano
        dr=new DocumentoRosa(null, null, null, new Stopword());
        getAttributes(filename, dr);
        try(DataInputStream br=new DataInputStream(new BufferedInputStream(new FileInputStream(filename)))){//devo aggiungere utf
           String line;
           try {
               while (true) {
                   line = br.readUTF();
                   dr.parole.add(CryptoAlphabet.decripta(line));
               }
           }catch(EOFException e){
               return dr;
           }


        }

    }

    private static void getAttributes(String filename, DocumentoRosa dr){
        String[] split= filename.split("[/.]");
        dr.titolo=split[split.length-2];
        dr.difficolta=Difficolta.valueOf(split[split.length-3].toUpperCase());
        dr.lingua=Lingua.valueOf(split[split.length-4].toUpperCase());
        dr.path=filename;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s : parole) {
            sb.append(s).append("\n");
        }
        return sb.toString();
    }
}