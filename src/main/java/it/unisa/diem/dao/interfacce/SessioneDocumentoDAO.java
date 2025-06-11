package it.unisa.diem.dao.interfacce;

import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Documento;
import it.unisa.diem.model.gestione.sessione.Sessione;
import it.unisa.diem.model.gestione.sessione.SessioneDocumento;

import java.util.List;


public interface SessioneDocumentoDAO extends NotEditableDAO<SessioneDocumento> {

    List<Documento> selectDocumentsBySession(long idSessione) throws DBException;

}
