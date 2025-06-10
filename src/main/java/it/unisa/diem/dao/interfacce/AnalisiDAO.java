package it.unisa.diem.dao.interfacce;

import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Analisi;

import java.sql.SQLException;
import java.util.Optional;

public interface AnalisiDAO extends NotEditableDAO<Analisi>{

    Optional<Analisi> selectAnalisiByTitle(String titolo) throws DBException;

    void insert(Analisi analisi) throws DBException;

    void delete(Analisi analisi) throws DBException;

}
