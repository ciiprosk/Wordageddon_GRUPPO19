package it.unisa.diem.model.gestione.analisi;

import it.unisa.diem.exceptions.DeleteException;
import it.unisa.diem.exceptions.UpdateException;
import it.unisa.diem.model.gestione.analisi.stopword.StopwordENG;
import it.unisa.diem.model.gestione.analisi.stopword.StopwordITA;
import it.unisa.diem.model.gestione.analisi.stopword.StopwordManager;
import it.unisa.diem.utility.CryptoAlphabet;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * La classe Analisi rappresenta l'analisi di un documento, inclusa lingua, difficoltà, titolo, escludendo le stopword.
 * L'analisi prevede il conteggio delle parole e la frequenza con cui esse compaiono nel documento.
 * Fornisce inoltre metodi che permettono il salvataggio su file e lettura dell'analisi.
 */
public class Analisi {
    private Documento documento;
    private Map<String, Integer> frequenzeTesto;
    private Lingua linguaAnalisi;
    private Difficolta difficoltaAnalisi;
    private String titolo;
    private StopwordManager stopwordAnalisi;
    private String pathAnalisi;


    /**
     * Costruisce un oggetto Analisi basato sul documento fornito.
     * Inizializza i campi come attributi linguistici, livello di difficoltà,
     * stopword e altri dati correlati estraendoli dal documento fornito.
     *
     * @param documento l'istanza di Documento su cui verrà effettuata l'analisi.
     *                 Fornisce gli attributi necessari come lingua, difficoltà,
     *                 stopword e contenuto testuale rilevante per l'analisi.
     */

    public Analisi(Documento documento, StopwordManager stopwordAnalisi){
        //dal documento posso ricavare la difficoltà, lingua, stopword...
        frequenzeTesto=new HashMap<>();
        this.documento= documento;
        linguaAnalisi=documento.getLingua();
        difficoltaAnalisi=documento.getDifficolta();
        titolo=documento.getTitolo();
        pathAnalisi="analysis/"+ linguaAnalisi+"/"+difficoltaAnalisi+"/"+titolo+"_analysis.bin";//cpstruisco il percorso in cui deve finire l'analisi
        if(stopwordAnalisi != null){
            this.stopwordAnalisi=stopwordAnalisi;
        }
    }
    public Analisi(Documento documento) {
        frequenzeTesto = new HashMap<>();
        this.documento = documento;
        linguaAnalisi = documento.getLingua();
        difficoltaAnalisi = documento.getDifficolta();
        titolo = documento.getTitolo();
        pathAnalisi = "analysis/" + linguaAnalisi + "/" + difficoltaAnalisi + "/" + titolo + "_analysis.bin";

    }
    


    /**
     * Restituisce il documento associato a questa analisi.
     *
     * @return il documento oggetto dell'analisi
     */
    public Documento getDocumento() {
        return documento;
    }

    public String getTitolo() {
        return titolo;
    }
    public String getPathAnalisi() {
        return pathAnalisi;
    }
    /**
     * Restituisce la mappa delle frequenze delle parole nel testo.
     *
     * @return mappa contenente le parole come chiavi e il numero di occorrenze come valori
     */
    public Map<String, Integer> getFrequenzeTesto() {
        return frequenzeTesto;
    }

    /**
     * Elabora il documento per estrarre tutte le parole presenti nel testo.
     * Il metodo esegue le seguenti operazioni:
     * - Rimuove la punteggiatura in base alla lingua del documento
     * - Converte tutte le parole in minuscolo
     * - Rimuove le stopword se configurate
     * - Mantiene i duplicati delle parole
     *
     * @return lista di tutte le parole presenti nel testo
     */

    //il metodo prevede la divisione del testo in parole(con duplicati se presenti) -> ritorna la lista di tutte le parole presenti nel testo
    private List<String> getWordsDocument() {
        List<String> testo=documento.getTesto();
        List<String> punteggiatura= switch(documento.getLingua().toString()){
            case "ITA" -> Arrays.stream(new StopwordITA().getPunteggiatura()).collect(Collectors.toList());
            case "ENG" -> Arrays.stream(new StopwordENG().getPunteggiatura()).collect(Collectors.toList());
            default -> throw new IllegalStateException("Unexpected value: " + documento.getLingua().toString());
        };
        //tolgo la punteggiatura a prescindere dalle stopword
        List<String> parole = testo.stream()
            .flatMap(line -> Arrays.stream(line.split(" "))).map(word-> {
                for(String punt: punteggiatura)
                    word=word.replace(punt, "");
                return word;
                })
            .filter(word -> !word.trim().isEmpty())
            .map(String::toLowerCase)
            .collect(Collectors.toList());
    
    // nel caso in cui stopwrod analisi non sia null
    if (stopwordAnalisi != null) {
        parole = parole.stream()
                .filter(word -> !word.isEmpty())
                .filter(word -> !stopwordAnalisi.getParole().contains(word)) //esclude le stopword
                .collect(Collectors.toList());
    }
    
    return parole;
}

    /**
     * Calcola la frequenza di ogni parola nel documento.
     * Utilizza il metodo getWordsDocument() per ottenere la lista delle parole
     * e costruisce una mappa dove ogni chiave è una parola e il valore è il numero
     * di volte che essa appare nel testo.
     *
     * @return mappa delle frequenze delle parole
     * @throws IOException se si verifica un errore durante l'elaborazione
     * @throws ClassNotFoundException se si verifica un errore durante il caricamento delle classi
     */

    public Map<String, Integer> frequenzeDocumento() throws IOException, ClassNotFoundException {
        List<String> parole=getWordsDocument(); // mi prendo le parole del documento

        frequenzeTesto = new HashMap<>();

        for(String parola : parole){
            if(frequenzeTesto.containsKey(parola)){
                frequenzeTesto.put(parola, frequenzeTesto.get(parola)+1);
            }else{
                frequenzeTesto.put(parola, 1);
            }
        }
        //caricaAnalisi();
        return frequenzeTesto;
    }

    /**
     * Salva l'analisi delle frequenze su file.
     * Le parole e le loro frequenze vengono crittografate prima del salvataggio.
     *
     * @throws ClassNotFoundException se si verifica un errore durante la crittografia
     * @throws IOException se si verifica un errore durante la scrittura su file
     */

    public void caricaAnalisi() throws ClassNotFoundException,IOException  {
        //devo scrivere su file frequenze testi così com'è-> ma devo crittografare le parole
        File file=new File(pathAnalisi);
        try(DataOutputStream dos=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)))){
        for(Map.Entry<String, Integer> parola : frequenzeTesto.entrySet()) {
            dos.writeUTF(CryptoAlphabet.cripta(parola.getKey()));
            String frequenza = Integer.toString(parola.getValue());
            dos.writeUTF(CryptoAlphabet.cripta(frequenza));
        }
        }catch(IOException e){
            throw new IOException("Errore durante il salvataggio dell'analisi");
        }

    }
    /**
     * Carica un'analisi precedentemente salvata per un documento specifico.
     * Decrittografa le parole e le frequenze dal file salvato.
     *
     * @param doc il documento di cui caricare l'analisi
     * @return una nuova istanza di Analisi con i dati caricati
     * @throws IOException se si verifica un errore durante la lettura del file
     */

    public static Analisi leggiAnalisi(Documento doc) throws IOException {
        Analisi a = null;
        String path= recuperaAnalisiPath(doc);
        Map<String, Integer> frequenze=new HashMap<>();
        try(DataInputStream dis= new DataInputStream(new BufferedInputStream(new FileInputStream(path)))) {
            a = new Analisi(doc, null);
            while (dis.available() > 0) {
                String parola = CryptoAlphabet.decripta(dis.readUTF());
                int frequenza = Integer.parseInt(CryptoAlphabet.decripta(dis.readUTF()));
                frequenze.put(parola, frequenza);
            }
        }
        a.frequenzeTesto=new HashMap<>(frequenze);
        return a;
    }

    /**
     * Genera il percorso del file dove viene salvata l'analisi.
     *
     * @param doc il documento per cui generare il percorso
     * @return il percorso completo del file di analisi
     */

    private static String recuperaAnalisiPath(Documento doc){
       String path="analysis/"+ doc.getLingua().toString()+"/"+doc.getDifficolta().toString()+"/"+doc.getTitolo()+"_analysis.bin";

       return path;
    }

    /**
     * Modifica il nome del file di analisi corrente utilizzando il nuovo nome del documento.
     * Il metodo mantiene la stessa struttura di directory e aggiunge il suffisso "_analysis.bin"
     * al nuovo nome del file.
     *
     * @param nuovoNomeDoc il nuovo nome del documento da utilizzare per rinominare il file di analisi
     */
    public void modificaNomeAnalisi(String nuovoNomeDoc) throws UpdateException {
        //1. cerco file analisi con vecchio nome
        String path= this.pathAnalisi.trim();
        File vecchioFile = Path.of(path).toFile();
        // 2. crarto il file devo verifiacre che esista
        if(!vecchioFile.exists()){
            throw new RuntimeException("Il file di analisi non esiste: " + vecchioFile.getName());
        }
        //3. devo sostituire questo percorso con il nuovo nome
        File nuovo= new File(vecchioFile.getParentFile(), nuovoNomeDoc + "_analysis.bin");
        if(!vecchioFile.renameTo(nuovo)) {
            throw new UpdateException("Impossibile rinominare il file di analisi: " + vecchioFile.getName());
        }
        //4. aggiorno il path analisi
        this.pathAnalisi = nuovo.getPath();
        //5. aggiorno il titolo
        this.titolo = nuovoNomeDoc;
    }
/**
     * Elimina il file di analisi associato all'oggetto corrente.
     * Se il file esiste nel percorso specificato da pathAnalisi, viene rimosso dal filesystem.
     * Se il file non esiste, non viene eseguita alcuna operazione.
     */
    public void eliminaAnalisi() throws DeleteException{
        String path=this.pathAnalisi;
        File file = Path.of(path).toFile();

        if(file.exists()){
            if(!file.delete()) throw new DeleteException("Impossibile eliminare il file di analisi: " + file.getName());
            else {
                this.titolo = null; // resetto il titolo
                this.frequenzeTesto.clear(); // resetto le frequenze
                this.pathAnalisi = null; // resetto il path
                this.stopwordAnalisi = null; // resetto le stopword
                this.documento = null; // resetto il documento
                this.linguaAnalisi = null; // resetto la lingua
                this.difficoltaAnalisi = null; // resetto la difficoltà
            }
        }else  throw new DeleteException("Il file di analisi non esiste: " + file.getName());
    }

    @Override
    public String toString(){
        return frequenzeTesto.entrySet().stream().map(e -> e.getKey() + " " + e.getValue()).toString();
    }

}