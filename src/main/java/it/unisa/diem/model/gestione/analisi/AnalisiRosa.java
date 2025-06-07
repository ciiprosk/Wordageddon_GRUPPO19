package it.unisa.diem.model.gestione.analisi;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class AnalisiRosa {
    private final DocumentoRosa doc;
    private Map<String, Integer> frequenzeTesti;
    private Lingua linguaAnalisi;
    private Difficolta difficoltaAnalisi;
    private String titolo;
    private Stopword stopwordAnalisi;
    private List<String> testo;

    public AnalisiRosa(String path) throws IOException, ClassNotFoundException {
        //path si riferisce al documento da analizzare
        frequenzeTesti=new HashMap<>();
        doc= DocumentoRosa.leggiDocumento(path);
        recuperaDatiFromDocumentoRosa();
    }

    private void recuperaDatiFromDocumentoRosa(){
        linguaAnalisi=doc.getLingua();
        difficoltaAnalisi=doc.getDifficolta();
        titolo=doc.getTitolo();
        stopwordAnalisi=doc.getStopword();
        testo=doc.getTesto().stream().collect(Collectors.toList()); //il testo è su "una sola" linea non so se ha senso

    }

    public DocumentoRosa getDocumento() {
        return doc;
    }

    public Map<String, Integer> getFrequenzeTesti() {
        return frequenzeTesti;
    }

    private List<String> getWordsDocument() throws IOException, ClassNotFoundException {
        //devo recuperare tutte le parole del documento letto
        //il documento mi deve passare il filename in cui si trova--> cosrtruttore
        DocumentoRosa dr= DocumentoRosa.leggiDocumento("data/ITA/facile/crypto.bin");
        //List<String> parole=doc.getTesto().stream().flatMap(line-> Arrays.stream());
        return null;
    }

    public void caricaAnalisi(){
    //devo scrivere su file in base al documento-> lingua e difficoltà non posso usare classpath

    }

    public AnalisiRosa leggiAnalisi(DocumentoAntonio doc){
        //qui posso usare ClassPath!!!!!!!--> non va bene perché non è una risorsa! è dinamco e cambia
        AnalisiRosa a = null;
        return a;
    }

    public HashMap<String, Integer> getFrequenzeTestiRosa() throws IOException, ClassNotFoundException {
        List<String> parole=getWordsDocument();
        HashMap<String, Integer> frequenze=new HashMap<>();
        for(String parola : parole){
            if(frequenze.containsKey(parola)){
                frequenze.put(parola, frequenze.get(parola)+1);
            }else{
                frequenze.put(parola, 1);
            }
        }
        return frequenze;
    }







}
