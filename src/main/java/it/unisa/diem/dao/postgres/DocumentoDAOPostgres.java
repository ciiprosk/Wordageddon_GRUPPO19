package it.unisa.diem.dao.postgres;

import it.unisa.diem.dao.interfacce.DocumentoDAO;
import it.unisa.diem.model.gestione.analisi.Documento;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DocumentoDAOPostgres implements DocumentoDAO {

    @Override
    public Optional<Documento> selectById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Documento> selectAll() {
        return Collections.emptyList();
    }

    @Override
    public void insert(Documento documento) {

    }

    @Override
    public void update(Documento documento) {

    }

    @Override
    public void delete(long id) {

    }

}
