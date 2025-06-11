package it.unisa.diem.model.gestione.sessione;

import it.unisa.diem.model.gestione.analisi.Documento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessioneDocumento {
    //è una classe che salva la sessione che è associata a uno o più documenti
    private final long idSessione;
    private final String nomeDocumento;

    public SessioneDocumento(long idSessione, String nomeDocumento) {

        this.idSessione = idSessione;
        this.nomeDocumento = nomeDocumento;

    }

    public long getIdSessione() {
        return idSessione;
    }

    public String getNomeDocumento() {
        return nomeDocumento;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null)  return false;

        if (this == o) return true;

        if (!(o instanceof SessioneDocumento)) return false;

        SessioneDocumento sessioneDocumento = (SessioneDocumento) o;

        return this.getIdSessione() == sessioneDocumento.getIdSessione()
                && this.getNomeDocumento().equals(sessioneDocumento.getNomeDocumento());

    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(idSessione);
        result = 31 * result + nomeDocumento.hashCode();
        return result;
    }

}
