package it.unisa.diem.model.gestione.sessione;

import it.unisa.diem.model.gestione.analisi.Documento;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessioneDocumento {
    //è una classe che salva la sessione che è associata a uno o più documenti
    Map<Sessione, List<Documento>> sessioneDocumenti;
    public SessioneDocumento(){
        sessioneDocumenti = new HashMap<>();
    }
    public void addDocumentoToSessione(Sessione s, Documento d){
        if(sessioneDocumenti.containsKey(s)) {
            sessioneDocumenti.get(s).add(d);
        } else {
            sessioneDocumenti.put(s, List.of(d));
        }
    }
    //ti serve per sapere che domande fare in una sessione interrotta
    public List<Documento> getDocumentiBySessione(Sessione s){
        return sessioneDocumenti.getOrDefault(s, List.of());
    }



}
