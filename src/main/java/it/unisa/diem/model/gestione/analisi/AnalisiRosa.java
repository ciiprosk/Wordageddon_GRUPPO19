package it.unisa.diem.model.gestione.analisi;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalisiRosa {
    private final DocumentoAntonio doc;
    private Map<String, Integer> frequenzeTesti;
    private Lingua linguaAnalisi;
    private Difficolta difficoltaAnalisi;
    private String titolo;
    private Stopword stopwordAnalisi;
    public AnalisiRosa(DocumentoAntonio doc) {
        this.doc = doc;
        frequenzeTesti=new HashMap<>();
        recuperaDatiFromDocumentoAntonio();

    }

    private void recuperaDatiFromDocumentoAntonio(){
        linguaAnalisi=doc.getLingua();
        difficoltaAnalisi=doc.getDifficolta();
        titolo=doc.getTitolo();
        stopwordAnalisi=doc.getStopword();
    }
    public DocumentoAntonio getDocumento() {
        return doc;
    }

    public Map<String, Integer> getFrequenzeTesti() {
        return frequenzeTesti;
    }

    public List<String> getWordsDocument(){
        List<String> wordsDocument=new ArrayList<>();
        DocumentoAntonio prova = doc.leggiDocumento(linguaAnalisi+"/"+difficoltaAnalisi+"/"+titolo);
        System.out.println("Prova: " +prova);
        return wordsDocument;
    }

    public void caricaAnalisi(){
    //devo scrivere su file in base al documento-> lingua e difficoltà non posso usare classpath

    }

    public AnalisiRosa leggiAnalisi(DocumentoAntonio doc){
        //qui posso usare ClassPath!!!!!!!--> non va bene perché non è una risorsa! è dinamco e cambia
        AnalisiRosa a = null;
        return a;
    }

    private void countWords(){}
    private void words(){}





}
