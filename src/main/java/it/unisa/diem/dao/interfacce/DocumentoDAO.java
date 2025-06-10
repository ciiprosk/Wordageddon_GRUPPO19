package it.unisa.diem.dao.interfacce;

import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Documento;
import it.unisa.diem.model.gestione.analisi.Lingua;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DocumentoDAO extends DAO<Documento> {

    Optional<Documento> selectByTitle(String titolo) throws SQLException, DBException;

    List<String> selectTitlesByLangAndDif(Lingua lingua, Difficolta difficolta) throws DBException;

}
