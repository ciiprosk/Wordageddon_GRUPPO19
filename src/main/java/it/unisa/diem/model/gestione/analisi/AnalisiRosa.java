package it.unisa.diem.model.gestione.analisi;

import it.unisa.diem.model.gestione.analisi.stopword.StopwordManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class AnalisiRosa {
    private final DocumentoRosa documento;
    private Map<String, Integer> frequenzeTesti;
    private Lingua linguaAnalisi;
    private Difficolta difficoltaAnalisi;
    private String titolo;
    private StopwordManager stopwordAnalisi;
    private List<String> testo;

    public AnalisiRosa(DocumentoRosa documento) throws IOException, ClassNotFoundException {
        //path si riferisce al documento da analizzare
        frequenzeTesti=new HashMap<>();
        this.documento= documento;
        recuperaDatiFromDocumentoRosa();
    }

    private void recuperaDatiFromDocumentoRosa() throws IOException, ClassNotFoundException {
        linguaAnalisi=documento.getLingua();
        difficoltaAnalisi=documento.getDifficolta();
        titolo=documento.getTitolo();
        stopwordAnalisi=documento.getStopword();
        DocumentoRosa doc= DocumentoRosa.leggiDocumento(documento.getPath());
        testo=doc.getTesto();
    }

    public DocumentoRosa getDocumento() {
        return documento;
    }

    public Map<String, Integer> getFrequenzeTesti() {

        return frequenzeTesti;
    }

    private List<String> getWordsDocument() {
        List<String> parole = testo.stream()
            .flatMap(line -> Arrays.stream(line.split(" ")))
            .filter(word -> !word.trim().isEmpty())
            .map(String::toLowerCase)
            .collect(Collectors.toList());
    
    // Applica il filtro stopword solo se stopwordAnalisi non è null
    if (stopwordAnalisi != null) {
        parole = parole.stream()
                .filter(word -> !stopwordAnalisi.getParole().contains(word))
                .collect(Collectors.toList());
    }
    
    return parole;
}


    public Map<String, Integer> getFrequenzeTestiRosa() throws IOException, ClassNotFoundException {
        List<String> parole=getWordsDocument();
        for(String parola : parole){
            if(frequenzeTesti.containsKey(parola)){
                frequenzeTesti.put(parola, frequenzeTesti.get(parola)+1);
            }else{
                frequenzeTesti.put(parola, 1);
            }
        }
        return frequenzeTesti;
    }

    public void caricaAnalisi(){
        //devo scrivere su file in base al documento-> lingua e difficoltà non posso usare classpath

    }

    public AnalisiRosa leggiAnalisi(DocumentoRosa doc){

        AnalisiRosa a = null;
        return a;
    }

}