/**
 * @file SessioneDocumentoDAO.java
 * @brief Interfaccia per la gestione delle associazioni Sessione-Documento
 */
package it.unisa.diem.dao.interfacce;

import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Documento;
import it.unisa.diem.model.gestione.sessione.Sessione;
import it.unisa.diem.model.gestione.sessione.SessioneDocumento;
import java.util.List;

/**
 * @interface SessioneDocumentoDAO
 * @brief Interfaccia per l'accesso alle associazioni tra sessioni e documenti
 *
 * Estende NotEditableDAO<SessioneDocumento> fornendo operazioni specifiche per:
 * - Recupero dei documenti associati a una sessione
 * - Gestione delle associazioni
 */
public interface SessioneDocumentoDAO extends NotEditableDAO<SessioneDocumento> {

    /**
     * @brief Recupera tutti i documenti associati a una sessione
     * @param idSessione Identificativo della sessione
     * @return Lista dei documenti associati alla sessione
     * @throws DBException in caso di errori di accesso al database
     *
     */
    List<Documento> selectDocumentsBySession(long idSessione) throws DBException;

    /**
     * @brief Elimina tutte le associazioni per una sessione
     * @param sessioneId Identificativo della sessione
     * @throws DBException in caso di errori di accesso al database
     *
     */
    void deleteBySessioneId(long sessioneId) throws DBException;
}