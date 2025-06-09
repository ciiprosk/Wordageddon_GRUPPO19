package it.unisa.diem.dao.interfacce;

import it.unisa.diem.model.gestione.analisi.Documento;

import java.sql.SQLException;
import java.util.Optional;

public interface DocumentoDAO extends DAO<Documento> {

    Optional<Documento> selectByTitle(String titolo) throws SQLException;

}
