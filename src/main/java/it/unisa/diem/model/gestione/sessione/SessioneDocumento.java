package it.unisa.diem.model.gestione.sessione;

import it.unisa.diem.model.gestione.analisi.Documento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe che rappresenta una sessione associata a uno o più documenti.
 * Mantiene l'associazione tra un ID di sessione e il nome di un documento.
 */
public class SessioneDocumento {
    private final long idSessione;
    private final String nomeDocumento;

    /**
     * Costruttore della classe SessioneDocumento.
     *
     * @param idSessione L'identificativo della sessione
     * @param nomeDocumento Il nome del documento associato alla sessione
     */
    public SessioneDocumento(long idSessione, String nomeDocumento) {
        this.idSessione = idSessione;
        this.nomeDocumento = nomeDocumento;
    }

    /**
     * Restituisce l'ID della sessione.
     *
     * @return L'ID della sessione
     */
    public long getIdSessione() {
        return idSessione;
    }

    /**
     * Restituisce il nome del documento associato alla sessione.
     *
     * @return Il nome del documento
     */
    public String getNomeDocumento() {
        return nomeDocumento;
    }

    /**
     * Confronta questo oggetto con un altro per verificarne l'uguaglianza.
     *
     * @param o L'oggetto da confrontare
     * @return true se gli oggetti sono uguali, false altrimenti
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (!(o instanceof SessioneDocumento)) return false;

        SessioneDocumento sessioneDocumento = (SessioneDocumento) o;
        return this.getIdSessione() == sessioneDocumento.getIdSessione()
                && this.getNomeDocumento().equals(sessioneDocumento.getNomeDocumento());
    }

    /**
     * Calcola e restituisce il codice hash dell'oggetto.
     *
     * @return Il codice hash calcolato
     */
    @Override
    public int hashCode() {
        int result = Long.hashCode(idSessione);
        result = 31 * result + nomeDocumento.hashCode();
        return result;
    }
}