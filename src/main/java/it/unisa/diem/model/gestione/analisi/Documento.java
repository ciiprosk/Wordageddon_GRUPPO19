package it.unisa.diem.model.gestione.analisi;

import it.unisa.diem.exceptions.DeleteException;
import it.unisa.diem.utility.CryptoAlphabet;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


/**
 * La classe Documento rappresenta un documento inserito dall'utente autorizzato (Admin).
 * La classe dispone di attributi quali: titolo, lingua, difficoltà, possibili stopword, e il testo del documento.
 * 
 */
public class Documento {

    private String titolo;
    private Lingua lingua;
    private Difficolta difficolta;
    private String path;
    private String testoD;


    /**
     * Costruttore della classe Documento.
     * @param titolo rappresenta il titolo del documento.
     * @param lingua rappresenta in che lingua si presenta il documento.
     * @param difficolta rappresenta la difficoltà del documento fornito.
     * Crea inoltre una nuova lista per contenere il testo del documento.
     */
    public Documento(String titolo, Lingua lingua, Difficolta difficolta) {
        this.titolo = titolo;
        this.lingua = lingua;
        this.difficolta = difficolta;
        path="data/"+lingua+"/"+difficolta.toString().toLowerCase()+"/"+titolo+".bin";
        testoD = "";
    }

    public Documento(String titolo, Lingua lingua, Difficolta difficolta, String path) {

        this.titolo = titolo;
        this.lingua = lingua;
        this.difficolta = difficolta;
        this.path = path;
        this.testoD = "";

    }

    public String getTestoD() {
            return testoD;
    }

    /**
     * Costruttore predefinito che inizializza un nuovo documento vuoto.
     * Crea una nuova lista vuota per contenere il testo del documento.
     */
    public Documento() {
        testoD = "";
    }

    /**
     * Restituisce il titolo del documento.
     * @return il titolo del documento
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Restituisce la lingua del documento.
     * @return la lingua del documento
     */
    public Lingua getLingua() {
        return lingua;
    }

    /**
     * Restituisce il percorso del file del documento.
     * @return il percorso del file
     */
    public String getPath() {
        return path;
    }

    /**
     * Imposta il percorso del file del documento.
     * @param path il nuovo percorso da impostare
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Restituisce il livello di difficoltà del documento.
     * @return il livello di difficoltà
     */
    public Difficolta getDifficolta() {
        return difficolta;
    }

    /**
     * Restituisce una copia del testo del documento.
     * @return una nuova lista contenente il testo del documento
     */
//   public List<String> getTesto() {
//       return new ArrayList<>(testo); // non ritornare mai il dato effettivo
//   }

    /**
     * Converte un file di testo in formato binario, crittografando il contenuto.
     * Legge il file di input riga per riga, aggiunge il testo al documento e lo salva in formato binario crittografato.
     *
     * @param inputFile il file di testo da convertire
     * @throws IOException se si verificano errori durante la lettura o scrittura dei file
     */
    public void convertiTxtToBin(File inputFile) throws IOException {
        File outputFile= new File(path);
        File parent=outputFile.getParentFile();
        if(parent.mkdirs()) System.out.println("Directory created");
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             DataOutputStream dos = new DataOutputStream(new   BufferedOutputStream(new FileOutputStream(outputFile)))) {

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                //testo.add(line);
                sb.append(line).append("\n");
                dos.writeUTF(CryptoAlphabet.cripta(line));
            }
            testoD = sb.toString();
        }
    }

    /**
     * Legge un documento da un file binario crittografato.
     * Estrae gli attributi dal percorso del file e decrittografa il contenuto.
     *
     * @param filename il percorso del file da leggere
     * @return il documento letto dal file
     * @throws IOException se si verificano errori durante la lettura del file
     * @throws ClassNotFoundException se si verificano errori durante la decrittografia
     */
    public static Documento leggiDocumento(String filename) throws IOException {
        Documento dr = null;
        List<String> parole=new ArrayList<>();
        /*
        COSA FARE SECONDO ROSA
        1. in input ho il nome del file da leggere, quindi ho il file e lo devo legggere dalla directory esatta
        2. quando lo leggo per mostrarlo devo decriptarlo
         */
        //lingua e difficolta dipendono dalla cartella in cui si trovano--> dove finiscono le stopword?? sul db per ora
        dr=new Documento();
        getAttributes(filename, dr);
        try(DataInputStream br=new DataInputStream(new BufferedInputStream(new FileInputStream(filename)))){
           String line;
           StringBuilder sb=new StringBuilder();

               while (br.available() > 0) {
                   line = br.readUTF();
                   parole.add(CryptoAlphabet.decripta(line));
                   if (!sb.isEmpty()) sb.append("\n");
                   sb.append(CryptoAlphabet.decripta(line));
               }
               dr.testoD=sb.toString();


        }
        return dr;
    }

    /**
     * Estrae gli attributi del documento dal percorso del file.
     * Analizza il percorso per determinare titolo, difficoltà e lingua del documento.
     *
     * @param filename il percorso del file
     * @param dr il documento da popolare con gli attributi estratti
     */
    private static void getAttributes(String filename, Documento dr){
        System.out.println("DEBUG - Filename ricevuto: " + filename);

        String[] split = filename.split("[/.]");
        System.out.println("DEBUG - Split length: " + split.length);
        for(int i=0; i<split.length; i++){
            System.out.println("DEBUG - split[" + i + "] = " + split[i]);
        }

        if(split.length < 4){
            throw new IllegalArgumentException("Path non valido: " + filename + " - Split length: " + split.length);
        }

        dr.titolo = split[split.length - 2];
        dr.difficolta = Difficolta.valueOf(split[split.length - 3].toUpperCase());
        dr.lingua = Lingua.valueOf(split[split.length - 4].toUpperCase());
        dr.path = filename;
    }



    /**
     * Elimina il file del documento dal filesystem.
     * Il metodo verifica se il file da eliminare esiste e, in caso affermativo, tenta di eliminarlo.
     * Se il file non esiste o se si verifica un errore durante l'eliminazione, viene sollevata un'eccezione DeleteException.
     */
    public void eliminaDocumento() throws DeleteException {
        String path = this.path.trim();
        File file = Path.of(path).toFile();
        if (file.exists())
            if (!file.delete()) throw new DeleteException("Impossibile eliminare il file del documento: " + file.getName());
            else {
                this.titolo = null;
                this.lingua = null;
                this.difficolta = null;
                this.path = null;
                this.testoD = null;
            }
        else throw new DeleteException("Il file del documento non esiste: " + file.getName());
    }
    /**
     * Restituisce una rappresentazione testuale del documento.
     * Concatena tutte le righe del testo con dei caratteri di nuova riga.
     *
     * @return il contenuto del documento come stringa
     */
    @Override
    public String toString() {
        return  testoD;
    }

}