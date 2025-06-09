package it.unisa.diem.dao.interfacce;

import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Analisi;

import java.sql.SQLException;
import java.util.Optional;

public interface AnalisiDAO extends DAO<Analisi>{

    public Optional<Analisi> selectAnalisiByTitle(String titolo) throws SQLException, DBException;
    public void update(Analisi a, String oldTitolo) throws SQLException, DBException;
}
