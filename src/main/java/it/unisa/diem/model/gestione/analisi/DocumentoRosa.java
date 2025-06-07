package it.unisa.diem.model.gestione.analisi;

import it.unisa.diem.model.gestione.analisi.stopword.StopwordManager;
import it.unisa.diem.utility.CryptoAlphabet;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class DocumentoRosa {

    private String titolo;
    private Lingua lingua;
    private Difficolta difficolta;
    private StopwordManager stopword;
    private String path ;
    private List<String> testo;

    public DocumentoRosa(String titolo, Lingua lingua, Difficolta difficolta, StopwordManager stopword) {
        this.titolo = titolo;
        this.lingua = lingua;
        this.difficolta = difficolta;
        this.stopword = stopword;
        testo=new ArrayList<>();
        path="data/"+lingua+"/"+difficolta+"/"+titolo+".bin";
    }
    public DocumentoRosa(String titolo, Lingua lingua, Difficolta difficolta) {
        this.titolo = titolo;
        this.lingua = lingua;
        this.difficolta = difficolta;
        testo=new ArrayList<>();
        //path="data/"+lingua+"/"+difficolta+"/"+titolo+".bin";
    }

    public String getTitolo() {
        return titolo;
    }

    public Lingua getLingua() {
        return lingua;
    }

    public StopwordManager getStopword() {
        return stopword;
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

    public AnalisiRosa analisiDocumento() throws IOException, ClassNotFoundException {
       AnalisiRosa a=new AnalisiRosa(this);
       a.getFrequenzeTestiRosa();
       return a;
    }

   public List<String> getTesto() {
       return new ArrayList<>(testo);
   }

    public void convertiTxtToBin(File inputFile) throws IOException {
        File outputFile= new File(path);
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             DataOutputStream dos = new DataOutputStream(new   BufferedOutputStream(new FileOutputStream(outputFile)))) {

            String line;
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
        dr=new DocumentoRosa(null, null, null); //non mi servono le stopword perché è una lettura
        getAttributes(filename, dr);
        try(DataInputStream br=new DataInputStream(new BufferedInputStream(new FileInputStream(filename)))){//devo aggiungere utf
           String line;
           try {
               while (true) {
                   line = br.readUTF();
                   dr.testo.add(CryptoAlphabet.decripta(line));
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
        for (String s : testo) {
            sb.append(s).append("\n");
        }
        return sb.toString();
    }
}