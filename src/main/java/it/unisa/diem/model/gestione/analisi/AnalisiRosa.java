package it.unisa.diem.model.gestione.analisi;

import java.util.HashMap;
import java.util.Map;

public class AnalisiRosa {
    private final Documento doc;
    private Map<String, Integer> frequenzeTesti;
    public AnalisiRosa(Documento doc) {
        this.doc = doc;
        frequenzeTesti=new HashMap<>();
    }

    public Documento getDocumento() {
        return doc;
    }


}
