package it.unisa.diem.model.gestione.analisi;

import java.util.HashMap;
import java.util.Map;

public class AnalisiRosa {
    private final DocumentoAntonio doc;
    private Map<String, Integer> frequenzeTesti;
    public AnalisiRosa(DocumentoAntonio doc) {
        this.doc = doc;
        frequenzeTesti=new HashMap<>();
    }

    public DocumentoAntonio getDocumento() {
        return doc;
    }

    public Map<String, Integer> getFrequenzeTesti() {
        return frequenzeTesti;
    }

    public void caricaAnalisi(){

    }

    public Analisi leggiAnalisi(){
        Analisi a = null;
        return a;
    }





}
